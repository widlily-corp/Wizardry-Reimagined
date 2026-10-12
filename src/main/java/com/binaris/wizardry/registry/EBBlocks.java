package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.block.crystal.CrystalBlock;
import com.binaris.wizardry.content.block.crystal.CrystalFlowerBlock;
import com.binaris.wizardry.content.block.crystal.CrystalOreBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.WebBlock;
import com.binaris.wizardry.content.block.crust.ObsidianCrustBlock;
import com.binaris.wizardry.content.block.ice.PermafrostBlock;
import com.binaris.wizardry.content.block.lectern.WizardryLecternBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WizardryMod.MODID);

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_MAGIC =
            BLOCKS.register("crystal_block_magic", () -> new CrystalBlock(MapColor.COLOR_PINK, Elements.MAGIC));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_FIRE =
            BLOCKS.register("crystal_block_fire", () -> new CrystalBlock(MapColor.TERRACOTTA_ORANGE, Elements.FIRE));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_ICE =
            BLOCKS.register("crystal_block_ice", () -> new CrystalBlock(MapColor.COLOR_LIGHT_BLUE, Elements.ICE));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_LIGHTNING =
            BLOCKS.register("crystal_block_lightning", () -> new CrystalBlock(MapColor.COLOR_CYAN, Elements.LIGHTNING));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_NECROMANCY =
            BLOCKS.register("crystal_block_necromancy", () -> new CrystalBlock(MapColor.COLOR_PURPLE, Elements.NECROMANCY));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_EARTH =
            BLOCKS.register("crystal_block_earth", () -> new CrystalBlock(MapColor.COLOR_GREEN, Elements.EARTH));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_SORCERY =
            BLOCKS.register("crystal_block_sorcery", () -> new CrystalBlock(MapColor.COLOR_LIGHT_GREEN, Elements.SORCERY));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_HEALING =
            BLOCKS.register("crystal_block_healing", () -> new CrystalBlock(MapColor.COLOR_YELLOW, Elements.HEALING));

    public static final DeferredBlock<CrystalOreBlock> CRYSTAL_ORE =
            BLOCKS.register("crystal_ore", () -> new CrystalOreBlock(false));

    public static final DeferredBlock<CrystalOreBlock> DEEPSLATE_CRYSTAL_ORE =
            BLOCKS.register("deepslate_crystal_ore", () -> new CrystalOreBlock(true));

    public static final DeferredBlock<CrystalFlowerBlock> CRYSTAL_FLOWER =
            BLOCKS.register("crystal_flower", () -> new CrystalFlowerBlock());

    public static final DeferredBlock<FlowerPotBlock> POTTED_CRYSTAL_FLOWER =
            BLOCKS.register("potted_crystal_flower", () -> new FlowerPotBlock(
                    () -> (FlowerPotBlock) Blocks.FLOWER_POT,
                    CRYSTAL_FLOWER,
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().lightLevel(s -> 15).pushReaction(PushReaction.DESTROY)
            ));

    // ==========================================
    // Runestones
    // ==========================================
    public static final DeferredBlock<Block> FIRE_RUNESTONE =
            BLOCKS.register("fire_runestone", () -> new com.binaris.wizardry.content.block.runestone.RunestoneBlock(Elements.FIRE));
    public static final DeferredBlock<Block> EARTH_RUNESTONE =
            BLOCKS.register("earth_runestone", () -> new com.binaris.wizardry.content.block.runestone.RunestoneBlock(Elements.EARTH));
    public static final DeferredBlock<Block> ICE_RUNESTONE =
            BLOCKS.register("ice_runestone", () -> new com.binaris.wizardry.content.block.runestone.RunestoneBlock(Elements.ICE));
    public static final DeferredBlock<Block> LIGHTNING_RUNESTONE =
            BLOCKS.register("lightning_runestone", () -> new com.binaris.wizardry.content.block.runestone.RunestoneBlock(Elements.LIGHTNING));
    public static final DeferredBlock<Block> NECROMANCY_RUNESTONE =
            BLOCKS.register("necromancy_runestone", () -> new com.binaris.wizardry.content.block.runestone.RunestoneBlock(Elements.NECROMANCY));
    public static final DeferredBlock<Block> SORCERY_RUNESTONE =
            BLOCKS.register("sorcery_runestone", () -> new com.binaris.wizardry.content.block.runestone.RunestoneBlock(Elements.SORCERY));
    public static final DeferredBlock<Block> HEALING_RUNESTONE =
            BLOCKS.register("healing_runestone", () -> new com.binaris.wizardry.content.block.runestone.RunestoneBlock(Elements.HEALING));

    // ==========================================
    // Runestone Pedestals
    // ==========================================
    public static final DeferredBlock<Block> FIRE_RUNESTONE_PEDESTAL =
            BLOCKS.register("fire_runestone_pedestal", () -> new com.binaris.wizardry.content.block.runestone.RunestonePedestalBlock(Elements.FIRE));
    public static final DeferredBlock<Block> EARTH_RUNESTONE_PEDESTAL =
            BLOCKS.register("earth_runestone_pedestal", () -> new com.binaris.wizardry.content.block.runestone.RunestonePedestalBlock(Elements.EARTH));
    public static final DeferredBlock<Block> ICE_RUNESTONE_PEDESTAL =
            BLOCKS.register("ice_runestone_pedestal", () -> new com.binaris.wizardry.content.block.runestone.RunestonePedestalBlock(Elements.ICE));
    public static final DeferredBlock<Block> LIGHTNING_RUNESTONE_PEDESTAL =
            BLOCKS.register("lightning_runestone_pedestal", () -> new com.binaris.wizardry.content.block.runestone.RunestonePedestalBlock(Elements.LIGHTNING));
    public static final DeferredBlock<Block> NECROMANCY_RUNESTONE_PEDESTAL =
            BLOCKS.register("necromancy_runestone_pedestal", () -> new com.binaris.wizardry.content.block.runestone.RunestonePedestalBlock(Elements.NECROMANCY));
    public static final DeferredBlock<Block> SORCERY_RUNESTONE_PEDESTAL =
            BLOCKS.register("sorcery_runestone_pedestal", () -> new com.binaris.wizardry.content.block.runestone.RunestonePedestalBlock(Elements.SORCERY));
    public static final DeferredBlock<Block> HEALING_RUNESTONE_PEDESTAL =
            BLOCKS.register("healing_runestone_pedestal", () -> new com.binaris.wizardry.content.block.runestone.RunestonePedestalBlock(Elements.HEALING));

    // ==========================================
    // Library & Decorative Worldgen Blocks
    // ==========================================
    public static final DeferredBlock<Block> OAK_BOOKSHELF =
            BLOCKS.register("oak_bookshelf", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> SPRUCE_BOOKSHELF =
            BLOCKS.register("spruce_bookshelf", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> BIRCH_BOOKSHELF =
            BLOCKS.register("birch_bookshelf", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> JUNGLE_BOOKSHELF =
            BLOCKS.register("jungle_bookshelf", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> ACACIA_BOOKSHELF =
            BLOCKS.register("acacia_bookshelf", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> DARK_OAK_BOOKSHELF =
            BLOCKS.register("dark_oak_bookshelf", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> BOOKSHELF =
            BLOCKS.register("bookshelf", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F).sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> GILDED_WOOD =
            BLOCKS.register("gilded_wood", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> GILDED_OAK_WOOD =
            BLOCKS.register("gilded_oak_wood", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> GILDED_SPRUCE_WOOD =
            BLOCKS.register("gilded_spruce_wood", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> GILDED_BIRCH_WOOD =
            BLOCKS.register("gilded_birch_wood", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> GILDED_JUNGLE_WOOD =
            BLOCKS.register("gilded_jungle_wood", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> GILDED_ACACIA_WOOD =
            BLOCKS.register("gilded_acacia_wood", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> GILDED_DARK_OAK_WOOD =
            BLOCKS.register("gilded_dark_oak_wood", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> IMBUEMENT_ALTAR =
            BLOCKS.register("imbuement_altar", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F).sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<Block> RECEPTACLE =
            BLOCKS.register("receptacle", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F).sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<Block> WALL_RECEPTACLE =
            BLOCKS.register("wall_receptacle", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F).sound(SoundType.STONE).noOcclusion()));

    // ==========================================
    // Lecterns
    // ==========================================
    public static final DeferredBlock<WizardryLecternBlock> ACACIA_LECTERN =
            BLOCKS.register("acacia_lectern", () -> new WizardryLecternBlock());
    public static final DeferredBlock<WizardryLecternBlock> BIRCH_LECTERN =
            BLOCKS.register("birch_lectern", () -> new WizardryLecternBlock());
    public static final DeferredBlock<WizardryLecternBlock> DARK_OAK_LECTERN =
            BLOCKS.register("dark_oak_lectern", () -> new WizardryLecternBlock());
    public static final DeferredBlock<WizardryLecternBlock> JUNGLE_LECTERN =
            BLOCKS.register("jungle_lectern", () -> new WizardryLecternBlock());
    public static final DeferredBlock<WizardryLecternBlock> OAK_LECTERN =
            BLOCKS.register("oak_lectern", () -> new WizardryLecternBlock());
    public static final DeferredBlock<WizardryLecternBlock> SPRUCE_LECTERN =
            BLOCKS.register("spruce_lectern", () -> new WizardryLecternBlock());

    // ==========================================
    // Special Blocks
    // ==========================================
    public static final DeferredBlock<ObsidianCrustBlock> OBSIDIAN_CRUST =
            BLOCKS.register("obsidian_crust", () -> new ObsidianCrustBlock());

    public static final DeferredBlock<PermafrostBlock> PERMAFROST =
            BLOCKS.register("permafrost", () -> new PermafrostBlock());

    public static final DeferredBlock<Block> SPECTRAL_BLOCK =
            BLOCKS.register("spectral_block", () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(1.5F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()));

    public static final DeferredBlock<WebBlock> VANISHING_COBWEB =
            BLOCKS.register("vanishing_cobweb", () -> new WebBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOL)
                    .forceSolidOn()
                    .noCollission()
                    .strength(4.0F)
                    .pushReaction(PushReaction.DESTROY)));

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }

    private EBBlocks() {}
}
