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
            // =========================================================================
            // Phase 2: 50-BLOCK HOLY SANCTUARY OF ASTRAEA (LINGERING AFTERMATH - 12S)
            // =========================================================================
            postImpactTicks++;

            if (level().isClientSide) {
                // 1. Triple concentric rotating celestial sacred geometry mandalas (radii 10, 25, 45 blocks)
                double angleOuter = (postImpactTicks * 0.03);
                double angleMid = -(postImpactTicks * 0.05);
                double angleInner = (postImpactTicks * 0.08);

                for (int i = 0; i < 6; i++) {
                    double offset = (i * Math.PI / 3.0);
                    // Outer ring (radius 45)
                    level().addParticle(ParticleTypes.END_ROD, tx + 45.0 * Math.cos(angleOuter + offset), ty + 0.2, tz + 45.0 * Math.sin(angleOuter + offset), 0, 0.05, 0);
                    level().addParticle(ParticleTypes.GLOW, tx + 45.0 * Math.cos(angleOuter + offset), ty + 0.3, tz + 45.0 * Math.sin(angleOuter + offset), 0, 0.02, 0);

                    // Mid ring (radius 25)
                    level().addParticle(ParticleTypes.END_ROD, tx + 25.0 * Math.cos(angleMid + offset), ty + 0.2, tz + 25.0 * Math.sin(angleMid + offset), 0, 0.04, 0);

                    // Inner mandala (radius 10)
                    level().addParticle(ParticleTypes.GLOW, tx + 10.0 * Math.cos(angleInner + offset), ty + 0.2, tz + 10.0 * Math.sin(angleInner + offset), 0, 0.03, 0);
                }

                // 2. Eight perimeter celestial beacon light pillars (radius 45 blocks)
                int pillarIdx = (postImpactTicks / 2) % 8;
                double pAngle = pillarIdx * (Math.PI / 4.0);
                double px = tx + 45.0 * Math.cos(pAngle);
                double pz = tz + 45.0 * Math.sin(pAngle);
                level().addParticle(ParticleTypes.END_ROD, px, ty + 0.5, pz, 0, 0.6, 0);
                level().addParticle(ParticleTypes.FLASH, px, ty + 0.5, pz, 0, 0, 0);

                // 3. Ambient falling celestial stars across the 50-block dome
                for (int i = 0; i < 4; i++) {
                    double rx = tx + (random.nextDouble() - 0.5) * 80.0;
                    double rz = tz + (random.nextDouble() - 0.5) * 80.0;
                    if ((rx - tx) * (rx - tx) + (rz - tz) * (rz - tz) <= 2200) {
                        level().addParticle(ParticleTypes.END_ROD, rx, ty + 8.0 + random.nextDouble() * 6.0, rz, 0, -0.06, 0);
                        level().addParticle(ParticleTypes.INSTANT_EFFECT, rx, ty + 1.0, rz, 0, 0.02, 0);
                    }
                }
            } else if (level() instanceof ServerLevel serverLevel) {
                // Cathedral bells and divine resonance audio (volume 10.0f audible across 160 blocks)
                if (postImpactTicks % 20 == 0) {
                    level().playSound(null, tx, ty, tz, SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, 10.0f, 1.2f);
                    level().playSound(null, tx, ty, tz, SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 10.0f, 1.4f);
                    level().playSound(null, tx, ty, tz, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 8.0f, 1.1f);
                }

                // Every 15 ticks (~0.75s): 48-Block Holy Sanctuary Aura Pulses
                if (postImpactTicks % 15 == 0) {
                    LivingEntity caster = getCaster();

                    // 1. Bless caster and allies anywhere within 48 blocks
                    List<LivingEntity> allies = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(48.0),
                            e -> e == caster || (caster != null && caster.isAlliedTo(e)));
                    for (LivingEntity ally : allies) {
                        ally.heal(6.0f);
                        ally.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 1, false, false, true));
                        ally.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 80, 1, false, false, true));
                        ally.removeEffect(MobEffects.WITHER);
                        ally.removeEffect(MobEffects.POISON);
                        ally.removeEffect(MobEffects.BLINDNESS);
                        ally.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, ally.getX(), ally.getY() + 1.0, ally.getZ(), 4, 0.3, 0.3, 0.3, 0.02);
                    }

                    // 2. Smite enemies across 48 blocks
                    List<LivingEntity> enemies = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(48.0),
                            this::isValidTarget);
                    for (LivingEntity enemy : enemies) {
                        float holyDamage = enemy.isInvertedHealAndHarm() ? 12.0f : 6.0f; // Massive smite against undead
                        enemy.hurt(level().damageSources().magic(), holyDamage);
                        enemy.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 0, false, false));
                        enemy.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1, false, false));
                        serverLevel.sendParticles(ParticleTypes.GLOW, enemy.getX(), enemy.getY() + enemy.getBbHeight() / 2, enemy.getZ(), 6, 0.2, 0.2, 0.2, 0.02);
                    }
                }

                // Despawn after 240 ticks (12 full seconds) with grand celestial nova
                if (postImpactTicks >= 240) {
                    serverLevel.sendParticles(ParticleTypes.END_ROD, tx, ty + 4.0, tz, 250, 8.0, 6.0, 8.0, 0.35);
                    serverLevel.sendParticles(ParticleTypes.FLASH, tx, ty + 4.0, tz, 8, 1.0, 1.0, 1.0, 0);
                    level().playSound(null, tx, ty, tz, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 12.0f, 1.1f);
                    level().playSound(null, tx, ty, tz, SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 10.0f, 1.3f);
                    this.discard();
                }
            }
        }
    }

    private void performCataclysmicImpact(float x, float y, float z) {
        if (level() instanceof ServerLevel serverLevel) {
            // Cataclysmic Multi-layer Audio (volume 12.0f audible across 190 blocks)
            level().playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 12.0f, 0.6f);
            level().playSound(null, x, y, z, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 10.0f, 0.85f);
            level().playSound(null, x, y, z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 10.0f, 0.75f);
            level().playSound(null, x, y, z, SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 8.0f, 1.6f);

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
