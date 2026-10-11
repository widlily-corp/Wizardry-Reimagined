package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.component.SpellData;
import com.binaris.wizardry.content.component.UpgradeData;
import com.binaris.wizardry.content.component.WandData;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(WizardryMod.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WandData>> WAND_DATA =
            DATA_COMPONENTS.registerComponentType("wand_data", builder -> builder
                    .persistent(WandData.CODEC)
                    .networkSynchronized(WandData.STREAM_CODEC)
                    .cacheEncoding());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SpellData>> SPELL_DATA =
            DATA_COMPONENTS.registerComponentType("spell_data", builder -> builder
                    .persistent(SpellData.CODEC)
                    .networkSynchronized(SpellData.STREAM_CODEC)
                    .cacheEncoding());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UpgradeData>> UPGRADE_DATA =
            DATA_COMPONENTS.registerComponentType("upgrade_data", builder -> builder
                    .persistent(UpgradeData.CODEC)
                    .networkSynchronized(UpgradeData.STREAM_CODEC)
                    .cacheEncoding());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MANA =
            DATA_COMPONENTS.registerComponentType("mana", builder -> builder
                    .persistent(com.mojang.serialization.Codec.INT)
                    .networkSynchronized(net.minecraft.network.codec.ByteBufCodecs.VAR_INT));

    public static void register(IEventBus modBus) {
        DATA_COMPONENTS.register(modBus);
    }

    private EBDataComponents() {}
}
