package com.binaris.wizardry.client.renderer.entity.construct;

import com.binaris.wizardry.content.entity.construct.TitansHammerEntity;
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

public class TitansHammerRenderer extends EntityRenderer<TitansHammerEntity> {
    private final BlockRenderDispatcher dispatcher;

    public TitansHammerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
        this.shadowRadius = 2.0F;
    }

    @Override
    public void render(@NotNull TitansHammerEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // Slamming hammer angle
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.hasImpacted() ? 25.0F : 45.0F));

        BlockState handleState = Blocks.NETHERITE_BLOCK.defaultBlockState();
        BlockState headState = Blocks.GOLD_BLOCK.defaultBlockState();
        BlockState coreState = Blocks.SEA_LANTERN.defaultBlockState();

        var renderer = this.dispatcher.getModelRenderer();
        var handleModel = this.dispatcher.getBlockModel(handleState);
        var headModel = this.dispatcher.getBlockModel(headState);
        var coreModel = this.dispatcher.getBlockModel(coreState);

        // Shaft (handle)
        for (int y = 0; y < 4; y++) {
            poseStack.pushPose();
            poseStack.translate(-0.25D, y * 0.8D, -0.25D);
            poseStack.scale(0.5F, 0.8F, 0.5F);
            renderer.tesselateBlock(
                    entity.level(), handleModel, handleState, entity.blockPosition(),
                    poseStack, bufferSource.getBuffer(RenderType.cutout()), false,
                    entity.getRandom(), entity.getId(), OverlayTexture.NO_OVERLAY
            );
            poseStack.popPose();
        }

        // Hammer Head (massive golden maul)
        poseStack.pushPose();
        poseStack.translate(-1.0D, 3.2D, -0.8D);
        poseStack.scale(2.0F, 1.4F, 1.6F);
        renderer.tesselateBlock(
                entity.level(), headModel, headState, entity.blockPosition(),
                poseStack, bufferSource.getBuffer(RenderType.cutout()), false,
                entity.getRandom(), entity.getId(), OverlayTexture.NO_OVERLAY
        );
        poseStack.popPose();

        // Glowing Core inside head
        poseStack.pushPose();
        poseStack.translate(-0.5D, 3.4D, -0.5D);
        poseStack.scale(1.0F, 1.0F, 1.0F);
        renderer.tesselateBlock(
                entity.level(), coreModel, coreState, entity.blockPosition(),
                poseStack, bufferSource.getBuffer(RenderType.translucent()), false,
                entity.getRandom(), entity.getId(), OverlayTexture.NO_OVERLAY
        );
        poseStack.popPose();

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TitansHammerEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
