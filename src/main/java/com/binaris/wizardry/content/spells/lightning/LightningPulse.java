package com.binaris.wizardry.content.spells.lightning;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.spell.properties.SpellProperty;
import com.binaris.wizardry.api.content.util.AllyDesignation;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LightningPulse extends Spell {
    private static final SpellProperty<Float> REPULSION_VELOCITY = SpellProperty.floatProperty("repulsion_velocity", 0.8f);

    @Override
    public boolean cast(PlayerCastContext ctx) {
        if (!ctx.caster().onGround()) return false;
        float radius = ctx.modifiers().get(SpellModifiers.BLAST, property(DefaultProperties.EFFECT_RADIUS));

        List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(radius, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(), ctx.world());
        targets.removeIf(t -> !AllyDesignation.isValidTarget(ctx.caster(), t));
        for (LivingEntity target : targets) {
            target.hurt(
                    MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.SHOCK),
                    ctx.modifiers().get(SpellModifiers.POTENCY, property(DefaultProperties.DAMAGE))
            );

            if (!ctx.world().isClientSide()) {
                double dx = target.getX() - ctx.caster().getX();
                double dz = target.getZ() - ctx.caster().getZ();
                double vectorLength = Math.sqrt(dx * dx + dz * dz);
                if (vectorLength > 0.001) {
                    dx /= vectorLength;
                    dz /= vectorLength;
                }
                target.setDeltaMovement(property(REPULSION_VELOCITY) * dx, 0.2, property(REPULSION_VELOCITY) * dz);
                target.hurtMarked = true;
            }
        }

        if (ctx.world().isClientSide()) {
            ParticleBuilder.create(EBParticles.LIGHTNING_PULSE.get())
                    .pos(ctx.caster().getX(), ctx.caster().getY() + 0.05, ctx.caster().getZ())
                    .scale(ctx.modifiers().getFactor(SpellModifiers.BLAST))
                    .spawn(ctx.world());
        }

        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.ADVANCED, Elements.LIGHTNING, SpellTypes.ATTACK, SpellAction.POINT_DOWN, 25, 0, 75)
                .add(DefaultProperties.DAMAGE, 8.0F)
                .add(DefaultProperties.EFFECT_RADIUS, 3)
                .add(REPULSION_VELOCITY)
                .add(DefaultProperties.SENSIBLE, true)
                .build();
    }
}
