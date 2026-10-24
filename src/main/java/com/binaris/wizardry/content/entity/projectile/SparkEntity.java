package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SparkEntity extends MagicProjectileEntity {

    public SparkEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        this.setLifetime(50);
    }

    public SparkEntity(Level level) {
        super(EBEntities.SPARK.get(), level);
        this.setLifetime(50);
    }

    public SparkEntity(Level level, LivingEntity shooter) {
        super(EBEntities.SPARK.get(), level);
        this.setLifetime(50);
        this.aim(shooter, 1.2f);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public float getDamage(@NotNull EntityHitResult result) {
        return 5.0f;
    }

    @Override
    public DamageSource getDamageSource(@NotNull EntityHitResult result) {
        return level().damageSources().lightningBolt();
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_HOMING_SPARK_HIT.get();
    }

    @Override
    public void tick() {
        super.tick();

        // Homing behavior towards nearest hostile/valid target
        if (!this.level().isClientSide && this.tickCount > 2) {
            AABB searchBox = this.getBoundingBox().inflate(8.0);
            List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, searchBox, this::isValidTarget);
            LivingEntity closest = null;
            double minDist = Double.MAX_VALUE;
            for (LivingEntity target : targets) {
                double dist = this.distanceToSqr(target);
                if (dist < minDist) {
                    minDist = dist;
                    closest = target;
                }
            }

            if (closest != null) {
                Vec3 targetPos = closest.getEyePosition();
                Vec3 toTarget = targetPos.subtract(this.position()).normalize();
                Vec3 currentMotion = this.getDeltaMovement();
                Vec3 newMotion = currentMotion.add(toTarget.scale(0.15)).normalize().scale(currentMotion.length());
                this.setDeltaMovement(newMotion);
            }
        }

        if (this.level().isClientSide) {
            double dx = getX() - xo;
            double dy = getY() - yo;
            double dz = getZ() - zo;
            for (int i = 0; i < 2; i++) {
                double pct = (i + 0.5) / 2.0;
                double px = xo + dx * pct + (random.nextDouble() - 0.5) * 0.1;
                double py = yo + dy * pct + (random.nextDouble() - 0.5) * 0.1;
                double pz = zo + dz * pct + (random.nextDouble() - 0.5) * 0.1;
                this.level().addParticle(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 0, 0, 0);
            }
        }
    }
}
