package com.binaris.wizardry.registry;

import java.util.function.Supplier;

public final class Spells {
    public static final Supplier<Spell> NONE = () -> EBSpells.NONE;

    public static void init() {}

    private Spells() {}
}
