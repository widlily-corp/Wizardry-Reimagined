package com.binaris.wizardry.content.spells.healing;

import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Evade extends Spell {
    @Override
    public boolean cast(PlayerCastContext ctx) {
        if (!ctx.caster().onGround()) return false;

        Vec3 look = ctx.caster().getLookAngle();
        look = new Vec3(look.x, 0, look.z).normalize();

        Vec3 evadeDirection = look.yRot((float) Math.PI / 2f);
        evadeDirection = evadeDirection.scale(ctx.modifiers().get(SpellModifiers.POTENCY, property(DefaultProperties.SPEED)));
        ctx.caster().addDeltaMovement(new Vec3(evadeDirection.x, 0.25f, evadeDirection.z));
        ctx.caster().hurtMarked = true;
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
                .assignBaseProperties(SpellTiers.NOVICE, Elements.HEALING, SpellTypes.DEFENCE, SpellAction.NONE, 5, 0, 5)
                .add(DefaultProperties.SPEED, 1F).build();
    }
}
