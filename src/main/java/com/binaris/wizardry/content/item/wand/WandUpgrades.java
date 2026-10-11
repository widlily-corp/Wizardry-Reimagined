package com.binaris.wizardry.content.item.wand;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.registry.SpellTier;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class WandUpgrades {
    public static final String STORAGE = "storage";
    public static final String ATTUNEMENT = "attunement";
    public static final String CONDENSER = "condenser";
    public static final String COOLDOWN = "cooldown";
    public static final String RANGE = "range";
    public static final String DURATION = "duration";
    public static final String BLAST = "blast";
    public static final String SIPHON = "siphon";
    public static final String MELEE = "melee";

    public static final List<String> ALL_UPGRADE_IDS = List.of(
            STORAGE, ATTUNEMENT, CONDENSER, COOLDOWN, RANGE, DURATION, BLAST, SIPHON, MELEE
    );

    private static final Map<Item, String> CUSTOM_UPGRADES = new HashMap<>();

    public static void register(Item item, String upgradeId) {
        CUSTOM_UPGRADES.put(item, upgradeId);
    }

    @Nullable
    public static String getUpgradeId(ItemStack stack) {
        if (stack.isEmpty()) return null;
        if (CUSTOM_UPGRADES.containsKey(stack.getItem())) {
            return CUSTOM_UPGRADES.get(stack.getItem());
        }

        ResourceLocation loc = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (loc.getNamespace().equals(WizardryMod.MODID) && loc.getPath().endsWith("_upgrade")) {
            String id = loc.getPath().substring(0, loc.getPath().length() - "_upgrade".length());
            if (ALL_UPGRADE_IDS.contains(id)) {
                return id;
            }
        }
        return null;
    }

    public static boolean isWandUpgrade(ItemStack stack) {
        return getUpgradeId(stack) != null;
    }

    @Nullable
    public static SpellTier getTomeTier(ItemStack stack) {
        if (stack.isEmpty()) return null;
        ResourceLocation loc = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (loc.getNamespace().equals(WizardryMod.MODID)) {
            String path = loc.getPath();
            if (path.equals("arcane_tome") || path.equals("apprentice_arcane_tome")) {
                return SpellTiers.APPRENTICE.get();
            } else if (path.equals("advanced_arcane_tome")) {
                return SpellTiers.ADVANCED.get();
            } else if (path.equals("master_arcane_tome")) {
                return SpellTiers.MASTER.get();
            }
        }
        return null;
    }

    public static boolean isTome(ItemStack stack) {
        return getTomeTier(stack) != null;
    }

    public static boolean isUpgradeOrTome(ItemStack stack) {
        return isWandUpgrade(stack) || isTome(stack);
    }

    private WandUpgrades() {}
}
