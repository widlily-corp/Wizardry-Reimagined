package com.binaris.wizardry.client.renderer.entity.construct;

import com.binaris.wizardry.content.entity.construct.CelestialMonolithEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CelestialMonolithRenderer extends EntityRenderer<CelestialMonolithEntity> {
    private final BlockRenderDispatcher dispatcher;

    public CelestialMonolithRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
        this.shadowRadius = 1.5F;
    }

    @Override
    public void render(@NotNull CelestialMonolithEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // Slow majestic rotation
        float rotation = (entity.tickCount + partialTicks) * 2.5F;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        BlockState coreState = Blocks.SEA_LANTERN.defaultBlockState();
        BlockState crystalState = Blocks.AMETHYST_BLOCK.defaultBlockState();
        BlockState crownState = Blocks.GILDED_BLACKSTONE.defaultBlockState();

        var renderer = this.dispatcher.getModelRenderer();
        var coreModel = this.dispatcher.getBlockModel(coreState);
        var crystalModel = this.dispatcher.getBlockModel(crystalState);
        var crownModel = this.dispatcher.getBlockModel(crownState);

        // Render towering 10-block obelisk
        int height = 10;
        for (int y = 0; y < height; y++) {
            poseStack.pushPose();
            poseStack.translate(-0.5D, y, -0.5D);

            // Shape profile: wider in center, tapering at top and bottom
            float scaleXZ = (y == 0 || y >= height - 2) ? 0.75F : 1.35F;
            poseStack.scale(scaleXZ, 1.0F, scaleXZ);

            BlockState toRender = (y >= height - 2) ? crownState : ((y % 2 == 0) ? crystalState : coreState);
            var modelToRender = (y >= height - 2) ? crownModel : ((y % 2 == 0) ? crystalModel : coreModel);

            renderer.tesselateBlock(
                    entity.level(),
                    modelToRender,
                    toRender,
                    entity.blockPosition(),
                    poseStack,
                    bufferSource.getBuffer(RenderType.solid()),
                    false,
                    entity.level().random,
                    toRender.getSeed(entity.blockPosition()),
                    OverlayTexture.NO_OVERLAY
            );

            poseStack.popPose();
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, 0xF000F0);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CelestialMonolithEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
