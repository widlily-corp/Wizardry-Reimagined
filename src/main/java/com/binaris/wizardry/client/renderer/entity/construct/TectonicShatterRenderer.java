package com.binaris.wizardry.client.renderer.entity.construct;

import com.binaris.wizardry.content.entity.construct.TectonicShatterEntity;
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

public class TectonicShatterRenderer extends EntityRenderer<TectonicShatterEntity> {
    private final BlockRenderDispatcher dispatcher;

    public TectonicShatterRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
        this.shadowRadius = 2.5F;
    }

    @Override
    public void render(@NotNull TectonicShatterEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        BlockState deepslateState = Blocks.POLISHED_DEEPSLATE.defaultBlockState();
        BlockState basaltState = Blocks.BASALT.defaultBlockState();
        BlockState tuffState = Blocks.TUFF.defaultBlockState();

        var renderer = this.dispatcher.getModelRenderer();
        var deepslateModel = this.dispatcher.getBlockModel(deepslateState);
        var basaltModel = this.dispatcher.getBlockModel(basaltState);
        var tuffModel = this.dispatcher.getBlockModel(tuffState);

        // Render 5 jagged seismic stone pillars thrusting from earth
        double[][] pillarOffsets = {
                {0.0, 0.0, 4.5, 0.0},
                {2.2, 1.8, 3.5, 15.0},
                {-2.0, 1.5, 3.8, -20.0},
                {1.8, -2.2, 3.2, 35.0},
                {-1.9, -1.9, 4.0, -15.0}
        };

        for (int p = 0; p < pillarOffsets.length; p++) {
            double ox = pillarOffsets[p][0];
            double oz = pillarOffsets[p][1];
            double height = pillarOffsets[p][2];
            float tilt = (float) pillarOffsets[p][3];

            poseStack.pushPose();
            poseStack.translate(ox, 0.0D, oz);
            poseStack.mulPose(Axis.YP.rotationDegrees(tilt * 2.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(tilt * 0.4F));

            for (int y = 0; y < (int) height; y++) {
                poseStack.pushPose();
                float scaleXZ = 1.3F - (y * 0.15F);
                poseStack.translate(-0.5D * scaleXZ, y * 0.95D, -0.5D * scaleXZ);
                poseStack.scale(scaleXZ, 1.0F, scaleXZ);

                BlockState toRender = (y % 3 == 0) ? deepslateState : ((y % 3 == 1) ? basaltState : tuffState);
                var modelToRender = (y % 3 == 0) ? deepslateModel : ((y % 3 == 1) ? basaltModel : tuffModel);

                renderer.tesselateBlock(
                        entity.level(), modelToRender, toRender, entity.blockPosition(),
                        poseStack, bufferSource.getBuffer(RenderType.cutout()), false,
                        entity.getRandom(), entity.getId(), OverlayTexture.NO_OVERLAY
                );
                poseStack.popPose();
            }
            poseStack.popPose();
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TectonicShatterEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
