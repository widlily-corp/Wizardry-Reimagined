package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.registry.EBEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Glacial Oblivion entity for "Absolute Zero: Glacial Oblivion" Grand Ice Spell.
 * Phase 0: Instant frost eruption (45 dmg, freezing, slowness IV).
 * Phase 1: Absolute zero vortex (pulls enemies inward, persistent frostbite).
 * Phase 2: Cataclysmic glacial shatter explosion (20 dmg, glass break sound, frost shrapnel).
 */
public class GlacialOblivionEntity extends MagicConstructEntity {
    private static final EntityDataAccessor<Boolean> SHATTERED = SynchedEntityData.defineId(GlacialOblivionEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int STORM_DURATION = 80; // 4 seconds
    private int stormTicks = 0;

    public GlacialOblivionEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.noPhysics = true;
    }

    public GlacialOblivionEntity(Level world, double x, double y, double z) {
        this(EBEntities.GLACIAL_OBLIVION.get(), world);
        setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SHATTERED, false);
    }

    public boolean isShattered() {
        return this.entityData.get(SHATTERED);
    }

    @Override
    public void tick() {
        super.tick();

        if (isShattered()) {
            // ==========================================
            // Phase 2: Lingering Permafrost Aftermath Zone (12s)
            // ==========================================
            stormTicks++;

            if (level().isClientSide) {
                // Triple swirling concentric blizzard rings (radii 15, 30, 48 blocks)
                double angle1 = (stormTicks * 0.12);
                double angle2 = -(stormTicks * 0.08);
                double angle3 = (stormTicks * 0.05);

                for (int i = 0; i < 6; i++) {
                    double offset = (i * Math.PI / 3.0);
                    // Outer blizzard ring (radius 48)
                    level().addParticle(ParticleTypes.SNOWFLAKE, getX() + 48.0 * Math.cos(angle1 + offset), getY() + 0.3, getZ() + 48.0 * Math.sin(angle1 + offset), -Math.sin(angle1 + offset) * 0.4, 0.05, Math.cos(angle1 + offset) * 0.4);

                    // Mid blizzard ring (radius 30)
                    level().addParticle(ParticleTypes.SNOWFLAKE, getX() + 30.0 * Math.cos(angle2 + offset), getY() + 0.3, getZ() + 30.0 * Math.sin(angle2 + offset), Math.sin(angle2 + offset) * 0.3, 0.05, -Math.cos(angle2 + offset) * 0.3);
                    level().addParticle(ParticleTypes.ITEM_SNOWBALL, getX() + 30.0 * Math.cos(angle2 + offset), getY() + 0.5, getZ() + 30.0 * Math.sin(angle2 + offset), 0, 0.02, 0);

                    // Inner frost ring (radius 15)
                    level().addParticle(ParticleTypes.CLOUD, getX() + 15.0 * Math.cos(angle3 + offset), getY() + 0.2, getZ() + 15.0 * Math.sin(angle3 + offset), 0, 0.02, 0);
                }

                // Swirling ground snow across the 50-block dome
                for (int i = 0; i < 5; i++) {
                    double rx = getX() + (random.nextDouble() - 0.5) * 80.0;
                    double rz = getZ() + (random.nextDouble() - 0.5) * 80.0;
                    if ((rx - getX()) * (rx - getX()) + (rz - getZ()) * (rz - getZ()) <= 2300) {
                        level().addParticle(ParticleTypes.SNOWFLAKE, rx, getY() + 0.2 + random.nextDouble() * 2.0, rz, (random.nextDouble() - 0.5) * 0.2, 0.05, (random.nextDouble() - 0.5) * 0.2);
                    }
                }
            } else if (level() instanceof ServerLevel serverLevel) {
                // Howling arctic tempest audio (volume 10.0f audible across 160 blocks)
                if (stormTicks % 20 == 0) {
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_HURT_FREEZE, SoundSource.WEATHER, 10.0f, 0.6f);
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.GLASS_BREAK, SoundSource.WEATHER, 8.0f, 0.6f);
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.WEATHER, 10.0f, 0.7f);
                }

                // Every 10 ticks (~0.5s): 48-Block Absolute Zero Frostbite Hazard Pulse
                if (stormTicks % 10 == 0) {
                    List<LivingEntity> enemies = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(48.0),
                            this::isValidTarget);
                    for (LivingEntity enemy : enemies) {
                        enemy.hurt(level().damageSources().freeze(), 5.5f);
                        enemy.setTicksFrozen(350);
                        enemy.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 3, false, false));
                        enemy.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40, 2, false, false));
                        serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, enemy.getX(), enemy.getY() + 0.5, enemy.getZ(), 5, 0.3, 0.3, 0.3, 0.05);
                    }
                }

                // Discard after 240 ticks post-shatter with final frost explosion
                if (stormTicks >= STORM_DURATION + 240) {
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, getX(), getY() + 2.0, getZ(), 200, 8.0, 3.0, 8.0, 0.2);
                    serverLevel.sendParticles(ParticleTypes.ITEM_SNOWBALL, getX(), getY() + 2.0, getZ(), 100, 6.0, 3.0, 6.0, 0.25);
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.GLASS_BREAK, SoundSource.WEATHER, 12.0f, 0.5f);
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.WEATHER, 10.0f, 0.8f);
                    discard();
                }
            }
            return;
        }

        if (stormTicks == 0) {
            onInitialEruption();
        }

        stormTicks++;

        // Ongoing swirling blizzard vortex
        spawnStormParticles();

        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            Vec3 center = position();
            double radius = 10.0;
            List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(radius));

            for (LivingEntity target : targets) {
                if (!isValidTarget(target)) continue;

                // Pull enemies inward towards absolute zero epicenter
                Vec3 toCenter = center.subtract(target.position());
                if (toCenter.lengthSqr() > 1.0) {
                    Vec3 pull = toCenter.normalize().scale(0.22);
                    target.setDeltaMovement(target.getDeltaMovement().add(pull.x, 0.03, pull.z));
                    if (target instanceof ServerPlayer player) {
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));
                    }
                }

                // Periodic frostbite damage
                if (stormTicks % 10 == 0) {
                    target.hurt(level().damageSources().freeze(), 5.0f);
                    target.setTicksFrozen(Math.max(target.getTicksFrozen(), 350));
                }
            }

            // Shatter at end of storm duration
            if (stormTicks >= STORM_DURATION) {
                onShatter(serverLevel);
                this.entityData.set(SHATTERED, true);
            }
        }
    }

    private void onInitialEruption() {
        Vec3 pos = position();

        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 4.0f, 0.5f);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 3.5f, 0.75f);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.PLAYER_HURT_FREEZE, SoundSource.PLAYERS, 3.0f, 0.7f);

        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            // Massive radial frost shockwave
            for (int i = 0; i < 48; i++) {
                double angle = (Math.PI * 2 / 48) * i;
                double speed = 1.2;
                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y + 0.3, pos.z, 3, Math.cos(angle) * speed, 0.2, Math.sin(angle) * speed, 0.08);
            }

            double initialRadius = 11.0;
            List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(initialRadius));
            for (LivingEntity target : targets) {
                if (!isValidTarget(target)) continue;

                target.hurt(level().damageSources().magic(), 55.0f);
                target.setTicksFrozen(400); // 20s freeze overlay
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 3)); // Slowness IV for 10s
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 2)); // Weakness III
            }

            // Freeze terrain (water -> blue ice, grass -> packed ice/snow)
            BlockPos centerPos = blockPosition();
            boolean canGrief = com.binaris.wizardry.api.content.util.EntityUtil.canDamageBlocks(getCaster(), level());
            for (int x = -4; x <= 4; x++) {
                for (int z = -4; z <= 4; z++) {
                    if (x * x + z * z <= 18) {
                        BlockPos targetPos = centerPos.offset(x, 0, z);
                        var state = level().getBlockState(targetPos);
                        if (state.getBlock() == net.minecraft.world.level.block.Blocks.WATER) {
                            level().setBlockAndUpdate(targetPos, net.minecraft.world.level.block.Blocks.BLUE_ICE.defaultBlockState());
                        } else if (state.getBlock() == net.minecraft.world.level.block.Blocks.FIRE) {
                            level().setBlockAndUpdate(targetPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
                        } else if (canGrief && random.nextFloat() < 0.35f) {
                            BlockPos surfacePos = targetPos.below();
                            if (level().getBlockState(surfacePos).isSolidRender(level(), surfacePos) && level().getBlockState(targetPos).isAir()) {
                                level().setBlockAndUpdate(targetPos, net.minecraft.world.level.block.Blocks.SNOW.defaultBlockState());
                            }
                        }
                    }
                }
            }
        }
    }

    private void onShatter(ServerLevel serverLevel) {
        Vec3 pos = position();

        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 4.0f, 0.8f);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.WEATHER, 3.5f, 1.2f);

        // Burst of ice shrapnel
        serverLevel.sendParticles(ParticleTypes.FLASH, pos.x, pos.y + 1.5, pos.z, 3, 0.5, 0.5, 0.5, 0);
        serverLevel.sendParticles(ParticleTypes.ITEM_SNOWBALL, pos.x, pos.y + 1.5, pos.z, 120, 3.5, 2.0, 3.5, 0.3);
        serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y + 1.5, pos.z, 80, 4.0, 2.0, 4.0, 0.2);

        // Final shatter explosion damage
        double shatterRadius = 10.0;
        List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(shatterRadius));
        for (LivingEntity target : targets) {
            if (!isValidTarget(target)) continue;

            target.hurt(level().damageSources().magic(), 20.0f);
            Vec3 knock = target.position().subtract(pos).normalize().scale(1.3).add(0, 0.4, 0);
            target.setDeltaMovement(knock);
            if (target instanceof ServerPlayer player) {
                player.connection.send(new ClientboundSetEntityMotionPacket(player));
            }
        }
    }

    private void spawnStormParticles() {
        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 6; i++) {
                double angle = (stormTicks * 0.3) + (i * Math.PI / 3.0);
                double r = 2.0 + (random.nextDouble() * 6.0);
                double px = getX() + Math.cos(angle) * r;
                double py = getY() + (random.nextDouble() * 3.5);
                double pz = getZ() + Math.sin(angle) * r;

                // Swirling vector
                double vx = -Math.sin(angle) * 0.4;
                double vz = Math.cos(angle) * 0.4;

                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, px, py, pz, 1, vx, 0.05, vz, 0.05);
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.stormTicks = tag.getInt("StormTicks");
        if (tag.contains("Shattered")) {
            this.entityData.set(SHATTERED, tag.getBoolean("Shattered"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("StormTicks", stormTicks);
        tag.putBoolean("Shattered", this.entityData.get(SHATTERED));
    }
}
