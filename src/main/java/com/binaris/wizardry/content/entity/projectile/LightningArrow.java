package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class LightningArrow extends MagicArrowEntity {
    public LightningArrow(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
        setLifetime(40);
    }

    public LightningArrow(Level world) {
        this(EBEntities.LIGHTNING_ARROW.get(), world);
    }

    @Override
    public float getDamageAmount(@NotNull EntityHitResult hitResult) {
        return EBSpells.LIGHTNING_ARROW.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.LIGHTNING_ARROW.property(DefaultProperties.DAMAGE) : 7.0f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_LIGHTNING_ARROW_HIT != null && EBSounds.ENTITY_LIGHTNING_ARROW_HIT.get() != null
                ? EBSounds.ENTITY_LIGHTNING_ARROW_HIT.get() : super.getHitSound(result);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide && tickCount > 1) {
            ParticleBuilder.create(EBParticles.SPARK.get()).pos(position()).time(10).spawn(level());
        }
    }
}
