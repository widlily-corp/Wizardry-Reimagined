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
        double maxRange = 50.0;
        Vec3 maxEndpoint = origin.add(look.scale(maxRange));

        // Raycast to find physical block impact point
        net.minecraft.world.phys.HitResult hit = ctx.world().clip(new net.minecraft.world.level.ClipContext(
                origin, maxEndpoint, net.minecraft.world.level.ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, ctx.caster()));
        Vec3 endpoint = (hit.getType() != net.minecraft.world.phys.HitResult.Type.MISS) ? hit.getLocation() : maxEndpoint;
        double actualRange = origin.distanceTo(endpoint);

        // Heavy kinetic recoil kick on caster
        ctx.caster().setDeltaMovement(ctx.caster().getDeltaMovement().subtract(look.x * 0.45, 0, look.z * 0.45));
        ctx.caster().hurtMarked = true;

        // Colossal blinding audio blast at caster
        ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 3.5f, 1.3f);
        ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 3.0f, 1.1f);
        ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 2.5f, 1.8f);
        ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                SoundEvents.TRIDENT_THUNDER.value(), SoundSource.PLAYERS, 3.0f, 1.4f);

        // Client dense plasma beam rendering
        if (ctx.world().isClientSide) {
            for (double d = 0.4; d <= actualRange; d += 0.4) {
                double px = origin.x + look.x * d;
                double py = origin.y + look.y * d;
                double pz = origin.z + look.z * d;

                ctx.world().addParticle(ParticleTypes.ELECTRIC_SPARK, px, py, pz,
                        (ctx.world().random.nextDouble() - 0.5) * 0.15,
                        (ctx.world().random.nextDouble() - 0.5) * 0.15,
                        (ctx.world().random.nextDouble() - 0.5) * 0.15);
                if (d % 1.6 < 0.4) {
                    ctx.world().addParticle(ParticleTypes.FLASH, px, py, pz, 0, 0, 0);
                }
                if (d % 3.6 < 0.4) {
                    ctx.world().addParticle(ParticleTypes.SONIC_BOOM, px, py, pz, 0, 0, 0);
                }
            }
        } else if (ctx.world() instanceof ServerLevel serverLevel) {
            // Server synchronized beam particles
            for (double d = 0.8; d <= actualRange; d += 0.8) {
                double px = origin.x + look.x * d;
                double py = origin.y + look.y * d;
                double pz = origin.z + look.z * d;
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 5, 0.12, 0.12, 0.12, 0.06);
                if (d % 2.4 < 0.8) {
                    serverLevel.sendParticles(ParticleTypes.FLASH, px, py, pz, 1, 0, 0, 0, 0);
                }
            }

            // Piercing beam damage: obliterates all targets within 2.5 blocks of beam axis
            AABB beamBox = new AABB(origin, endpoint).inflate(2.5);
            List<LivingEntity> potentialTargets = ctx.world().getEntitiesOfClass(LivingEntity.class, beamBox, e -> e != ctx.caster() && e.isAlive());

            for (LivingEntity target : potentialTargets) {
                Vec3 toTarget = target.getBoundingBox().getCenter().subtract(origin);
                double dot = toTarget.dot(look);
                if (dot > 0 && dot <= actualRange) {
                    Vec3 proj = origin.add(look.scale(dot));
                    double distSq = target.getBoundingBox().getCenter().distanceToSqr(proj);
                    if (distSq <= 6.25) { // Within 2.5 blocks radius
                        target.invulnerableTime = 0;
                        target.hurt(MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.SHOCK), 75.0f);
                        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 3));
                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 120, 2));

                        // High kinetic knockback along the line of fire
                        target.setDeltaMovement(target.getDeltaMovement().add(look.x * 1.1, 0.45, look.z * 1.1));
                        target.hurtMarked = true;
                        if (target instanceof ServerPlayer serverPlayer) {
                            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(target));
                        }

                        serverLevel.sendParticles(ParticleTypes.FLASH, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 3, 0, 0, 0, 0);
                        serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 35, 0.5, 0.5, 0.5, 0.2);
                    }
                }
            }

            // ==========================================
            // Terminal Detonation & Lightning Strike
            // ==========================================
            serverLevel.sendParticles(ParticleTypes.FLASH, endpoint.x, endpoint.y, endpoint.z, 6, 0.3, 0.3, 0.3, 0);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, endpoint.x, endpoint.y, endpoint.z, 2, 0, 0, 0, 0);
            serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, endpoint.x, endpoint.y, endpoint.z, 3, 0.2, 0.2, 0.2, 0);
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, endpoint.x, endpoint.y, endpoint.z, 80, 2.0, 2.0, 2.0, 0.25);

            ctx.world().playSound(null, endpoint.x, endpoint.y, endpoint.z, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 4.0f, 1.0f);
            ctx.world().playSound(null, endpoint.x, endpoint.y, endpoint.z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 3.5f, 0.8f);

            // Spawn visual lightning bolt at impact point
            net.minecraft.world.entity.LightningBolt bolt = net.minecraft.world.entity.EntityType.LIGHTNING_BOLT.create(serverLevel);
            if (bolt != null) {
                bolt.moveTo(endpoint.x, endpoint.y, endpoint.z);
                bolt.setVisualOnly(true);
                serverLevel.addFreshEntity(bolt);
            }

            // Terminal AOE blast: 35 damage to surrounding entities
            List<LivingEntity> splashTargets = ctx.world().getEntitiesOfClass(LivingEntity.class, new AABB(endpoint.x - 5, endpoint.y - 5, endpoint.z - 5, endpoint.x + 5, endpoint.y + 5, endpoint.z + 5));
            for (LivingEntity splashTarget : splashTargets) {
                if (splashTarget != ctx.caster() && splashTarget.isAlive()) {
                    splashTarget.hurt(MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.SHOCK), 35.0f);
                    Vec3 push = splashTarget.position().subtract(endpoint).normalize().scale(1.2).add(0, 0.4, 0);
                    splashTarget.setDeltaMovement(splashTarget.getDeltaMovement().add(push));
                    splashTarget.hurtMarked = true;
                }
            }

            // Fulgurite Terrain Vitrification Scars
            net.minecraft.core.BlockPos centerImpact = net.minecraft.core.BlockPos.containing(endpoint);
            boolean canGrief = com.binaris.wizardry.api.content.util.EntityUtil.canDamageBlocks(ctx.caster(), ctx.world());
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    if (dx * dx + dz * dz <= 5) {
                        for (int dy = -1; dy <= 1; dy++) {
                            net.minecraft.core.BlockPos bpos = centerImpact.offset(dx, dy, dz);
                            net.minecraft.world.level.block.state.BlockState bstate = ctx.world().getBlockState(bpos);

                            if (bstate.getBlock() == net.minecraft.world.level.block.Blocks.SAND || bstate.getBlock() == net.minecraft.world.level.block.Blocks.RED_SAND) {
                                if (canGrief) ctx.world().setBlockAndUpdate(bpos, net.minecraft.world.level.block.Blocks.GLASS.defaultBlockState());
                            } else if (bstate.getBlock() == net.minecraft.world.level.block.Blocks.WATER) {
                                ctx.world().setBlockAndUpdate(bpos, net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());
                            } else if (canGrief && (bstate.getBlock() == net.minecraft.world.level.block.Blocks.GRASS_BLOCK || bstate.getBlock() == net.minecraft.world.level.block.Blocks.DIRT)) {
                                ctx.world().setBlockAndUpdate(bpos, net.minecraft.world.level.block.Blocks.SMOOTH_BASALT.defaultBlockState());
                            }
                        }
                    }
                }
            }

            // Lingering Ionized Plasma Wake Field (8 seconds)
            net.minecraft.world.entity.AreaEffectCloud cloud = new net.minecraft.world.entity.AreaEffectCloud(ctx.world(), endpoint.x, endpoint.y, endpoint.z);
            cloud.setOwner(ctx.caster());
            cloud.setRadius(4.5f);
            cloud.setRadiusOnUse(-0.1f);
            cloud.setWaitTime(0);
            cloud.setDuration(160); // 8 seconds
            cloud.setParticle(ParticleTypes.ELECTRIC_SPARK);
            cloud.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 3, false, false));
            cloud.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2, false, false));
            ctx.world().addFreshEntity(cloud);
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
