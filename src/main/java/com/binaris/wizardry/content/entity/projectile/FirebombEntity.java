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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FirebombEntity extends MagicProjectileEntity {

    public FirebombEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        this.setLifetime(100);
    }

    public FirebombEntity(Level level, LivingEntity shooter) {
        super(EBEntities.FIREBOMB.get(), level);
        this.setLifetime(100);
        this.aim(shooter, 1.1f);
    }

    @Override
    public float getDamage(@NotNull EntityHitResult result) {
        return 5.0f;
    }

    @Override
    public DamageSource getDamageSource(@NotNull EntityHitResult result) {
        return level().damageSources().inFire();
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_FIREBOMB_SMASH.get();
    }

    private void explodeArea() {
        if (!this.level().isClientSide) {
            AABB area = this.getBoundingBox().inflate(3.0);
            List<LivingEntity> nearby = this.level().getEntitiesOfClass(LivingEntity.class, area, this::isValidTarget);
            for (LivingEntity target : nearby) {
                target.hurt(level().damageSources().inFire(), 4.0f * damageMultiplier);
                target.igniteForSeconds(4);
            }

            BlockPos center = this.blockPosition();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos p = center.offset(dx, 0, dz);
                    if (this.level().isEmptyBlock(p) && Blocks.FIRE.defaultBlockState().canSurvive(this.level(), p)) {
                        this.level().setBlockAndUpdate(p, Blocks.FIRE.defaultBlockState());
                    }
                }
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        explodeArea();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        explodeArea();
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
