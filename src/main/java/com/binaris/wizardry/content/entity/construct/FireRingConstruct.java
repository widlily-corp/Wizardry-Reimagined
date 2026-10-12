package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FireRingConstruct extends ScaledConstructEntity {
    public FireRingConstruct(EntityType<?> entityType, Level level) {
        super(entityType, level);
        float radius = EBSpells.RING_OF_FIRE.hasProperty(DefaultProperties.EFFECT_RADIUS) ? EBSpells.RING_OF_FIRE.property(DefaultProperties.EFFECT_RADIUS) : 3.0f;
        this.setBaseSize(radius * 2, 1);
    }

    public FireRingConstruct(Level world) {
        this(EBEntities.RING_OF_FIRE.get(), world);
        int duration = EBSpells.RING_OF_FIRE.hasProperty(DefaultProperties.DURATION) ? EBSpells.RING_OF_FIRE.property(DefaultProperties.DURATION) : 600;
        this.setLifetime(duration);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 40 == 1 && EBSounds.ENTITY_FIRE_RING_AMBIENT != null && EBSounds.ENTITY_FIRE_RING_AMBIENT.get() != null) {
            this.playSound(EBSounds.ENTITY_FIRE_RING_AMBIENT.get(), 4.0f, 0.7f);
        }
        if (this.tickCount % 5 != 0 || this.level().isClientSide) return;

        List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(getBbWidth() / 2, this.getX(), this.getY(), this.getZ(), this.level());
        int burnDuration = EBSpells.RING_OF_FIRE.hasProperty(DefaultProperties.EFFECT_DURATION) ? EBSpells.RING_OF_FIRE.property(DefaultProperties.EFFECT_DURATION) : 10;
        float damage = EBSpells.RING_OF_FIRE.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.RING_OF_FIRE.property(DefaultProperties.DAMAGE) : 1.0f;

        for (LivingEntity target : targets) {
            if (this.isValidTarget(target)) {
                Vec3 originalVec = target.getDeltaMovement();
                target.igniteForSeconds(burnDuration);
                target.hurt(level().damageSources().inFire(), damage * damageMultiplier);
                target.setDeltaMovement(originalVec);
            }
        }
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }
}
