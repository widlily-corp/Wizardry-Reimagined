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
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/force_orb.png"), 1.1f));

        event.registerEntityRenderer(EBEntities.DARKNESS_ORB.get(),
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/darkness_orb.png"), 1.1f));

        event.registerEntityRenderer(EBEntities.POISON_BOMB.get(),
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/dart.png"), 0.6f));

        event.registerEntityRenderer(EBEntities.FIRE_BOLT.get(),
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/firebolt.png"), 0.5f));

        event.registerEntityRenderer(EBEntities.FLAME_CATCHER_ARROW.get(),
                ctx -> new MagicArrowRenderer<>(ctx, WizardryMod.id("textures/entity/flamecatcher_arrow.png")));

        event.registerEntityRenderer(EBEntities.THUNDERBOLT.get(), BlankRenderer::new);

        event.registerEntityRenderer(EBEntities.SPARK_BOMB.get(),
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/spark.png"), 0.5f));

        event.registerEntityRenderer(EBEntities.SMOKE_BOMB.get(),
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/dart.png"), 0.5f));

        event.registerEntityRenderer(EBEntities.LIGHTNING_ARROW.get(),
                ctx -> new MagicArrowRenderer<>(ctx, WizardryMod.id("textures/entity/lightning_arrow.png")));

        event.registerEntityRenderer(EBEntities.ICE_LANCE.get(),
                ctx -> new MagicArrowRenderer<>(ctx, WizardryMod.id("textures/entity/ice_lance.png")));

        event.registerEntityRenderer(EBEntities.ICE_BALL.get(),
                ctx -> new MagicProjectileRenderer<>(ctx, WizardryMod.id("textures/entity/iceball.png"), 0.6f));

        event.registerEntityRenderer(EBEntities.FORCE_ARROW.get(),
                ctx -> new MagicArrowRenderer<>(ctx, WizardryMod.id("textures/entity/force_arrow.png")));

        event.registerEntityRenderer(EBEntities.CONJURED_ARROW.get(),
                ctx -> new MagicArrowRenderer<>(ctx, WizardryMod.id("textures/entity/conjured_arrow.png")));
        event.registerEntityRenderer(EBEntities.SWARM_ARROW.get(),
                ctx -> new MagicArrowRenderer<>(ctx, WizardryMod.id("textures/entity/conjured_arrow.png")));

        event.registerEntityRenderer(EBEntities.RING_OF_FIRE.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.FIRE_SIGIL.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.COMBUSTION_RUNE.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.METEOR.get(), net.minecraft.client.renderer.entity.FallingBlockRenderer::new);
        event.registerEntityRenderer(EBEntities.BLIZZARD.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.HAILSTORM.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.FROST_SIGIL.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.ICE_SPIKE.get(), com.binaris.wizardry.client.renderer.entity.construct.IceSpikeRenderer::new);
        event.registerEntityRenderer(EBEntities.ICE_BARRIER.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.LIGHTNING_SIGIL.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.TORNADO.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.HEAL_AURA.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.BUBBLE.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.BOULDER.get(), com.binaris.wizardry.client.renderer.entity.construct.BoulderRenderer::new);
        event.registerEntityRenderer(EBEntities.DECAY.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.ARROW_RAIN.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.ARROW_ORB.get(), BlankRenderer::new);
        event.registerEntityRenderer(EBEntities.ZOMBIE_SPAWNER.get(), BlankRenderer::new);

        event.registerEntityRenderer(EBEntities.WIZARD.get(), com.binaris.wizardry.client.renderer.entity.mob.WizardRenderer::new);
        event.registerEntityRenderer(EBEntities.EVIL_WIZARD.get(), com.binaris.wizardry.client.renderer.entity.mob.EvilWizardRenderer::new);
        event.registerEntityRenderer(EBEntities.REMNANT.get(), com.binaris.wizardry.client.renderer.entity.mob.EvilWizardRenderer::new);
        event.registerEntityRenderer(EBEntities.ICE_GIANT.get(), com.binaris.wizardry.client.renderer.entity.mob.IceGiantRenderer::new);
        event.registerEntityRenderer(EBEntities.LIGHTNING_WRAITH.get(), com.binaris.wizardry.client.renderer.entity.mob.LightningWraithRenderer::new);
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
