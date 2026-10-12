package com.binaris.wizardry.api.content.spell.internal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EntityCastContext extends CastContext {
    private final LivingEntity target;

    public EntityCastContext(Level world, LivingEntity caster, @Nullable LivingEntity target, int castingTicks, SpellModifiers modifiers) {
        super(world, caster, castingTicks, modifiers);
        this.target = target;
    }

    public @Nullable LivingEntity target() {
        return target;
    }

    public net.minecraft.world.InteractionHand hand() {
        return net.minecraft.world.InteractionHand.MAIN_HAND;
    }
}
