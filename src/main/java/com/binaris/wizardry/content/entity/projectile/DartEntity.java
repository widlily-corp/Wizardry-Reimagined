package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class DartEntity extends MagicArrowEntity {

    public DartEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.setLifetime(60);
    }

    public DartEntity(Level level) {
        super(EBEntities.DART.get(), level);
        this.setLifetime(60);
    }

    public DartEntity(Level level, LivingEntity shooter) {
        super(EBEntities.DART.get(), level);
        this.setLifetime(60);
        this.aim(shooter, 1.5f);
    }

    @Override
    public float getDamageAmount(@NotNull EntityHitResult hitResult) {
        return 4.5f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_DART_HIT.get();
    }

    @Override
    public void onHitTargetExtraEffects(@NotNull EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 1));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            double dx = getX() - xo;
            double dy = getY() - yo;
            double dz = getZ() - zo;
            for (int i = 0; i < 2; i++) {
                double pct = (i + 0.5) / 2.0;
                double px = xo + dx * pct + (random.nextDouble() - 0.5) * 0.05;
                double py = yo + dy * pct + (random.nextDouble() - 0.5) * 0.05;
                double pz = zo + dz * pct + (random.nextDouble() - 0.5) * 0.05;
                this.level().addParticle(ParticleTypes.CRIT, px, py, pz, 0, 0, 0);
            }
        }
    }
}
