package com.binaris.wizardry.content.spells.lightning;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Arc extends RaySpell {

    public Arc() {
        this.aimAssist(0.6f);
        this.soundValues(1, 1.7f, 0.2f);
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        if (!(entityHit.getEntity() instanceof LivingEntity target)) {
            return false;
        }

        if (ctx.world() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            Vec3 targetCenter = target.position().add(0, target.getBbHeight() / 2, 0);
            Vec3 delta = targetCenter.subtract(origin);
            double dist = delta.length();
            int steps = Math.max(6, (int) (dist * 4));
            for (int i = 0; i <= steps; i++) {
                double pct = (double) i / steps;
                double px = origin.x + delta.x * pct + (serverLevel.random.nextDouble() - 0.5) * 0.3;
                double py = origin.y + delta.y * pct + (serverLevel.random.nextDouble() - 0.5) * 0.3;
                double pz = origin.z + delta.z * pct + (serverLevel.random.nextDouble() - 0.5) * 0.3;
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK, px, py, pz, 1, 0, 0, 0, 0.02);
            }
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.FLASH, targetCenter.x, targetCenter.y, targetCenter.z, 1, 0, 0, 0, 0);
        } else if (ctx.world().isClientSide) {
            ParticleBuilder.create(EBParticles.LIGHTNING.get())
                    .entity(ctx.caster())
                    .pos(ctx.caster() != null ? origin.subtract(ctx.caster().position()) : origin)
                    .target(target)
                    .spawn(ctx.world());
            ParticleBuilder.spawnShockParticles(ctx.world(), target.xo, target.yo + target.getBbHeight() / 2, target.zo);
        }

        target.hurt(MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.SHOCK),
                ctx.modifiers().get(SpellModifiers.POTENCY, property(DefaultProperties.DAMAGE)));

        return true;
    }

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        return false;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return false;
    }

    @Override
    protected void spawnParticle(CastContext ctx, double x, double y, double z, double vx, double vy, double vz) {
        if (ctx.world().isClientSide) {
            ctx.world().addParticle(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK, x, y, z, (ctx.world().random.nextDouble() - 0.5) * 0.1, (ctx.world().random.nextDouble() - 0.5) * 0.1, (ctx.world().random.nextDouble() - 0.5) * 0.1);
        } else if (ctx.world() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK, x, y, z, 1, 0.05, 0.05, 0.05, 0.02);
        }
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.NOVICE, Elements.LIGHTNING, SpellTypes.ATTACK, SpellAction.POINT, 5, 0, 15)
                .add(DefaultProperties.RANGE, 3F)
                .add(DefaultProperties.DAMAGE, 8F)
                .build();
    }
}
