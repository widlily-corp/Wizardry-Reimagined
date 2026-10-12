package com.binaris.wizardry.client.renderer.entity.construct;

import com.binaris.wizardry.content.entity.construct.SupernovaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SupernovaRenderer extends EntityRenderer<SupernovaEntity> {
    private final BlockRenderDispatcher dispatcher;

    public SupernovaRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
        this.shadowRadius = 2.0F;
    }

    @Override
    public void render(@NotNull SupernovaEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.hasImpacted()) {
            return; // Particles handle the blast effect after impact
        }

        poseStack.pushPose();

        float age = entity.tickCount + partialTicks;
        int charge = entity.getChargeTicks();

        // Pulsating scale based on charge accumulation
        float baseScale = 1.4F + Math.min(charge * 0.04F, 1.2F);
        float pulse = 0.15F * Mth.sin(age * 0.5F);
        float totalScale = baseScale + pulse;

        poseStack.scale(totalScale, totalScale, totalScale);

        // Multi-axis gyroscopic stellar rotation
        poseStack.mulPose(Axis.YP.rotationDegrees(age * 6.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(age * 4.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(age * 3.0F));

        BlockState magmaState = Blocks.MAGMA_BLOCK.defaultBlockState();
        BlockState glowState = Blocks.SHROOMLIGHT.defaultBlockState();

        var renderer = this.dispatcher.getModelRenderer();
        var magmaModel = this.dispatcher.getBlockModel(magmaState);
        var glowModel = this.dispatcher.getBlockModel(glowState);

        // Core block
        poseStack.pushPose();
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        renderer.tesselateBlock(
                entity.level(),
                magmaModel,
                magmaState,
                entity.blockPosition(),
                poseStack,
                bufferSource.getBuffer(RenderType.cutout()),
                false,
                entity.getRandom(),
                entity.getId(),
                OverlayTexture.NO_OVERLAY
        );
        poseStack.popPose();

        // Outer counter-rotating stellar mantle
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-age * 10.0F));
        poseStack.scale(0.8F, 0.8F, 0.8F);
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        renderer.tesselateBlock(
                entity.level(),
                glowModel,
                glowState,
                entity.blockPosition(),
                poseStack,
                bufferSource.getBuffer(RenderType.translucent()),
                false,
                entity.getRandom(),
                entity.getId(),
                OverlayTexture.NO_OVERLAY
        );
        poseStack.popPose();

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SupernovaEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
