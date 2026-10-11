package com.binaris.wizardry.client.renderer.blockentity;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.block.workbench.ArcaneWorkbenchBlockEntity;
import com.binaris.wizardry.content.menu.ArcaneWorkbenchMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class ArcaneWorkbenchRender implements BlockEntityRenderer<ArcaneWorkbenchBlockEntity> {
    private static final ResourceLocation RUNE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/entity/arcane_workbench_rune.png");

    public ArcaneWorkbenchRender(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ArcaneWorkbenchBlockEntity entity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stack = entity.getItem(ArcaneWorkbenchMenu.CENTRE_SLOT);
        if (stack.isEmpty()) return;

        poseStack.pushPose();
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));

        // Calculate interpolated angle
        float f1 = entity.rot - entity.oRot;
        while (f1 >= (float) Math.PI) f1 -= ((float) Math.PI * 2F);
        while (f1 < -(float) Math.PI) f1 += ((float) Math.PI * 2F);
        double angle = Math.toDegrees(entity.oRot + f1 * partialTicks);

        this.renderEffect(poseStack, entity, partialTicks, bufferSource, packedLight);
        this.renderWand(poseStack, stack, entity, angle, partialTicks, packedLight, bufferSource);

        poseStack.popPose();
    }

    private void renderEffect(PoseStack poseStack, ArcaneWorkbenchBlockEntity entity, float partialTicks, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.timer + partialTicks));
        poseStack.translate(0.0F, 0.65F, 0.0F);

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(RUNE_TEXTURE));
        Matrix4f matrix = poseStack.last().pose();

        // Top face
        buffer.addVertex(matrix, -0.5F, 0, -0.5F).setColor(255, 255, 255, 255).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 1, 0);
        buffer.addVertex(matrix, -0.5F, 0, 0.5F).setColor(255, 255, 255, 255).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 0.5F, 0, 0.5F).setColor(255, 255, 255, 255).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 0.5F, 0, -0.5F).setColor(255, 255, 255, 255).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 1, 0);

        // Bottom face (reverse winding for two-sided visibility)
        buffer.addVertex(matrix, 0.5F, 0, -0.5F).setColor(255, 255, 255, 255).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, -1, 0);
        buffer.addVertex(matrix, 0.5F, 0, 0.5F).setColor(255, 255, 255, 255).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, -1, 0);
        buffer.addVertex(matrix, -0.5F, 0, 0.5F).setColor(255, 255, 255, 255).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, -1, 0);
        buffer.addVertex(matrix, -0.5F, 0, -0.5F).setColor(255, 255, 255, 255).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, -1, 0);

        poseStack.popPose();
    }

    private void renderWand(PoseStack poseStack, ItemStack stack, ArcaneWorkbenchBlockEntity entity, double angle, float partialTicks, int light, MultiBufferSource bufferSource) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.ZP.rotationDegrees((float) (angle + 45)));

        poseStack.translate(0.0F, 0.0F, 0.56F + 0.05F * Mth.sin((entity.timer + partialTicks) / 15.0F));
        poseStack.scale(0.75F, 0.75F, 0.75F);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                light,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                entity.getLevel(),
                0
        );

        poseStack.popPose();
    }
}
