package com.binaris.wizardry.content.spells.healing;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.spell.properties.SpellProperty;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RayOfPurification extends RaySpell {
    private static final SpellProperty<Float> UNDEAD_DAMAGE_MULTIPLIER = SpellProperty.floatProperty("undead_damage_multiplier", 2);

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        if (!(entityHit.getEntity() instanceof LivingEntity target)) return false;

        if (ctx.castingTicks() % 10 != 0) return true;
        float damage = ctx.modifiers().get(SpellModifiers.POTENCY, property(UNDEAD_DAMAGE_MULTIPLIER));
        if (target.isInvertedHealAndHarm()) damage *= property(UNDEAD_DAMAGE_MULTIPLIER);

        EntityUtil.attackEntityWithoutKnockback(target,
                MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.RADIANT), damage);

        if (!ctx.world().isClientSide) {
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,
                    (int) (ctx.modifiers().get(SpellModifiers.DURATION, property(DefaultProperties.EFFECT_DURATION)))));
        }

        return true;
    }

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        return false;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return true;
    }

    @Override
    protected void spawnParticle(CastContext ctx, double x, double y, double z, double vx, double vy, double vz) {
        ParticleBuilder.create(EBParticles.SPARKLE.get()).pos(x, y, z).color(1.0f, 1.0f, 0.7f).spawn(ctx.world());
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.MASTER, Elements.HEALING, SpellTypes.ATTACK, SpellAction.POINT, 40, 10, 100)
                .add(DefaultProperties.RANGE, 15F)
                .add(DefaultProperties.DAMAGE, 5F)
                .add(DefaultProperties.EFFECT_DURATION, 100)
                .add(UNDEAD_DAMAGE_MULTIPLIER)
                .build();
    }
}
