package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBMobEffects;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class IceLanceEntity extends MagicArrowEntity {
    public IceLanceEntity(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
        setLifetime(60);
    }

    public IceLanceEntity(Level world) {
        this(EBEntities.ICE_LANCE.get(), world);
    }

    @Override
    public float getDamageAmount(@NotNull EntityHitResult hitResult) {
        return EBSpells.ICE_LANCE.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.ICE_LANCE.property(DefaultProperties.DAMAGE) : 10f;
    }

    @Override
    public void onHitTargetExtraEffects(@NotNull EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity livingEntity && !level().isClientSide) {
            int duration = EBSpells.ICE_LANCE.hasProperty(DefaultProperties.EFFECT_DURATION) ? EBSpells.ICE_LANCE.property(DefaultProperties.EFFECT_DURATION) : 300;
            livingEntity.addEffect(new MobEffectInstance(EBMobEffects.FROST, duration, 0));
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (this.level().isClientSide) {
            for (int j = 0; j < 10; j++) {
                ParticleBuilder.create(EBParticles.ICE.get()).pos(position()).count(1).spread(0.5, 0.5, 0.5).spawn(level());
            }
        }
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        if (result instanceof BlockHitResult) {
            return EBSounds.ENTITY_ICE_LANCE_SMASH != null && EBSounds.ENTITY_ICE_LANCE_SMASH.get() != null
                    ? EBSounds.ENTITY_ICE_LANCE_SMASH.get() : super.getHitSound(result);
        }
        return EBSounds.ENTITY_ICE_LANCE_HIT != null && EBSounds.ENTITY_ICE_LANCE_HIT.get() != null
                ? EBSounds.ENTITY_ICE_LANCE_HIT.get() : super.getHitSound(result);
    }
}
