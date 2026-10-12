package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.registry.EBDamageSources;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Supernova entity for "Wrath of Helios" Grand Fire Spell.
 * Phase 0: Gathering plasma in sky (25 ticks).
 * Phase 1: High-velocity dive to ground (impact).
 * Phase 2: Massive solar explosion, expanding fire shockwave and incineration.
 */
public class SupernovaEntity extends MagicConstructEntity {
    private static final EntityDataAccessor<Float> TARGET_X = SynchedEntityData.defineId(SupernovaEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TARGET_Y = SynchedEntityData.defineId(SupernovaEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TARGET_Z = SynchedEntityData.defineId(SupernovaEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> IMPACTED = SynchedEntityData.defineId(SupernovaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> CHARGE_TICKS = SynchedEntityData.defineId(SupernovaEntity.class, EntityDataSerializers.INT);

    private static final int CHARGE_DURATION = 25;
    private int postImpactTicks = 0;

    public SupernovaEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.noPhysics = true;
    }

    public SupernovaEntity(Level world, double targetX, double targetY, double targetZ) {
        this(EBEntities.SUPERNOVA.get(), world);
        setTargetPos((float) targetX, (float) targetY, (float) targetZ);
        setPos(targetX, targetY + 24.0, targetZ);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TARGET_X, 0.0f);
        builder.define(TARGET_Y, 0.0f);
        builder.define(TARGET_Z, 0.0f);
        builder.define(IMPACTED, false);
        builder.define(CHARGE_TICKS, 0);
    }

    public void setTargetPos(float x, float y, float z) {
        this.entityData.set(TARGET_X, x);
        this.entityData.set(TARGET_Y, y);
        this.entityData.set(TARGET_Z, z);
    }

    public Vec3 getTargetPos() {
        return new Vec3(this.entityData.get(TARGET_X), this.entityData.get(TARGET_Y), this.entityData.get(TARGET_Z));
    }

    public boolean hasImpacted() {
        return this.entityData.get(IMPACTED);
    }

    public int getChargeTicks() {
        return this.entityData.get(CHARGE_TICKS);
    }

    @Override
    public void tick() {
        super.tick();

        int charge = getChargeTicks();
        boolean impacted = hasImpacted();

        if (!impacted) {
            if (charge < CHARGE_DURATION) {
                // Phase 0: Concentrating solar energy high above
                this.entityData.set(CHARGE_TICKS, charge + 1);
                spawnChargingParticles(charge);
                if (charge % 6 == 0) {
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.5f, 0.6f + (charge * 0.03f));
                }
            } else {
                // Phase 1: High velocity descent
                Vec3 target = getTargetPos();
                double dy = target.y - getY();

                spawnFallingParticles();

                if (dy >= -1.8) {
                    // Impact reached!
                    setPos(target.x, target.y, target.z);
                    this.entityData.set(IMPACTED, true);
                    onImpact();
                } else {
                    setPos(getX(), getY() - 1.8, getZ());
                }
            }
        } else {
            // ==========================================
            // Phase 2: Scorched Supernova Crater (Lingering Aftermath Zone - 12s)
            // ==========================================
            postImpactTicks++;

            if (level().isClientSide) {
                // Rising volcanic smoke pillars and lava cinders across the crater
                for (int i = 0; i < 3; i++) {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double r = random.nextDouble() * 8.0;
                    double px = getX() + Math.cos(angle) * r;
                    double pz = getZ() + Math.sin(angle) * r;
                    level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, px, getY() + 0.2, pz, 0, 0.08, 0);
                    level().addParticle(ParticleTypes.FLAME, px, getY() + 0.1, pz, (random.nextDouble() - 0.5) * 0.05, 0.05, (random.nextDouble() - 0.5) * 0.05);
                }
            } else if (level() instanceof ServerLevel serverLevel) {
                // Every 10 ticks (~0.5s): Scorched Crater Hazard Pulse
                if (postImpactTicks % 10 == 0) {
                    List<LivingEntity> enemies = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(10.0),
                            this::isValidTarget);
                    for (LivingEntity enemy : enemies) {
                        enemy.hurt(level().damageSources().inFire(), 6.0f);
                        enemy.setRemainingFireTicks(160);
                        enemy.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1, false, false));
                        serverLevel.sendParticles(ParticleTypes.LAVA, enemy.getX(), enemy.getY() + 0.5, enemy.getZ(), 3, 0.2, 0.2, 0.2, 0.02);
                    }

                    if (postImpactTicks % 30 == 0) {
                        level().playSound(null, getX(), getY(), getZ(), SoundEvents.FIRE_AMBIENT, SoundSource.WEATHER, 1.8f, 0.8f);
                    }
                }

                // Discard after 240 ticks (12 full seconds)
                if (postImpactTicks >= 240) {
                    serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, getX(), getY() + 1.0, getZ(), 60, 4.0, 1.0, 4.0, 0.05);
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.WEATHER, 2.5f, 0.8f);
                    discard();
                }
            }
        }
    }

    private void onImpact() {
        Vec3 pos = position();

        // 1. Play cataclysmic explosion sounds
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.WEATHER, 6.0f, 0.55f);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 5.0f, 0.7f);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.BEACON_DEACTIVATE, SoundSource.WEATHER, 3.5f, 0.5f);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.FIRECHARGE_USE, SoundSource.WEATHER, 4.0f, 0.6f);

        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            // Massive flash + multi-stage blast emitters
            serverLevel.sendParticles(ParticleTypes.FLASH, pos.x, pos.y + 0.5, pos.z, 8, 1.0, 1.0, 1.0, 0.0);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, pos.x, pos.y + 1.0, pos.z, 8, 2.0, 2.0, 2.0, 0.0);
            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.x, pos.y + 1.0, pos.z, 80, 5.0, 3.0, 5.0, 0.1);

            // Expanding rings of fire and lava
            for (int r = 1; r <= 3; r++) {
                double speed = r * 0.55;
                for (int i = 0; i < 36; i++) {
                    double angle = (Math.PI * 2 / 36) * i;
                    double vx = Math.cos(angle) * speed;
                    double vz = Math.sin(angle) * speed;
                    serverLevel.sendParticles(ParticleTypes.FLAME, pos.x, pos.y + 0.3, pos.z, 2, vx, 0.25, vz, 0.12);
                    serverLevel.sendParticles(ParticleTypes.LAVA, pos.x, pos.y + 0.5, pos.z, 1, vx * 0.7, 0.3, vz * 0.7, 0.08);
                }
            }

            // 2. Colossal damage, incineration and massive knockback
            float blastRadius = 14.0f;
            List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(blastRadius));

            for (LivingEntity target : targets) {
                if (!isValidTarget(target)) {
                    continue;
                }

                double dist = target.distanceTo(this);
                if (dist <= blastRadius) {
                    float damageMultiplier = (float) (1.0 - (dist / blastRadius) * 0.35);
                    float totalDamage = 75.0f * damageMultiplier;

                    target.hurt(level().damageSources().inFire(), totalDamage);
                    target.setRemainingFireTicks(400); // 20 seconds fire

                    // Outward + upward explosion knockback
                    Vec3 knock = target.position().subtract(pos).normalize().scale(2.2).add(0, 0.9, 0);
                    target.setDeltaMovement(knock);
                    if (target instanceof ServerPlayer player) {
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));
                    }
                }
            }

            // 3. Environmental Scorch (terrain conversion & water vaporisation)
            BlockPos centerPos = blockPosition();
            boolean canGrief = EntityUtil.canDamageBlocks(getCaster(), level());
            for (int x = -4; x <= 4; x++) {
                for (int z = -4; z <= 4; z++) {
                    if (x * x + z * z <= 18) {
                        BlockPos targetPos = centerPos.offset(x, 0, z);
                        BlockState state = level().getBlockState(targetPos);

                        // Flash-vaporise water
                        if (state.getBlock() == Blocks.WATER) {
                            level().setBlockAndUpdate(targetPos, Blocks.AIR.defaultBlockState());
                            serverLevel.sendParticles(ParticleTypes.POOF, targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5, 4, 0.2, 0.2, 0.2, 0.05);
                            level().playSound(null, targetPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.8f, 1.2f);
                        } else if (state.getBlock() == Blocks.ICE) {
                            level().setBlockAndUpdate(targetPos, Blocks.WATER.defaultBlockState());
                        } else if (canGrief && random.nextFloat() < 0.35f) {
                            BlockPos surfacePos = targetPos.below();
                            if (level().getBlockState(surfacePos).isSolidRender(level(), surfacePos) && level().getBlockState(targetPos).isAir()) {
                                if (random.nextBoolean()) {
                                    level().setBlockAndUpdate(surfacePos, Blocks.MAGMA_BLOCK.defaultBlockState());
                                } else {
                                    level().setBlockAndUpdate(targetPos, Blocks.FIRE.defaultBlockState());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void spawnChargingParticles(int charge) {
        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            double radius = 1.5 + (charge * 0.06);
            for (int i = 0; i < 10; i++) {
                double u = random.nextDouble() * Math.PI * 2;
                double v = (random.nextDouble() - 0.5) * Math.PI;
                double px = getX() + radius * Math.cos(v) * Math.cos(u);
                double py = getY() + radius * Math.sin(v);
                double pz = getZ() + radius * Math.cos(v) * Math.sin(u);

                // Inward motion towards core
                double vx = (getX() - px) * 0.2;
                double vy = (getY() - py) * 0.2;
                double vz = (getZ() - pz) * 0.2;

                serverLevel.sendParticles(ParticleTypes.FLAME, px, py, pz, 1, vx, vy, vz, 0.05);
                if (random.nextFloat() < 0.3f) {
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, px, py, pz, 1, vx, vy, vz, 0.05);
                }
            }

            // Target reticle on ground below
            Vec3 target = getTargetPos();
            for (int i = 0; i < 16; i++) {
                double angle = (Math.PI * 2 / 16) * i;
                double rx = target.x + Math.cos(angle) * 3.5;
                double rz = target.z + Math.sin(angle) * 3.5;
                serverLevel.sendParticles(ParticleTypes.SMALL_FLAME, rx, target.y + 0.1, rz, 1, 0, 0.02, 0, 0);
            }
        }
    }

    private void spawnFallingParticles() {
        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 14; i++) {
                double ox = (random.nextDouble() - 0.5) * 1.6;
                double oy = (random.nextDouble() - 0.5) * 1.6;
                double oz = (random.nextDouble() - 0.5) * 1.6;
                serverLevel.sendParticles(ParticleTypes.FLAME, getX() + ox, getY() + oy, getZ() + oz, 1, 0, 0.4, 0, 0.05);
                serverLevel.sendParticles(ParticleTypes.LAVA, getX() + ox, getY() + oy, getZ() + oz, 1, 0, 0.1, 0, 0.02);
            }
        }
    }

    private void spawnPostImpactParticles(int tick) {
        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            double currentRadius = (tick * 0.45);
            if (currentRadius <= 12.0) {
                int count = (int) (currentRadius * 6);
                for (int i = 0; i < count; i++) {
                    double angle = (Math.PI * 2 / count) * i;
                    double px = getX() + Math.cos(angle) * currentRadius;
                    double pz = getZ() + Math.sin(angle) * currentRadius;
                    serverLevel.sendParticles(ParticleTypes.FLAME, px, getY() + 0.2, pz, 1, 0, 0.05, 0, 0.02);
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("TargetX")) {
            setTargetPos(tag.getFloat("TargetX"), tag.getFloat("TargetY"), tag.getFloat("TargetZ"));
        }
        if (tag.contains("Impacted")) {
            this.entityData.set(IMPACTED, tag.getBoolean("Impacted"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("TargetX", this.entityData.get(TARGET_X));
        tag.putFloat("TargetY", this.entityData.get(TARGET_Y));
        tag.putFloat("TargetZ", this.entityData.get(TARGET_Z));
        tag.putBoolean("Impacted", this.entityData.get(IMPACTED));
    }
}
