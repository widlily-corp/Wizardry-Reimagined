package com.binaris.wizardry.content.spells.fire;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Detonate extends RaySpell {
    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        if (ctx.world().isClientSide) {
            ctx.world().addParticle(ParticleTypes.EXPLOSION_EMITTER, blockHit.getBlockPos().getX() + 0.5, blockHit.getBlockPos().getY() + 0.5, blockHit.getBlockPos().getZ() + 0.5, 0, 0, 0);
            ctx.world().playSound(null, blockHit.getBlockPos(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE);
            return true;
        }

        double radius = ctx.modifiers().get(SpellModifiers.BLAST, property(DefaultProperties.BLAST_RADIUS));
        List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(radius,
                blockHit.getBlockPos().getX(), blockHit.getBlockPos().getY(), blockHit.getBlockPos().getZ(), ctx.world());

        for (LivingEntity target : targets) {
            float baseDamage = property(DefaultProperties.DAMAGE);
            double distSq = target.distanceToSqr(blockHit.getBlockPos().getX() + 0.5, blockHit.getBlockPos().getY() + 0.5, blockHit.getBlockPos().getZ() + 0.5);
            float actualDamage = Math.max(0, (baseDamage - (float) distSq * 4)) * ctx.modifiers().getFactor(SpellModifiers.POTENCY);
            target.hurt(ctx.caster() != null ? ctx.world().damageSources().indirectMagic(ctx.caster(), ctx.caster()) : ctx.world().damageSources().magic(), actualDamage);
        }
        return true;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return false;
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.FIRE, SpellTypes.ATTACK, SpellAction.POINT, 20, 0, 30)
                .add(DefaultProperties.RANGE, 15F)
                .add(DefaultProperties.DAMAGE, 12F)
                .add(DefaultProperties.BLAST_RADIUS, 3F)
                .build();
    }
}
