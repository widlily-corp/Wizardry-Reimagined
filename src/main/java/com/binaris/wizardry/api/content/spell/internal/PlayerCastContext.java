package com.binaris.wizardry.api.content.spell.internal;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlayerCastContext extends CastContext {
    private final InteractionHand hand;

    public PlayerCastContext(Level world, Player caster, InteractionHand hand, int castingTicks, SpellModifiers modifiers) {
        super(world, caster, castingTicks, modifiers);
        this.hand = hand != null ? hand : InteractionHand.MAIN_HAND;
    }

    @Override
    public Player caster() {
        return (Player) caster;
    }

    public InteractionHand hand() {
        return hand;
    }
}
