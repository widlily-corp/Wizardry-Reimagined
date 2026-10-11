package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.bauble.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBBaubleItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WizardryMod.MODID);

    // ==========================================
    // Rings
    // ==========================================
    public static final DeferredItem<RingOfCondensationItem> RING_CONDENSING =
            ITEMS.register("ring_condensing", () -> new RingOfCondensationItem(Rarity.RARE));
    public static final DeferredItem<RingItem> RING_ARCANE_FROST =
            ITEMS.register("ring_arcane_frost", () -> new RingItem(Rarity.EPIC));
    public static final DeferredItem<RingItem> RING_BATTLEMAGE =
            ITEMS.register("ring_battlemage", () -> new RingItem(Rarity.UNCOMMON));
    public static final DeferredItem<RingItem> RING_BLOCKWRANGLER =
            ITEMS.register("ring_blockwrangler", () -> new RingItem(Rarity.RARE));
    public static final DeferredItem<RingItem> RING_COMBUSTION =
            ITEMS.register("ring_combustion", () -> new RingItem(Rarity.EPIC));
    public static final DeferredItem<RingItem> RING_CONJURER =
            ITEMS.register("ring_conjurer", () -> new RingItem(Rarity.RARE));
    public static final DeferredItem<RingItem> RING_DEFENDER =
            ITEMS.register("ring_defender", () -> new RingItem(Rarity.RARE));
    public static final DeferredItem<RingItem> RING_DISINTEGRATION =
            ITEMS.register("ring_disintegration", () -> new RingItem(Rarity.RARE));
    public static final DeferredItem<RingItem> RING_EARTH_BIOME =
            ITEMS.register("ring_earth_biome", () -> new RingItem(Rarity.UNCOMMON));
    public static final DeferredItem<RingItem> RING_EARTH_MELEE =
            ITEMS.register("ring_earth_melee", () -> new RingItem(Rarity.UNCOMMON));
    public static final DeferredItem<RingItem> RING_EVOKER =
            ITEMS.register("ring_evoker", () -> new RingItem(Rarity.RARE));
    public static final DeferredItem<RingItem> RING_EXTRACTION =
            ITEMS.register("ring_extraction", () -> new RingItem(Rarity.UNCOMMON));
    public static final DeferredItem<RingItem> RING_FIRE_BIOME =
            ITEMS.register("ring_fire_biome", () -> new RingItem(Rarity.UNCOMMON));
    public static final DeferredItem<RingItem> RING_FIRE_FOCUS =
            ITEMS.register("ring_fire_focus", () -> new RingItem(Rarity.RARE));
    public static final DeferredItem<RingItem> RING_FIRE_MELEE =
            ITEMS.register("ring_fire_melee", () -> new RingItem(Rarity.UNCOMMON));
    public static final DeferredItem<RingItem> RING_FULL_MOON =
            ITEMS.register("ring_full_moon", () -> new RingItem(Rarity.RARE));
    public static final DeferredItem<RingItem> RING_HAMMER =
            ITEMS.register("ring_hammer", () -> new RingItem(Rarity.EPIC));
    public static final DeferredItem<RingItem> RING_ICE_BIOME =
            ITEMS.register("ring_ice_biome", () -> new RingItem(Rarity.UNCOMMON));
    public static final DeferredItem<RingItem> RING_ICE_MELEE =
            ITEMS.register("ring_ice_melee", () -> new RingItem(Rarity.UNCOMMON));
    public static final DeferredItem<RingItem> RING_ICE_PROTECTION =
            ITEMS.register("ring_ice_protection", () -> new RingItem(Rarity.UNCOMMON));
    public static final DeferredItem<RingItem> RING_INTERDICTION =
            ITEMS.register("ring_interdiction", () -> new RingItem(Rarity.EPIC));
    public static final DeferredItem<RingItem> RING_LEECHING =
            ITEMS.register("ring_leeching", () -> new RingItem(Rarity.RARE));
    public static final DeferredItem<RingItem> RING_LIGHTNING_MELEE =
            ITEMS.register("ring_lightning_melee", () -> new RingItem(Rarity.UNCOMMON));
    public static final DeferredItem<RingItem> RING_MANA_RETURN =
            ITEMS.register("ring_mana_return", () -> new RingItem(Rarity.EPIC));
    public static final DeferredItem<RingItem> RING_METEOR =
            ITEMS.register("ring_meteor", () -> new RingItem(Rarity.EPIC));
    public static final DeferredItem<RingItem> RING_MIND_CONTROL =
            ITEMS.register("ring_mind_control", () -> new RingItem(Rarity.RARE));
    public static final DeferredItem<RingItem> RING_NECROMANCY_MELEE =
            ITEMS.register("ring_necromancy_melee", () -> new RingItem(Rarity.UNCOMMON));
    public static final DeferredItem<RingItem> RING_PALADIN =
            ITEMS.register("ring_paladin", () -> new RingItem(Rarity.UNCOMMON));
    public static final DeferredItem<RingItem> RING_POISON =
            ITEMS.register("ring_poison", () -> new RingItem(Rarity.RARE));
    public static final DeferredItem<RingItem> RING_SEEKING =
            ITEMS.register("ring_seeking", () -> new RingItem(Rarity.EPIC));
    public static final DeferredItem<RingItem> RING_SHATTERING =
            ITEMS.register("ring_shattering", () -> new RingItem(Rarity.RARE));
    public static final DeferredItem<RingItem> RING_SIPHONING =
            ITEMS.register("ring_siphoning", () -> new RingItem(Rarity.UNCOMMON));
    public static final DeferredItem<RingItem> RING_SOULBINDING =
            ITEMS.register("ring_soulbinding", () -> new RingItem(Rarity.EPIC));
    public static final DeferredItem<RingItem> RING_STORM =
            ITEMS.register("ring_storm", () -> new RingItem(Rarity.RARE));
    public static final DeferredItem<RingItem> RING_STORMCLOUD =
            ITEMS.register("ring_stormcloud", () -> new RingItem(Rarity.RARE));

    // ==========================================
    // Amulets
    // ==========================================
    public static final DeferredItem<AmuletOfArcaneDefenseItem> AMULET_ARCANE_DEFENCE =
            ITEMS.register("amulet_arcane_defence", () -> new AmuletOfArcaneDefenseItem(Rarity.RARE));
    public static final DeferredItem<AmuletItem> AMULET_WARDING =
            ITEMS.register("amulet_warding", () -> new AmuletItem(Rarity.UNCOMMON));
    public static final DeferredItem<AmuletItem> AMULET_WISDOM =
            ITEMS.register("amulet_wisdom", () -> new AmuletItem(Rarity.RARE));
    public static final DeferredItem<AmuletItem> AMULET_FIRE_PROTECTION =
            ITEMS.register("amulet_fire_protection", () -> new AmuletItem(Rarity.UNCOMMON));
    public static final DeferredItem<AmuletItem> AMULET_FIRE_CLOAKING =
            ITEMS.register("amulet_fire_cloaking", () -> new AmuletItem(Rarity.RARE));
    public static final DeferredItem<AmuletItem> AMULET_ICE_IMMUNITY =
            ITEMS.register("amulet_ice_immunity", () -> new AmuletItem(Rarity.EPIC));
    public static final DeferredItem<AmuletItem> AMULET_ICE_PROTECTION =
            ITEMS.register("amulet_ice_protection", () -> new AmuletItem(Rarity.UNCOMMON));
    public static final DeferredItem<AmuletItem> AMULET_FROST_WARDING =
            ITEMS.register("amulet_frost_warding", () -> new AmuletItem(Rarity.RARE));
    public static final DeferredItem<AmuletItem> AMULET_POTENTIAL =
            ITEMS.register("amulet_potential", () -> new AmuletItem(Rarity.RARE));
    public static final DeferredItem<AmuletItem> AMULET_CHANNELING =
            ITEMS.register("amulet_channeling", () -> new AmuletItem(Rarity.RARE));
    public static final DeferredItem<AmuletItem> AMULET_LICH =
            ITEMS.register("amulet_lich", () -> new AmuletItem(Rarity.UNCOMMON));
    public static final DeferredItem<AmuletOfWitherImmunityItem> AMULET_WITHER_IMMUNITY =
            ITEMS.register("amulet_wither_immunity", () -> new AmuletOfWitherImmunityItem(Rarity.EPIC));
    public static final DeferredItem<AmuletOfGlideItem> AMULET_GLIDE =
            ITEMS.register("amulet_glide", () -> new AmuletOfGlideItem(Rarity.RARE));
    public static final DeferredItem<AmuletItem> AMULET_BANISHING =
            ITEMS.register("amulet_banishing", () -> new AmuletItem(Rarity.UNCOMMON));
    public static final DeferredItem<AmuletItem> AMULET_ANCHORING =
            ITEMS.register("amulet_anchoring", () -> new AmuletItem(Rarity.RARE));
    public static final DeferredItem<AmuletOfRecoveryItem> AMULET_RECOVERY =
            ITEMS.register("amulet_recovery", () -> new AmuletOfRecoveryItem(Rarity.UNCOMMON));
    public static final DeferredItem<AmuletItem> AMULET_TRANSIENCE =
            ITEMS.register("amulet_transience", () -> new AmuletItem(Rarity.EPIC));
    public static final DeferredItem<AmuletItem> AMULET_RESURRECTION =
            ITEMS.register("amulet_resurrection", () -> new AmuletItem(Rarity.EPIC));
    public static final DeferredItem<AmuletItem> AMULET_AUTO_SHIELD =
            ITEMS.register("amulet_auto_shield", () -> new AmuletItem(Rarity.RARE));
    public static final DeferredItem<AmuletItem> AMULET_ABSORPTION =
            ITEMS.register("amulet_absorption", () -> new AmuletItem(Rarity.EPIC));

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    private EBBaubleItems() {}
}
