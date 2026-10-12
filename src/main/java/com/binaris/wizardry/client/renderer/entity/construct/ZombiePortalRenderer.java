package com.binaris.wizardry.client.renderer.entity.construct;

import com.binaris.wizardry.content.entity.construct.ZombieSpawnerConstruct;
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

/**
 * 3D Renderer for Zombie Apocalypse Underworld Portal.
 * Renders a hovering, rotating abyssal rift with an obsidian frame and glowing void aperture.
 */
public class ZombiePortalRenderer extends EntityRenderer<ZombieSpawnerConstruct> {
    private final BlockRenderDispatcher dispatcher;

    public ZombiePortalRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
        this.shadowRadius = 2.0F;
    }

    @Override
    public void render(@NotNull ZombieSpawnerConstruct entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        float age = entity.tickCount + partialTicks;

        // Eerie breathing portal pulsation
        float pulse = 1.35F + 0.12F * Mth.sin(age * 0.2F);
        poseStack.scale(pulse, 0.35F, pulse);

        // Counter-rotating portal rings
        float outerRotation = age * 4.0F;
        poseStack.mulPose(Axis.YP.rotationDegrees(outerRotation));

        BlockState frameState = Blocks.CRYING_OBSIDIAN.defaultBlockState();
        BlockState innerState = Blocks.NETHER_PORTAL.defaultBlockState();
        BlockState capState = Blocks.OBSIDIAN.defaultBlockState();

        var renderer = this.dispatcher.getModelRenderer();
        var frameModel = this.dispatcher.getBlockModel(frameState);
        var innerModel = this.dispatcher.getBlockModel(innerState);
        var capModel = this.dispatcher.getBlockModel(capState);

        // Glowing underworld portal core
        poseStack.pushPose();
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        renderer.tesselateBlock(
                entity.level(), innerModel, innerState, entity.blockPosition(),
                poseStack, bufferSource.getBuffer(RenderType.translucent()), false,
                entity.getRandom(), entity.getId(), OverlayTexture.NO_OVERLAY
        );
        poseStack.popPose();

        // 8-segment obsidian rift perimeter frame
        int segments = 8;
        double frameRadius = 1.6;
        for (int i = 0; i < segments; i++) {
            double angle = (Math.PI * 2.0 / segments) * i;
            double ox = Math.cos(angle) * frameRadius;
            double oz = Math.sin(angle) * frameRadius;

            poseStack.pushPose();
            poseStack.translate(ox - 0.25D, -0.5D, oz - 0.25D);
            poseStack.scale(0.5F, 1.0F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(-angle)));

            BlockState toRender = (i % 2 == 0) ? frameState : capState;
            var modelToRender = (i % 2 == 0) ? frameModel : capModel;

            renderer.tesselateBlock(
                    entity.level(), modelToRender, toRender, entity.blockPosition(),
                    poseStack, bufferSource.getBuffer(RenderType.cutout()), false,
                    entity.getRandom(), entity.getId(), OverlayTexture.NO_OVERLAY
            );
            poseStack.popPose();
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ZombieSpawnerConstruct entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
