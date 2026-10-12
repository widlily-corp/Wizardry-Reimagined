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

public class TitansCataclysmSpell extends Spell {
    private static final int CHANNEL_TICKS_REQUIRED = 25; // 1.25 seconds chargeup

    @Override
    public boolean isInstantCast() {
        return false;
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        Vec3 look = ctx.caster().getLookAngle();
        Vec3 origin = ctx.caster().getEyePosition();
        double maxRange = 45.0;
        Vec3 endpoint = origin.add(look.scale(maxRange));

        HitResult hit = RayTracer.rayTrace(ctx.world(), ctx.caster(), origin, endpoint, 0, false, net.minecraft.world.entity.Entity.class, RayTracer.ignoreEntityFilter(ctx.caster()));
        Vec3 targetPos = hit != null && hit.getType() != HitResult.Type.MISS ? hit.getLocation() : endpoint;

        double tx = targetPos.x;
        double ty = targetPos.y + 0.1;
        double tz = targetPos.z;

        // Visual electric tempest targeting sigil on ground
        if (ctx.world().isClientSide) {
            float progress = (float) ctx.castingTicks() / (float) CHANNEL_TICKS_REQUIRED;
            float radius = 5.0f * Math.min(1.0f, progress + 0.25f);
            for (int i = 0; i < 8; i++) {
                float angle = (ctx.castingTicks() * 0.3f) + (float) (i * Math.PI / 4.0);
                ctx.world().addParticle(ParticleTypes.ELECTRIC_SPARK, tx + radius * Mth.cos(angle), ty, tz + radius * Mth.sin(angle), 0, 0.05, 0);
            }
        }

        if (ctx.castingTicks() % 6 == 1) {
            ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                    SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.9f, 1.2f + (ctx.castingTicks() * 0.04f));
        }

        // On full charge: call down Titan's Cataclysm!
        if (ctx.castingTicks() >= CHANNEL_TICKS_REQUIRED) {
            if (!ctx.world().isClientSide) {
                TitansHammerEntity hammer = new TitansHammerEntity(ctx.world(), tx, targetPos.y, tz);
                hammer.setCaster(ctx.caster());
                ctx.world().addFreshEntity(hammer);

                ctx.world().playSound(null, tx, targetPos.y, tz,
                        SoundEvents.TRIDENT_THUNDER.value(), SoundSource.WEATHER, 4.0f, 0.6f);
            }
            return true;
        }

        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.GRAND, Elements.LIGHTNING, SpellTypes.ATTACK, SpellAction.SUMMON, 75, 330, CHANNEL_TICKS_REQUIRED)
                .add(DefaultProperties.DAMAGE, 60.0F)
                .add(DefaultProperties.RANGE, 45.0F)
                .add(DefaultProperties.EFFECT_RADIUS, 12)
                .build();
    }
}
