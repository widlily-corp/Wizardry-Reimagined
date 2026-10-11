package com.binaris.wizardry.content.spells.ice;

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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class FreezeSpell extends Spell {

    public FreezeSpell() {
        super(Elements.ICE, SpellTiers.NOVICE, 5, 10, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        HitResult hit = SpellRayHelper.rayTrace(level, caster, 12.0);
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_FREEZE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity target) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 2));
            target.setTicksFrozen(Math.min(target.getTicksRequiredToFreeze() + 140, target.getTicksFrozen() + 140));
            target.hurt(level.damageSources().freeze(), 3.0f);
            if (target.isOnFire()) target.clearFire();
            return true;
        } else if (hit instanceof BlockHitResult blockHit) {
            BlockPos pos = blockHit.getBlockPos();
            if (level.getBlockState(pos).is(Blocks.WATER)) {
                if (!level.isClientSide) level.setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
                return true;
            } else if (level.getBlockState(pos).is(Blocks.LAVA)) {
                if (!level.isClientSide) level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
                return true;
            }
        }
        return false;
    }
}
