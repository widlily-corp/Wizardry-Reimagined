package com.binaris.wizardry.client.renderer.entity;

import com.binaris.wizardry.content.entity.projectile.MagicArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MagicArrowRenderer<T extends MagicArrowEntity> extends ArrowRenderer<T> {
    private final ResourceLocation texture;

    public MagicArrowRenderer(EntityRendererProvider.Context context, ResourceLocation texture) {
        super(context);
        this.texture = texture;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return this.texture;
    }
}
