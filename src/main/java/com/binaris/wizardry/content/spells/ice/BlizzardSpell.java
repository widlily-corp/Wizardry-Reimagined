package com.binaris.wizardry.content.spells.ice;

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

public class BlizzardSpell extends Spell {

    public BlizzardSpell() {
        super(Elements.ICE, SpellTiers.MASTER, 45, 90, 10);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_BLIZZARD.get(), SoundSource.PLAYERS, 1.2f, 1.0f);
        AABB area = caster.getBoundingBox().inflate(10.0);

        if (!level.isClientSide) {
            List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, e -> e != caster && !caster.isAlliedTo(e));
            for (LivingEntity target : targets) {
                target.hurt(level.damageSources().freeze(), 12.0f);
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 3));
                target.setTicksFrozen(300);
            }
        } else {
            for (int i = 0; i < 40; i++) {
                double px = caster.getX() + (level.random.nextDouble() - 0.5) * 16.0;
                double py = caster.getY() + level.random.nextDouble() * 5.0;
                double pz = caster.getZ() + (level.random.nextDouble() - 0.5) * 16.0;
                level.addParticle(ParticleTypes.SNOWFLAKE, px, py, pz, 0, -0.1, 0);
            }
        }
        return true;
    }
}
