package com.binaris.wizardry.content.spells.healing;

import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class CureEffectsSpell extends Spell {

    public CureEffectsSpell() {
        super(Elements.HEALING, SpellTiers.APPRENTICE, 10, 20, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        List<net.minecraft.core.Holder<MobEffect>> harmfulEffects = new ArrayList<>();
        for (MobEffectInstance instance : caster.getActiveEffects()) {
            if (instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                harmfulEffects.add(instance.getEffect());
            }
        }

        if (harmfulEffects.isEmpty() && !caster.isOnFire()) return false;

        for (net.minecraft.core.Holder<MobEffect> effect : harmfulEffects) {
            caster.removeEffect(effect);
        }
        if (caster.isOnFire()) caster.clearFire();

        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_CURE_EFFECTS.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (level.isClientSide) {
            for (int i = 0; i < 12; i++) {
                level.addParticle(ParticleTypes.HAPPY_VILLAGER, caster.getX() + (level.random.nextDouble() - 0.5) * 0.8,
                        caster.getY() + 1.0, caster.getZ() + (level.random.nextDouble() - 0.5) * 0.8, 0, 0.1, 0);
            }
        }
        return true;
    }
}
