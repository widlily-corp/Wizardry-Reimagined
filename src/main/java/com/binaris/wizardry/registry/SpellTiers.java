package com.binaris.wizardry.registry;

import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public final class SpellTiers {
    public static final DeferredHolder<SpellTier, SpellTier> NOVICE = EBRegistries.TIERS.register("novice",
            () -> new SpellTier("novice", 700, 3, 12, 0, ChatFormatting.WHITE, 100));
    public static final DeferredHolder<SpellTier, SpellTier> BASIC = NOVICE;

    public static final DeferredHolder<SpellTier, SpellTier> APPRENTICE = EBRegistries.TIERS.register("apprentice",
            () -> new SpellTier("apprentice", 1000, 5, 5, 1, ChatFormatting.AQUA, 1500));

    public static final DeferredHolder<SpellTier, SpellTier> ADVANCED = EBRegistries.TIERS.register("advanced",
            () -> new SpellTier("advanced", 1500, 7, 2, 2, ChatFormatting.DARK_BLUE, 3000));

    public static final DeferredHolder<SpellTier, SpellTier> MASTER = EBRegistries.TIERS.register("master",
            () -> new SpellTier("master", 2500, 9, 1, 3, ChatFormatting.DARK_PURPLE, 6000));

    public static final DeferredHolder<SpellTier, SpellTier> GRAND = EBRegistries.TIERS.register("grand",
            () -> new SpellTier("grand", 5000, 11, 0, 4, ChatFormatting.GOLD, 15000));

    public static final List<DeferredHolder<SpellTier, SpellTier>> ALL = List.of(NOVICE, APPRENTICE, ADVANCED, MASTER, GRAND);

    public static SpellTier getNextByLevel(int level) {
        for (DeferredHolder<SpellTier, SpellTier> tierHolder : ALL) {
            if (tierHolder.get().getLevel() == level) return tierHolder.get();
        }
        return null;
    }

    public static SpellTier fromName(String name) {
        if (name == null) return NOVICE.get();
        for (DeferredHolder<SpellTier, SpellTier> holder : ALL) {
            if (holder.get().getName().equalsIgnoreCase(name)) return holder.get();
        }
        return NOVICE.get();
    }

    public static void init() {}

    private SpellTiers() {}
}
