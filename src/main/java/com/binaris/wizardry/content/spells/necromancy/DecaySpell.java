package com.binaris.wizardry.content.spells.necromancy;

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
import net.minecraft.world.phys.AABB;

import java.util.List;

public class DecaySpell extends Spell {

    public DecaySpell() {
        super(Elements.NECROMANCY, SpellTiers.ADVANCED, 30, 60, 5);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_DECAY.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        AABB area = caster.getBoundingBox().inflate(6.0);

        if (!level.isClientSide) {
            List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, e -> e != caster && !caster.isAlliedTo(e));
            for (LivingEntity target : targets) {
                target.hurt(level.damageSources().wither(), 6.0f);
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 160, 1));
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 1));
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 160, 1));
            }
        } else {
            for (int i = 0; i < 25; i++) {
                double px = caster.getX() + (level.random.nextDouble() - 0.5) * 12.0;
                double py = caster.getY() + level.random.nextDouble() * 3.0;
                double pz = caster.getZ() + (level.random.nextDouble() - 0.5) * 12.0;
                level.addParticle(ParticleTypes.SQUID_INK, px, py, pz, 0, 0.05, 0);
            }
        }
        return true;
    }
}
