package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.block.crystal.CrystalBlock;
import com.binaris.wizardry.content.block.crystal.CrystalFlowerBlock;
import com.binaris.wizardry.content.block.crystal.CrystalOreBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WizardryMod.MODID);

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_MAGIC =
            BLOCKS.register("crystal_block_magic", () -> new CrystalBlock(MapColor.COLOR_PINK, Elements.MAGIC.get()));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_FIRE =
            BLOCKS.register("crystal_block_fire", () -> new CrystalBlock(MapColor.TERRACOTTA_ORANGE, Elements.FIRE.get()));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_ICE =
            BLOCKS.register("crystal_block_ice", () -> new CrystalBlock(MapColor.COLOR_LIGHT_BLUE, Elements.ICE.get()));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_LIGHTNING =
            BLOCKS.register("crystal_block_lightning", () -> new CrystalBlock(MapColor.COLOR_CYAN, Elements.LIGHTNING.get()));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_NECROMANCY =
            BLOCKS.register("crystal_block_necromancy", () -> new CrystalBlock(MapColor.COLOR_PURPLE, Elements.NECROMANCY.get()));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_EARTH =
            BLOCKS.register("crystal_block_earth", () -> new CrystalBlock(MapColor.COLOR_GREEN, Elements.EARTH.get()));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_SORCERY =
            BLOCKS.register("crystal_block_sorcery", () -> new CrystalBlock(MapColor.COLOR_LIGHT_GREEN, Elements.SORCERY.get()));

    public static final DeferredBlock<CrystalBlock> CRYSTAL_BLOCK_HEALING =
            BLOCKS.register("crystal_block_healing", () -> new CrystalBlock(MapColor.COLOR_YELLOW, Elements.HEALING.get()));

    public static final DeferredBlock<CrystalOreBlock> CRYSTAL_ORE =
            BLOCKS.register("crystal_ore", () -> new CrystalOreBlock(false));

    public static final DeferredBlock<CrystalOreBlock> DEEPSLATE_CRYSTAL_ORE =
            BLOCKS.register("deepslate_crystal_ore", () -> new CrystalOreBlock(true));

    public static final DeferredBlock<CrystalFlowerBlock> CRYSTAL_FLOWER =
            BLOCKS.register("crystal_flower", CrystalFlowerBlock::new);

    public static final DeferredBlock<FlowerPotBlock> POTTED_CRYSTAL_FLOWER =
            BLOCKS.register("potted_crystal_flower", () -> new FlowerPotBlock(
                    () -> (FlowerPotBlock) Blocks.FLOWER_POT,
                    CRYSTAL_FLOWER,
                    BlockBehaviour.Properties.of().instabreak().noOcclusion().lightLevel(s -> 15).pushReaction(PushReaction.DESTROY)
            ));

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }

    private EBBlocks() {}
}
