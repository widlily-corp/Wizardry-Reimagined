package com.binaris.wizardry.client.renderer.entity.construct;

import com.binaris.wizardry.content.entity.construct.BoulderConstruct;
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

public class BoulderRenderer extends EntityRenderer<BoulderConstruct> {
    private final BlockRenderDispatcher dispatcher;

    public BoulderRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
        this.shadowRadius = 1.2F;
    }

    @Override
    public void render(@NotNull BoulderConstruct entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        float scale = 2.2F;
        poseStack.scale(scale, scale, scale);
        poseStack.translate(-0.5D, 0.05D, -0.5D);

        // Dynamic roll rotation based on velocity
        float roll = (float) ((entity.tickCount + partialTicks) * 20.0F);
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.XP.rotationDegrees(roll));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        BlockState blockState = Blocks.STONE.defaultBlockState();
        var model = this.dispatcher.getBlockModel(blockState);
        this.dispatcher.getModelRenderer().tesselateBlock(
                entity.level(),
                model,
                blockState,
                entity.blockPosition(),
                poseStack,
                bufferSource.getBuffer(RenderType.cutout()),
                false,
                entity.level().random,
                blockState.getSeed(entity.blockPosition()),
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BoulderConstruct entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
