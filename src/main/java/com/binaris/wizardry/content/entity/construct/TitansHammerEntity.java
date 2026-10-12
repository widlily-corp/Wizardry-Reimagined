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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Titan's Hammer entity for "Titan's Cataclysm" Grand Lightning Spell.
 * Descends from skies with thunder arcs, smashes ground causing real lightning strikes,
 * seismic shockwave and catastrophic 60 damage.
 */
public class TitansHammerEntity extends MagicConstructEntity {
    private static final EntityDataAccessor<Float> TARGET_X = SynchedEntityData.defineId(TitansHammerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TARGET_Y = SynchedEntityData.defineId(TitansHammerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TARGET_Z = SynchedEntityData.defineId(TitansHammerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> IMPACTED = SynchedEntityData.defineId(TitansHammerEntity.class, EntityDataSerializers.BOOLEAN);

    private int postImpactTicks = 0;

    public TitansHammerEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.noPhysics = true;
    }

    public TitansHammerEntity(Level world, double targetX, double targetY, double targetZ) {
        this(EBEntities.TITANS_HAMMER.get(), world);
        setTargetPos((float) targetX, (float) targetY, (float) targetZ);
        setPos(targetX, targetY + 26.0, targetZ);
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

    public Vec3 getTargetPos() {
        return new Vec3(this.entityData.get(TARGET_X), this.entityData.get(TARGET_Y), this.entityData.get(TARGET_Z));
    }

    public boolean hasImpacted() {
        return this.entityData.get(IMPACTED);
    }

    @Override
    public void tick() {
        super.tick();

        if (!hasImpacted()) {
            Vec3 target = getTargetPos();
            double dy = target.y - getY();

            if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 8; i++) {
                    double ox = (random.nextDouble() - 0.5) * 2.0;
                    double oy = (random.nextDouble() - 0.5) * 2.0;
                    double oz = (random.nextDouble() - 0.5) * 2.0;
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, getX() + ox, getY() + oy, getZ() + oz, 1, 0, 0, 0, 0);
                }
            }

            if (dy >= -2.2) {
                setPos(target.x, target.y, target.z);
                this.entityData.set(IMPACTED, true);
                onImpact();
            } else {
                setPos(getX(), getY() - 2.2, getZ());
            }
        } else {
            // =========================================================================
            // Phase 2: 50-BLOCK STATIC TEMPEST CRATER (LINGERING AFTERMATH - 12S)
            // =========================================================================
            postImpactTicks++;

            if (level().isClientSide) {
                // Expanding electric discharge rings (radii 15, 30, 48 blocks)
                double angle1 = (postImpactTicks * 0.15);
                double angle2 = -(postImpactTicks * 0.1);

                for (int i = 0; i < 5; i++) {
                    double offset = (i * Math.PI * 2.0 / 5.0);
                    level().addParticle(ParticleTypes.ELECTRIC_SPARK, getX() + 48.0 * Math.cos(angle1 + offset), getY() + 0.2, getZ() + 48.0 * Math.sin(angle1 + offset), 0, 0.05, 0);
                    level().addParticle(ParticleTypes.ELECTRIC_SPARK, getX() + 28.0 * Math.cos(angle2 + offset), getY() + 0.2, getZ() + 28.0 * Math.sin(angle2 + offset), 0, 0.05, 0);
                }

                // Sparks crawling across the 50-block floor
                for (int i = 0; i < 6; i++) {
                    double rx = getX() + (random.nextDouble() - 0.5) * 80.0;
                    double rz = getZ() + (random.nextDouble() - 0.5) * 80.0;
                    if ((rx - getX()) * (rx - getX()) + (rz - getZ()) * (rz - getZ()) <= 2300) {
                        level().addParticle(ParticleTypes.ELECTRIC_SPARK, rx, getY() + 0.15, rz, (random.nextDouble() - 0.5) * 0.15, 0.08, (random.nextDouble() - 0.5) * 0.15);
                    }
                }
            } else if (level() instanceof ServerLevel serverLevel) {
                // Secondary Lightning Strikes across the 50-block perimeter!
                if (postImpactTicks % 30 == 0) {
                    double pAngle = random.nextDouble() * Math.PI * 2.0;
                    double pDist = 12.0 + random.nextDouble() * 26.0;
                    double lx = getX() + Math.cos(pAngle) * pDist;
                    double lz = getZ() + Math.sin(pAngle) * pDist;

                    LightningBolt secondaryBolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
                    if (secondaryBolt != null) {
                        secondaryBolt.moveTo(lx, getY(), lz);
                        secondaryBolt.setVisualOnly(true);
                        serverLevel.addFreshEntity(secondaryBolt);
                    }
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10.0f, 1.3f);
                    level().playSound(null, lx, getY(), lz, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 10.0f, 1.0f);
                }

                // Every 10 ticks (~0.5s): 48-Block Static Shock Hazard Pulse
                if (postImpactTicks % 10 == 0) {
                    List<LivingEntity> enemies = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(48.0),
                            this::isValidTarget);
                    for (LivingEntity enemy : enemies) {
                        enemy.hurt(level().damageSources().magic(), 6.5f);
                        enemy.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2, false, false));
                        enemy.setDeltaMovement(enemy.getDeltaMovement().multiply(0.3, 1.0, 0.3)); // Micro-paralysis
                        serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, enemy.getX(), enemy.getY() + 0.8, enemy.getZ(), 8, 0.3, 0.3, 0.3, 0.1);
                    }
                }

                // Discard after 240 ticks (12 full seconds) with massive electromagnetic pulse
                if (postImpactTicks >= 240) {
                    serverLevel.sendParticles(ParticleTypes.FLASH, getX(), getY() + 2.0, getZ(), 8, 1.0, 1.0, 1.0, 0);
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, getX(), getY() + 2.0, getZ(), 200, 8.0, 3.0, 8.0, 0.3);
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 12.0f, 1.2f);
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.WEATHER, 10.0f, 1.5f);
                    discard();
                }
            }
        }
    }

    private void onImpact() {
        Vec3 pos = position();

        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 6.0f, 0.6f);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.TRIDENT_THUNDER.value(), SoundSource.WEATHER, 5.0f, 0.55f);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.WEATHER, 4.5f, 0.7f);

        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            // Visual cataclysm blast
            serverLevel.sendParticles(ParticleTypes.FLASH, pos.x, pos.y + 0.5, pos.z, 8, 1.0, 1.0, 1.0, 0);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, pos.x, pos.y + 1.0, pos.z, 6, 2.0, 2.0, 2.0, 0);
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, pos.x, pos.y + 1.0, pos.z, 160, 6.0, 3.0, 6.0, 0.4);

            // Expanding sonic shockwave
            for (int i = 0; i < 36; i++) {
                double angle = (Math.PI * 2 / 36) * i;
                serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, pos.x + 7.0 * Math.cos(angle), pos.y + 0.3, pos.z + 7.0 * Math.sin(angle), 1, 0, 0, 0, 0);
            }

            // Spawn genuine lightning strikes around impact
            for (int i = 0; i < 4; i++) {
                double ox = (random.nextDouble() - 0.5) * 8.0;
                double oz = (random.nextDouble() - 0.5) * 8.0;
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
                if (bolt != null) {
                    bolt.moveTo(pos.x + ox, pos.y, pos.z + oz);
                    bolt.setVisualOnly(true);
                    serverLevel.addFreshEntity(bolt);
                }
            }

            // Damage, stun and knockback entities
            float blastRadius = 14.0f;
            List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(blastRadius));
            for (LivingEntity target : targets) {
                if (!isValidTarget(target)) continue;

                target.hurt(level().damageSources().magic(), 70.0f);
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 3)); // Stun/Slow IV for 8s

                // Seismic upward + outward knockback
                Vec3 knock = target.position().subtract(pos).normalize().scale(2.2).add(0, 0.95, 0);
                target.setDeltaMovement(knock);
                if (target instanceof ServerPlayer player) {
                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
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
