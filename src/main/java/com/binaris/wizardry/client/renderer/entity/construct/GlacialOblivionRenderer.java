package com.binaris.wizardry.client.renderer.entity.construct;

import com.binaris.wizardry.content.entity.construct.GlacialOblivionEntity;
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

public class GlacialOblivionRenderer extends EntityRenderer<GlacialOblivionEntity> {
    private final BlockRenderDispatcher dispatcher;

    public GlacialOblivionRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
        this.shadowRadius = 2.0F;
    }

    @Override
    public void render(@NotNull GlacialOblivionEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.isShattered()) {
            return;
        }

        poseStack.pushPose();

        BlockState blueIceState = Blocks.BLUE_ICE.defaultBlockState();
        BlockState packedIceState = Blocks.PACKED_ICE.defaultBlockState();

        var renderer = this.dispatcher.getModelRenderer();
        var blueIceModel = this.dispatcher.getBlockModel(blueIceState);
        var packedIceModel = this.dispatcher.getBlockModel(packedIceState);

        // Render towering central crystal spike
        int height = 6;
        for (int y = 0; y < height; y++) {
            poseStack.pushPose();
            float scaleXZ = 1.6F - (y * 0.22F);
            poseStack.translate(-0.5D * scaleXZ, y * 0.9D, -0.5D * scaleXZ);
            poseStack.scale(scaleXZ, 1.0F, scaleXZ);
            poseStack.mulPose(Axis.YP.rotationDegrees(y * 18.0F));

            BlockState toRender = (y % 2 == 0) ? blueIceState : packedIceState;
            var modelToRender = (y % 2 == 0) ? blueIceModel : packedIceModel;

            renderer.tesselateBlock(
                    entity.level(),
                    modelToRender,
                    toRender,
                    entity.blockPosition(),
                    poseStack,
                    bufferSource.getBuffer(RenderType.translucent()),
                    false,
                    entity.getRandom(),
                    entity.getId(),
                    OverlayTexture.NO_OVERLAY
            );
            poseStack.popPose();
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GlacialOblivionEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
