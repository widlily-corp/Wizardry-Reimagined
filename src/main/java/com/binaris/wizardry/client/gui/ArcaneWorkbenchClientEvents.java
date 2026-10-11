package com.binaris.wizardry.client.gui;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.client.renderer.blockentity.ArcaneWorkbenchRender;
import com.binaris.wizardry.registry.EBMenuTypes;
import com.binaris.wizardry.registry.EBWorkbenchBlocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = WizardryMod.MODID, value = Dist.CLIENT)
public final class ArcaneWorkbenchClientEvents {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(EBMenuTypes.ARCANE_WORKBENCH.get(), ArcaneWorkbenchScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(EBWorkbenchBlocks.ARCANE_WORKBENCH_BE.get(), ArcaneWorkbenchRender::new);
    }

    private ArcaneWorkbenchClientEvents() {}
}
