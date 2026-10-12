package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Levitation extends Spell {
    public Levitation() {
        soundValues(0.5f, 1, 0);
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        LivingEntity caster = ctx.caster();
        caster.fallDistance = 0;

        // Try to find a target in front of the caster up to 16 blocks away
        Vec3 eyePos = caster.getEyePosition(1.0f);
        Vec3 lookVec = caster.getViewVector(1.0f);
        Vec3 reachVec = eyePos.add(lookVec.scale(16.0));
        HitResult hit = ProjectileUtil.getEntityHitResult(ctx.world(), caster, eyePos, reachVec,
                caster.getBoundingBox().expandTowards(lookVec.scale(16.0)).inflate(1.0),
                e -> e instanceof LivingEntity && e != caster && !e.isSpectator());

        LivingEntity subject = (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity target)
                ? target : caster;

        if (!ctx.world().isClientSide) {
            if (subject == caster && caster.isShiftKeyDown()) {
                // Sneaking allows smoothly cancelling levitation
                caster.removeEffect(MobEffects.LEVITATION);
                caster.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 60, 0));
            } else {
                subject.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 100, 1));
                subject.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 160, 0));
                subject.fallDistance = 0;
            }
        }

        if (ctx.world().isClientSide) {
            for (int i = 0; i < 3; i++) {
                double px = subject.getX() + (ctx.world().random.nextDouble() - 0.5) * subject.getBbWidth() * 1.5;
                double py = subject.getY() + ctx.world().random.nextDouble() * subject.getBbHeight();
                double pz = subject.getZ() + (ctx.world().random.nextDouble() - 0.5) * subject.getBbWidth() * 1.5;
                ctx.world().addParticle(ParticleTypes.PORTAL, px, py, pz, 0, 0.2, 0);
                ctx.world().addParticle(ParticleTypes.ENCHANTED_HIT, px, py, pz, 0, 0.1, 0);
            }
        }

        this.playSound(ctx.world(), subject, ctx.castingTicks(), -1);
        return true;
    }

    @Override
    protected void playSound(Level world, LivingEntity entity, int castTicks, int duration) {
        this.playSoundLoop(world, entity, castTicks);
    }

    @Override
    protected void playSound(Level world, double x, double y, double z, int ticksInUse, int duration) {
        this.playSoundLoop(world, x, y, z, ticksInUse, duration);
    }

    @Override
    public boolean isInstantCast() {
        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.ADVANCED, Elements.SORCERY, SpellTypes.UTILITY, SpellAction.NONE, 10, 0, 0)
                .add(DefaultProperties.ACCELERATION, 0.05F)
                .add(DefaultProperties.SPEED, 0.2F)
                .build();
    }
}
