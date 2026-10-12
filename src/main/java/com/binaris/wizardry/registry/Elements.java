package com.binaris.wizardry.registry;

import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public final class Elements {
    public static final DeferredHolder<Element, Element> MAGIC = register("magic", ChatFormatting.GRAY, 0xe4c7cd, 0xfeffbe, 0x9d2cf3);
    public static final DeferredHolder<Element, Element> NONE = MAGIC;
    public static final DeferredHolder<Element, Element> FIRE = register("fire", ChatFormatting.DARK_RED, 0xff9600, 0xfffe67, 0xd02700);
    public static final DeferredHolder<Element, Element> ICE = register("ice", ChatFormatting.AQUA, 0xa3e8f4, 0xe9f9fc, 0x138397);
    public static final DeferredHolder<Element, Element> LIGHTNING = register("lightning", ChatFormatting.DARK_AQUA, 0x409ee1, 0xf5f0ff, 0x225474);
    public static final DeferredHolder<Element, Element> NECROMANCY = register("necromancy", ChatFormatting.DARK_PURPLE, 0xa811ce, 0xf575f5, 0x382366);
    public static final DeferredHolder<Element, Element> EARTH = register("earth", ChatFormatting.DARK_GREEN, 0xa8f408, 0xc8ffb2, 0x795c28);
    public static final DeferredHolder<Element, Element> SORCERY = register("sorcery", ChatFormatting.GREEN, 0x56e8e3, 0xe8fcfc, 0x16a64d);
    public static final DeferredHolder<Element, Element> HEALING = register("healing", ChatFormatting.YELLOW, 0xfff69e, 0xfffff6, 0xa18200);

    public static final List<DeferredHolder<Element, Element>> ALL = List.of(
            MAGIC, FIRE, ICE, LIGHTNING, NECROMANCY, EARTH, SORCERY, HEALING
    );

    private static DeferredHolder<Element, Element> register(String name, ChatFormatting formatting, int... colors) {
        return EBRegistries.ELEMENTS.register(name, () -> new Element(name, formatting, colors));
    }

    public static Element fromName(String name) {
        if (name == null) return MAGIC.get();
        for (DeferredHolder<Element, Element> holder : ALL) {
            if (holder.get().getName().equalsIgnoreCase(name)) return holder.get();
        }
        return MAGIC.get();
    }

    public static void init() {}

    private Elements() {}
}
