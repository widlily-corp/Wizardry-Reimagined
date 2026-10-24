package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class IceShardEntity extends MagicArrowEntity {
    private static final ItemParticleOption SNOWBALL_PARTICLE = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SNOWBALL));

    public IceShardEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.setLifetime(45);
    }

    public IceShardEntity(Level level) {
        super(EBEntities.ICE_SHARD.get(), level);
        this.setLifetime(45);
    }

    public IceShardEntity(Level level, LivingEntity shooter) {
        super(EBEntities.ICE_SHARD.get(), level);
        this.setLifetime(45);
        this.aim(shooter, 1.8f);
    }

    @Override
    public float getDamageAmount(@NotNull EntityHitResult hitResult) {
        return 6.0f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_ICE_SHARD_HIT.get();
    }

    @Override
    public void onHitTargetExtraEffects(@NotNull EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
            living.setTicksFrozen(Math.min(living.getTicksRequiredToFreeze() + 100, living.getTicksFrozen() + 100));
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
                double px = xo + dx * pct + (random.nextDouble() - 0.5) * 0.08;
                double py = yo + dy * pct + (random.nextDouble() - 0.5) * 0.08;
                double pz = zo + dz * pct + (random.nextDouble() - 0.5) * 0.08;
                this.level().addParticle(SNOWBALL_PARTICLE, px, py, pz, 0, 0.01, 0);
                this.level().addParticle(ParticleTypes.SNOWFLAKE, px, py, pz, 0, 0, 0);
            }
        }
    }
}
