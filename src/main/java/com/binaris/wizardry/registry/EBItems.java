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

    // Runestone Items
    public static final DeferredItem<BlockItem> FIRE_RUNESTONE_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.FIRE_RUNESTONE);
    public static final DeferredItem<BlockItem> EARTH_RUNESTONE_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.EARTH_RUNESTONE);
    public static final DeferredItem<BlockItem> ICE_RUNESTONE_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.ICE_RUNESTONE);
    public static final DeferredItem<BlockItem> LIGHTNING_RUNESTONE_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.LIGHTNING_RUNESTONE);
    public static final DeferredItem<BlockItem> NECROMANCY_RUNESTONE_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.NECROMANCY_RUNESTONE);
    public static final DeferredItem<BlockItem> SORCERY_RUNESTONE_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.SORCERY_RUNESTONE);
    public static final DeferredItem<BlockItem> HEALING_RUNESTONE_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.HEALING_RUNESTONE);

    // Pedestal Items
    public static final DeferredItem<BlockItem> FIRE_RUNESTONE_PEDESTAL_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.FIRE_RUNESTONE_PEDESTAL);
    public static final DeferredItem<BlockItem> EARTH_RUNESTONE_PEDESTAL_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.EARTH_RUNESTONE_PEDESTAL);
    public static final DeferredItem<BlockItem> ICE_RUNESTONE_PEDESTAL_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.ICE_RUNESTONE_PEDESTAL);
    public static final DeferredItem<BlockItem> LIGHTNING_RUNESTONE_PEDESTAL_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.LIGHTNING_RUNESTONE_PEDESTAL);
    public static final DeferredItem<BlockItem> NECROMANCY_RUNESTONE_PEDESTAL_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.NECROMANCY_RUNESTONE_PEDESTAL);
    public static final DeferredItem<BlockItem> SORCERY_RUNESTONE_PEDESTAL_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.SORCERY_RUNESTONE_PEDESTAL);
    public static final DeferredItem<BlockItem> HEALING_RUNESTONE_PEDESTAL_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.HEALING_RUNESTONE_PEDESTAL);

    // Library & Decorative Items
    public static final DeferredItem<BlockItem> OAK_BOOKSHELF_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.OAK_BOOKSHELF);
    public static final DeferredItem<BlockItem> SPRUCE_BOOKSHELF_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.SPRUCE_BOOKSHELF);
    public static final DeferredItem<BlockItem> BIRCH_BOOKSHELF_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.BIRCH_BOOKSHELF);
    public static final DeferredItem<BlockItem> JUNGLE_BOOKSHELF_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.JUNGLE_BOOKSHELF);
    public static final DeferredItem<BlockItem> ACACIA_BOOKSHELF_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.ACACIA_BOOKSHELF);
    public static final DeferredItem<BlockItem> DARK_OAK_BOOKSHELF_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.DARK_OAK_BOOKSHELF);
    public static final DeferredItem<BlockItem> BOOKSHELF_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.BOOKSHELF);

    public static final DeferredItem<BlockItem> GILDED_WOOD_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.GILDED_WOOD);
    public static final DeferredItem<BlockItem> GILDED_OAK_WOOD_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.GILDED_OAK_WOOD);
    public static final DeferredItem<BlockItem> GILDED_SPRUCE_WOOD_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.GILDED_SPRUCE_WOOD);
    public static final DeferredItem<BlockItem> GILDED_BIRCH_WOOD_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.GILDED_BIRCH_WOOD);
    public static final DeferredItem<BlockItem> GILDED_JUNGLE_WOOD_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.GILDED_JUNGLE_WOOD);
    public static final DeferredItem<BlockItem> GILDED_ACACIA_WOOD_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.GILDED_ACACIA_WOOD);
    public static final DeferredItem<BlockItem> GILDED_DARK_OAK_WOOD_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.GILDED_DARK_OAK_WOOD);

    public static final DeferredItem<BlockItem> IMBUEMENT_ALTAR_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.IMBUEMENT_ALTAR);
    public static final DeferredItem<BlockItem> RECEPTACLE_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.RECEPTACLE);
    public static final DeferredItem<BlockItem> WALL_RECEPTACLE_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.WALL_RECEPTACLE);

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    private EBItems() {}
}
