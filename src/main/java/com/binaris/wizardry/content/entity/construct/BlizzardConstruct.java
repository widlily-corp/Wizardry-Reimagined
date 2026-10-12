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
                }
                if (EBMobEffects.FROST != null && EBMobEffects.FROST.get() != null) {
                    target.addEffect(new MobEffectInstance(EBMobEffects.FROST, 20));
                }
            }

        } else {
            for (int i = 0; i < 6; i++) {
                double speed = (random.nextBoolean() ? 1 : -1) * (0.1 + 0.05 * random.nextDouble());
                ParticleBuilder.create(EBParticles.SNOW.get()).pos(this.getX(), this.getY() + random.nextDouble() * getBbHeight(), this.getZ()).velocity(0, 0, 0)
                        .time(100).scale(2).spin(random.nextDouble() * (radius - 0.5) + 0.5, speed).shaded(true).spawn(level());
            }

            for (int i = 0; i < 3; i++) {
                double speed = (random.nextBoolean() ? 1 : -1) * (0.05 + 0.02 * random.nextDouble());
                ParticleBuilder.create(EBParticles.CLOUD.get()).pos(this.getX(), this.getY() + random.nextDouble() * (getBbHeight() - 0.5), this.getZ()).color(0xffffff).shaded(true).spin(random.nextDouble() * (radius - 1) + 0.5, speed).spawn(level());
            }
        }
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }
}
