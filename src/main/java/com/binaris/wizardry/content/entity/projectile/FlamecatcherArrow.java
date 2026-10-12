package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class FlamecatcherArrow extends MagicArrowEntity {
    public static final float SPEED = 3.0f;

    public FlamecatcherArrow(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
        setLifetime(60);
    }

    public FlamecatcherArrow(Level world) {
        this(EBEntities.FLAME_CATCHER_ARROW.get(), world);
    }

    @Override
    public float getDamageAmount(@NotNull EntityHitResult hitResult) {
        return EBSpells.FLAMECATCHER.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.FLAMECATCHER.property(DefaultProperties.DAMAGE) : 16f;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void onHitTargetExtraEffects(@NotNull EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity livingEntity) {
            int duration = EBSpells.FLAMECATCHER.hasProperty(DefaultProperties.EFFECT_DURATION) ? EBSpells.FLAMECATCHER.property(DefaultProperties.EFFECT_DURATION) : 15;
            livingEntity.igniteForSeconds(duration);
        }
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_FLAMECATCHER_ARROW_HIT != null && EBSounds.ENTITY_FLAMECATCHER_ARROW_HIT.get() != null
                ? EBSounds.ENTITY_FLAMECATCHER_ARROW_HIT.get() : super.getHitSound(result);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide && tickCount > 1) {
            ParticleBuilder.create(EBParticles.MAGIC_FIRE.get()).pos(position()).time(15).spawn(level());
        }
    }
}
