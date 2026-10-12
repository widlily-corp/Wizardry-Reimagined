package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBMobEffects;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class FrostSigilConstruct extends ScaledConstructEntity {
    public FrostSigilConstruct(EntityType<?> entityType, Level level) {
        super(entityType, level);
        float rad = EBSpells.FROST_SIGIL != null && EBSpells.FROST_SIGIL.hasProperty(DefaultProperties.EFFECT_RADIUS)
                ? EBSpells.FROST_SIGIL.property(DefaultProperties.EFFECT_RADIUS).floatValue() : 1.0f;
        this.setBaseSize(rad * 2, 0.2f);
    }

    public FrostSigilConstruct(Level world) {
        this(EBEntities.FROST_SIGIL.get(), world);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            float sigilRadius = getBbWidth() / 2;
            for (int i = 0; i < 2; i++) {
                float angle = random.nextFloat() * (float) Math.PI * 2;
                double r = sigilRadius * (0.3 + random.nextDouble() * 0.7);
                level().addParticle(net.minecraft.core.particles.ParticleTypes.SNOWFLAKE, this.getX() + r * Mth.cos(angle), this.getY() + 0.05, this.getZ() + r * Mth.sin(angle), 0, 0.01, 0);
            }
            if (this.random.nextInt(3) == 0) {
                float angle = random.nextFloat() * (float) Math.PI * 2;
                level().addParticle(net.minecraft.core.particles.ParticleTypes.ITEM_SNOWBALL, this.getX() + sigilRadius * Mth.cos(angle), this.getY() + 0.05, this.getZ() + sigilRadius * Mth.sin(angle), 0, 0, 0);
            }
            return;
        }

        List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(getBbWidth() / 2, this.getX(), this.getY(), this.getZ(), this.level());

        for (LivingEntity target : targets) {
            if (!this.isValidTarget(target)) continue;
            float dmg = EBSpells.FROST_SIGIL != null && EBSpells.FROST_SIGIL.hasProperty(DefaultProperties.DAMAGE)
                    ? EBSpells.FROST_SIGIL.property(DefaultProperties.DAMAGE) : 8.0f;
            int dur = EBSpells.FROST_SIGIL != null && EBSpells.FROST_SIGIL.hasProperty(DefaultProperties.EFFECT_DURATION)
                    ? EBSpells.FROST_SIGIL.property(DefaultProperties.EFFECT_DURATION) : 200;
            int str = EBSpells.FROST_SIGIL != null && EBSpells.FROST_SIGIL.hasProperty(DefaultProperties.EFFECT_STRENGTH)
                    ? EBSpells.FROST_SIGIL.property(DefaultProperties.EFFECT_STRENGTH) : 1;

            EntityUtil.attackEntityWithoutKnockback(target, this.getCaster() != null ?
                    MagicDamageSource.causeIndirectMagicDamage(this, getCaster(), EBDamageSources.FROST) :
                    MagicDamageSource.causeDirectMagicDamage(this, EBDamageSources.SORCERY), dmg * damageMultiplier);

            target.setTicksFrozen(target.getTicksRequiredToFreeze() + dur);
            if (EBMobEffects.FROST != null && EBMobEffects.FROST.get() != null) {
                target.addEffect(new MobEffectInstance(EBMobEffects.FROST, dur, str));
            }

            if (level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SNOWFLAKE, this.getX(), this.getY() + 0.5, this.getZ(), 40, 0.8, 0.5, 0.8, 0.05);
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY() + 0.5, this.getZ(), 20, 0.6, 0.4, 0.6, 0.05);
            }

            if (EBSounds.ENTITY_FROST_SIGIL_TRIGGER != null && EBSounds.ENTITY_FROST_SIGIL_TRIGGER.get() != null) {
                this.playSound(EBSounds.ENTITY_FROST_SIGIL_TRIGGER.get(), 1.0f, 1.0f);
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
