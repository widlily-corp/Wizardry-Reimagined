package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WizardryMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB =
            CREATIVE_MODE_TABS.register("ebwizardry", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ebwizardry"))
                    .icon(() -> new ItemStack(EBItems.MAGIC_CRYSTAL.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(EBItems.MAGIC_CRYSTAL_SHARD.get());
                        output.accept(EBItems.MAGIC_CRYSTAL.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_GRAND.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_EARTH.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_FIRE.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_HEALING.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_ICE.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_LIGHTNING.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_NECROMANCY.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_SORCERY.get());

                        output.accept(EBBlocks.CRYSTAL_BLOCK_MAGIC.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_EARTH.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_FIRE.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_HEALING.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_ICE.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_LIGHTNING.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_NECROMANCY.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_SORCERY.get());

                        output.accept(EBBlocks.CRYSTAL_ORE.get());
                        output.accept(EBBlocks.DEEPSLATE_CRYSTAL_ORE.get());
                        output.accept(EBBlocks.CRYSTAL_FLOWER.get());
                    })
                    .build());

    public static void register(IEventBus modBus) {
        CREATIVE_MODE_TABS.register(modBus);
    }

    private EBCreativeTabs() {}
}
