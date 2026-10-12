package com.binaris.wizardry.content.spells.necromancy;

import com.binaris.wizardry.content.spells.SpellRayHelper;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class WitherSpell extends Spell {

    public WitherSpell() {
        super(Elements.NECROMANCY, SpellTiers.APPRENTICE, 15, 30, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        EntityHitResult hit = SpellRayHelper.rayTraceEntity(level, caster, 20.0);
        if (hit == null || !(hit.getEntity() instanceof LivingEntity target)) return false;

        target.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 1));
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));

        level.playSound(null, target.getX(), target.getY(), target.getZ(), EBSounds.SPELL_WITHER.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (level.isClientSide) {
            for (int i = 0; i < 15; i++) {
                level.addParticle(ParticleTypes.SMOKE,
                        target.getX() + (level.random.nextDouble() - 0.5) * 0.8,
                        target.getY() + level.random.nextDouble() * 1.5,
                        target.getZ() + (level.random.nextDouble() - 0.5) * 0.8, 0, 0.05, 0);
            }
        }
        return true;
    }
}
