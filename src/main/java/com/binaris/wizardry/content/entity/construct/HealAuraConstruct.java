package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class HealAuraConstruct extends ScaledConstructEntity {
    public HealAuraConstruct(EntityType<?> type, Level world) {
        super(type, world);
        float rad = EBSpells.HEALING_AURA != null && EBSpells.HEALING_AURA.hasProperty(DefaultProperties.EFFECT_RADIUS)
                ? EBSpells.HEALING_AURA.property(DefaultProperties.EFFECT_RADIUS).floatValue() : 3.0f;
        this.setBaseSize(rad * 2, 1);
    }

    public HealAuraConstruct(Level world) {
        this(EBEntities.HEAL_AURA.get(), world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 25 == 1 && EBSounds.ENTITY_HEAL_AURA_AMBIENT != null && EBSounds.ENTITY_HEAL_AURA_AMBIENT.get() != null) {
            this.playSound(EBSounds.ENTITY_HEAL_AURA_AMBIENT.get(), 0.1f, 1.0f);
        }

        if (this.level().isClientSide) {
            for (int i = 1; i < 3; i++) {
                float brightness = 0.5f + (random.nextFloat() * 0.5f);
                double radius = random.nextDouble() * (getBbWidth() / 2);
                float angle = random.nextFloat() * (float) Math.PI * 2;

                ParticleBuilder.create(EBParticles.SPARKLE.get())
                        .pos(this.xo + radius * Mth.cos(angle), this.yo, this.zo + radius * Mth.sin(angle))
                        .velocity(0, 0.05, 0)
                        .time(48 + this.random.nextInt(12))
                        .color(1.0f, 1.0f, brightness)
                        .spawn(level());
            }
            return;
        }

        List<LivingEntity> targets = EntityUtil.getLivingWithinCylinder(getBbWidth() / 2, xo, yo, zo, getBbHeight(), level());
        for (LivingEntity target : targets) {
            if (!this.isValidTarget(target)) {
                if (target.getHealth() < target.getMaxHealth() && target.tickCount % 5 == 0) {
                    float healAmount = EBSpells.HEALING_AURA != null && EBSpells.HEALING_AURA.hasProperty(DefaultProperties.HEALTH)
                            ? EBSpells.HEALING_AURA.property(DefaultProperties.HEALTH) : 1.0f;
                    target.heal(healAmount * damageMultiplier);
                }
            }
        }
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }
}
