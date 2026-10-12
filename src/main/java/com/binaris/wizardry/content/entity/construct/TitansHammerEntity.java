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
            // ==========================================
            // Phase 2: Static Tempest Crater (Lingering Aftermath Zone - 12s)
            // ==========================================
            postImpactTicks++;

            if (level().isClientSide) {
                // Electric sparks crawling across the crater ground
                for (int i = 0; i < 4; i++) {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double r = random.nextDouble() * 9.0;
                    double px = getX() + Math.cos(angle) * r;
                    double pz = getZ() + Math.sin(angle) * r;
                    level().addParticle(ParticleTypes.ELECTRIC_SPARK, px, getY() + 0.15, pz, (random.nextDouble() - 0.5) * 0.1, 0.05, (random.nextDouble() - 0.5) * 0.1);
                }
            } else if (level() instanceof ServerLevel serverLevel) {
                // Every 40 ticks (2 seconds): Secondary Lightning Strike from charged clouds!
                if (postImpactTicks % 40 == 0) {
                    double ox = (random.nextDouble() - 0.5) * 8.0;
                    double oz = (random.nextDouble() - 0.5) * 8.0;
                    LightningBolt secondaryBolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
                    if (secondaryBolt != null) {
                        secondaryBolt.moveTo(getX() + ox, getY(), getZ() + oz);
                        secondaryBolt.setVisualOnly(true);
                        serverLevel.addFreshEntity(secondaryBolt);
                    }
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 3.5f, 1.4f);

                    // Chain shock pulse to all enemies in crater
                    List<LivingEntity> enemies = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(10.0),
                            this::isValidTarget);
                    for (LivingEntity enemy : enemies) {
                        enemy.hurt(level().damageSources().magic(), 8.0f);
                        enemy.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2, false, false));
                        enemy.setDeltaMovement(enemy.getDeltaMovement().multiply(0.2, 1.0, 0.2)); // Micro-paralysis
                        serverLevel.sendParticles(ParticleTypes.FLASH, enemy.getX(), enemy.getY() + 1.0, enemy.getZ(), 1, 0, 0, 0, 0);
                        serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, enemy.getX(), enemy.getY() + 1.0, enemy.getZ(), 15, 0.3, 0.3, 0.3, 0.1);
                    }
                }

                // Discard after 240 ticks (12 full seconds)
                if (postImpactTicks >= 240) {
                    serverLevel.sendParticles(ParticleTypes.FLASH, getX(), getY() + 1.0, getZ(), 3, 0.2, 0.2, 0.2, 0);
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, getX(), getY() + 1.0, getZ(), 60, 3.0, 1.0, 3.0, 0.1);
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.WEATHER, 2.0f, 1.5f);
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
