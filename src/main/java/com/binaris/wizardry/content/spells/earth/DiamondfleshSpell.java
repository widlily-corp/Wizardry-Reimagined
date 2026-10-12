package com.binaris.wizardry.content.spells.earth;

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

public class DiamondfleshSpell extends Spell {

    public DiamondfleshSpell() {
        super(Elements.EARTH, SpellTiers.ADVANCED, 20, 50, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 2));
        caster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 0));
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_DIAMONDFLESH.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (level.isClientSide) {
            for (int i = 0; i < 16; i++) {
                level.addParticle(ParticleTypes.ENCHANTED_HIT,
                        caster.getX() + (level.random.nextDouble() - 0.5) * 1.0,
                        caster.getY() + 0.5 + level.random.nextDouble() * 1.0,
                        caster.getZ() + (level.random.nextDouble() - 0.5) * 1.0, 0, 0.15, 0);
            }
        }
        return true;
    }
}
