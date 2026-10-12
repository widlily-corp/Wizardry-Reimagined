package com.binaris.wizardry.client.renderer.entity;

import com.binaris.wizardry.content.entity.projectile.MagicProjectileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class MagicProjectileRenderer<T extends MagicProjectileEntity> extends EntityRenderer<T> {
    private final ResourceLocation texture;
    private final float scale;

    public MagicProjectileRenderer(EntityRendererProvider.Context context, ResourceLocation texture) {
        this(context, texture, 0.6f);
    }

    public MagicProjectileRenderer(EntityRendererProvider.Context context, ResourceLocation texture, float scale) {
        super(context);
        this.texture = texture;
        this.scale = scale;
    }

    @Override
    public void render(@NotNull T entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        int light = 0xF000F0; // Full bright magic illumination
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));
        Matrix4f matrix = poseStack.last().pose();

        // Quad 1
        consumer.addVertex(matrix, -0.5F, -0.5F, 0.0F).setColor(255, 255, 255, 255).setUv(0.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(matrix, 0.5F, -0.5F, 0.0F).setColor(255, 255, 255, 255).setUv(1.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(matrix, 0.5F, 0.5F, 0.0F).setColor(255, 255, 255, 255).setUv(1.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(matrix, -0.5F, 0.5F, 0.0F).setColor(255, 255, 255, 255).setUv(0.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);

        // Quad 2 (Cross angle for 3D fullness)
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        Matrix4f matrix2 = poseStack.last().pose();
        consumer.addVertex(matrix2, -0.5F, -0.5F, 0.0F).setColor(255, 255, 255, 255).setUv(0.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(matrix2, 0.5F, -0.5F, 0.0F).setColor(255, 255, 255, 255).setUv(1.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(matrix2, 0.5F, 0.5F, 0.0F).setColor(255, 255, 255, 255).setUv(1.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
        consumer.addVertex(matrix2, -0.5F, 0.5F, 0.0F).setColor(255, 255, 255, 255).setUv(0.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, light);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return this.texture;
    }
}
