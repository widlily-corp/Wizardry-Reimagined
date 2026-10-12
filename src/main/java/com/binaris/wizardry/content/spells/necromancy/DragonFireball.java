package com.binaris.wizardry.content.spells.necromancy;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.EntityCastContext;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DragonFireball extends Spell {
    @Override
    public boolean cast(PlayerCastContext ctx) {
        Vec3 look = ctx.caster().getLookAngle();

        if (!ctx.world().isClientSide) {
            double acceleration = ctx.modifiers().get(SpellModifiers.RANGE, property(DefaultProperties.ACCELERATION));
            Vec3 accelVec = new Vec3(look.x * acceleration, look.y * acceleration, look.z * acceleration);
            net.minecraft.world.entity.projectile.DragonFireball fireball =
                    new net.minecraft.world.entity.projectile.DragonFireball(ctx.world(), ctx.caster(), accelVec);

            fireball.setPos(ctx.caster().getX() + look.x, ctx.caster().getY() + look.y + 1.3, ctx.caster().getZ() + look.z);
            ctx.world().addFreshEntity(fireball);
        }

        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        ctx.caster().swing(InteractionHand.MAIN_HAND);
        return true;
    }

    @Override
    public boolean cast(EntityCastContext ctx) {
        Vec3 look = ctx.caster().getLookAngle();

        if (!ctx.world().isClientSide) {
            double acceleration = property(DefaultProperties.ACCELERATION);
            Vec3 accelVec = new Vec3(look.x * acceleration, look.y * acceleration, look.z * acceleration);
            net.minecraft.world.entity.projectile.DragonFireball fireball =
                    new net.minecraft.world.entity.projectile.DragonFireball(ctx.world(), ctx.caster(), accelVec);

            fireball.setPos(ctx.caster().getX() + look.x, ctx.caster().getY() + look.y + 1.3, ctx.caster().getZ() + look.z);
            ctx.world().addFreshEntity(fireball);
        }

        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        ctx.caster().swing(InteractionHand.MAIN_HAND);
        return true;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.MASTER, Elements.NECROMANCY, SpellTypes.PROJECTILE, SpellAction.POINT, 50, 10, 100)
                .add(DefaultProperties.ACCELERATION, 0.1F)
                .build();
    }
}
