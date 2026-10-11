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
    public static final DeferredItem<WandItem> NOVICE_WAND = registerWand("wand_novice", SpellTiers.NOVICE, Elements.MAGIC);
    public static final DeferredItem<WandItem> APPRENTICE_WAND = registerWand("wand_apprentice", SpellTiers.APPRENTICE, Elements.MAGIC);
    public static final DeferredItem<WandItem> ADVANCED_WAND = registerWand("wand_advanced", SpellTiers.ADVANCED, Elements.MAGIC);
    public static final DeferredItem<WandItem> MASTER_WAND = registerWand("wand_master", SpellTiers.MASTER, Elements.MAGIC);

    // Earth Wands
    public static final DeferredItem<WandItem> NOVICE_EARTH_WAND = registerWand("wand_novice_earth", SpellTiers.NOVICE, Elements.EARTH);
    public static final DeferredItem<WandItem> APPRENTICE_EARTH_WAND = registerWand("wand_apprentice_earth", SpellTiers.APPRENTICE, Elements.EARTH);
    public static final DeferredItem<WandItem> ADVANCED_EARTH_WAND = registerWand("wand_advanced_earth", SpellTiers.ADVANCED, Elements.EARTH);
    public static final DeferredItem<WandItem> MASTER_EARTH_WAND = registerWand("wand_master_earth", SpellTiers.MASTER, Elements.EARTH);

    // Fire Wands
    public static final DeferredItem<WandItem> NOVICE_FIRE_WAND = registerWand("wand_novice_fire", SpellTiers.NOVICE, Elements.FIRE);
    public static final DeferredItem<WandItem> APPRENTICE_FIRE_WAND = registerWand("wand_apprentice_fire", SpellTiers.APPRENTICE, Elements.FIRE);
    public static final DeferredItem<WandItem> ADVANCED_FIRE_WAND = registerWand("wand_advanced_fire", SpellTiers.ADVANCED, Elements.FIRE);
    public static final DeferredItem<WandItem> MASTER_FIRE_WAND = registerWand("wand_master_fire", SpellTiers.MASTER, Elements.FIRE);

    // Healing Wands
    public static final DeferredItem<WandItem> NOVICE_HEALING_WAND = registerWand("wand_novice_healing", SpellTiers.NOVICE, Elements.HEALING);
    public static final DeferredItem<WandItem> APPRENTICE_HEALING_WAND = registerWand("wand_apprentice_healing", SpellTiers.APPRENTICE, Elements.HEALING);
    public static final DeferredItem<WandItem> ADVANCED_HEALING_WAND = registerWand("wand_advanced_healing", SpellTiers.ADVANCED, Elements.HEALING);
    public static final DeferredItem<WandItem> MASTER_HEALING_WAND = registerWand("wand_master_healing", SpellTiers.MASTER, Elements.HEALING);

    // Ice Wands
    public static final DeferredItem<WandItem> NOVICE_ICE_WAND = registerWand("wand_novice_ice", SpellTiers.NOVICE, Elements.ICE);
    public static final DeferredItem<WandItem> APPRENTICE_ICE_WAND = registerWand("wand_apprentice_ice", SpellTiers.APPRENTICE, Elements.ICE);
    public static final DeferredItem<WandItem> ADVANCED_ICE_WAND = registerWand("wand_advanced_ice", SpellTiers.ADVANCED, Elements.ICE);
    public static final DeferredItem<WandItem> MASTER_ICE_WAND = registerWand("wand_master_ice", SpellTiers.MASTER, Elements.ICE);

    // Lightning Wands
    public static final DeferredItem<WandItem> NOVICE_LIGHTNING_WAND = registerWand("wand_novice_lightning", SpellTiers.NOVICE, Elements.LIGHTNING);
    public static final DeferredItem<WandItem> APPRENTICE_LIGHTNING_WAND = registerWand("wand_apprentice_lightning", SpellTiers.APPRENTICE, Elements.LIGHTNING);
    public static final DeferredItem<WandItem> ADVANCED_LIGHTNING_WAND = registerWand("wand_advanced_lightning", SpellTiers.ADVANCED, Elements.LIGHTNING);
    public static final DeferredItem<WandItem> MASTER_LIGHTNING_WAND = registerWand("wand_master_lightning", SpellTiers.MASTER, Elements.LIGHTNING);

    // Necromancy Wands
    public static final DeferredItem<WandItem> NOVICE_NECROMANCY_WAND = registerWand("wand_novice_necromancy", SpellTiers.NOVICE, Elements.NECROMANCY);
    public static final DeferredItem<WandItem> APPRENTICE_NECROMANCY_WAND = registerWand("wand_apprentice_necromancy", SpellTiers.APPRENTICE, Elements.NECROMANCY);
    public static final DeferredItem<WandItem> ADVANCED_NECROMANCY_WAND = registerWand("wand_advanced_necromancy", SpellTiers.ADVANCED, Elements.NECROMANCY);
    public static final DeferredItem<WandItem> MASTER_NECROMANCY_WAND = registerWand("wand_master_necromancy", SpellTiers.MASTER, Elements.NECROMANCY);

    // Sorcery Wands
    public static final DeferredItem<WandItem> NOVICE_SORCERY_WAND = registerWand("wand_novice_sorcery", SpellTiers.NOVICE, Elements.SORCERY);
    public static final DeferredItem<WandItem> APPRENTICE_SORCERY_WAND = registerWand("wand_apprentice_sorcery", SpellTiers.APPRENTICE, Elements.SORCERY);
    public static final DeferredItem<WandItem> ADVANCED_SORCERY_WAND = registerWand("wand_advanced_sorcery", SpellTiers.ADVANCED, Elements.SORCERY);
    public static final DeferredItem<WandItem> MASTER_SORCERY_WAND = registerWand("wand_master_sorcery", SpellTiers.MASTER, Elements.SORCERY);

    private static DeferredItem<WandItem> registerWand(String name, Supplier<SpellTier> tierSupplier, Supplier<Element> elementSupplier) {
        DeferredItem<WandItem> item = ITEMS.register(name, () -> new WandItem(tierSupplier.get(), elementSupplier.get()));
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
