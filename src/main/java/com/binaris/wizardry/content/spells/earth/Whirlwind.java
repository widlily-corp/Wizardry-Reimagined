package com.binaris.wizardry.content.spells.earth;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Whirlwind extends RaySpell {
    public Whirlwind() {
        this.soundValues(0.8f, 0.7f, 0.2f);
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        if (entityHit.getEntity() instanceof LivingEntity target) {
            Vec3 dir = ctx.caster() != null ? ctx.caster().getLookAngle() : new Vec3(0, 1, 0);
            target.setDeltaMovement(target.getDeltaMovement().add(dir.x * 1.2, 0.4, dir.z * 1.2));
            target.hurtMarked = true;
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
        ctx.world().addParticle(ParticleTypes.CLOUD, x, y, z, vx * 0.1, vy * 0.1, vz * 0.1);
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.EARTH, SpellTypes.ATTACK, SpellAction.POINT, 15, 0, 20)
                .add(DefaultProperties.RANGE, 10F)
                .add(DefaultProperties.KNOCKBACK, 1.2F)
                .build();
    }
}
