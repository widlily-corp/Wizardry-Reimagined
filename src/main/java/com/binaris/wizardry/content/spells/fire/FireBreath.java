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
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FireBreath extends RaySpell {
    public FireBreath() {
        this.particleVelocity(1);
        this.particleJitter(0.3);
        this.particleSpacing(0.25);
    }

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        BlockPos pos = blockHit.getBlockPos().relative(blockHit.getDirection());
        if (ctx.world().isEmptyBlock(pos)) {
            if (!ctx.world().isClientSide) ctx.world().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
            return true;
        }
        return false;
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        if (entityHit.getEntity() instanceof LivingEntity target) {
            if (ctx.castingTicks() % 6 == 1 || ctx.castingTicks() <= 1) {
                int duration = (int) (ctx.modifiers().get(SpellModifiers.DURATION, property(DefaultProperties.EFFECT_DURATION)));
                target.igniteForSeconds(duration);
                float damage = ctx.modifiers().get(SpellModifiers.POTENCY, property(DefaultProperties.DAMAGE));
                target.invulnerableTime = 0;
                target.hurt(ctx.caster() != null ? ctx.world().damageSources().indirectMagic(ctx.caster(), ctx.caster()) : ctx.world().damageSources().inFire(), damage);
            }
        }
        return true;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return true;
    }

    @Override
    public boolean isInstantCast() {
        return false;
    }

    @Override
    protected void spawnParticle(CastContext ctx, double x, double y, double z, double vx, double vy, double vz) {
        if (ctx.world().isClientSide) {
            ctx.world().addParticle(net.minecraft.core.particles.ParticleTypes.FLAME, x, y, z, vx * 0.15, vy * 0.15, vz * 0.15);
            if (ctx.world().random.nextInt(2) == 0) {
                ctx.world().addParticle(net.minecraft.core.particles.ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, vx * 0.05, vy * 0.05, vz * 0.05);
            }
        } else if (ctx.world() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.FLAME, x, y, z, 1, vx * 0.15, vy * 0.15, vz * 0.15, 0.02);
        }
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.ADVANCED, Elements.FIRE, SpellTypes.ATTACK, SpellAction.POINT, 8, 0, 0)
                .add(DefaultProperties.RANGE, 12F)
                .add(DefaultProperties.DAMAGE, 5F)
                .add(DefaultProperties.EFFECT_DURATION, 15)
                .build();
    }
}
