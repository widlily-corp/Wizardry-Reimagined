package com.binaris.wizardry;

import com.binaris.wizardry.registry.EBBlocks;
import com.binaris.wizardry.registry.EBCreativeTabs;
import com.binaris.wizardry.registry.EBDataComponents;
import com.binaris.wizardry.registry.EBItems;
import com.binaris.wizardry.registry.EBRegistries;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import com.binaris.wizardry.registry.Spells;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(WizardryMod.MODID)
public class WizardryMod {
    public static final String MODID = "ebwizardry";

    public WizardryMod(IEventBus modBus) {
        // 1. Core custom registries (Elements, Tiers, Spells)
        EBRegistries.register(modBus);
        Elements.init();
        SpellTiers.init();
        Spells.init();

        // 2. Data Components
        EBDataComponents.register(modBus);

        // 3. Blocks & Items
        EBBlocks.register(modBus);
        EBItems.register(modBus);

        // 4. Sounds
        EBSounds.register(modBus);

        // 5. Creative Mode Tabs
        EBCreativeTabs.register(modBus);

        // Setup listener
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(
                    BuiltInRegistries.BLOCK.getKey(EBBlocks.CRYSTAL_FLOWER.get()),
                    EBBlocks.POTTED_CRYSTAL_FLOWER
            );
        });
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
