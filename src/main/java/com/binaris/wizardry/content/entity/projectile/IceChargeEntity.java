package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IceChargeEntity extends MagicProjectileEntity {

    public IceChargeEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        this.setLifetime(60);
    }

    public IceChargeEntity(Level level, LivingEntity shooter) {
        super(EBEntities.ICE_CHARGE.get(), level);
        this.setLifetime(60);
        this.aim(shooter, 1.3f);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public float getDamage(@NotNull EntityHitResult result) {
        return 6.0f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_ICE_CHARGE_SMASH.get();
    }

    private void freezeArea() {
        if (!this.level().isClientSide) {
            AABB area = this.getBoundingBox().inflate(3.5);
            List<LivingEntity> nearby = this.level().getEntitiesOfClass(LivingEntity.class, area, this::isValidTarget);
            for (LivingEntity target : nearby) {
                target.hurt(getDamageSource(null), 3.0f * damageMultiplier);
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 140, 2));
                target.setTicksFrozen(Math.min(target.getTicksRequiredToFreeze() + 140, target.getTicksFrozen() + 140));
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        freezeArea();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        freezeArea();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SNOWFLAKE, getX(), getY(), getZ(), 0, 0, 0);
        }
    }
}
