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
            List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(1.0d, this.getX(), this.getY(), this.getZ(), this.level());
            for (LivingEntity target : targets) {
                if (!this.isValidTarget(target)) continue;
                if (EBMobEffects.DECAY != null && EBMobEffects.DECAY.get() != null && !target.hasEffect(EBMobEffects.DECAY)) {
                    int effDur = EBSpells.DECAY != null && EBSpells.DECAY.hasProperty(DefaultProperties.EFFECT_DURATION)
                            ? EBSpells.DECAY.property(DefaultProperties.EFFECT_DURATION) : 100;
                    target.addEffect(new MobEffectInstance(EBMobEffects.DECAY, effDur));
                }
            }
        } else if (this.random.nextInt(15) == 0) {
            double radius = random.nextDouble() * 0.8;
            float angle = random.nextFloat() * (float) Math.PI * 2;
            float brightness = random.nextFloat() * 0.4f;

            ParticleBuilder.create(EBParticles.DARK_MAGIC.get())
                    .pos(this.getX() + radius * Mth.cos(angle), this.getY(), this.getZ() + radius * Mth.sin(angle))
                    .color(brightness, 0, brightness + 0.1f)
                    .spawn(level());
        }
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }
}
