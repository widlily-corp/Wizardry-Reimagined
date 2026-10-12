package com.binaris.wizardry.client.renderer.entity.construct;

import com.binaris.wizardry.content.entity.construct.IceBarrierConstruct;
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

public class IceBarrierRenderer extends EntityRenderer<IceBarrierConstruct> {
    private final BlockRenderDispatcher dispatcher;

    public IceBarrierRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
        this.shadowRadius = 0.6F;
    }

    @Override
    public void render(@NotNull IceBarrierConstruct entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // Rotate wall according to entity yaw
        poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw));

        BlockState iceBlock = Blocks.PACKED_ICE.defaultBlockState();
        var model = this.dispatcher.getBlockModel(iceBlock);
        var renderer = this.dispatcher.getModelRenderer();

        // Render 3x3 ice wall slabs
        int halfWidth = 1;
        int height = 3;

        for (int x = -halfWidth; x <= halfWidth; x++) {
            for (int y = 0; y < height; y++) {
                poseStack.pushPose();
                poseStack.translate(x - 0.5D, y, -0.2D);
                poseStack.scale(1.0F, 1.0F, 0.4F);

                renderer.tesselateBlock(
                        entity.level(),
                        model,
                        iceBlock,
                        entity.blockPosition(),
                        poseStack,
                        bufferSource.getBuffer(RenderType.translucent()),
                        false,
                        entity.level().random,
                        iceBlock.getSeed(entity.blockPosition()),
                        OverlayTexture.NO_OVERLAY
                );

                poseStack.popPose();
            }
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, 0xF000F0);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull IceBarrierConstruct entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
