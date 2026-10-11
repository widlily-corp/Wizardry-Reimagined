package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class FireballEntity extends MagicProjectileEntity {

    public FireballEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        this.setLifetime(60);
    }

    public FireballEntity(Level level, LivingEntity shooter) {
        super(EBEntities.FIREBALL.get(), level);
        this.setLifetime(60);
        this.aim(shooter, 1.4f);
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
    public DamageSource getDamageSource(@NotNull EntityHitResult result) {
        return level().damageSources().inFire();
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_FIREBOLT_HIT.get();
    }

    @Override
    public void onHitTargetExtraEffects(@NotNull EntityHitResult result) {
        result.getEntity().igniteForSeconds(5);
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            BlockPos firePos = result.getBlockPos().relative(result.getDirection());
            if (this.level().isEmptyBlock(firePos)) {
                this.level().setBlockAndUpdate(firePos, Blocks.FIRE.defaultBlockState());
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.FLAME, getX(), getY(), getZ(), 0, 0, 0);
            this.level().addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0, 0, 0);
        }
    }
}
