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
            // Phase 2: Holy Sanctuary of Astraea (Lingering Aftermath Zone - 12s)
            // ==========================================
            postImpactTicks++;

            if (level().isClientSide) {
                // Sacred rotating ground mandala
                double radius = 8.5;
                for (int i = 0; i < 4; i++) {
                    double angle = (postImpactTicks * 0.05) + (i * Math.PI / 2.0);
                    level().addParticle(ParticleTypes.END_ROD, tx + radius * Math.cos(angle), ty + 0.15, tz + radius * Math.sin(angle), 0, 0.04, 0);
                    level().addParticle(ParticleTypes.GLOW, tx + (radius * 0.5) * Math.cos(-angle), ty + 0.2, tz + (radius * 0.5) * Math.sin(-angle), 0, 0.02, 0);
                }
                // Light beams rising from the monolith core
                if (random.nextInt(3) == 0) {
                    level().addParticle(ParticleTypes.FIREWORK, tx + (random.nextDouble() - 0.5) * 1.5, ty + 0.5, tz + (random.nextDouble() - 0.5) * 1.5, 0, 0.25, 0);
                }
            } else if (level() instanceof ServerLevel serverLevel) {
                // Every 15 ticks (~0.75s): Holy Sanctuary Aura Pulses
                if (postImpactTicks % 15 == 0) {
                    LivingEntity caster = getCaster();

                    // 1. Bless caster and allies
                    List<LivingEntity> allies = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(12.0),
                            e -> e == caster || (caster != null && caster.isAlliedTo(e)));
                    for (LivingEntity ally : allies) {
                        ally.heal(4.0f);
                        ally.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 1, false, false, true));
                        ally.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 80, 0, false, false, true));
                        ally.removeEffect(MobEffects.WITHER);
                        ally.removeEffect(MobEffects.POISON);
                        ally.removeEffect(MobEffects.BLINDNESS);
                        ally.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, ally.getX(), ally.getY() + 1.0, ally.getZ(), 4, 0.3, 0.3, 0.3, 0.02);
                    }

                    // 2. Smite enemies in sanctuary zone
                    List<LivingEntity> enemies = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(10.0),
                            this::isValidTarget);
                    for (LivingEntity enemy : enemies) {
                        float holyDamage = enemy.isInvertedHealAndHarm() ? 10.0f : 5.0f; // Extra smite against undead
                        enemy.hurt(level().damageSources().magic(), holyDamage);
                        enemy.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 0, false, false));
                        enemy.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1, false, false));
                        serverLevel.sendParticles(ParticleTypes.GLOW, enemy.getX(), enemy.getY() + enemy.getBbHeight() / 2, enemy.getZ(), 6, 0.2, 0.2, 0.2, 0.02);
                    }

                    // Audio pulse
                    if (postImpactTicks % 30 == 0) {
                        level().playSound(null, tx, ty, tz, SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, 1.6f, 1.5f);
                    }
                }

                // Despawn after 240 ticks (12 full seconds)
                if (postImpactTicks >= 240) {
                    serverLevel.sendParticles(ParticleTypes.END_ROD, tx, ty + 3.0, tz, 120, 3.0, 4.0, 3.0, 0.3);
                    serverLevel.sendParticles(ParticleTypes.FLASH, tx, ty + 3.0, tz, 4, 0.5, 0.5, 0.5, 0);
                    level().playSound(null, tx, ty, tz, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 2.5f, 1.1f);
                    level().playSound(null, tx, ty, tz, SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 2.0f, 1.4f);
                    this.discard();
                }
            }
        }
    }

    private void performCataclysmicImpact(float x, float y, float z) {
        if (level() instanceof ServerLevel serverLevel) {
            // Cataclysmic Multi-layer Audio
            level().playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 4.0f, 0.6f);
            level().playSound(null, x, y, z, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 3.5f, 0.85f);
            level().playSound(null, x, y, z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 3.0f, 0.75f);
            level().playSound(null, x, y, z, SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 3.0f, 1.6f);

            // Blinding visual shockwave explosion
            serverLevel.sendParticles(ParticleTypes.FLASH, x, y + 1.0, z, 8, 1.0, 1.0, 1.0, 0);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y + 1.0, z, 4, 1.0, 1.0, 1.0, 0);
            serverLevel.sendParticles(ParticleTypes.END_ROD, x, y + 1.0, z, 150, 6.0, 3.0, 6.0, 0.45);

            // 3 Concentric Sonic Boom Expanding Rings
            for (int ring = 1; ring <= 3; ring++) {
                double waveR = ring * 3.8;
                for (int i = 0; i < 24; i++) {
                    double angle = (i * Math.PI * 2.0) / 24.0;
                    serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, x + waveR * Math.cos(angle), y + 0.3, z + waveR * Math.sin(angle), 1, 0, 0, 0, 0);
                }
            }

            // Damage and kinetic knockback
            double impactRadius = 12.0;
            List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(impactRadius, x, y, z, level());
            for (LivingEntity target : targets) {
                if (!isValidTarget(target)) continue;

                target.hurt(level().damageSources().magic(), 65.0f);

                // Violent radial shockwave impulse
                Vec3 diff = target.position().subtract(new Vec3(x, y, z));
                if (diff.lengthSqr() > 0.001) {
                    Vec3 dir = diff.normalize();
                    target.setDeltaMovement(target.getDeltaMovement().add(dir.x * 2.2, 0.95, dir.z * 2.2));
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
