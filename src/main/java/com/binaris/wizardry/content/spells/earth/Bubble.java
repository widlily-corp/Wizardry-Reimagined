package com.binaris.wizardry.content.spells.earth;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.entity.construct.BubbleConstruct;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Bubble extends RaySpell {

    public Bubble() {
        this.soundValues(0.5f, 1.1f, 0.2f);
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        if (!(entityHit.getEntity() instanceof LivingEntity target)) return true;
        if (ctx.world().isClientSide) return true;

        DamageSource source = ctx.caster() != null ? MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.SORCERY) : target.damageSources().magic();
        target.hurt(source, 1.0f);

        BubbleConstruct bubble = new BubbleConstruct(ctx.world());
        bubble.setPos(target.getX(), target.getY(), target.getZ());
        if (ctx.caster() != null) bubble.setCaster(ctx.caster());
        bubble.setLifetime(((int) (ctx.modifiers().get(SpellModifiers.DURATION, property(DefaultProperties.DURATION).floatValue()))));
        bubble.setDarkOrb(false);
        bubble.damageMultiplier = ctx.modifiers().getFactor(SpellModifiers.POTENCY);
        ctx.world().addFreshEntity(bubble);
        target.startRiding(bubble);

        return true;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return false;
    }

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        return false;
    }

    @Override
    protected void spawnParticle(CastContext ctx, double x, double y, double z, double vx, double vy, double vz) {
        if (ctx.world().isClientSide) {
            ctx.world().addParticle(ParticleTypes.SPLASH, x, y, z, 0, 0.05, 0);
            ctx.world().addParticle(ParticleTypes.BUBBLE_POP, x, y, z, vx * 0.1, vy * 0.1, vz * 0.1);
        } else if (ctx.world() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SPLASH, x, y, z, 2, 0.1, 0.1, 0.1, 0.02);
            serverLevel.sendParticles(ParticleTypes.BUBBLE_POP, x, y, z, 1, vx * 0.1, vy * 0.1, vz * 0.1, 0.02);
        }
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.EARTH, SpellTypes.ATTACK, SpellAction.POINT, 15, 0, 20)
                .add(DefaultProperties.RANGE, 10F)
                .add(DefaultProperties.DURATION, 200)
                .build();
    }
}
