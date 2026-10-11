package com.binaris.wizardry.client.renderer.entity.mob;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.client.model.WizardModel;
import com.binaris.wizardry.content.entity.mob.WizardEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WizardRenderer extends HumanoidMobRenderer<WizardEntity, HumanoidModel<WizardEntity>> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[6];

    static {
        for (int i = 0; i < 6; i++) {
            TEXTURES[i] = WizardryMod.id("textures/entity/wizard/wizard_" + i + ".png");
        }
    }

    public WizardRenderer(EntityRendererProvider.Context context) {
        super(context, new WizardModel<>(context.bakeLayer(WizardModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(
                this,
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                context.getModelManager()
        ));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(WizardEntity wizard) {
        int index = Math.abs(wizard.getTextureIndex()) % 6;
        return TEXTURES[index];
    }
}
