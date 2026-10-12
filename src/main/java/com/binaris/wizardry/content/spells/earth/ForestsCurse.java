package com.binaris.wizardry.content.spells.earth;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.AreaEffectSpell;
import com.binaris.wizardry.content.spells.abstr.BuffSpell;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ForestsCurse extends AreaEffectSpell {
    public ForestsCurse() {
        this.alwaysSucceed(true);
        this.soundValues(1, 1.1f, 0.2f);
    }

    @Override
    protected boolean affectEntity(CastContext ctx, Vec3 origin, LivingEntity target, int targetCount) {
        if (!ctx.world().isClientSide) {
            DamageSource source = ctx.caster() != null ? MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.POISON)
                    : target.damageSources().magic();
            target.hurt(source, ctx.modifiers().get(SpellModifiers.POTENCY, property(DefaultProperties.DAMAGE)));

            int bonusAmplifier = BuffSpell.getStandardBonusAmplifier(ctx.modifiers().getFactor(SpellModifiers.POTENCY));
            int duration = (int) (ctx.modifiers().get(SpellModifiers.DURATION, property(DefaultProperties.EFFECT_DURATION)));
            int amplifier = property(DefaultProperties.EFFECT_STRENGTH) + bonusAmplifier;

            target.addEffect(new MobEffectInstance(MobEffects.POISON, duration, amplifier));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, amplifier));
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, amplifier));
        }

        return true;
    }

    @Override
    protected void spawnParticle(Level world, double x, double y, double z) {
        y += 2;
        float brightness = world.random.nextFloat() / 4;
        ParticleBuilder.create(EBParticles.DARK_MAGIC.get()).pos(x, y, z).velocity(0, -0.2, 0)
                .color(0.05f + brightness, 0.2f + brightness, 0).spawn(world);

        brightness = world.random.nextFloat() / 4;
        ParticleBuilder.create(EBParticles.SPARKLE.get()).pos(x, y, z).velocity(0, -0.05, 0).time(50)
                .color(0.1f + brightness, 0.2f + brightness, 0).spawn(world);
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.MASTER, Elements.EARTH, SpellTypes.ATTACK, SpellAction.POINT_DOWN, 70, 20, 250)
                .add(DefaultProperties.EFFECT_RADIUS, 7)
                .add(DefaultProperties.DAMAGE, 6F)
                .add(DefaultProperties.EFFECT_DURATION, 300)
                .add(DefaultProperties.EFFECT_STRENGTH, 1)
                .build();
    }
}
