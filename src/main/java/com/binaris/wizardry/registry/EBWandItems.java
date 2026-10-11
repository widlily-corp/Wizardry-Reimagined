package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.wand.WandItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class EBWandItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WizardryMod.MODID);
    private static final Map<String, DeferredItem<WandItem>> WAND_MAP = new HashMap<>();

    // Magic (Non-elemental) Wands
    public static final DeferredItem<WandItem> NOVICE_WAND = registerWand("wand_novice", SpellTiers.NOVICE, Elements.MAGIC, 700);
    public static final DeferredItem<WandItem> APPRENTICE_WAND = registerWand("wand_apprentice", SpellTiers.APPRENTICE, Elements.MAGIC, 1000);
    public static final DeferredItem<WandItem> ADVANCED_WAND = registerWand("wand_advanced", SpellTiers.ADVANCED, Elements.MAGIC, 1500);
    public static final DeferredItem<WandItem> MASTER_WAND = registerWand("wand_master", SpellTiers.MASTER, Elements.MAGIC, 2500);

    // Earth Wands
    public static final DeferredItem<WandItem> NOVICE_EARTH_WAND = registerWand("wand_novice_earth", SpellTiers.NOVICE, Elements.EARTH, 700);
    public static final DeferredItem<WandItem> APPRENTICE_EARTH_WAND = registerWand("wand_apprentice_earth", SpellTiers.APPRENTICE, Elements.EARTH, 1000);
    public static final DeferredItem<WandItem> ADVANCED_EARTH_WAND = registerWand("wand_advanced_earth", SpellTiers.ADVANCED, Elements.EARTH, 1500);
    public static final DeferredItem<WandItem> MASTER_EARTH_WAND = registerWand("wand_master_earth", SpellTiers.MASTER, Elements.EARTH, 2500);

    // Fire Wands
    public static final DeferredItem<WandItem> NOVICE_FIRE_WAND = registerWand("wand_novice_fire", SpellTiers.NOVICE, Elements.FIRE, 700);
    public static final DeferredItem<WandItem> APPRENTICE_FIRE_WAND = registerWand("wand_apprentice_fire", SpellTiers.APPRENTICE, Elements.FIRE, 1000);
    public static final DeferredItem<WandItem> ADVANCED_FIRE_WAND = registerWand("wand_advanced_fire", SpellTiers.ADVANCED, Elements.FIRE, 1500);
    public static final DeferredItem<WandItem> MASTER_FIRE_WAND = registerWand("wand_master_fire", SpellTiers.MASTER, Elements.FIRE, 2500);

    // Healing Wands
    public static final DeferredItem<WandItem> NOVICE_HEALING_WAND = registerWand("wand_novice_healing", SpellTiers.NOVICE, Elements.HEALING, 700);
    public static final DeferredItem<WandItem> APPRENTICE_HEALING_WAND = registerWand("wand_apprentice_healing", SpellTiers.APPRENTICE, Elements.HEALING, 1000);
    public static final DeferredItem<WandItem> ADVANCED_HEALING_WAND = registerWand("wand_advanced_healing", SpellTiers.ADVANCED, Elements.HEALING, 1500);
    public static final DeferredItem<WandItem> MASTER_HEALING_WAND = registerWand("wand_master_healing", SpellTiers.MASTER, Elements.HEALING, 2500);

    // Ice Wands
    public static final DeferredItem<WandItem> NOVICE_ICE_WAND = registerWand("wand_novice_ice", SpellTiers.NOVICE, Elements.ICE, 700);
    public static final DeferredItem<WandItem> APPRENTICE_ICE_WAND = registerWand("wand_apprentice_ice", SpellTiers.APPRENTICE, Elements.ICE, 1000);
    public static final DeferredItem<WandItem> ADVANCED_ICE_WAND = registerWand("wand_advanced_ice", SpellTiers.ADVANCED, Elements.ICE, 1500);
    public static final DeferredItem<WandItem> MASTER_ICE_WAND = registerWand("wand_master_ice", SpellTiers.MASTER, Elements.ICE, 2500);

    // Lightning Wands
    public static final DeferredItem<WandItem> NOVICE_LIGHTNING_WAND = registerWand("wand_novice_lightning", SpellTiers.NOVICE, Elements.LIGHTNING, 700);
    public static final DeferredItem<WandItem> APPRENTICE_LIGHTNING_WAND = registerWand("wand_apprentice_lightning", SpellTiers.APPRENTICE, Elements.LIGHTNING, 1000);
    public static final DeferredItem<WandItem> ADVANCED_LIGHTNING_WAND = registerWand("wand_advanced_lightning", SpellTiers.ADVANCED, Elements.LIGHTNING, 1500);
    public static final DeferredItem<WandItem> MASTER_LIGHTNING_WAND = registerWand("wand_master_lightning", SpellTiers.MASTER, Elements.LIGHTNING, 2500);

    // Necromancy Wands
    public static final DeferredItem<WandItem> NOVICE_NECROMANCY_WAND = registerWand("wand_novice_necromancy", SpellTiers.NOVICE, Elements.NECROMANCY, 700);
    public static final DeferredItem<WandItem> APPRENTICE_NECROMANCY_WAND = registerWand("wand_apprentice_necromancy", SpellTiers.APPRENTICE, Elements.NECROMANCY, 1000);
    public static final DeferredItem<WandItem> ADVANCED_NECROMANCY_WAND = registerWand("wand_advanced_necromancy", SpellTiers.ADVANCED, Elements.NECROMANCY, 1500);
    public static final DeferredItem<WandItem> MASTER_NECROMANCY_WAND = registerWand("wand_master_necromancy", SpellTiers.MASTER, Elements.NECROMANCY, 2500);

    // Sorcery Wands
    public static final DeferredItem<WandItem> NOVICE_SORCERY_WAND = registerWand("wand_novice_sorcery", SpellTiers.NOVICE, Elements.SORCERY, 700);
    public static final DeferredItem<WandItem> APPRENTICE_SORCERY_WAND = registerWand("wand_apprentice_sorcery", SpellTiers.APPRENTICE, Elements.SORCERY, 1000);
    public static final DeferredItem<WandItem> ADVANCED_SORCERY_WAND = registerWand("wand_advanced_sorcery", SpellTiers.ADVANCED, Elements.SORCERY, 1500);
    public static final DeferredItem<WandItem> MASTER_SORCERY_WAND = registerWand("wand_master_sorcery", SpellTiers.MASTER, Elements.SORCERY, 2500);

    private static DeferredItem<WandItem> registerWand(String name, Supplier<SpellTier> tierSupplier, Supplier<Element> elementSupplier, int durability) {
        DeferredItem<WandItem> item = ITEMS.register(name, () -> new WandItem(tierSupplier, elementSupplier, durability));
        WAND_MAP.put(name, item);
        return item;
    }

    public static WandItem getWand(SpellTier tier, Element element) {
        String tierName = tier != null ? tier.getName() : "novice";
        String elementName = (element != null && !element.getName().equals("magic")) ? "_" + element.getName() : "";
        String key = "wand_" + tierName + elementName;
        DeferredItem<WandItem> holder = WAND_MAP.get(key);
        if (holder != null) {
            return holder.get();
        }
        return NOVICE_WAND.get();
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    private EBWandItems() {}
}
