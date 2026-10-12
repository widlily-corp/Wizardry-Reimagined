package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForceOrbEntity extends MagicProjectileEntity {

    public ForceOrbEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        this.setLifetime(50);
    }

    public ForceOrbEntity(Level level) {
        super(EBEntities.FORCE_ORB.get(), level);
        this.setLifetime(50);
    }

    public ForceOrbEntity(Level level, LivingEntity shooter) {
        super(EBEntities.FORCE_ORB.get(), level);
        this.setLifetime(50);
        this.aim(shooter, 1.4f);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public float getDamage(@NotNull EntityHitResult result) {
        return 7.0f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_FORCE_ORB_HIT.get();
    }

    private void blastKnockback() {
        if (!this.level().isClientSide) {
            AABB area = this.getBoundingBox().inflate(4.0);
            List<LivingEntity> nearby = this.level().getEntitiesOfClass(LivingEntity.class, area, this::isValidTarget);
            for (LivingEntity target : nearby) {
                Vec3 dir = target.position().subtract(this.position()).normalize().scale(1.2);
                target.push(dir.x, 0.4, dir.z);
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        blastKnockback();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        blastKnockback();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.POOF, getX(), getY(), getZ(), 0, 0, 0);
        }
    }
}
