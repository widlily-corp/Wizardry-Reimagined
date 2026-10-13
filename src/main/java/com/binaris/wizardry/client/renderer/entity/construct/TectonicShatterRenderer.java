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
import net.minecraft.core.BlockPos;
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

        // Colossal monolith formation spanning up to 14 blocks with massive heights
        double[][] pillarOffsets = {
                {0.0, 0.0, 8.5, 0.0},
                {4.5, 3.2, 7.0, 18.0},
                {-4.2, 3.5, 7.5, -22.0},
                {3.8, -4.5, 6.5, 30.0},
                {-4.0, -4.2, 7.8, -18.0},
                {8.5, 0.5, 5.5, 12.0},
                {-8.2, -1.0, 6.0, -15.0},
                {0.5, 8.8, 5.8, -10.0},
                {-1.2, -8.5, 6.2, 14.0}
        };

        float age = entity.tickCount + partialTicks;
        float shakeX = 0.0F;
        float shakeZ = 0.0F;
        float sinkY = 0.0F;
        if (entity.tickCount > 210) {
            float progress = Math.min((entity.tickCount - 210 + partialTicks) / 30.0F, 1.0F);
            shakeX = (float) Math.sin(age * 3.5F) * 0.45F * progress;
            shakeZ = (float) Math.cos(age * 3.5F) * 0.45F * progress;
            sinkY = -progress * 2.8F; // Monoliths sink into the earth before catastrophic collapse
        }

        for (int p = 0; p < pillarOffsets.length; p++) {
            double ox = pillarOffsets[p][0];
            double oz = pillarOffsets[p][1];
            double height = pillarOffsets[p][2];
            float tilt = (float) pillarOffsets[p][3];

            poseStack.pushPose();
            poseStack.translate(ox + shakeX, sinkY, oz + shakeZ);
            poseStack.mulPose(Axis.YP.rotationDegrees(tilt * 2.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(tilt * 0.4F));

            for (int y = 0; y < (int) height; y++) {
                poseStack.pushPose();
                float scaleXZ = 1.4F - (y * 0.1F);
                poseStack.translate(-0.5D * scaleXZ, y * 0.95D, -0.5D * scaleXZ);
                poseStack.scale(scaleXZ, 1.0F, scaleXZ);

                BlockState toRender = (y % 3 == 0) ? deepslateState : ((y % 3 == 1) ? basaltState : tuffState);
                var modelToRender = (y % 3 == 0) ? deepslateModel : ((y % 3 == 1) ? basaltModel : tuffModel);

                BlockPos pillarPos = entity.blockPosition().offset((int) Math.round(ox), (int) Math.round(sinkY + y * 0.95D), (int) Math.round(oz));

                renderer.tesselateBlock(
                        entity.level(), modelToRender, toRender, pillarPos,
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
