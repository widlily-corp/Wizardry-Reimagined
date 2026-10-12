package com.binaris.wizardry.content.spells.fire;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.RayTracer;
import com.binaris.wizardry.content.entity.construct.SupernovaEntity;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import com.binaris.wizardry.api.content.util.BlockUtil;
import net.minecraft.core.BlockPos;

public class WrathOfHeliosSpell extends Spell {

    @Override
    public boolean isInstantCast() {
        return true;
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        Vec3 look = ctx.caster().getLookAngle();
        Vec3 origin = ctx.caster().getEyePosition();
        double maxRange = 50.0;
        Vec3 endpoint = origin.add(look.scale(maxRange));

        HitResult hit = RayTracer.rayTrace(ctx.world(), ctx.caster(), origin, endpoint, 0, false, net.minecraft.world.entity.Entity.class, RayTracer.ignoreEntityFilter(ctx.caster()));
        Vec3 targetPos = hit != null && hit.getType() != HitResult.Type.MISS ? hit.getLocation() : endpoint;

        // Snap target to ground floor if in air
        Integer floorY = BlockUtil.getNearestFloor(ctx.world(), BlockPos.containing(targetPos), 20);
        double finalY = floorY != null ? floorY : targetPos.y;

        double tx = targetPos.x;
        double tz = targetPos.z;

        if (!ctx.world().isClientSide) {
            SupernovaEntity supernova = new SupernovaEntity(ctx.world(), tx, finalY, tz);
            supernova.setCaster(ctx.caster());
            ctx.world().addFreshEntity(supernova);

            ctx.world().playSound(null, tx, finalY, tz,
                    SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 2.5f, 0.6f);
            ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                    SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.5f, 0.8f);
        } else {
            for (int i = 0; i < 16; i++) {
                double angle = (i * Math.PI / 8.0);
                double rx = tx + 4.5 * Math.cos(angle);
                double rz = tz + 4.5 * Math.sin(angle);
                ctx.world().addParticle(ParticleTypes.FLAME, rx, finalY + 0.1, rz, 0, 0.04, 0);
                ctx.world().addParticle(ParticleTypes.LAVA, rx, finalY + 0.1, rz, 0, 0.02, 0);
            }
        }

        ctx.caster().swing(ctx.hand(), true);
        return true;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.GRAND, Elements.FIRE, SpellTypes.ATTACK, SpellAction.SUMMON, 75, 0, 300)
                .add(DefaultProperties.DAMAGE, 65.0F)
                .add(DefaultProperties.RANGE, 50.0F)
                .add(DefaultProperties.EFFECT_RADIUS, 14)
                .add(DefaultProperties.DURATION, 20)
                .build();
    }
}
