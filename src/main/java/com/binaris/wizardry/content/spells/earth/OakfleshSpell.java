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

public class OakfleshSpell extends Spell {

    public OakfleshSpell() {
        super(Elements.EARTH, SpellTiers.NOVICE, 5, 15, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 0));
        caster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 0));
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_OAKFLESH.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (level.isClientSide) {
            for (int i = 0; i < 8; i++) {
                level.addParticle(ParticleTypes.HAPPY_VILLAGER,
                        caster.getX() + (level.random.nextDouble() - 0.5) * 0.8,
                        caster.getY() + 0.5 + level.random.nextDouble() * 0.8,
                        caster.getZ() + (level.random.nextDouble() - 0.5) * 0.8, 0, 0.1, 0);
            }
        }
        return true;
    }
}
