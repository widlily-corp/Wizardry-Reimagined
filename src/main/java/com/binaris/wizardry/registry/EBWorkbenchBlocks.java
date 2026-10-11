package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.block.workbench.ArcaneWorkbenchBlock;
import com.binaris.wizardry.content.block.workbench.ArcaneWorkbenchBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBWorkbenchBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(WizardryMod.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WizardryMod.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, WizardryMod.MODID);

    public static final DeferredBlock<ArcaneWorkbenchBlock> ARCANE_WORKBENCH =
            BLOCKS.register("arcane_workbench", () -> new ArcaneWorkbenchBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0F, 3.0F).noOcclusion()
            ));

    public static final DeferredItem<BlockItem> ARCANE_WORKBENCH_ITEM =
            ITEMS.registerSimpleBlockItem(ARCANE_WORKBENCH);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ArcaneWorkbenchBlockEntity>> ARCANE_WORKBENCH_BE =
            BLOCK_ENTITIES.register("arcane_workbench", () -> BlockEntityType.Builder.of(
                    ArcaneWorkbenchBlockEntity::new,
                    ARCANE_WORKBENCH.get()
            ).build(null));

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
    }

    private EBWorkbenchBlocks() {}
}
