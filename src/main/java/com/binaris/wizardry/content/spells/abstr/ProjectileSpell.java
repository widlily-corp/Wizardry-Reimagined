package com.binaris.wizardry.content.spells.abstr;

import com.binaris.wizardry.api.content.spell.internal.*;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.content.entity.projectile.BombEntity;
import com.binaris.wizardry.content.entity.projectile.MagicProjectileEntity;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Spell;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class ProjectileSpell<T extends MagicProjectileEntity> extends Spell {
    protected final Function<Level, T> projectileFactory;

    public ProjectileSpell(Function<Level, T> projectileFactory) {
        this.projectileFactory = projectileFactory;
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
            T projectile = projectileFactory.apply(ctx.world());
            projectile.aim(ctx.caster(), calculateVelocity(ctx, projectile));
            projectile.damageMultiplier = ctx.modifiers().getFactor(SpellModifiers.POTENCY);
            if (projectile instanceof BombEntity bomb) {
                bomb.blastMultiplier = ctx.modifiers().getFactor(SpellModifiers.BLAST);
            }
            ctx.world().addFreshEntity(projectile);
        }

        ctx.caster().swing(ctx.hand(), true);
        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean cast(EntityCastContext ctx) {
        if (ctx.caster() == null) return false;
        if (!ctx.world().isClientSide) {
            T projectile = projectileFactory.apply(ctx.world());
            float speed = calculateVelocity(ctx, projectile);
            if (ctx.target() != null) {
                int aimingError = EntityUtil.getDefaultAimingError(ctx.world().getDifficulty());
                projectile.aim(ctx.caster(), ctx.target(), speed, aimingError);
            } else {
                projectile.aim(ctx.caster(), speed);
            }
            projectile.damageMultiplier = ctx.modifiers().getFactor(SpellModifiers.POTENCY);
            if (projectile instanceof BombEntity bomb) {
                bomb.blastMultiplier = ctx.modifiers().getFactor(SpellModifiers.BLAST);
            }
            ctx.world().addFreshEntity(projectile);
        }

        ctx.caster().swing(ctx.hand(), true);
        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean cast(LocationCastContext ctx) {
        if (!ctx.world().isClientSide) {
            T projectile = projectileFactory.apply(ctx.world());
            projectile.setPos(ctx.vec3());
            Vec3i vec = ctx.direction().getNormal();
            projectile.shoot(vec.getX(), vec.getY(), vec.getZ(), calculateVelocity(ctx, projectile), 1.0f);
            projectile.damageMultiplier = ctx.modifiers().getFactor(SpellModifiers.POTENCY);
            if (projectile instanceof BombEntity bomb) {
                bomb.blastMultiplier = ctx.modifiers().getFactor(SpellModifiers.BLAST);
            }
            ctx.world().addFreshEntity(projectile);
        }
        return true;
    }

    protected float calculateVelocity(CastContext ctx, T projectile) {
        float range = hasProperty(DefaultProperties.RANGE) ? property(DefaultProperties.RANGE) : 15.0f;
        return (range / 10.0f) * ctx.modifiers().getFactor(SpellModifiers.RANGE);
    }
}
