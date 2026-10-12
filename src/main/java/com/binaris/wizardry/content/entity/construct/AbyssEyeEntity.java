package com.binaris.wizardry.content.entity.construct;

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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Abyss Eye entity for "Eclipse of the Abyss: Eye of Chaos" Grand Necromancy Spell.
 * Phase 0: Gravitational void singularity pulls foes inward, drains life & feeds caster (5s).
 * Phase 1: Supermassive void collapse explosion (45 damage, sonic boom shockwave).
 */
public class AbyssEyeEntity extends MagicConstructEntity {
    private static final EntityDataAccessor<Boolean> COLLAPSED = SynchedEntityData.defineId(AbyssEyeEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int DRAIN_DURATION = 100; // 5 seconds
    private int drainTicks = 0;

    public AbyssEyeEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.noPhysics = true;
    }

    public AbyssEyeEntity(Level world, double x, double y, double z) {
        this(EBEntities.ABYSS_EYE.get(), world);
        setPos(x, y + 4.0, z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLLAPSED, false);
    }

    public boolean hasCollapsed() {
        return this.entityData.get(COLLAPSED);
    }

    @Override
    public void tick() {
        super.tick();

        if (hasCollapsed()) {
            return;
        }

        drainTicks++;

        // Swirling black hole singularity visuals
        spawnSingularityParticles();

        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            Vec3 center = position();
            double radius = 14.0;
            List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(radius));
            LivingEntity caster = getCaster();

            for (LivingEntity target : targets) {
                if (!isValidTarget(target)) continue;

                // Gravitational pull inward towards singularity
                Vec3 toCenter = center.subtract(target.position());
                if (toCenter.lengthSqr() > 1.0) {
                    Vec3 pull = toCenter.normalize().scale(0.24);
                    target.setDeltaMovement(target.getDeltaMovement().add(pull.x, 0.04, pull.z));
                    if (target instanceof ServerPlayer player) {
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));
                    }
                }

                // Vampiric health siphon every 8 ticks
                if (drainTicks % 8 == 0) {
                    target.hurt(level().damageSources().wither(), 5.0f);
                    target.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1));
                    target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));
                    target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 1));

                    // Heal caster with harvested soul vitality
                    if (caster != null && caster.isAlive()) {
                        caster.heal(1.5f);
                        serverLevel.sendParticles(ParticleTypes.SOUL, caster.getX(), caster.getY() + 1.0, caster.getZ(), 2, 0.2, 0.2, 0.2, 0.02);
                    }
                }
            }

            if (drainTicks % 16 == 0) {
                level().playSound(null, center.x, center.y, center.z, SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.8f, 0.6f);
            }

            // Cataclysmic Void Collapse after 5 seconds
            if (drainTicks >= DRAIN_DURATION) {
                onVoidCollapse(serverLevel);
                this.entityData.set(COLLAPSED, true);
                discard();
            }
        }
    }

    private void onVoidCollapse(ServerLevel serverLevel) {
        Vec3 pos = position();

        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 4.0f, 0.8f);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.WITHER_DEATH, SoundSource.WEATHER, 3.5f, 0.7f);

        // Sonic boom + void blastwave
        serverLevel.sendParticles(ParticleTypes.FLASH, pos.x, pos.y, pos.z, 5, 0.5, 0.5, 0.5, 0);
        serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, pos.x, pos.y, pos.z, 2, 0, 0, 0, 0);
        serverLevel.sendParticles(ParticleTypes.SQUID_INK, pos.x, pos.y, pos.z, 140, 4.0, 3.0, 4.0, 0.25);
        serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, pos.x, pos.y, pos.z, 90, 3.5, 2.5, 3.5, 0.15);

        // Damage and outward expulsion
        double blastRadius = 12.0;
        List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(blastRadius));
        for (LivingEntity target : targets) {
            if (!isValidTarget(target)) continue;

            target.hurt(level().damageSources().magic(), 45.0f);
            Vec3 knock = target.position().subtract(pos).normalize().scale(1.6).add(0, 0.5, 0);
            target.setDeltaMovement(knock);
            if (target instanceof ServerPlayer player) {
                player.connection.send(new ClientboundSetEntityMotionPacket(player));
            }
        }
    }

    private void spawnSingularityParticles() {
        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 8; i++) {
                double angle = (drainTicks * 0.25) + (i * Math.PI / 4.0);
                double r = 1.8 + (random.nextDouble() * 3.5);
                double px = getX() + Math.cos(angle) * r;
                double py = getY() + (random.nextDouble() - 0.5) * 2.5;
                double pz = getZ() + Math.sin(angle) * r;

                // Inward spiraling vector
                double vx = (getX() - px) * 0.25;
                double vy = (getY() - py) * 0.25;
                double vz = (getZ() - pz) * 0.25;

                serverLevel.sendParticles(ParticleTypes.SQUID_INK, px, py, pz, 1, vx, vy, vz, 0.05);
                if (random.nextBoolean()) {
                    serverLevel.sendParticles(ParticleTypes.PORTAL, px, py, pz, 1, vx, vy, vz, 0.1);
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.drainTicks = tag.getInt("DrainTicks");
        if (tag.contains("Collapsed")) {
            this.entityData.set(COLLAPSED, tag.getBoolean("Collapsed"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("DrainTicks", drainTicks);
        tag.putBoolean("Collapsed", this.entityData.get(COLLAPSED));
    }
}
