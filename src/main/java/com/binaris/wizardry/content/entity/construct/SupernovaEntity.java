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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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
            // Phase 2: Expanding solar blast wave
            postImpactTicks++;
            spawnPostImpactParticles(postImpactTicks);

            if (postImpactTicks > 30) {
                discard();
            }
        }
    }

    private void onImpact() {
        Vec3 pos = position();

        // 1. Play cataclysmic explosion sounds
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.WEATHER, 5.0f, 0.6f);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 4.0f, 0.7f);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.BEACON_DEACTIVATE, SoundSource.WEATHER, 3.0f, 0.5f);

        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            // Huge flash + blast
            serverLevel.sendParticles(ParticleTypes.FLASH, pos.x, pos.y + 0.5, pos.z, 5, 0.5, 0.5, 0.5, 0.0);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, pos.x, pos.y + 1.0, pos.z, 6, 1.5, 1.5, 1.5, 0.0);

            // Ring of fire on blast
            for (int i = 0; i < 48; i++) {
                double angle = (Math.PI * 2 / 48) * i;
                double speed = 1.2;
                double vx = Math.cos(angle) * speed;
                double vz = Math.sin(angle) * speed;
                serverLevel.sendParticles(ParticleTypes.FLAME, pos.x, pos.y + 0.3, pos.z, 2, vx, 0.2, vz, 0.15);
                serverLevel.sendParticles(ParticleTypes.LAVA, pos.x, pos.y + 0.5, pos.z, 1, vx * 0.8, 0.3, vz * 0.8, 0.1);
            }

            // 2. Damage, incinerate and knockback entities
            float blastRadius = 12.0f;
            List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(blastRadius));
            Entity caster = getOwner();

            for (LivingEntity target : targets) {
                if (!isValidTarget(target)) {
                    continue;
                }

                double dist = target.distanceTo(this);
                if (dist <= blastRadius) {
                    float damageMultiplier = (float) (1.0 - (dist / blastRadius) * 0.4);
                    float totalDamage = 55.0f * damageMultiplier;

                    target.hurt(level().damageSources().magic(), totalDamage);
                    target.setRemainingFireTicks(400); // 20 seconds fire

                    // Outward + upward knockback
                    Vec3 knock = target.position().subtract(pos).normalize().scale(1.4).add(0, 0.75, 0);
                    target.setDeltaMovement(knock);
                    if (target instanceof ServerPlayer player) {
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));
                    }
                }
            }

            // 3. Ignite a few safe blocks around center
            BlockPos centerPos = blockPosition();
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if (random.nextFloat() < 0.45f) {
                        BlockPos targetPos = centerPos.offset(x, 0, z);
                        if (level().getBlockState(targetPos).isAir() && level().getBlockState(targetPos.below()).isSolidRender(level(), targetPos.below())) {
                            level().setBlockAndUpdate(targetPos, Blocks.FIRE.defaultBlockState());
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
