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

    private static final int LIFETIME_TICKS = 60; // 3 seconds
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

    @Override
    public void tick() {
        super.tick();

        if (activeTicks == 0) {
            onSeismicEruption();
            this.entityData.set(ERUPTED, true);
        }

        activeTicks++;

        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            if (activeTicks >= LIFETIME_TICKS) {
                // Monoliths crumble into rubble
                Vec3 pos = position();
                level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.STONE_BREAK, SoundSource.PLAYERS, 3.0f, 0.7f);
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DEEPSLATE.defaultBlockState()),
                        pos.x, pos.y + 1.5, pos.z, 80, 2.5, 1.5, 2.5, 0.15);
                discard();
            }
        }
    }

    private void onSeismicEruption() {
        Vec3 pos = position();

        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 4.0f, 0.5f);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.WEATHER, 3.5f, 0.6f);
        level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.IRON_GOLEM_ATTACK, SoundSource.PLAYERS, 3.0f, 0.7f);

        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            // Seismic dust and rubble burst
            serverLevel.sendParticles(ParticleTypes.FLASH, pos.x, pos.y + 0.5, pos.z, 4, 0.5, 0.5, 0.5, 0);
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DEEPSLATE.defaultBlockState()),
                    pos.x, pos.y + 1.0, pos.z, 120, 3.5, 2.0, 3.5, 0.25);
            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.x, pos.y + 0.5, pos.z, 25, 3.0, 0.5, 3.0, 0.05);

            // Damage, launch skyward and stun
            double shatterRadius = 12.0;
            List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(shatterRadius));
            for (LivingEntity target : targets) {
                if (!isValidTarget(target)) continue;

                target.hurt(level().damageSources().magic(), 55.0f);
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 3)); // Slowness IV for 8s

                // Violent vertical launch upward and outward
                Vec3 diff = target.position().subtract(pos);
                Vec3 horiz = diff.lengthSqr() > 0.01 ? diff.normalize().scale(0.8) : Vec3.ZERO;
                target.setDeltaMovement(target.getDeltaMovement().add(horiz.x, 1.45, horiz.z));
                if (target instanceof ServerPlayer player) {
                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
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
