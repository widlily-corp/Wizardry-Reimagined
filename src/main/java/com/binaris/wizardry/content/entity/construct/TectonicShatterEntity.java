package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.registry.EBEntities;
import net.minecraft.core.particles.BlockParticleOption;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Tectonic Shatter entity for "Tectonic Shatter: Titan's Grasp" Grand Earth Spell.
 * Phase 0: Seismic eruption bursts deepslate earth spikes upward (55 damage, massive vertical launch, stun).
 * Phase 1: Standing stone monoliths persist as battlefield cover for 3 seconds, then crumble.
 */
public class TectonicShatterEntity extends MagicConstructEntity {
    private static final EntityDataAccessor<Boolean> ERUPTED = SynchedEntityData.defineId(TectonicShatterEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int LIFETIME_TICKS = 240; // 12 seconds
    private int activeTicks = 0;

    public TectonicShatterEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.noPhysics = true;
    }

    public TectonicShatterEntity(Level world, double x, double y, double z) {
        this(EBEntities.TECTONIC_SHATTER.get(), world);
        setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ERUPTED, false);
    }

    public boolean hasErupted() {
        return this.entityData.get(ERUPTED);
    }

    public int getActiveTicks() {
        return this.activeTicks;
    }

    @Override
    public void tick() {
        super.tick();

        if (activeTicks == 0) {
            onSeismicEruption();
            if (!level().isClientSide) {
                this.entityData.set(ERUPTED, true);
            }
        }

        activeTicks++;

        // =========================================================================
        // Phase 2: 50-BLOCK GEOTHERMAL FAULTLINE & STEAM GEYSERS (12 SECONDS)
        // =========================================================================
        if (activeTicks < LIFETIME_TICKS) {
            // Geothermal steam geysers erupting across the 50-block battlefield
            // 12 vent coordinates across radii 8, 18, 30, 42 blocks
            double[][] vents = {
                    {7.0, 4.0}, {-6.0, 7.0}, {5.0, -8.0}, {-7.0, -5.0},
                    {16.0, 9.0}, {-17.0, 10.0}, {12.0, -15.0}, {-14.0, -14.0},
                    {28.0, 12.0}, {-29.0, 10.0}, {18.0, -26.0}, {-24.0, -22.0}
            };

            // Client and server particle fountains
            if (level().isClientSide) {
                // Pick 3-4 active geysers per tick to erupt
                for (int g = 0; g < 4; g++) {
                    int vIdx = (activeTicks * 3 + g) % vents.length;
                    double vx = vents[vIdx][0] + (random.nextDouble() - 0.5) * 2.0;
                    double vz = vents[vIdx][1] + (random.nextDouble() - 0.5) * 2.0;

                    // Geyser steam jet shooting 12-15 blocks into the sky
                    level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, getX() + vx, getY() + 0.1, getZ() + vz, 0, 0.45 + random.nextDouble() * 0.45, 0);
                    level().addParticle(ParticleTypes.LAVA, getX() + vx, getY() + 0.2, getZ() + vz, (random.nextDouble() - 0.5) * 0.1, 0.25, (random.nextDouble() - 0.5) * 0.1);
                    level().addParticle(ParticleTypes.FLAME, getX() + vx, getY() + 0.1, getZ() + vz, 0, 0.15, 0);
                }

                // Monolith base ground dust
                for (int i = 0; i < 3; i++) {
                    double ox = (random.nextDouble() - 0.5) * 12.0;
                    double oz = (random.nextDouble() - 0.5) * 12.0;
                    level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, getX() + ox, getY() + 0.2, getZ() + oz, 0, 0.1, 0);
                }

                // Shaking audio cue in final seconds
                if (activeTicks > 210 && activeTicks % 5 == 0) {
                    level().playLocalSound(getX(), getY(), getZ(), SoundEvents.STONE_HIT, SoundSource.PLAYERS, 4.0f, 0.6f, false);
                }
            } else if (level() instanceof ServerLevel serverLevel) {
                // Subterranean tectonic rumble audio (loud 10.0f volume audible across 160 blocks)
                if (activeTicks % 15 == 0) {
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.WARDEN_DIG, SoundSource.WEATHER, 10.0f, 0.55f);
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.MINECART_RIDING, SoundSource.WEATHER, 8.0f, 0.5f);
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.WEATHER, 6.0f, 0.6f);
                }

                // Every 10 ticks (~0.5s): 48-Block Seismic Tremor & Armor Shatter Hazard
                if (activeTicks % 10 == 0) {
                    List<LivingEntity> enemies = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(48.0), this::isValidTarget);
                    for (LivingEntity enemy : enemies) {
                        enemy.hurt(level().damageSources().magic(), 5.0f);
                        enemy.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 3, false, false));
                        enemy.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 2, false, false));
                        serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, enemy.getX(), enemy.getY() + 0.2, enemy.getZ(), 3, 0.2, 0.1, 0.2, 0.05);
                    }
                }
            }
        } else {
            // =========================================================================
            // FINALE: COLOSSAL STONE AVALANCHE & MONOLITH COLLAPSE CRUMBLE (TICK 240)
            // =========================================================================
            Vec3 pos = position();
            if (!level().isClientSide) {
                level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.STONE_BREAK, SoundSource.PLAYERS, 14.0f, 0.5f);
                level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.WEATHER, 12.0f, 0.55f);
                level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.WEATHER, 10.0f, 0.6f);
            }

            if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
                // Expanding sonic boom shockwaves across 30 blocks
                for (int r = 1; r <= 4; r++) {
                    double speed = r * 0.55;
                    for (int i = 0; i < 36; i++) {
                        double angle = (Math.PI * 2 / 36) * i;
                        serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, pos.x, pos.y + 0.3, pos.z, 1, Math.cos(angle) * speed, 0.05, Math.sin(angle) * speed, 0.1);
                    }
                }

                // 400 deepslate, basalt and smoke avalanche rubble particles
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DEEPSLATE.defaultBlockState()),
                        pos.x, pos.y + 2.0, pos.z, 300, 6.0, 3.5, 6.0, 0.4);
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.BASALT.defaultBlockState()),
                        pos.x, pos.y + 2.0, pos.z, 200, 5.0, 3.0, 5.0, 0.35);
                serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        pos.x, pos.y + 1.5, pos.z, 120, 5.0, 2.0, 5.0, 0.1);
                serverLevel.sendParticles(ParticleTypes.FLASH, pos.x, pos.y + 2.0, pos.z, 8, 1.0, 1.0, 1.0, 0);

                // Avalanche knockback to nearby entities
                List<LivingEntity> nearby = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(20.0), this::isValidTarget);
                for (LivingEntity entity : nearby) {
                    entity.hurt(level().damageSources().magic(), 25.0f);
                    Vec3 push = entity.position().subtract(pos).normalize().scale(1.8).add(0, 0.6, 0);
                    entity.setDeltaMovement(entity.getDeltaMovement().add(push));
                    if (entity instanceof ServerPlayer player) {
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));
                    }
                }
            }
            discard();
        }
    }

    private void onSeismicEruption() {
        Vec3 pos = position();

        // Layered colossal earthquake soundscape (volume 12.0f audible across 190 blocks)
        if (!level().isClientSide) {
            level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.WEATHER, 12.0f, 0.55f);
            level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 10.0f, 0.4f);
            level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.WEATHER, 10.0f, 0.5f);
            level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.RAVAGER_STUNNED, SoundSource.PLAYERS, 8.0f, 0.65f);
            level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.IRON_GOLEM_ATTACK, SoundSource.PLAYERS, 8.0f, 0.7f);
        }

        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            // Seismic shockwave rings
            serverLevel.sendParticles(ParticleTypes.FLASH, pos.x, pos.y + 0.5, pos.z, 6, 0.5, 0.5, 0.5, 0);

            for (int r = 1; r <= 4; r++) {
                double speed = r * 0.55;
                for (int i = 0; i < 36; i++) {
                    double angle = (Math.PI * 2 / 36) * i;
                    double vx = Math.cos(angle) * speed;
                    double vz = Math.sin(angle) * speed;
                    serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, pos.x, pos.y + 0.2, pos.z, 1, vx, 0.05, vz, 0.1);
                }
            }

            // Seismic dust and rubble burst
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DEEPSLATE.defaultBlockState()),
                    pos.x, pos.y + 1.0, pos.z, 160, 4.0, 2.5, 4.0, 0.3);
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.BASALT.defaultBlockState()),
                    pos.x, pos.y + 1.0, pos.z, 100, 3.5, 2.0, 3.5, 0.25);
            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.x, pos.y + 0.5, pos.z, 45, 3.5, 0.8, 3.5, 0.06);
            serverLevel.sendParticles(ParticleTypes.LAVA, pos.x, pos.y + 0.8, pos.z, 35, 2.5, 1.2, 2.5, 0.1);

            // Damage, launch skyward and shatter armor
            double shatterRadius = 14.0;
            List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(shatterRadius));
            for (LivingEntity target : targets) {
                if (!isValidTarget(target)) continue;

                target.hurt(level().damageSources().magic(), 65.0f);
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 240, 3)); // Slowness IV for 12s
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 240, 2)); // Weakness III (Armor Shatter) for 12s

                // Violent vertical launch upward and outward
                Vec3 diff = target.position().subtract(pos);
                Vec3 horiz = diff.lengthSqr() > 0.01 ? diff.normalize().scale(1.25) : Vec3.ZERO;
                target.setDeltaMovement(target.getDeltaMovement().add(horiz.x, 1.6, horiz.z));
                if (target instanceof ServerPlayer player) {
                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                }
            }

            // Environmental Terrain Scars (Fissure cracks, basalt teeth & magma vents)
            net.minecraft.core.BlockPos centerPos = blockPosition();
            boolean canGrief = com.binaris.wizardry.api.content.util.EntityUtil.canDamageBlocks(getCaster(), level());
            for (int x = -4; x <= 4; x++) {
                for (int z = -4; z <= 4; z++) {
                    int distSq = x * x + z * z;
                    if (distSq <= 18) {
                        for (int y = -2; y <= 2; y++) {
                            net.minecraft.core.BlockPos targetPos = centerPos.offset(x, y, z);
                            net.minecraft.world.level.block.state.BlockState state = level().getBlockState(targetPos);

                            // Turn water into solid stone
                            if (state.getBlock() == Blocks.WATER) {
                                level().setBlockAndUpdate(targetPos, Blocks.STONE.defaultBlockState());
                            } else if (canGrief && !state.isAir() && state.isSolid() && level().getBlockState(targetPos.above()).isAir()) {
                                if (distSq <= 4 && random.nextFloat() < 0.65f) {
                                    level().setBlockAndUpdate(targetPos, Blocks.MAGMA_BLOCK.defaultBlockState());
                                } else if (distSq <= 10) {
                                    level().setBlockAndUpdate(targetPos, Blocks.POLISHED_BASALT.defaultBlockState());
                                } else if (random.nextFloat() < 0.5f) {
                                    level().setBlockAndUpdate(targetPos, Blocks.BASALT.defaultBlockState());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.activeTicks = tag.getInt("ActiveTicks");
        if (tag.contains("Erupted")) {
            this.entityData.set(ERUPTED, tag.getBoolean("Erupted"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ActiveTicks", activeTicks);
        tag.putBoolean("Erupted", this.entityData.get(ERUPTED));
    }
}
