package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBMobEffects;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class IceBall extends MagicProjectileEntity {
    public IceBall(EntityType<? extends ThrowableProjectile> entityType, Level world) {
        super(entityType, world);
        setLifetime(20);
    }

    public IceBall(Level world) {
        this(EBEntities.ICE_BALL.get(), world);
    }

    @Override
    public float getDamage(@NotNull EntityHitResult hitResult) {
        return EBSpells.ICE_BALL.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.ICE_BALL.property(DefaultProperties.DAMAGE) : 5f;
    }

    @Override
    public void onHitTargetExtraEffects(@NotNull EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity livingEntity && !level().isClientSide) {
            int duration = EBSpells.ICE_BALL.hasProperty(DefaultProperties.EFFECT_DURATION) ? EBSpells.ICE_BALL.property(DefaultProperties.EFFECT_DURATION) : 100;
            int strength = EBSpells.ICE_BALL.hasProperty(DefaultProperties.EFFECT_STRENGTH) ? EBSpells.ICE_BALL.property(DefaultProperties.EFFECT_STRENGTH) : 0;
            livingEntity.addEffect(new MobEffectInstance(EBMobEffects.FROST, duration, strength));
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        if (level().isClientSide) {
            for (int i = 0; i < 8; i++) {
                level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SNOWBALL)),
                        getX(), getY(), getZ(),
                        (random.nextDouble() - 0.5) * 0.2, random.nextDouble() * 0.2, (random.nextDouble() - 0.5) * 0.2);
            }
        }
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_ICEBALL_HIT != null && EBSounds.ENTITY_ICEBALL_HIT.get() != null
                ? EBSounds.ENTITY_ICEBALL_HIT.get() : super.getHitSound(result);
    }
}
