package com.binaris.wizardry.content.spells.earth;

import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GrowthAuraSpell extends Spell {

    public GrowthAuraSpell() {
        super(Elements.EARTH, SpellTiers.APPRENTICE, 15, 30, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        BlockPos center = caster.blockPosition();
        int radius = 5;
        int grownCount = 0;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -2; y <= 2; y++) {
                    BlockPos target = center.offset(x, y, z);
                    BlockState state = level.getBlockState(target);
                    if (state.getBlock() instanceof BonemealableBlock bonemealable) {
                        if (bonemealable.isValidBonemealTarget(level, target, state)) {
                            if (level instanceof ServerLevel serverLevel) {
                                if (bonemealable.isBonemealSuccess(level, level.random, target, state)) {
                                    bonemealable.performBonemeal(serverLevel, level.random, target, state);
                                    grownCount++;
                                }
                            } else {
                                level.addParticle(ParticleTypes.HAPPY_VILLAGER,
                                        target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5, 0, 0.1, 0);
                            }
                        }
                    }
                }
            }
        }

        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_GROWTH_AURA.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        return true;
    }
}
