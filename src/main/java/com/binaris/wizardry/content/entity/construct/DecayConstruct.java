package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBMobEffects;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;

import java.util.List;

public class DecayConstruct extends ScaledConstructEntity {
    public int textureIndex;

    public DecayConstruct(Level world) {
        this(EBEntities.DECAY.get(), world);
    }

    public DecayConstruct(EntityType<?> entityType, Level world) {
        super(entityType, world);
        textureIndex = this.random.nextInt(10);
        this.noCulling = true;
        int dur = EBSpells.DECAY != null && EBSpells.DECAY.hasProperty(DefaultProperties.DURATION)
                ? EBSpells.DECAY.property(DefaultProperties.DURATION) : 600;
        this.setLifetime(dur);
        this.setBaseSize(2, 0.2F);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.random.nextInt(700) == 0 && this.tickCount + 100 < lifetime
                && EBSounds.ENTITY_DECAY_AMBIENT != null && EBSounds.ENTITY_DECAY_AMBIENT.get() != null) {
            this.playSound(EBSounds.ENTITY_DECAY_AMBIENT.get(), 0.2F + random.nextFloat() * 0.2F, 0.6F + random.nextFloat() * 0.15F);
        }

        if (!this.level().isClientSide) {
            List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(1.4d, this.getX(), this.getY(), this.getZ(), this.level());
            for (LivingEntity target : targets) {
                if (!this.isValidTarget(target)) continue;
                if (EBMobEffects.DECAY != null && !target.hasEffect(EBMobEffects.DECAY)) {
                    int effDur = EBSpells.DECAY != null && EBSpells.DECAY.hasProperty(DefaultProperties.EFFECT_DURATION)
                            ? EBSpells.DECAY.property(DefaultProperties.EFFECT_DURATION) : 120;
                    target.addEffect(new MobEffectInstance(EBMobEffects.DECAY, effDur));
                    target.hurt(level().damageSources().wither(), 1.5f);
                    level().playSound(null, target.getX(), target.getY(), target.getZ(), net.minecraft.sounds.SoundEvents.WITHER_SHOOT, net.minecraft.sounds.SoundSource.HOSTILE, 0.4f, 1.8f);
                }
            }
        } else {
            // Visible creeping miasma of corruption on the ground
            for (int i = 0; i < 3; i++) {
                double r = random.nextDouble() * 1.2;
                float angle = random.nextFloat() * (float) Math.PI * 2;
                double px = this.getX() + r * Mth.cos(angle);
                double pz = this.getZ() + r * Mth.sin(angle);
                double py = this.getY() + 0.05 + random.nextDouble() * 0.15;

                level().addParticle(ParticleTypes.SQUID_INK, px, py, pz, 0, 0.02, 0);
                if (random.nextInt(2) == 0) {
                    level().addParticle(ParticleTypes.WITCH, px, py, pz, 0, 0.01, 0);
                }
            }
        }
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }
}
