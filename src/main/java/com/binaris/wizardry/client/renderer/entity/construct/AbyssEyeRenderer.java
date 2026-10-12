package com.binaris.wizardry.client.renderer.entity.construct;

import com.binaris.wizardry.content.entity.construct.AbyssEyeEntity;
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

public class AbyssEyeRenderer extends EntityRenderer<AbyssEyeEntity> {
    private final BlockRenderDispatcher dispatcher;

    public AbyssEyeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
        this.shadowRadius = 1.8F;
    }

    @Override
    public void render(@NotNull AbyssEyeEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.hasCollapsed()) {
            return;
        }

        poseStack.pushPose();

        float age = entity.tickCount + partialTicks;
        float pulse = 1.2F + 0.2F * Mth.sin(age * 0.4F);
        poseStack.scale(pulse, pulse, pulse);

        // Sinister multi-axis void rotation
        poseStack.mulPose(Axis.YP.rotationDegrees(age * 8.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(age * 5.0F));

        BlockState coreState = Blocks.CRYING_OBSIDIAN.defaultBlockState();
        BlockState outerState = Blocks.OBSIDIAN.defaultBlockState();

        var renderer = this.dispatcher.getModelRenderer();
        var coreModel = this.dispatcher.getBlockModel(coreState);
        var outerModel = this.dispatcher.getBlockModel(outerState);

        // Dark Singularity Core
        poseStack.pushPose();
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        renderer.tesselateBlock(
                entity.level(), coreModel, coreState, entity.blockPosition(),
                poseStack, bufferSource.getBuffer(RenderType.cutout()), false,
                entity.getRandom(), entity.getId(), OverlayTexture.NO_OVERLAY
        );
        poseStack.popPose();

        // Orbiting void mantle
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(-age * 12.0F));
        poseStack.scale(0.8F, 0.8F, 0.8F);
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        renderer.tesselateBlock(
                entity.level(), outerModel, outerState, entity.blockPosition(),
                poseStack, bufferSource.getBuffer(RenderType.cutout()), false,
                entity.getRandom(), entity.getId(), OverlayTexture.NO_OVERLAY
        );
        poseStack.popPose();

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull AbyssEyeEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
