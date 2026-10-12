package com.binaris.wizardry.content.entity.construct;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class ScaledConstructEntity extends MagicConstructEntity {
    protected float sizeMultiplier = 1.0f;

    public ScaledConstructEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    public void setSizeMultiplier(float multiplier) {
        this.sizeMultiplier = multiplier;
    }

    public float getSizeMultiplier() {
        return sizeMultiplier;
    }
}
