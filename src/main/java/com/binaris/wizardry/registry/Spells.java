package com.binaris.wizardry.registry;

import net.neoforged.neoforge.registries.DeferredHolder;

public final class Spells {
    public static final DeferredHolder<Spell, NoneSpell> NONE = EBRegistries.SPELLS.register("none", NoneSpell::new);

    public static void init() {}

    private Spells() {}
}
