package com.binaris.wizardry.content.spells.necromancy;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.BuffSpell;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import com.binaris.wizardry.registry.EBParticles;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Wither extends RaySpell {
    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        if (entityHit.getEntity() instanceof LivingEntity target) {
            if (ctx.world().isClientSide) return true;
            DamageSource source = ctx.caster() != null ? MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.WITHER)
                    : target.damageSources().wither();

            target.hurt(source, ctx.modifiers().get(SpellModifiers.POTENCY, property(DefaultProperties.DAMAGE)));
            target.addEffect(new MobEffectInstance(MobEffects.WITHER,
                    (int) (ctx.modifiers().get(SpellModifiers.DURATION, property(DefaultProperties.EFFECT_DURATION))),
                    property(DefaultProperties.EFFECT_STRENGTH) + BuffSpell.getStandardBonusAmplifier(ctx.modifiers().getFactor(SpellModifiers.POTENCY))));
        }

        return true;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return true;
    }

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        return false;
    }

    @Override
    protected void spawnParticle(CastContext ctx, double x, double y, double z, double vx, double vy, double vz) {
        ParticleBuilder.create(EBParticles.DARK_MAGIC).pos(x, y, z).color(0.1f, 0, 0).spawn(ctx.world());
        ParticleBuilder.create(EBParticles.SPARKLE).pos(x, y, z).time(12 + ctx.world().random.nextInt(8)).color(0.1f, 0, 0.05f).spawn(ctx.world());
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.NECROMANCY, SpellTypes.ATTACK, SpellAction.POINT, 15, 0, 20)
                .add(DefaultProperties.RANGE, 10F)
                .add(DefaultProperties.DAMAGE, 2.0f)
                .add(DefaultProperties.EFFECT_DURATION, 100)
                .add(DefaultProperties.EFFECT_STRENGTH, 1)
                .build();
    }
}
