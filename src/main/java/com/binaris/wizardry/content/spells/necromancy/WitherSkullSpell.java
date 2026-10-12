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
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class WitherSkullSpell extends Spell {
    @Override
    public boolean cast(PlayerCastContext ctx) {
        Vec3 look = ctx.caster().getLookAngle();
        double acceleration = ctx.modifiers().get(SpellModifiers.RANGE, property(DefaultProperties.SPEED));
        Vec3 accelVec = new Vec3(look.x * acceleration, look.y * acceleration, look.z * acceleration);
        WitherSkull witherSkull = new WitherSkull(ctx.world(), ctx.caster(), accelVec);
        witherSkull.setPos(ctx.caster().getX() + look.x, ctx.caster().getY() + look.y + 1.3, ctx.caster().getZ() + look.z);
        witherSkull.setOwner(ctx.caster());
        ctx.world().addFreshEntity(witherSkull);

        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean cast(EntityCastContext ctx) {
        Vec3 look = ctx.caster().getLookAngle();
        double acceleration = property(DefaultProperties.SPEED);
        Vec3 accelVec = new Vec3(look.x * acceleration, look.y * acceleration, look.z * acceleration);
        WitherSkull witherSkull = new WitherSkull(ctx.world(), ctx.caster(), accelVec);
        witherSkull.setPos(ctx.caster().getX() + look.x, ctx.caster().getY() + look.y + 1.3, ctx.caster().getZ() + look.z);
        witherSkull.setOwner(ctx.caster());
        ctx.world().addFreshEntity(witherSkull);

        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean requiresPacket() {
        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.ADVANCED, Elements.NECROMANCY, SpellTypes.PROJECTILE, SpellAction.POINT, 25, 0, 40)
                .add(DefaultProperties.RANGE, 10F)
                .add(DefaultProperties.SPEED, 0.1F)
                .build();
    }
}
