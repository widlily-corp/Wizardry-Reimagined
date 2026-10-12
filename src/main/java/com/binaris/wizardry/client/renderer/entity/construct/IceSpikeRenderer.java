package com.binaris.wizardry.client.renderer.entity.construct;

import com.binaris.wizardry.content.entity.construct.IceSpikeConstruct;
import com.mojang.blaze3d.vertex.PoseStack;
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

public class IceSpikeRenderer extends EntityRenderer<IceSpikeConstruct> {
    private final BlockRenderDispatcher dispatcher;

    public IceSpikeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(@NotNull IceSpikeConstruct entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        float scaleXZ = 0.75F;
        float scaleY = 1.7F;
        poseStack.translate(-scaleXZ * 0.5D, 0.0D, -scaleXZ * 0.5D);
        poseStack.scale(scaleXZ, scaleY, scaleXZ);

        BlockState blockState = Blocks.PACKED_ICE.defaultBlockState();
        var model = this.dispatcher.getBlockModel(blockState);
        this.dispatcher.getModelRenderer().tesselateBlock(
                entity.level(),
                model,
                blockState,
                entity.blockPosition(),
                poseStack,
                bufferSource.getBuffer(RenderType.translucent()),
                false,
                entity.level().random,
                blockState.getSeed(entity.blockPosition()),
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, 0xF000F0);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull IceSpikeConstruct entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
