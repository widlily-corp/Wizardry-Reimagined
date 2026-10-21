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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;

import java.util.List;

public class BlizzardConstruct extends ScaledConstructEntity {
    public BlizzardConstruct(EntityType<?> type, Level level) {
        super(type, level);
        int dur = EBSpells.BLIZZARD != null && EBSpells.BLIZZARD.hasProperty(DefaultProperties.DURATION) ? EBSpells.BLIZZARD.property(DefaultProperties.DURATION) : 600;
        float rad = EBSpells.BLIZZARD != null && EBSpells.BLIZZARD.hasProperty(DefaultProperties.EFFECT_RADIUS) ? EBSpells.BLIZZARD.property(DefaultProperties.EFFECT_RADIUS).floatValue() : 3.0f;
        this.setLifetime(dur);
        this.setBaseSize(rad * 2, 3);
    }

    public BlizzardConstruct(Level world) {
        this(EBEntities.BLIZZARD.get(), world);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount % 120 == 1 && EBSounds.ENTITY_BLIZZARD_AMBIENT != null && EBSounds.ENTITY_BLIZZARD_AMBIENT.get() != null) {
            this.playSound(EBSounds.ENTITY_BLIZZARD_AMBIENT.get(), 1.0f, 1.0f);
        }
        double radius = getBbWidth() / 2;

        if (!this.level().isClientSide) {
            List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(radius, this.getX(), this.getY(), this.getZ(), level());

            for (LivingEntity target : targets) {
                if (this.isValidTarget(target)) {
                    EntityUtil.attackEntityWithoutKnockback(target, getCaster() != null ? MagicDamageSource.causeIndirectMagicDamage(this, getCaster(), EBDamageSources.FROST)
                                    : MagicDamageSource.causeDirectMagicDamage(this, EBDamageSources.SORCERY),
                            1.0f * damageMultiplier);
                    if (EBMobEffects.FROST != null && EBMobEffects.FROST.get() != null) {
                        target.addEffect(new MobEffectInstance(EBMobEffects.FROST, 20));
                    }
                }
            }

        } else {
            // Authentic swirling snowstorm vortex
            for (int i = 0; i < 8; i++) {
                double angle = (this.tickCount * 0.18 + i * (Math.PI * 2 / 8));
                double dist = 0.4 + random.nextDouble() * (radius - 0.4);
                double px = this.getX() + dist * Math.cos(angle);
                double pz = this.getZ() + dist * Math.sin(angle);
                double py = this.getY() + random.nextDouble() * getBbHeight();

                double vx = -Math.sin(angle) * 0.22;
                double vz = Math.cos(angle) * 0.22;
                double vy = -0.04 + random.nextDouble() * 0.08;

                level().addParticle(ParticleTypes.SNOWFLAKE, px, py, pz, vx, vy, vz);
                if (random.nextInt(3) == 0) {
                    level().addParticle(ParticleTypes.ITEM_SNOWBALL, px, py, pz, vx * 0.5, vy, vz * 0.5);
                }
            }

            if (random.nextInt(2) == 0) {
                double angle = random.nextDouble() * Math.PI * 2;
                double dist = random.nextDouble() * radius;
                level().addParticle(ParticleTypes.CLOUD, this.getX() + dist * Math.cos(angle), this.getY() + random.nextDouble() * (getBbHeight() * 0.8), this.getZ() + dist * Math.sin(angle), 0, 0.03, 0);
            }
        }
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }
}
