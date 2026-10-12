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
        return true;
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        Vec3 look = ctx.caster().getLookAngle();
        Vec3 origin = ctx.caster().getEyePosition().subtract(0, 0.15, 0);
        double range = 50.0;
        Vec3 endpoint = origin.add(look.scale(range));

        // Recoil kick on caster
        ctx.caster().setDeltaMovement(ctx.caster().getDeltaMovement().subtract(look.x * 0.35, 0, look.z * 0.35));
        ctx.caster().hurtMarked = true;

        // Blinding audio blast
        ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 2.5f, 1.6f);
        ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 2.0f, 1.4f);
        ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                SoundEvents.TRIDENT_RIPTIDE_3.value(), SoundSource.PLAYERS, 2.0f, 1.8f);

        // Client beam rendering
        if (ctx.world().isClientSide) {
            for (double d = 0.5; d <= range; d += 0.5) {
                double px = origin.x + look.x * d;
                double py = origin.y + look.y * d;
                double pz = origin.z + look.z * d;

                ctx.world().addParticle(ParticleTypes.ELECTRIC_SPARK, px, py, pz, (ctx.world().random.nextDouble() - 0.5) * 0.1, (ctx.world().random.nextDouble() - 0.5) * 0.1, (ctx.world().random.nextDouble() - 0.5) * 0.1);
                if (d % 2.0 < 0.6) {
                    ctx.world().addParticle(ParticleTypes.FLASH, px, py, pz, 0, 0, 0);
                }
                if (d % 4.0 < 0.6) {
                    ctx.world().addParticle(ParticleTypes.SONIC_BOOM, px, py, pz, 0, 0, 0);
                }
            }
        } else if (ctx.world() instanceof ServerLevel serverLevel) {
            // Server synchronized beam particles
            for (double d = 1.0; d <= range; d += 1.0) {
                double px = origin.x + look.x * d;
                double py = origin.y + look.y * d;
                double pz = origin.z + look.z * d;
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 4, 0.1, 0.1, 0.1, 0.05);
                if (d % 3.0 < 1.0) {
                    serverLevel.sendParticles(ParticleTypes.FLASH, px, py, pz, 1, 0, 0, 0, 0);
                }
            }

            // Piercing beam damage: annihilates all targets within 2.5 blocks of beam axis
            AABB beamBox = new AABB(origin, endpoint).inflate(2.5);
            List<LivingEntity> potentialTargets = ctx.world().getEntitiesOfClass(LivingEntity.class, beamBox, e -> e != ctx.caster() && e.isAlive());

            for (LivingEntity target : potentialTargets) {
                Vec3 toTarget = target.getBoundingBox().getCenter().subtract(origin);
                double dot = toTarget.dot(look);
                if (dot > 0 && dot <= range) {
                    Vec3 proj = origin.add(look.scale(dot));
                    double distSq = target.getBoundingBox().getCenter().distanceToSqr(proj);
                    if (distSq <= 6.25) { // Within 2.5 blocks radius
                        target.invulnerableTime = 0;
                        target.hurt(MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.SHOCK), 70.0f);

                        // High kinetic knockback
                        target.setDeltaMovement(target.getDeltaMovement().add(look.x * 0.8, 0.35, look.z * 0.8));
                        target.hurtMarked = true;
                        if (target instanceof ServerPlayer serverPlayer) {
                            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(target));
                        }

                        serverLevel.sendParticles(ParticleTypes.FLASH, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 2, 0, 0, 0, 0);
                        serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 25, 0.4, 0.4, 0.4, 0.15);
                    }
                }
            }

            // Detonation flash at endpoint
            serverLevel.sendParticles(ParticleTypes.FLASH, endpoint.x, endpoint.y, endpoint.z, 3, 0.2, 0.2, 0.2, 0);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, endpoint.x, endpoint.y, endpoint.z, 1, 0, 0, 0, 0);
            ctx.world().playSound(null, endpoint.x, endpoint.y, endpoint.z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 2.0f, 1.2f);
        }

        ctx.caster().swing(ctx.hand(), true);
        return true;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.GRAND, Elements.LIGHTNING, SpellTypes.ATTACK, SpellAction.POINT, 85, 0, 140)
                .add(DefaultProperties.DAMAGE, 70.0F)
                .add(DefaultProperties.RANGE, 50.0F)
                .build();
    }
}
