package com.binaris.wizardry.content.spells.abstr;

import com.binaris.wizardry.api.content.spell.internal.*;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.content.entity.projectile.MagicArrowEntity;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Spell;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

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
            arrow.aim(ctx.caster(), calculateVelocity(ctx, arrow, ctx.caster().getEyeHeight()) - (float) MagicArrowEntity.LAUNCH_Y_OFFSET);
            arrow.damageMultiplier = ctx.modifiers().getFactor(SpellModifiers.POTENCY);
            addArrowExtras(ctx, arrow);
            ctx.world().addFreshEntity(arrow);
        }

        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean cast(EntityCastContext ctx) {
        if (ctx.caster() == null) return false;

        if (!ctx.world().isClientSide) {
            T arrow = arrowFactory.apply(ctx.world());
            float speed = calculateVelocity(ctx, arrow, ctx.caster().getEyeHeight() - (float) MagicArrowEntity.LAUNCH_Y_OFFSET);
            if (ctx.target() != null) {
                int aimingError = EntityUtil.getDefaultAimingError(ctx.world().getDifficulty());
                arrow.aim(ctx.caster(), ctx.target(), speed, aimingError);
            } else {
                arrow.aim(ctx.caster(), speed);
            }
            arrow.damageMultiplier = ctx.modifiers().getFactor(SpellModifiers.POTENCY);
            addArrowExtras(ctx, arrow);
            ctx.world().addFreshEntity(arrow);
        }

        ctx.caster().swing(ctx.hand());
        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean cast(LocationCastContext ctx) {
        if (!ctx.world().isClientSide) {
            T arrow = arrowFactory.apply(ctx.world());
            arrow.setPos(ctx.vec3());
            Vec3 vec = Vec3.atLowerCornerOf(ctx.direction().getNormal());
            arrow.shoot(vec.x(), vec.y(), vec.z(), calculateVelocity(ctx, arrow, 0.375f), 1);
            arrow.damageMultiplier = ctx.modifiers().getFactor(SpellModifiers.POTENCY);
            addArrowExtras(ctx, arrow);
            ctx.world().addFreshEntity(arrow);
        }

        this.playSoundAt(ctx.world(), ctx.x() - ctx.direction().getStepX(),
                ctx.y() - ctx.direction().getStepY(), ctx.z() - ctx.direction().getStepZ(),
                ctx.castingTicks(), ctx.duration());
        return true;
    }

    public float calculateVelocity(CastContext ctx, MagicArrowEntity projectile, float launchHeight) {
        float range = ctx.modifiers().get(SpellModifiers.RANGE, property(DefaultProperties.RANGE));

        if (projectile.isNoGravity()) {
            if (projectile.getLifetime() <= 0) return 2;
            return range / projectile.getLifetime();
        } else {
            float g = 0.05f;
            return range / Mth.sqrt(2 * launchHeight / g);
        }
    }

    protected void addArrowExtras(CastContext ctx, T arrow) {
    }

    @Override
    public boolean requiresPacket() {
        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.empty();
    }
}
