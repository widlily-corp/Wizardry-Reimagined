package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BlinkSpell extends Spell {

    public BlinkSpell() {
        super(Elements.SORCERY, SpellTiers.APPRENTICE, 15, 20, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        Vec3 eyePos = caster.getEyePosition();
        Vec3 look = caster.getLookAngle();
        double distance = 12.0;
        Vec3 target = eyePos.add(look.scale(distance));

        BlockHitResult hit = level.clip(new ClipContext(eyePos, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, caster));
        if (hit.getType() != HitResult.Type.MISS) {
            target = hit.getLocation().subtract(look.scale(0.5));
        }

        BlockPos targetBlock = BlockPos.containing(target);
        if (!level.getBlockState(targetBlock).isAir() && !level.getBlockState(targetBlock.above()).isAir()) {
            targetBlock = targetBlock.above();
        }

        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_BLINK.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (level.isClientSide) {
            for (int i = 0; i < 16; i++) {
                level.addParticle(ParticleTypes.PORTAL, caster.getX(), caster.getY() + 1.0, caster.getZ(),
                        (level.random.nextDouble() - 0.5) * 0.5, (level.random.nextDouble() - 0.5) * 0.5, (level.random.nextDouble() - 0.5) * 0.5);
            }
        }

        caster.teleportTo(target.x, target.y, target.z);
        level.playSound(null, target.x, target.y, target.z, EBSounds.SPELL_BLINK.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        return true;
    }
}
