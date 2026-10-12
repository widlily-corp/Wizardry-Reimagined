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
        if (this.level().isClientSide) {
            float ringRadius = getBbWidth() / 2;
            for (int i = 0; i < 6; i++) {
                float angle = random.nextFloat() * (float) Math.PI * 2;
                double px = this.getX() + ringRadius * net.minecraft.util.Mth.cos(angle);
                double pz = this.getZ() + ringRadius * net.minecraft.util.Mth.sin(angle);
                level().addParticle(net.minecraft.core.particles.ParticleTypes.FLAME, px, this.getY() + random.nextDouble() * 0.8, pz, 0, 0.05, 0);
            }
            if (this.random.nextInt(3) == 0) {
                float angle = random.nextFloat() * (float) Math.PI * 2;
                double px = this.getX() + ringRadius * net.minecraft.util.Mth.cos(angle);
                double pz = this.getZ() + ringRadius * net.minecraft.util.Mth.sin(angle);
                level().addParticle(net.minecraft.core.particles.ParticleTypes.LAVA, px, this.getY() + 0.2, pz, 0, 0, 0);
            }
            return;
        }

        if (this.tickCount % 5 != 0) return;

        List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(getBbWidth() / 2 + 0.5, this.getX(), this.getY(), this.getZ(), this.level());
        int burnDuration = EBSpells.RING_OF_FIRE.hasProperty(DefaultProperties.EFFECT_DURATION) ? EBSpells.RING_OF_FIRE.property(DefaultProperties.EFFECT_DURATION) : 10;
        float damage = EBSpells.RING_OF_FIRE.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.RING_OF_FIRE.property(DefaultProperties.DAMAGE) : 2.0f;

        for (LivingEntity target : targets) {
            if (this.isValidTarget(target)) {
                target.igniteForSeconds(burnDuration);
                target.hurt(level().damageSources().inFire(), damage * damageMultiplier);
                // Push enemy back away from center
                Vec3 push = new Vec3(target.getX() - this.getX(), 0.1, target.getZ() - this.getZ()).normalize().scale(0.35);
                target.setDeltaMovement(target.getDeltaMovement().add(push));
                target.hurtMarked = true;
            }
        }
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }
}
