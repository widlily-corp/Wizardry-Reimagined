package com.binaris.wizardry.content.spells.lightning;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.RayTracer;
import com.binaris.wizardry.content.entity.construct.TitansHammerEntity;
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

public class TitansCataclysmSpell extends Spell {

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
            TitansHammerEntity hammer = new TitansHammerEntity(ctx.world(), tx, finalY, tz);
            hammer.setCaster(ctx.caster());
            ctx.world().addFreshEntity(hammer);

            ctx.world().playSound(null, tx, finalY, tz,
                    SoundEvents.TRIDENT_THUNDER.value(), SoundSource.WEATHER, 4.0f, 0.6f);
            ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                    SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.5f, 1.2f);
        } else {
            for (int i = 0; i < 16; i++) {
                double angle = (i * Math.PI / 8.0);
                double rx = tx + 5.0 * Math.cos(angle);
                double rz = tz + 5.0 * Math.sin(angle);
                ctx.world().addParticle(ParticleTypes.ELECTRIC_SPARK, rx, finalY + 0.1, rz, 0, 0.05, 0);
            }
        }

        ctx.caster().swing(ctx.hand(), true);
        return true;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.GRAND, Elements.LIGHTNING, SpellTypes.ATTACK, SpellAction.SUMMON, 75, 0, 300)
                .add(DefaultProperties.DAMAGE, 70.0F)
                .add(DefaultProperties.RANGE, 50.0F)
                .add(DefaultProperties.EFFECT_RADIUS, 14)
                .build();
    }
}
