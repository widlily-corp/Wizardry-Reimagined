package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.BlockUtil;
import com.binaris.wizardry.api.content.util.RayTracer;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Blink extends Spell {

    @Override
    public boolean cast(PlayerCastContext ctx) {
        float range = property(DefaultProperties.RANGE) * ctx.modifiers().getFactor(SpellModifiers.RANGE);
        HitResult hit = RayTracer.standardBlockRayTrace(ctx.world(), ctx.caster(), range, false, true, false);

        Vec3 targetPos;
        if (hit != null && hit.getType() == HitResult.Type.BLOCK && hit instanceof BlockHitResult blockHit) {
            BlockPos targetBlock = blockHit.getBlockPos().relative(blockHit.getDirection());
            if (!BlockUtil.isBlockPassable(ctx.world(), targetBlock)) {
                targetBlock = targetBlock.above();
            }
            targetPos = new Vec3(targetBlock.getX() + 0.5, targetBlock.getY(), targetBlock.getZ() + 0.5);
        } else {
            Vec3 look = ctx.caster().getLookAngle();
            targetPos = ctx.caster().position().add(look.scale(range));
            BlockPos pos = BlockPos.containing(targetPos);
            Integer floor = BlockUtil.getNearestFloor(ctx.world(), pos, 4);
            if (floor != null) {
                targetPos = new Vec3(pos.getX() + 0.5, floor + 1, pos.getZ() + 0.5);
            }
        }

        if (ctx.world().isClientSide) {
            for (int i = 0; i < 15; i++) {
                double x = ctx.caster().getX();
                double y = ctx.caster().getY() + 2 * ctx.world().random.nextFloat();
                double z = ctx.caster().getZ();
                ctx.world().addParticle(ParticleTypes.PORTAL, x, y, z,
                        ctx.world().random.nextDouble() - 0.5,
                        ctx.world().random.nextDouble() - 0.5,
                        ctx.world().random.nextDouble() - 0.5);
            }
        } else {
            if (ctx.caster() instanceof ServerPlayer sp) {
                sp.teleportTo(targetPos.x, targetPos.y, targetPos.z);
            } else {
                ctx.caster().setPos(targetPos.x, targetPos.y, targetPos.z);
            }
            ctx.world().playSound(null, targetPos.x, targetPos.y, targetPos.z,
                    SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
        }

        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.SORCERY, SpellTypes.UTILITY, SpellAction.POINT, 15, 0, 30)
                .add(DefaultProperties.RANGE, 16F)
                .build();
    }
}
