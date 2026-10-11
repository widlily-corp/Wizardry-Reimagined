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
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class DarknessOrbEntity extends MagicProjectileEntity {

    public DarknessOrbEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        this.setLifetime(60);
    }

    public DarknessOrbEntity(Level level, LivingEntity shooter) {
        super(EBEntities.DARKNESS_ORB.get(), level);
        this.setLifetime(60);
        this.aim(shooter, 1.3f);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public float getDamage(@NotNull EntityHitResult result) {
        return 7.5f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_DARKNESS_ORB_HIT.get();
    }

    @Override
    public void onHitTargetExtraEffects(@NotNull EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.WITHER, 160, 1));
            living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
            // Heal caster slightly (life drain effect)
            if (getOwner() instanceof LivingEntity caster) {
                caster.heal(2.0f);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SQUID_INK, getX(), getY(), getZ(), 0, 0, 0);
            this.level().addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0, 0, 0);
        }
    }
}
