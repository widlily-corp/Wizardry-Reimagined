package com.binaris.wizardry.content.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MagicArrowEntity extends AbstractArrow {
    public static final double LAUNCH_Y_OFFSET = 0.3;
    public static final float FORWARD_OFFSET = 0.8f;
    public float damageMultiplier = 1.0f;
    private int lifetime = 60;

    public MagicArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public MagicArrowEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity owner, Level level, ItemStack pickupItem, @Nullable ItemStack firedFromWeapon) {
        super(entityType, owner, level, pickupItem, firedFromWeapon);
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public abstract float getDamageAmount(@NotNull EntityHitResult hitResult);

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

    public void onHitTargetExtraEffects(@NotNull EntityHitResult hitResult) {
    }

    public boolean doOverpenetration() {
        return false;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult hitResult) {
        if (!isValidTarget(hitResult.getEntity())) return;

        hitResult.getEntity().hurt(getDamageSource(hitResult), getDamageAmount(hitResult) * damageMultiplier);
        this.onHitTargetExtraEffects(hitResult);
        this.playHitSound(hitResult);

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

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean tryPickup(@NotNull Player player) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("DamageMultiplier", damageMultiplier);
        compound.putInt("Lifetime", lifetime);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("DamageMultiplier")) damageMultiplier = compound.getFloat("DamageMultiplier");
        if (compound.contains("Lifetime")) lifetime = compound.getInt("Lifetime");
    }
}
