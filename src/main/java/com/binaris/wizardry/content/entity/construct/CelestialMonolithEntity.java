package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.EBEntities;
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
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CelestialMonolithEntity extends MagicConstructEntity {
    private static final EntityDataAccessor<Float> TARGET_X = SynchedEntityData.defineId(CelestialMonolithEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TARGET_Y = SynchedEntityData.defineId(CelestialMonolithEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TARGET_Z = SynchedEntityData.defineId(CelestialMonolithEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> IMPACTED = SynchedEntityData.defineId(CelestialMonolithEntity.class, EntityDataSerializers.BOOLEAN);

    private float fallSpeed = 1.6f;
    private int postImpactTicks = 0;

    public CelestialMonolithEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.noPhysics = true;
    }

    public CelestialMonolithEntity(Level world, double targetX, double targetY, double targetZ) {
        this(EBEntities.CELESTIAL_MONOLITH.get(), world);
        setTargetPos((float) targetX, (float) targetY, (float) targetZ);
        setPos(targetX, targetY + 30.0, targetZ);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TARGET_X, 0.0f);
        builder.define(TARGET_Y, 0.0f);
        builder.define(TARGET_Z, 0.0f);
        builder.define(IMPACTED, false);
    }

    public void setTargetPos(float x, float y, float z) {
        this.entityData.set(TARGET_X, x);
        this.entityData.set(TARGET_Y, y);
        this.entityData.set(TARGET_Z, z);
    }

    public float getTargetX() {
        return this.entityData.get(TARGET_X);
    }

    public float getTargetY() {
        return this.entityData.get(TARGET_Y);
    }

    public float getTargetZ() {
        return this.entityData.get(TARGET_Z);
    }

    public boolean isImpacted() {
        return this.entityData.get(IMPACTED);
    }

    @Override
    public void tick() {
        super.tick();

        float tx = getTargetX();
        float ty = getTargetY();
        float tz = getTargetZ();

        if (!isImpacted()) {
            // ==========================================
            // Phase 1: Descending from the celestial portal
            // ==========================================
            double newY = getY() - fallSpeed;
            fallSpeed += 0.08f;
            setPos(tx, newY, tz);

            // Client celestial portal and ground reticle particles
            if (level().isClientSide) {
                double portalY = ty + 30.0;
                // Double concentric celestial star circle in the sky
                for (int i = 0; i < 8; i++) {
                    float angle = (tickCount * 0.15f) + (float) (i * Math.PI / 4.0);
                    double r1 = 6.0;
                    double r2 = 9.0;
                    level().addParticle(ParticleTypes.END_ROD, tx + r1 * Mth.cos(angle), portalY, tz + r1 * Mth.sin(angle), 0, 0.02, 0);
                    level().addParticle(ParticleTypes.PORTAL, tx + r2 * Mth.cos(-angle), portalY, tz + r2 * Mth.sin(-angle), 0, 0, 0);
                }
                // Ground target beacon ring
                for (int i = 0; i < 4; i++) {
                    float angle = (float) (random.nextDouble() * Math.PI * 2);
                    double r = 4.0 + random.nextDouble() * 2.0;
                    level().addParticle(ParticleTypes.GLOW, tx + r * Mth.cos(angle), ty + 0.1, tz + r * Mth.sin(angle), 0, 0.02, 0);
                }
                // Comet trail falling behind the monolith
                for (int i = 0; i < 5; i++) {
                    level().addParticle(ParticleTypes.END_ROD, tx + (random.nextDouble() - 0.5) * 1.5, getY() + i * 2.0, tz + (random.nextDouble() - 0.5) * 1.5, 0, -0.2, 0);
                }
            }

            // Check impact
            if (newY <= ty) {
                setPos(tx, ty, tz);
                this.entityData.set(IMPACTED, true);
                performCataclysmicImpact(tx, ty, tz);
            }
        } else {
            // ==========================================
            // Phase 2: Embedded in the ground, pulsing astral radiance
            // ==========================================
            postImpactTicks++;

            if (level().isClientSide) {
                // Astral glow and star sparks from monolith base
                for (int i = 0; i < 3; i++) {
                    level().addParticle(ParticleTypes.END_ROD, tx + (random.nextDouble() - 0.5) * 2.0, ty + random.nextDouble() * 6.0, tz + (random.nextDouble() - 0.5) * 2.0, 0, 0.05, 0);
                }
            } else if (level() instanceof ServerLevel serverLevel) {
                // Every 15 ticks, pulse healing and cleanse to caster and allies
                if (postImpactTicks % 15 == 0) {
                    LivingEntity caster = getCaster();
                    if (caster != null && caster.isAlive() && caster.distanceToSqr(this) <= 400) {
                        caster.heal(3.0f);
                        caster.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1));
                    }
                    serverLevel.sendParticles(ParticleTypes.GLOW, tx, ty + 2.0, tz, 25, 2.0, 1.0, 2.0, 0.05);
                }

                // End of lifetime (4 seconds post impact)
                if (postImpactTicks >= 80) {
                    serverLevel.sendParticles(ParticleTypes.END_ROD, tx, ty + 3.0, tz, 60, 2.0, 3.0, 2.0, 0.2);
                    serverLevel.sendParticles(ParticleTypes.FLASH, tx, ty + 3.0, tz, 2, 0, 0, 0, 0);
                    level().playSound(null, tx, ty, tz, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 2.0f, 1.2f);
                    this.discard();
                }
            }
        }
    }

    private void performCataclysmicImpact(float x, float y, float z) {
        if (level() instanceof ServerLevel serverLevel) {
            // Cataclysmic Audio
            level().playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 2.5f, 0.7f);
            level().playSound(null, x, y, z, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 2.0f, 0.9f);
            level().playSound(null, x, y, z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 2.0f, 0.8f);

            // Visual shockwave explosion
            serverLevel.sendParticles(ParticleTypes.FLASH, x, y + 1.0, z, 4, 0.5, 0.5, 0.5, 0);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, x, y + 1.0, z, 15, 2.0, 1.0, 2.0, 0.1);
            serverLevel.sendParticles(ParticleTypes.END_ROD, x, y + 1.0, z, 80, 4.0, 1.5, 4.0, 0.35);

            // Expanding circular ground shockwave
            for (int i = 0; i < 36; i++) {
                double angle = (i * Math.PI * 2.0) / 36.0;
                double waveR = 8.0;
                serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, x + waveR * Math.cos(angle), y + 0.2, z + waveR * Math.sin(angle), 1, 0, 0, 0, 0);
            }

            // Damage and kinetic knockback
            double impactRadius = 10.0;
            List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(impactRadius, x, y, z, level());
            for (LivingEntity target : targets) {
                if (!isValidTarget(target)) continue;

                // Colossal Grand Tier Damage
                target.hurt(level().damageSources().magic(), 50.0f);

                // Violent radial shockwave impulse
                Vec3 diff = target.position().subtract(new Vec3(x, y, z));
                if (diff.lengthSqr() > 0.001) {
                    Vec3 dir = diff.normalize();
                    target.setDeltaMovement(target.getDeltaMovement().add(dir.x * 1.6, 0.6, dir.z * 1.6));
                    target.hurtMarked = true;
                    if (target instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(target));
                    }
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setTargetPos(tag.getFloat("tx"), tag.getFloat("ty"), tag.getFloat("tz"));
        this.entityData.set(IMPACTED, tag.getBoolean("impacted"));
        this.postImpactTicks = tag.getInt("postTicks");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("tx", getTargetX());
        tag.putFloat("ty", getTargetY());
        tag.putFloat("tz", getTargetZ());
        tag.putBoolean("impacted", isImpacted());
        tag.putInt("postTicks", postImpactTicks);
    }
}
