package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.wand.WandItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public final class EBWandItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WizardryMod.MODID);
    private static final Map<String, DeferredItem<WandItem>> WAND_MAP = new HashMap<>();

    // Magic (Non-elemental) Wands
    public static final DeferredItem<WandItem> NOVICE_WAND = registerWand("wand_novice", SpellTiers.NOVICE.get(), Elements.MAGIC.get());
    public static final DeferredItem<WandItem> APPRENTICE_WAND = registerWand("wand_apprentice", SpellTiers.APPRENTICE.get(), Elements.MAGIC.get());
    public static final DeferredItem<WandItem> ADVANCED_WAND = registerWand("wand_advanced", SpellTiers.ADVANCED.get(), Elements.MAGIC.get());
    public static final DeferredItem<WandItem> MASTER_WAND = registerWand("wand_master", SpellTiers.MASTER.get(), Elements.MAGIC.get());

    // Earth Wands
    public static final DeferredItem<WandItem> NOVICE_EARTH_WAND = registerWand("wand_novice_earth", SpellTiers.NOVICE.get(), Elements.EARTH.get());
    public static final DeferredItem<WandItem> APPRENTICE_EARTH_WAND = registerWand("wand_apprentice_earth", SpellTiers.APPRENTICE.get(), Elements.EARTH.get());
    public static final DeferredItem<WandItem> ADVANCED_EARTH_WAND = registerWand("wand_advanced_earth", SpellTiers.ADVANCED.get(), Elements.EARTH.get());
    public static final DeferredItem<WandItem> MASTER_EARTH_WAND = registerWand("wand_master_earth", SpellTiers.MASTER.get(), Elements.EARTH.get());

    // Fire Wands
    public static final DeferredItem<WandItem> NOVICE_FIRE_WAND = registerWand("wand_novice_fire", SpellTiers.NOVICE.get(), Elements.FIRE.get());
    public static final DeferredItem<WandItem> APPRENTICE_FIRE_WAND = registerWand("wand_apprentice_fire", SpellTiers.APPRENTICE.get(), Elements.FIRE.get());
    public static final DeferredItem<WandItem> ADVANCED_FIRE_WAND = registerWand("wand_advanced_fire", SpellTiers.ADVANCED.get(), Elements.FIRE.get());
    public static final DeferredItem<WandItem> MASTER_FIRE_WAND = registerWand("wand_master_fire", SpellTiers.MASTER.get(), Elements.FIRE.get());

    // Healing Wands
    public static final DeferredItem<WandItem> NOVICE_HEALING_WAND = registerWand("wand_novice_healing", SpellTiers.NOVICE.get(), Elements.HEALING.get());
    public static final DeferredItem<WandItem> APPRENTICE_HEALING_WAND = registerWand("wand_apprentice_healing", SpellTiers.APPRENTICE.get(), Elements.HEALING.get());
    public static final DeferredItem<WandItem> ADVANCED_HEALING_WAND = registerWand("wand_advanced_healing", SpellTiers.ADVANCED.get(), Elements.HEALING.get());
    public static final DeferredItem<WandItem> MASTER_HEALING_WAND = registerWand("wand_master_healing", SpellTiers.MASTER.get(), Elements.HEALING.get());

    // Ice Wands
    public static final DeferredItem<WandItem> NOVICE_ICE_WAND = registerWand("wand_novice_ice", SpellTiers.NOVICE.get(), Elements.ICE.get());
    public static final DeferredItem<WandItem> APPRENTICE_ICE_WAND = registerWand("wand_apprentice_ice", SpellTiers.APPRENTICE.get(), Elements.ICE.get());
    public static final DeferredItem<WandItem> ADVANCED_ICE_WAND = registerWand("wand_advanced_ice", SpellTiers.ADVANCED.get(), Elements.ICE.get());
    public static final DeferredItem<WandItem> MASTER_ICE_WAND = registerWand("wand_master_ice", SpellTiers.MASTER.get(), Elements.ICE.get());

    // Lightning Wands
    public static final DeferredItem<WandItem> NOVICE_LIGHTNING_WAND = registerWand("wand_novice_lightning", SpellTiers.NOVICE.get(), Elements.LIGHTNING.get());
    public static final DeferredItem<WandItem> APPRENTICE_LIGHTNING_WAND = registerWand("wand_apprentice_lightning", SpellTiers.APPRENTICE.get(), Elements.LIGHTNING.get());
    public static final DeferredItem<WandItem> ADVANCED_LIGHTNING_WAND = registerWand("wand_advanced_lightning", SpellTiers.ADVANCED.get(), Elements.LIGHTNING.get());
    public static final DeferredItem<WandItem> MASTER_LIGHTNING_WAND = registerWand("wand_master_lightning", SpellTiers.MASTER.get(), Elements.LIGHTNING.get());

    // Necromancy Wands
    public static final DeferredItem<WandItem> NOVICE_NECROMANCY_WAND = registerWand("wand_novice_necromancy", SpellTiers.NOVICE.get(), Elements.NECROMANCY.get());
    public static final DeferredItem<WandItem> APPRENTICE_NECROMANCY_WAND = registerWand("wand_apprentice_necromancy", SpellTiers.APPRENTICE.get(), Elements.NECROMANCY.get());
    public static final DeferredItem<WandItem> ADVANCED_NECROMANCY_WAND = registerWand("wand_advanced_necromancy", SpellTiers.ADVANCED.get(), Elements.NECROMANCY.get());
    public static final DeferredItem<WandItem> MASTER_NECROMANCY_WAND = registerWand("wand_master_necromancy", SpellTiers.MASTER.get(), Elements.NECROMANCY.get());

    // Sorcery Wands
    public static final DeferredItem<WandItem> NOVICE_SORCERY_WAND = registerWand("wand_novice_sorcery", SpellTiers.NOVICE.get(), Elements.SORCERY.get());
    public static final DeferredItem<WandItem> APPRENTICE_SORCERY_WAND = registerWand("wand_apprentice_sorcery", SpellTiers.APPRENTICE.get(), Elements.SORCERY.get());
    public static final DeferredItem<WandItem> ADVANCED_SORCERY_WAND = registerWand("wand_advanced_sorcery", SpellTiers.ADVANCED.get(), Elements.SORCERY.get());
    public static final DeferredItem<WandItem> MASTER_SORCERY_WAND = registerWand("wand_master_sorcery", SpellTiers.MASTER.get(), Elements.SORCERY.get());

    private static DeferredItem<WandItem> registerWand(String name, SpellTier tier, Element element) {
        DeferredItem<WandItem> item = ITEMS.register(name, () -> new WandItem(tier, element));
        String key = makeKey(tier, element);
        WAND_MAP.put(key, item);
        return item;
    }

    private static String makeKey(SpellTier tier, Element element) {
        String tierName = tier != null ? tier.getName() : "novice";
        String elementName = (element != null && element != Elements.MAGIC.get()) ? element.getName() : "magic";
        return tierName + "_" + elementName;
    }

    public static WandItem getWand(SpellTier tier, Element element) {
        String key = makeKey(tier, element);
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
