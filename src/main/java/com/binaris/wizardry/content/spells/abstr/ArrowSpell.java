package com.binaris.wizardry.content.spells.abstr;

import com.binaris.wizardry.api.content.spell.internal.*;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.content.entity.projectile.MagicArrowEntity;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Spell;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class ArrowSpell<T extends MagicArrowEntity> extends Spell {
    protected final Function<Level, T> arrowFactory;

    public ArrowSpell(Function<Level, T> arrowFactory) {
        this.arrowFactory = arrowFactory;
    }

    @Override
    public boolean canCastByEntity() {
        return true;
    }

    @Override
    public boolean canCastByLocation() {
        return true;
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        if (!ctx.world().isClientSide) {
            T arrow = arrowFactory.apply(ctx.world());
            arrow.aim(ctx.caster(), calculateVelocity(ctx, arrow));
            arrow.damageMultiplier = ctx.modifiers().getFactor(SpellModifiers.POTENCY);
            ctx.world().addFreshEntity(arrow);
        }

        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean cast(EntityCastContext ctx) {
        if (ctx.target() == null) return false;
        if (!ctx.world().isClientSide) {
            T arrow = arrowFactory.apply(ctx.world());
            int aimingError = EntityUtil.getDefaultAimingError(ctx.world().getDifficulty());
            arrow.aim(ctx.caster(), ctx.target(), calculateVelocity(ctx, arrow), aimingError);
            arrow.damageMultiplier = ctx.modifiers().getFactor(SpellModifiers.POTENCY);
            ctx.world().addFreshEntity(arrow);
        }

        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    protected float calculateVelocity(CastContext ctx, T arrow) {
        float range = hasProperty(DefaultProperties.RANGE) ? property(DefaultProperties.RANGE) : 15.0f;
        return (range / 10.0f) * ctx.modifiers().getFactor(SpellModifiers.RANGE);
    }
}
