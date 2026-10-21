package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.content.entity.projectile.ConjuredArrowEntity;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ArrowRainConstruct extends ScaledConstructEntity {
    public ArrowRainConstruct(Level world) {
        this(EBEntities.ARROW_RAIN.get(), world);
    }

    public ArrowRainConstruct(EntityType<?> entityType, Level level) {
        super(entityType, level);
        float rad = EBSpells.ARROW_RAIN != null && EBSpells.ARROW_RAIN.hasProperty(DefaultProperties.EFFECT_RADIUS)
                ? EBSpells.ARROW_RAIN.property(DefaultProperties.EFFECT_RADIUS).floatValue() : 4.0f;
        this.setBaseSize(rad * 4, 0.5f);
        this.setLifetime(60);
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            double x = this.xo + (this.level().random.nextDouble() - 0.5D) * (double) getBbWidth();
            double y = this.yo + this.level().random.nextDouble() * (double) getBbHeight();
            double z = this.zo + (this.level().random.nextDouble() - 0.5D) * (double) getBbWidth();

            ConjuredArrowEntity arrow = new ConjuredArrowEntity(x, y, z, this.level());
            arrow.setDeltaMovement((level().random.nextDouble() - 0.5D) * 0.1D, -1.0D, (level().random.nextDouble() - 0.5D) * 0.1D);
            arrow.setOwner(this.getCaster());
            arrow.setBaseDamage(7.0d * damageMultiplier);
            this.level().addFreshEntity(arrow);
        }
    }
}
