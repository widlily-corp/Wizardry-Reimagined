package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

import java.lang.ref.WeakReference;

public class BubbleConstruct extends MagicConstructEntity {

    private static final EntityDataAccessor<Boolean> IS_DARK_ORB =
            SynchedEntityData.defineId(BubbleConstruct.class, EntityDataSerializers.BOOLEAN);

    private WeakReference<LivingEntity> rider;

    public BubbleConstruct(EntityType<?> type, Level world) {
        super(type, world);
    }

    public BubbleConstruct(Level world) {
        this(EBEntities.BUBBLE.get(), world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_DARK_ORB, false);
    }

    public boolean isDarkOrb() {
        return this.entityData.get(IS_DARK_ORB);
    }

    public void setDarkOrb(boolean darkOrb) {
        this.entityData.set(IS_DARK_ORB, darkOrb);
    }

    @Override
    public void tick() {
        super.tick();

        Entity currentRider = EntityUtil.getRider(this);
        if ((rider == null || rider.get() == null) && currentRider instanceof LivingEntity living && living.isAlive()) {
            rider = new WeakReference<>(living);
        }

        if (this.level().isClientSide) {
            boolean dark = isDarkOrb();
            // Render visible floating spherical water cage
            for (int i = 0; i < 8; i++) {
                double u = random.nextDouble();
                double v = random.nextDouble();
                double theta = u * 2.0 * Math.PI;
                double phi = Math.acos(2.0 * v - 1.0);
                double r = 1.0;
                double sinPhi = Math.sin(phi);
                double px = getX() + r * sinPhi * Math.cos(theta);
                double py = getY() + 0.6 + r * Math.cos(phi);
                double pz = getZ() + r * sinPhi * Math.sin(theta);

                if (dark) {
                    level().addParticle(random.nextBoolean() ? ParticleTypes.PORTAL : ParticleTypes.WITCH, px, py, pz, 0, 0.02, 0);
                    if (random.nextInt(3) == 0) {
                        level().addParticle(ParticleTypes.SQUID_INK, px, py, pz, 0, 0, 0);
                    }
                } else {
                    level().addParticle(new net.minecraft.core.particles.DustParticleOptions(new org.joml.Vector3f(0.3f, 0.75f, 1.0f), 1.0f), px, py, pz, 0, 0.01, 0);
                    if (random.nextBoolean()) {
                        level().addParticle(ParticleTypes.BUBBLE_POP, px, py, pz, 0, 0.02, 0);
                    }
                    if (random.nextInt(3) == 0) {
                        level().addParticle(ParticleTypes.SPLASH, px, py, pz, 0, 0.01, 0);
                    }
                }
            }
            return;
        }

        if (rider != null && rider.get() != null) {
            LivingEntity living = rider.get();
            if (!living.isAlive() || living.getVehicle() != this) {
                popBubble();
                this.discard();
                return;
            }

            // Entrapment dark orb crushing damage
            if (isDarkOrb() && this.tickCount % 30 == 0) {
                living.hurt(level().damageSources().magic(), 2.0f * (float) damageMultiplier);
                level().playSound(null, getX(), getY(), getZ(), SoundEvents.WITHER_HURT, SoundSource.HOSTILE, 0.6f, 1.6f);
            }

            this.setDeltaMovement(0, 0.04, 0);
            this.move(MoverType.SELF, this.getDeltaMovement());
        } else if (this.tickCount > 10 && this.getPassengers().isEmpty()) {
            popBubble();
            this.discard();
        }
    }

    private void popBubble() {
        if (!level().isClientSide) {
            net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) level();
            if (isDarkOrb()) {
                serverLevel.sendParticles(ParticleTypes.SQUID_INK, getX(), getY() + 0.6, getZ(), 15, 0.5, 0.5, 0.5, 0.05);
                serverLevel.sendParticles(ParticleTypes.WITCH, getX(), getY() + 0.6, getZ(), 10, 0.4, 0.4, 0.4, 0.05);
                serverLevel.playSound(null, getX(), getY(), getZ(), SoundEvents.WITHER_SHOOT, SoundSource.HOSTILE, 0.8f, 1.8f);
            } else {
                serverLevel.sendParticles(ParticleTypes.BUBBLE_POP, getX(), getY() + 0.6, getZ(), 20, 0.5, 0.5, 0.5, 0.05);
                serverLevel.sendParticles(ParticleTypes.SPLASH, getX(), getY() + 0.6, getZ(), 15, 0.4, 0.4, 0.4, 0.05);
                serverLevel.playSound(null, getX(), getY(), getZ(), SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.NEUTRAL, 1.0f, 1.0f);
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("DarkOrb")) {
            setDarkOrb(tag.getBoolean("DarkOrb"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("DarkOrb", isDarkOrb());
    }
}
