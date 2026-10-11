package com.binaris.wizardry.client;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.client.renderer.entity.BlankRenderer;
import com.binaris.wizardry.client.renderer.entity.MagicArrowRenderer;
import com.binaris.wizardry.client.renderer.entity.MagicProjectileRenderer;
import com.binaris.wizardry.registry.EBEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = WizardryMod.MODID, value = Dist.CLIENT)
public final class ClientEntityEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EBEntities.MAGIC_MISSILE.get(),
                ctx -> new MagicArrowRenderer<>(ctx, WizardryMod.id("textures/entity/magic_missile.png")));

        event.registerEntityRenderer(EBEntities.FIREBALL.get(),
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/magic_fireball.png"), 0.7f));

        event.registerEntityRenderer(EBEntities.ICE_SHARD.get(),
                ctx -> new MagicArrowRenderer<>(ctx, WizardryMod.id("textures/entity/ice_shard.png")));

        event.registerEntityRenderer(EBEntities.DART.get(),
                ctx -> new MagicArrowRenderer<>(ctx, WizardryMod.id("textures/entity/dart.png")));

        event.registerEntityRenderer(EBEntities.SPARK.get(),
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/spark.png"), 0.5f));

        event.registerEntityRenderer(EBEntities.LIGHTNING_BOLT.get(), BlankRenderer::new);

        event.registerEntityRenderer(EBEntities.FIREBOMB.get(),
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/firebolt.png"), 0.6f));

        event.registerEntityRenderer(EBEntities.ICE_CHARGE.get(),
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/ice_charge.png"), 0.6f));

        event.registerEntityRenderer(EBEntities.FORCE_ORB.get(),
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/force_orb.png"), 0.7f));

        event.registerEntityRenderer(EBEntities.DARKNESS_ORB.get(),
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/darkness_orb.png"), 0.7f));

        event.registerEntityRenderer(EBEntities.POISON_BOMB.get(),
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/dart.png"), 0.6f));

        event.registerEntityRenderer(EBEntities.WIZARD.get(), com.binaris.wizardry.client.renderer.entity.mob.WizardRenderer::new);
        event.registerEntityRenderer(EBEntities.EVIL_WIZARD.get(), com.binaris.wizardry.client.renderer.entity.mob.EvilWizardRenderer::new);
        event.registerEntityRenderer(EBEntities.REMNANT.get(), com.binaris.wizardry.client.renderer.entity.mob.EvilWizardRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(com.binaris.wizardry.client.model.WizardModel.LAYER_LOCATION,
                com.binaris.wizardry.client.model.WizardModel::createBodyLayer);
        event.registerLayerDefinition(com.binaris.wizardry.client.model.WizardArmorModel.LAYER_LOCATION,
                com.binaris.wizardry.client.model.WizardArmorModel::createBodyLayer);
    }

    private ClientEntityEvents() {}
}
