package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.content.entity.projectile.IceShardEntity;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class HailstormConstruct extends ScaledConstructEntity {

    public HailstormConstruct(EntityType<?> entityType, Level level) {
        super(entityType, level);
        int dur = EBSpells.HAILSTORM != null && EBSpells.HAILSTORM.hasProperty(DefaultProperties.DURATION) ? EBSpells.HAILSTORM.property(DefaultProperties.DURATION) : 600;
        this.setLifetime(dur);
        this.setBaseSize(4, 5);
    }

    public HailstormConstruct(Level level) {
        this(EBEntities.HAILSTORM.get(), level);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            double x = getX() + (level().random.nextDouble() - 0.5D) * (double) getBbWidth();
            double y = getY() + level().random.nextDouble() * (double) getBbHeight();
            double z = getZ() + (level().random.nextDouble() - 0.5D) * (double) getBbWidth();

            IceShardEntity iceshard = new IceShardEntity(level());
            iceshard.setPos(x, y, z);
            iceshard.setDeltaMovement(Mth.cos((float) Math.toRadians(this.getYRot() + 90)), -0.6, Mth.sin((float) Math.toRadians(this.getYRot() + 90)));
            iceshard.setOwner(this.getCaster());
            iceshard.damageMultiplier = this.damageMultiplier;

            this.level().addFreshEntity(iceshard);
        }
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }
}
