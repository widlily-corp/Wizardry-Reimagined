package com.binaris.wizardry.api.content.spell.internal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class CastContext {
    protected Level world;
    protected int castingTicks;
    protected SpellModifiers modifiers;
    protected LivingEntity caster;

    public CastContext(Level world, int castingTicks, SpellModifiers modifiers) {
        this.world = world;
        this.castingTicks = castingTicks;
        this.modifiers = modifiers != null ? modifiers : new SpellModifiers();
        this.caster = null;
    }

    public CastContext(Level world, LivingEntity caster, int castingTicks, SpellModifiers modifiers) {
        this.world = world;
        this.castingTicks = castingTicks;
        this.modifiers = modifiers != null ? modifiers : new SpellModifiers();
        this.caster = caster;
    }

    public LivingEntity caster() {
        return caster;
    }

    public Level world() {
        return world;
    }

    public int castingTicks() {
        return castingTicks;
    }

    public SpellModifiers modifiers() {
        return modifiers;
    }
}
