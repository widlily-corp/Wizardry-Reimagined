package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.material.CrystalItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WizardryMod.MODID);

    // Crystals and shards
    public static final DeferredItem<CrystalItem> MAGIC_CRYSTAL_SHARD =
            ITEMS.register("magic_crystal_shard", () -> new CrystalItem(10, Elements.MAGIC));

    public static final DeferredItem<CrystalItem> MAGIC_CRYSTAL =
            ITEMS.register("magic_crystal", () -> new CrystalItem(100, Elements.MAGIC));

    public static final DeferredItem<CrystalItem> MAGIC_CRYSTAL_GRAND =
            ITEMS.register("magic_crystal_grand", () -> new CrystalItem(400, Elements.MAGIC));

    public static final DeferredItem<CrystalItem> MAGIC_CRYSTAL_EARTH =
            ITEMS.register("magic_crystal_earth", () -> new CrystalItem(100, Elements.EARTH));

    public static final DeferredItem<CrystalItem> MAGIC_CRYSTAL_FIRE =
            ITEMS.register("magic_crystal_fire", () -> new CrystalItem(100, Elements.FIRE));

    public static final DeferredItem<CrystalItem> MAGIC_CRYSTAL_HEALING =
            ITEMS.register("magic_crystal_healing", () -> new CrystalItem(100, Elements.HEALING));

    public static final DeferredItem<CrystalItem> MAGIC_CRYSTAL_ICE =
            ITEMS.register("magic_crystal_ice", () -> new CrystalItem(100, Elements.ICE));

    public static final DeferredItem<CrystalItem> MAGIC_CRYSTAL_LIGHTNING =
            ITEMS.register("magic_crystal_lightning", () -> new CrystalItem(100, Elements.LIGHTNING));

    public static final DeferredItem<CrystalItem> MAGIC_CRYSTAL_NECROMANCY =
            ITEMS.register("magic_crystal_necromancy", () -> new CrystalItem(100, Elements.NECROMANCY));

    public static final DeferredItem<CrystalItem> MAGIC_CRYSTAL_SORCERY =
            ITEMS.register("magic_crystal_sorcery", () -> new CrystalItem(100, Elements.SORCERY));

    // Block items
    public static final DeferredItem<BlockItem> CRYSTAL_BLOCK_MAGIC_ITEM =
            ITEMS.registerSimpleBlockItem(EBBlocks.CRYSTAL_BLOCK_MAGIC);

    public static final DeferredItem<BlockItem> CRYSTAL_BLOCK_FIRE_ITEM =
            ITEMS.registerSimpleBlockItem(EBBlocks.CRYSTAL_BLOCK_FIRE);

    public static final DeferredItem<BlockItem> CRYSTAL_BLOCK_ICE_ITEM =
            ITEMS.registerSimpleBlockItem(EBBlocks.CRYSTAL_BLOCK_ICE);

    public static final DeferredItem<BlockItem> CRYSTAL_BLOCK_LIGHTNING_ITEM =
            ITEMS.registerSimpleBlockItem(EBBlocks.CRYSTAL_BLOCK_LIGHTNING);

    public static final DeferredItem<BlockItem> CRYSTAL_BLOCK_NECROMANCY_ITEM =
            ITEMS.registerSimpleBlockItem(EBBlocks.CRYSTAL_BLOCK_NECROMANCY);

    public static final DeferredItem<BlockItem> CRYSTAL_BLOCK_EARTH_ITEM =
            ITEMS.registerSimpleBlockItem(EBBlocks.CRYSTAL_BLOCK_EARTH);

    public static final DeferredItem<BlockItem> CRYSTAL_BLOCK_SORCERY_ITEM =
            ITEMS.registerSimpleBlockItem(EBBlocks.CRYSTAL_BLOCK_SORCERY);

    public static final DeferredItem<BlockItem> CRYSTAL_BLOCK_HEALING_ITEM =
            ITEMS.registerSimpleBlockItem(EBBlocks.CRYSTAL_BLOCK_HEALING);

    public static final DeferredItem<BlockItem> CRYSTAL_ORE_ITEM =
            ITEMS.registerSimpleBlockItem(EBBlocks.CRYSTAL_ORE);

    public static final DeferredItem<BlockItem> DEEPSLATE_CRYSTAL_ORE_ITEM =
            ITEMS.registerSimpleBlockItem(EBBlocks.DEEPSLATE_CRYSTAL_ORE);

    public static final DeferredItem<BlockItem> CRYSTAL_FLOWER_ITEM =
            ITEMS.registerSimpleBlockItem(EBBlocks.CRYSTAL_FLOWER);

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    private EBItems() {}
}
