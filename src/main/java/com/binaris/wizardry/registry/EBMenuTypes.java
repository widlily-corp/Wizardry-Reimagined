package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.menu.ArcaneWorkbenchMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, WizardryMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ArcaneWorkbenchMenu>> ARCANE_WORKBENCH =
            MENUS.register("arcane_workbench", () -> new MenuType<>(ArcaneWorkbenchMenu::new, FeatureFlagSet.of()));

    public static void register(IEventBus modBus) {
        MENUS.register(modBus);
    }

    private EBMenuTypes() {}
}
