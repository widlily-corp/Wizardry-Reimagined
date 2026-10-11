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
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LightningBoltEntity extends MagicProjectileEntity {

    public LightningBoltEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        this.setLifetime(30);
    }

    public LightningBoltEntity(Level level, LivingEntity shooter) {
        super(EBEntities.LIGHTNING_BOLT.get(), level);
        this.setLifetime(30);
        this.aim(shooter, 2.5f);
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
    public DamageSource getDamageSource(@NotNull EntityHitResult result) {
        return level().damageSources().lightningBolt();
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_THUNDERBOLT_HIT.get();
    }

    @Override
    public void onHitTargetExtraEffects(@NotNull EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity living) {
            Vec3 knockback = this.getDeltaMovement().normalize().scale(0.8);
            living.push(knockback.x, 0.2, knockback.z);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            for (int i = 0; i < 3; i++) {
                this.level().addParticle(ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(),
                        (random.nextDouble() - 0.5) * 0.2, (random.nextDouble() - 0.5) * 0.2, (random.nextDouble() - 0.5) * 0.2);
            }
        }
    }
}
