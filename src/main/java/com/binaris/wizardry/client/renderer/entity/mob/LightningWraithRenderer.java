package com.binaris.wizardry.client.renderer.entity.mob;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.entity.mob.LightningWraith;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LightningWraithRenderer extends MobRenderer<LightningWraith, BlazeModel<LightningWraith>> {
    private static final ResourceLocation TEXTURE = WizardryMod.id("textures/entity/lightning_wraith.png");

    public LightningWraithRenderer(EntityRendererProvider.Context context) {
        super(context, new BlazeModel<>(context.bakeLayer(ModelLayers.BLAZE)), 0.5F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LightningWraith entity) {
        return TEXTURE;
    }
}
