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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ForceArrow extends MagicArrowEntity {
    public ForceArrow(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
        setLifetime(60);
    }

    private int mana;

    public ForceArrow(Level world) {
        this(EBEntities.FORCE_ARROW.get(), world);
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getMana() {
        return this.mana;
    }

    @Override
    public float getDamageAmount(@NotNull EntityHitResult hitResult) {
        return EBSpells.FORCE_ARROW.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.FORCE_ARROW.property(DefaultProperties.DAMAGE) : 8.0f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_FORCE_ARROW_HIT != null && EBSounds.ENTITY_FORCE_ARROW_HIT.get() != null
                ? EBSounds.ENTITY_FORCE_ARROW_HIT.get() : super.getHitSound(result);
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (level().isClientSide) {
            Vec3 pos = blockHitResult.getLocation().add(Vec3.atLowerCornerOf(blockHitResult.getDirection().getNormal()).scale(0.15));
            ParticleBuilder.create(EBParticles.FLASH.get())
                    .pos(pos)
                    .scale(1.6f)
                    .color(0.75f, 1.0f, 0.85f)
                    .time(6)
                    .spawn(level());
        }
    }
}
