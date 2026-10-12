package com.binaris.wizardry.client.renderer.entity.mob;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.entity.mob.IceGiant;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class IceGiantRenderer extends MobRenderer<IceGiant, IronGolemModel<IceGiant>> {
    private static final ResourceLocation TEXTURE = WizardryMod.id("textures/entity/ice_giant.png");

    public IceGiantRenderer(EntityRendererProvider.Context context) {
        super(context, new IronGolemModel<>(context.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull IceGiant entity) {
        return TEXTURE;
    }
}
