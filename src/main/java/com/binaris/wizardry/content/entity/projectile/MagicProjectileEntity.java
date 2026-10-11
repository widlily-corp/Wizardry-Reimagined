package com.binaris.wizardry.content.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class MagicProjectileEntity extends ThrowableProjectile {
    public static final double LAUNCH_Y_OFFSET = 0.3;
    public static final float FORWARD_OFFSET = 0.8f;
    public float damageMultiplier = 1.0f;
    private int lifetime = 60;

    public MagicProjectileEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public MagicProjectileEntity(EntityType<? extends ThrowableProjectile> entityType, LivingEntity shooter, Level level) {
        super(entityType, shooter, level);
    }

    public MagicProjectileEntity(EntityType<? extends ThrowableProjectile> entityType, double x, double y, double z, Level level) {
        super(entityType, x, y, z, level);
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public abstract float getDamage(@NotNull EntityHitResult result);

    public boolean isValidTarget(@NotNull Entity entity) {
        Entity owner = this.getOwner();
        if (owner == null) return entity != this;
        if (entity == owner) return false;
        if (owner instanceof LivingEntity livingOwner && entity instanceof LivingEntity livingTarget) {
            return !livingOwner.isAlliedTo(livingTarget);
        }
        return true;
    }

    public DamageSource getDamageSource(@NotNull EntityHitResult result) {
        Entity owner = getOwner();
        if (owner instanceof LivingEntity living) {
            return level().damageSources().indirectMagic(this, living);
        }
        return level().damageSources().magic();
    }

    public @NotNull SoundEvent getHitSound(HitResult result) {
        return SoundEvents.EMPTY;
    }

    protected void playHitSound(HitResult result) {
        SoundEvent sound = getHitSound(result);
        if (sound != SoundEvents.EMPTY) {
            this.playSound(sound, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        }
    }

    public void onHitTargetExtraEffects(@NotNull EntityHitResult result) {
    }

    public boolean doOverpenetration() {
        return false;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        if (!isValidTarget(result.getEntity())) return;

        result.getEntity().hurt(getDamageSource(result), getDamage(result) * damageMultiplier);
        this.onHitTargetExtraEffects(result);
        this.playHitSound(result);

        if (!this.level().isClientSide) {
            if (!this.doOverpenetration()) {
                this.discard();
            }
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        this.playHitSound(result);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.tickCount > this.getLifetime()) {
            this.discard();
        }
    }

    public void aim(LivingEntity caster, float speed) {
        Vec3 lookVector = caster.getLookAngle();
        this.setPos(
                caster.getX() + lookVector.x * FORWARD_OFFSET,
                caster.getY() + caster.getEyeHeight() - LAUNCH_Y_OFFSET,
                caster.getZ() + lookVector.z * FORWARD_OFFSET
        );
        this.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0.0f, speed, 1.0f);
        this.setOwner(caster);
    }

    public void aim(LivingEntity caster, Entity target, float speed, float aimingError) {
        this.setOwner(caster);
        double startY = caster.getY() + caster.getEyeHeight() - LAUNCH_Y_OFFSET;
        double dx = target.getX() - caster.getX();
        double dy = target.getY() + target.getBbHeight() / 2.0 - startY;
        double dz = target.getZ() - caster.getZ();
        double horizDist = Math.sqrt(dx * dx + dz * dz);

        if (horizDist >= 1.0E-7D) {
            float yaw = (float) (Math.atan2(dz, dx) * 180.0 / Math.PI) - 90.0f;
            float pitch = (float) (-(Math.atan2(dy, horizDist) * 180.0 / Math.PI));
            this.absMoveTo(caster.getX() + dx / horizDist, startY, caster.getZ() + dz / horizDist, yaw, pitch);
            this.shoot(dx, dy, dz, speed, aimingError);
        }
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("DamageMultiplier", damageMultiplier);
        compound.putInt("Lifetime", lifetime);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("DamageMultiplier")) damageMultiplier = compound.getFloat("DamageMultiplier");
        if (compound.contains("Lifetime")) lifetime = compound.getInt("Lifetime");
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }
}
