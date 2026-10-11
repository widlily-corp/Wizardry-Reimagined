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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PhaseStepSpell extends Spell {

    public PhaseStepSpell() {
        super(Elements.SORCERY, SpellTiers.MASTER, 40, 80, 5);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        Vec3 eyePos = caster.getEyePosition();
        Vec3 look = caster.getLookAngle();
        double maxDist = 24.0;
        BlockPos safePos = null;

        for (double d = maxDist; d >= 4.0; d -= 1.0) {
            Vec3 testPoint = eyePos.add(look.scale(d));
            BlockPos bp = BlockPos.containing(testPoint);
            if (level.getBlockState(bp).isAir() && level.getBlockState(bp.above()).isAir() && !level.getBlockState(bp.below()).isAir()) {
                safePos = bp;
                break;
            }
        }

        if (safePos != null) {
            level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_PHASE_STEP.get(), SoundSource.PLAYERS, 1.2f, 1.0f);
            if (level.isClientSide) {
                for (int i = 0; i < 20; i++) {
                    level.addParticle(ParticleTypes.REVERSE_PORTAL, caster.getX(), caster.getY() + 1.0, caster.getZ(),
                            (level.random.nextDouble() - 0.5) * 0.4, 0.2, (level.random.nextDouble() - 0.5) * 0.4);
                }
            }
            caster.teleportTo(safePos.getX() + 0.5, safePos.getY(), safePos.getZ() + 0.5);
            level.playSound(null, safePos.getX(), safePos.getY(), safePos.getZ(), EBSounds.SPELL_PHASE_STEP.get(), SoundSource.PLAYERS, 1.2f, 1.0f);
            return true;
        }
        return false;
    }
}
