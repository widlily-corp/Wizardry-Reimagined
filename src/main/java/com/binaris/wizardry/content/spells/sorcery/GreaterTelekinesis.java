package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class GreaterTelekinesis extends RaySpell {

    public GreaterTelekinesis() {
        this.aimAssist(0.5f);
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        Entity target = entityHit.getEntity();
        if (target == null || target == ctx.caster()) return false;

        Level world = ctx.world();
        if (ctx.caster() instanceof Player player) {
            boolean sneaking = player.isShiftKeyDown();

            if (sneaking) {
                // Throw entity forward with high velocity
                Vec3 look = player.getLookAngle();
                target.setDeltaMovement(look.x * 2.0, look.y * 1.2 + 0.4, look.z * 2.0);
                target.hurtMarked = true;
                target.fallDistance = 0.0f;

                if (!world.isClientSide && EBSounds.SPELL_GREATER_TELEKINESIS_END != null) {
                    world.playSound(null, target.getX(), target.getY(), target.getZ(),
                            EBSounds.SPELL_GREATER_TELEKINESIS_END.get(), SoundSource.PLAYERS, 1.2f, 1.0f);
                }
            } else {
                // Lift and hold entity in the air in front of the caster
                Vec3 eyePos = player.getEyePosition();
                Vec3 look = player.getLookAngle();
                double targetDist = Math.min(6.0, Math.max(2.5, eyePos.distanceTo(target.position())));
                Vec3 desiredPos = eyePos.add(look.scale(targetDist));
                Vec3 toDesired = desiredPos.subtract(target.position());

                double pullStrength = 0.45;
                target.setDeltaMovement(toDesired.scale(pullStrength));
                target.fallDistance = 0.0f;
                target.hurtMarked = true;

                if (!world.isClientSide && EBSounds.SPELL_GREATER_TELEKINESIS_LOOP != null) {
                    if (world.getGameTime() % 10 == 0) {
                        world.playSound(null, target.getX(), target.getY(), target.getZ(),
                                EBSounds.SPELL_GREATER_TELEKINESIS_LOOP.get(), SoundSource.PLAYERS, 0.8f, 1.0f);
                    }
                }
            }

            if (world.isClientSide) {
                ParticleBuilder.create(EBParticles.SPARKLE)
                        .pos(target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ())
                        .color(0.6f, 0.3f, 0.9f)
                        .count(6)
                        .spread(0.4, 0.4, 0.4)
                        .spawn(world);
            }
            return true;
        }

        return false;
    }

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        BlockPos pos = blockHit.getBlockPos();
        Level world = ctx.world();
        BlockState state = world.getBlockState(pos);

        if (ctx.caster() instanceof Player player && !world.isClientSide) {
            if (state.getDestroySpeed(world, pos) < 0 || state.isAir()) return false;

            FallingBlockEntity fallingBlock = FallingBlockEntity.fall(world, pos, state);
            if (fallingBlock != null) {
                fallingBlock.time = 1;
                fallingBlock.dropItem = true;
                Vec3 look = player.getLookAngle();
                if (player.isShiftKeyDown()) {
                    fallingBlock.setDeltaMovement(look.x * 1.6, look.y * 1.0 + 0.3, look.z * 1.6);
                } else {
                    fallingBlock.setDeltaMovement(0, 0.5, 0);
                }

                if (EBSounds.SPELL_GREATER_TELEKINESIS_START != null) {
                    world.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            EBSounds.SPELL_GREATER_TELEKINESIS_START.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.MASTER, Elements.SORCERY, SpellTypes.UTILITY, SpellAction.POINT, 40, 10, 30)
                .add(DefaultProperties.RANGE, 16.0f)
                .build();
    }
}
