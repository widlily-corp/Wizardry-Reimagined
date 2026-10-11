package com.binaris.wizardry.content.spells.magic;

import com.binaris.wizardry.content.spells.SpellRayHelper;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class LightSpell extends Spell {

    public LightSpell() {
        super(Elements.MAGIC, SpellTiers.NOVICE, 5, 10, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        HitResult hit = SpellRayHelper.rayTrace(level, caster, 12.0);
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_LIGHT.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        if (hit instanceof BlockHitResult blockHit) {
            BlockPos targetPos = blockHit.getBlockPos().relative(blockHit.getDirection());
            if (level.isEmptyBlock(targetPos)) {
                if (!level.isClientSide) {
                    level.setBlockAndUpdate(targetPos, Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 15));
                }
                return true;
            }
        }

        caster.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 600, 0));
        if (level.isClientSide) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.END_ROD, caster.getX(), caster.getY() + 1.2, caster.getZ(), 0, 0.05, 0);
            }
        }
        return true;
    }
}
