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

public class PoisonBombEntity extends MagicProjectileEntity {

    public PoisonBombEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        this.setLifetime(100);
    }

    public PoisonBombEntity(Level level) {
        super(EBEntities.POISON_BOMB.get(), level);
        this.setLifetime(100);
    }

    public PoisonBombEntity(Level level, LivingEntity shooter) {
        super(EBEntities.POISON_BOMB.get(), level);
        this.setLifetime(100);
        this.aim(shooter, 1.1f);
    }

    @Override
    public float getDamage(@NotNull EntityHitResult result) {
        return 4.0f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_POISON_BOMB_SMASH.get();
    }

    private void poisonArea() {
        if (!this.level().isClientSide) {
            AABB area = this.getBoundingBox().inflate(3.5);
            List<LivingEntity> nearby = this.level().getEntitiesOfClass(LivingEntity.class, area, this::isValidTarget);
            for (LivingEntity target : nearby) {
                target.hurt(getDamageSource(null), 3.0f * damageMultiplier);
                target.addEffect(new MobEffectInstance(MobEffects.POISON, 160, 1));
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        poisonArea();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        poisonArea();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.ITEM_SLIME, getX(), getY(), getZ(), 0, 0, 0);
        }
    }
}
