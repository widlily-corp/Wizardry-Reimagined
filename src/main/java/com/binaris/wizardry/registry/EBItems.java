package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.material.CrystalItem;
import com.binaris.wizardry.content.item.flask.ManaFlaskItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.binaris.wizardry.content.item.*;

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

    // Lectern Items
    public static final DeferredItem<BlockItem> ACACIA_LECTERN_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.ACACIA_LECTERN);
    public static final DeferredItem<BlockItem> BIRCH_LECTERN_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.BIRCH_LECTERN);
    public static final DeferredItem<BlockItem> DARK_OAK_LECTERN_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.DARK_OAK_LECTERN);
    public static final DeferredItem<BlockItem> JUNGLE_LECTERN_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.JUNGLE_LECTERN);
    public static final DeferredItem<BlockItem> OAK_LECTERN_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.OAK_LECTERN);
    public static final DeferredItem<BlockItem> SPRUCE_LECTERN_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.SPRUCE_LECTERN);

    // Special Block Items
    public static final DeferredItem<BlockItem> OBSIDIAN_CRUST_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.OBSIDIAN_CRUST);
    public static final DeferredItem<BlockItem> PERMAFROST_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.PERMAFROST);
    public static final DeferredItem<BlockItem> SPECTRAL_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.SPECTRAL_BLOCK);
    public static final DeferredItem<BlockItem> VANISHING_COBWEB_ITEM = ITEMS.registerSimpleBlockItem(EBBlocks.VANISHING_COBWEB);

    // Mana Flasks
    public static final DeferredItem<ManaFlaskItem> MANA_FLASK_SMALL =
            ITEMS.register("mana_flask_small", () -> new ManaFlaskItem(140, new Item.Properties().stacksTo(16)));
    public static final DeferredItem<ManaFlaskItem> MANA_FLASK_MEDIUM =
            ITEMS.register("mana_flask_medium", () -> new ManaFlaskItem(350, new Item.Properties().stacksTo(16)));
    public static final DeferredItem<ManaFlaskItem> MANA_FLASK_LARGE =
            ITEMS.register("mana_flask_large", () -> new ManaFlaskItem(700, new Item.Properties().stacksTo(16)));

    // Crafting Materials
    public static final DeferredItem<Item> MAGIC_SILK =
            ITEMS.register("magic_silk", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RUINED_SPELL_BOOK =
            ITEMS.register("ruined_spell_book", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    // Spectral Dusts
    public static final DeferredItem<Item> SPECTRAL_DUST =
            ITEMS.register("spectral_dust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPECTRAL_DUST_FIRE =
            ITEMS.register("spectral_dust_fire", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPECTRAL_DUST_ICE =
            ITEMS.register("spectral_dust_ice", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPECTRAL_DUST_LIGHTNING =
            ITEMS.register("spectral_dust_lightning", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPECTRAL_DUST_NECROMANCY =
            ITEMS.register("spectral_dust_necromancy", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPECTRAL_DUST_EARTH =
            ITEMS.register("spectral_dust_earth", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPECTRAL_DUST_SORCERY =
            ITEMS.register("spectral_dust_sorcery", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPECTRAL_DUST_HEALING =
            ITEMS.register("spectral_dust_healing", () -> new Item(new Item.Properties()));

    // Elemental Dusts
    public static final DeferredItem<Item> FIRE_ELEMENTAL_DUST =
            ITEMS.register("fire_elemental_dust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ICE_ELEMENTAL_DUST =
            ITEMS.register("ice_elemental_dust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LIGHTNING_ELEMENTAL_DUST =
            ITEMS.register("lightning_elemental_dust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NECROMANCY_ELEMENTAL_DUST =
            ITEMS.register("necromancy_elemental_dust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EARTH_ELEMENTAL_DUST =
            ITEMS.register("earth_elemental_dust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SORCERY_ELEMENTAL_DUST =
            ITEMS.register("sorcery_elemental_dust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HEALING_ELEMENTAL_DUST =
            ITEMS.register("healing_elemental_dust", () -> new Item(new Item.Properties()));

    // Banner Patterns
    public static final DeferredItem<BannerPatternItem> FIRE_BANNER_PATTERN =
            registerBannerPattern("fire");
    public static final DeferredItem<BannerPatternItem> ICE_BANNER_PATTERN =
            registerBannerPattern("ice");
    public static final DeferredItem<BannerPatternItem> LIGHTNING_BANNER_PATTERN =
            registerBannerPattern("lightning");
    public static final DeferredItem<BannerPatternItem> NECROMANCY_BANNER_PATTERN =
            registerBannerPattern("necromancy");
    public static final DeferredItem<BannerPatternItem> EARTH_BANNER_PATTERN =
            registerBannerPattern("earth");
    public static final DeferredItem<BannerPatternItem> SORCERY_BANNER_PATTERN =
            registerBannerPattern("sorcery");
    public static final DeferredItem<BannerPatternItem> HEALING_BANNER_PATTERN =
            registerBannerPattern("healing");

    // Spectral and Magic Weapons
    public static final DeferredItem<Item> SPECTRAL_SWORD =
            ITEMS.register("spectral_sword", SpectralSwordItem::new);
    public static final DeferredItem<Item> SPECTRAL_BOW =
            ITEMS.register("spectral_bow", SpectralBowItem::new);
    public static final DeferredItem<Item> SPECTRAL_PICKAXE =
            ITEMS.register("spectral_pickaxe", SpectralPickaxeItem::new);
    public static final DeferredItem<Item> SPECTRAL_HELMET =
            ITEMS.register("spectral_helmet", () -> new net.minecraft.world.item.ArmorItem(
                    net.minecraft.world.item.ArmorMaterials.IRON, net.minecraft.world.item.ArmorItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<Item> SPECTRAL_CHESTPLATE =
            ITEMS.register("spectral_chestplate", () -> new net.minecraft.world.item.ArmorItem(
                    net.minecraft.world.item.ArmorMaterials.IRON, net.minecraft.world.item.ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<Item> SPECTRAL_LEGGINGS =
            ITEMS.register("spectral_leggings", () -> new net.minecraft.world.item.ArmorItem(
                    net.minecraft.world.item.ArmorMaterials.IRON, net.minecraft.world.item.ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final DeferredItem<Item> SPECTRAL_BOOTS =
            ITEMS.register("spectral_boots", () -> new net.minecraft.world.item.ArmorItem(
                    net.minecraft.world.item.ArmorMaterials.IRON, net.minecraft.world.item.ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<Item> FLAMECATCHER =
            ITEMS.register("flamecatcher", FlameCatcherItem::new);
    public static final DeferredItem<Item> FLAMING_AXE =
            ITEMS.register("flaming_axe", FlamingAxeItem::new);
    public static final DeferredItem<Item> FROST_AXE =
            ITEMS.register("frost_axe", FrostAxeItem::new);
    public static final DeferredItem<Item> WIZARD_HANDBOOK =
            ITEMS.register("wizard_handbook", WizardHandbookItem::new);

    // Bombs
    public static final DeferredItem<Item> FIREBOMB =
            ITEMS.register("firebomb", () -> new BombItem(EBEntities.FIREBOMB, EBSounds.ENTITY_FIREBOMB_THROW));
    public static final DeferredItem<Item> POISON_BOMB =
            ITEMS.register("poison_bomb", () -> new BombItem(EBEntities.POISON_BOMB, EBSounds.ENTITY_POISON_BOMB_THROW));
    public static final DeferredItem<Item> SMOKE_BOMB =
            ITEMS.register("smoke_bomb", () -> new BombItem(EBEntities.SMOKE_BOMB, EBSounds.ENTITY_SMOKE_BOMB_THROW));
    public static final DeferredItem<Item> SPARK_BOMB =
            ITEMS.register("spark_bomb", () -> new BombItem(EBEntities.SPARK_BOMB, EBSounds.ENTITY_SPARK_BOMB_THROW));

    private static DeferredItem<BannerPatternItem> registerBannerPattern(String name) {
        return ITEMS.register(name + "_banner_pattern", () -> new BannerPatternItem(
                TagKey.create(Registries.BANNER_PATTERN, ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "pattern_item/" + name)),
                new Item.Properties().stacksTo(1)
        ));
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    private EBItems() {}
}
