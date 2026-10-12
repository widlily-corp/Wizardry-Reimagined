package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FireSigilConstruct extends ScaledConstructEntity {
    public FireSigilConstruct(EntityType<?> entityType, Level level) {
        super(entityType, level);
        float radius = EBSpells.FIRE_SIGIL.hasProperty(DefaultProperties.EFFECT_RADIUS) ? EBSpells.FIRE_SIGIL.property(DefaultProperties.EFFECT_RADIUS) : 1.0f;
        this.setBaseSize(radius * 2, 0.2f);
    }

    public FireSigilConstruct(Level world) {
        this(EBEntities.FIRE_SIGIL.get(), world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.random.nextInt(15) == 0) {
            double radius = (0.5 + random.nextDouble() * 0.3) * getBbWidth() / 2;
            float angle = random.nextFloat() * (float) Math.PI * 2;
            level().addParticle(ParticleTypes.FLAME, this.getX() + radius * Mth.cos(angle), this.getY() + 0.1, this.getZ() + radius * Mth.sin(angle), 0, 0, 0);
        }

        if (this.level().isClientSide) return;
        List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(getBbWidth() / 2, getX(), getY(), getZ(), level());

        float damage = EBSpells.FIRE_SIGIL.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.FIRE_SIGIL.property(DefaultProperties.DAMAGE) : 6.0f;
        int burnDuration = EBSpells.FIRE_SIGIL.hasProperty(DefaultProperties.EFFECT_DURATION) ? EBSpells.FIRE_SIGIL.property(DefaultProperties.EFFECT_DURATION) : 10;

        for (LivingEntity target : targets) {
            if (!this.isValidTarget(target)) continue;
            Vec3 originalVec = target.getDeltaMovement();
            target.hurt(level().damageSources().inFire(), damage * damageMultiplier);
            target.setDeltaMovement(originalVec);
            target.igniteForSeconds(burnDuration);

            if (EBSounds.ENTITY_FIRE_SIGIL_TRIGGER != null && EBSounds.ENTITY_FIRE_SIGIL_TRIGGER.get() != null) {
                this.playSound(EBSounds.ENTITY_FIRE_SIGIL_TRIGGER.get(), 1, 1);
            }
            this.discard();
            break;
        }
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }
}
