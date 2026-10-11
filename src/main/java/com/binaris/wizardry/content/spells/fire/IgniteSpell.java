package com.binaris.wizardry.content.spells.fire;

import com.binaris.wizardry.content.spells.SpellRayHelper;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class IgniteSpell extends Spell {

    public IgniteSpell() {
        super(Elements.FIRE, SpellTiers.NOVICE, 5, 10, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        HitResult hit = SpellRayHelper.rayTrace(level, caster, 12.0);
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_IGNITE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity target) {
            target.igniteForSeconds(8);
            if (level.isClientSide) {
                for (int i = 0; i < 8; i++) {
                    level.addParticle(ParticleTypes.FLAME, target.getX(), target.getY() + 0.5, target.getZ(),
                            (level.random.nextDouble() - 0.5) * 0.2, 0.1, (level.random.nextDouble() - 0.5) * 0.2);
                }
            }
            return true;
        } else if (hit instanceof BlockHitResult blockHit) {
            BlockPos pos = blockHit.getBlockPos().relative(blockHit.getDirection());
            if (level.isEmptyBlock(pos)) {
                if (!level.isClientSide) {
                    level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                }
                return true;
            }
        }
        return false;
    }
}
