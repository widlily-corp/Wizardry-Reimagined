package com.binaris.wizardry.content.spells.lightning;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LightningRailgunSpell extends Spell {

    @Override
    public boolean isInstantCast() {
        return false;
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        Vec3 look = ctx.caster().getLookAngle();
        Vec3 origin = ctx.caster().getEyePosition().subtract(0, 0.2, 0);
        double range = 35.0;
        Vec3 endpoint = origin.add(look.scale(range));

        // High recoil: slow down caster while channeling colossal plasma beam
        ctx.caster().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 1, false, false, false));

        // Audio cadence: continuous hum + periodic thunder crack
        if (ctx.castingTicks() % 8 == 1) {
            ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                    SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.2f, 1.8f);
            ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                    SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, 1.5f, 1.6f);
        }

        // Render blinding piercing beam of lightning plasma
        if (ctx.world().isClientSide) {
            for (double d = 1.0; d <= range; d += 0.8) {
                double pct = d / range;
                double px = origin.x + look.x * d + (ctx.world().random.nextDouble() - 0.5) * 0.25;
                double py = origin.y + look.y * d + (ctx.world().random.nextDouble() - 0.5) * 0.25;
                double pz = origin.z + look.z * d + (ctx.world().random.nextDouble() - 0.5) * 0.25;

                ctx.world().addParticle(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 0, 0, 0);
                if (ctx.world().random.nextInt(4) == 0) {
                    ctx.world().addParticle(ParticleTypes.FLASH, px, py, pz, 0, 0, 0);
                }
            }
        } else if (ctx.world() instanceof ServerLevel serverLevel) {
            // Periodic server particle beam synchronization
            if (ctx.castingTicks() % 2 == 0) {
                for (double d = 2.0; d <= range; d += 2.0) {
                    double px = origin.x + look.x * d;
                    double py = origin.y + look.y * d;
                    double pz = origin.z + look.z * d;
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 2, 0.15, 0.15, 0.15, 0.02);
                }
            }

            // Piercing damage cadence: every 4 ticks (~70 DPS)
            if (ctx.castingTicks() % 4 == 0) {
                AABB beamBox = new AABB(origin, endpoint).inflate(1.2);
                List<LivingEntity> potentialTargets = ctx.world().getEntitiesOfClass(LivingEntity.class, beamBox, e -> e != ctx.caster() && e.isAlive());

                for (LivingEntity target : potentialTargets) {
                    // Check distance from beam line
                    Vec3 toTarget = target.getBoundingBox().getCenter().subtract(origin);
                    double dot = toTarget.dot(look);
                    if (dot > 0 && dot <= range) {
                        Vec3 proj = origin.add(look.scale(dot));
                        double distSq = target.getBoundingBox().getCenter().distanceToSqr(proj);
                        if (distSq <= 2.25) { // within 1.5 blocks of beam axis
                            target.invulnerableTime = 0;
                            target.hurt(MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.SHOCK), 14.0f);

                            // Micro push
                            target.setDeltaMovement(target.getDeltaMovement().add(look.x * 0.2, 0.05, look.z * 0.2));
                            target.hurtMarked = true;
                            if (target instanceof ServerPlayer serverPlayer) {
                                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(target));
                            }

                            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 10, 0.3, 0.3, 0.3, 0.05);
                            serverLevel.sendParticles(ParticleTypes.FLASH, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 1, 0, 0, 0, 0);
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.GRAND, Elements.LIGHTNING, SpellTypes.ATTACK, SpellAction.POINT, 18, 0, 0)
                .add(DefaultProperties.DAMAGE, 14.0F)
                .add(DefaultProperties.RANGE, 35.0F)
                .build();
    }
}
