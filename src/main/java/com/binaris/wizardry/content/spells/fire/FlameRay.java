package com.binaris.wizardry.content.spells.fire;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FlameRay extends RaySpell {
    public FlameRay() {
        this.particleVelocity(1);
        this.particleSpacing(0.5);
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        if (entityHit.getEntity() instanceof LivingEntity target && ctx.castingTicks() % Math.max(1, target.invulnerableDuration) == 1) {
            int duration = (int) (ctx.modifiers().get(SpellModifiers.DURATION, property(DefaultProperties.EFFECT_DURATION)));
            target.igniteForSeconds(duration);
            float damage = ctx.modifiers().get(SpellModifiers.POTENCY, property(DefaultProperties.DAMAGE));
            target.hurt(ctx.caster() != null ? ctx.world().damageSources().indirectMagic(ctx.caster(), ctx.caster()) : ctx.world().damageSources().magic(), damage);
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
    public boolean isInstantCast() {
        return false;
    }

    @Override
    protected void spawnParticle(CastContext ctx, double x, double y, double z, double vx, double vy, double vz) {
        ParticleBuilder.create(EBParticles.MAGIC_FIRE.get()).pos(x, y, z).velocity(vx, vy, vz).time(10).spawn(ctx.world());
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.FIRE, SpellTypes.ATTACK, SpellAction.POINT, 5, 0, 0)
                .add(DefaultProperties.RANGE, 10F)
                .add(DefaultProperties.DAMAGE, 3F)
                .add(DefaultProperties.EFFECT_DURATION, 10)
                .build();
    }
}
