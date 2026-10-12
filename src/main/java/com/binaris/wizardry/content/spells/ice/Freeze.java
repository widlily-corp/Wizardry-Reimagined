package com.binaris.wizardry.content.spells.ice;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.BlockUtil;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Freeze extends RaySpell {
    public Freeze() {
        this.soundValues(1.0f, 1.4f, 0.4f);
        this.hitLiquids(true);
        this.ignoreUncollidables(false);
    }

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        if (!ctx.world().isClientSide && BlockUtil.canPlaceBlock(ctx.caster(), ctx.world(), blockHit.getBlockPos())) {
            BlockUtil.freeze(ctx.world(), blockHit.getBlockPos(), true);
        }
        return true;
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        if (!(entityHit.getEntity() instanceof LivingEntity target)) return false;

        DamageSource source = ctx.caster() != null ? MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.FROST) : target.damageSources().magic();
        float damage = ctx.modifiers().get(SpellModifiers.POTENCY, property(DefaultProperties.DAMAGE));
        if (target instanceof Blaze || target instanceof MagmaCube) {
            target.hurt(source, damage * 2.0f);
        } else {
            target.hurt(source, Math.max(1.0f, damage * 0.5f));
        }

        if (ctx.world().isClientSide) return true;

        int dur = (int) (ctx.modifiers().get(SpellModifiers.DURATION, property(DefaultProperties.EFFECT_DURATION)));
        int str = property(DefaultProperties.EFFECT_STRENGTH);

        target.setTicksFrozen(target.getTicksRequiredToFreeze() + dur);
        target.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN, dur, Math.max(2, str + 1)));

        if (EBMobEffects.FROST != null && EBMobEffects.FROST.get() != null) {
            target.addEffect(new MobEffectInstance(EBMobEffects.FROST, dur, str));
        }
        if (target.isOnFire()) target.clearFire();

        if (ctx.world() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SNOWFLAKE, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 25, 0.4, 0.5, 0.4, 0.05);
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.ITEM_SNOWBALL, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 10, 0.3, 0.4, 0.3, 0.05);
        }

        return true;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return false;
    }

    @Override
    protected void spawnParticle(CastContext ctx, double x, double y, double z, double vx, double vy, double vz) {
        float brightness = 0.5f + (ctx.world().random.nextFloat() / 2);
        ParticleBuilder.create(EBParticles.SPARKLE.get()).pos(x, y, z).time(12 + ctx.world().random.nextInt(8))
                .color(brightness, brightness + 0.1f, 1.0f).spawn(ctx.world());
        ParticleBuilder.create(EBParticles.SNOW.get()).pos(x, y, z).spawn(ctx.world());
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.NOVICE, Elements.ICE, SpellTypes.ATTACK, SpellAction.POINT, 5, 0, 10)
                .add(DefaultProperties.RANGE, 10f)
                .add(DefaultProperties.DAMAGE, 3f)
                .add(DefaultProperties.EFFECT_DURATION, 200)
                .add(DefaultProperties.EFFECT_STRENGTH, 1)
                .build();
    }
}
