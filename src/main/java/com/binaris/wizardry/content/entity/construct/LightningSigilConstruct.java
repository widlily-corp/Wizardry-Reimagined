package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.properties.SpellProperty;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LightningSigilConstruct extends ScaledConstructEntity {
    public static final SpellProperty<Integer> SECOND_RANGE = SpellProperty.intProperty("second_range");

    public LightningSigilConstruct(EntityType<?> entityType, Level level) {
        super(entityType, level);
        float rad = EBSpells.LIGHTNING_SIGIL != null && EBSpells.LIGHTNING_SIGIL.hasProperty(DefaultProperties.EFFECT_RADIUS)
                ? EBSpells.LIGHTNING_SIGIL.property(DefaultProperties.EFFECT_RADIUS).floatValue() : 1.0f;
        this.setBaseSize(rad * 2, 0.2f);
    }

    public LightningSigilConstruct(Level world) {
        this(EBEntities.LIGHTNING_SIGIL.get(), world);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount > 600 && this.getCaster() == null && !this.level().isClientSide) {
            this.discard();
        }

        List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(getBbWidth() / 2, getX(), getY(), getZ(), level());
        for (LivingEntity target : targets) {
            if (!isValidTarget(target)) continue;

            Vec3 originalVec = target.getDeltaMovement();

            float dmg = EBSpells.LIGHTNING_SIGIL != null && EBSpells.LIGHTNING_SIGIL.hasProperty(DefaultProperties.DAMAGE)
                    ? EBSpells.LIGHTNING_SIGIL.property(DefaultProperties.DAMAGE) : 6.0f;

            if (!MagicDamageSource.causeMagicDamage(this, target, dmg * damageMultiplier, EBDamageSources.SHOCK)) {
                continue;
            }

            if (EBSounds.ENTITY_LIGHTNING_SIGIL_TRIGGER != null && EBSounds.ENTITY_LIGHTNING_SIGIL_TRIGGER.get() != null) {
                playSound(EBSounds.ENTITY_LIGHTNING_SIGIL_TRIGGER.get(), 1.0f, 1.0f);
            }
            target.setDeltaMovement(originalVec);

            double seekerRange = EBSpells.LIGHTNING_SIGIL != null && EBSpells.LIGHTNING_SIGIL.hasProperty(DefaultProperties.RANGE)
                    ? EBSpells.LIGHTNING_SIGIL.property(DefaultProperties.RANGE).doubleValue() : 10.0;
            List<LivingEntity> secondaryTargets = EntityUtil.getLivingWithinRadius(seekerRange, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), level());
            int maxTargets = EBSpells.LIGHTNING_SIGIL != null && EBSpells.LIGHTNING_SIGIL.hasProperty(DefaultProperties.MAX_TARGETS)
                    ? EBSpells.LIGHTNING_SIGIL.property(DefaultProperties.MAX_TARGETS) : 3;
            int hitCount = 0;

            for (LivingEntity secondary : secondaryTargets) {
                if (secondary != target && isValidTarget(secondary) && hitCount < maxTargets) {
                    MagicDamageSource.causeMagicDamage(this, secondary, dmg * 0.75f * damageMultiplier, EBDamageSources.SHOCK);
                    hitCount++;
                }
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
