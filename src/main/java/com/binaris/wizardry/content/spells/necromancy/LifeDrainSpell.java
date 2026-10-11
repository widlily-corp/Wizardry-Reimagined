package com.binaris.wizardry.content.spells.necromancy;

import com.binaris.wizardry.content.spells.SpellRayHelper;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class LifeDrainSpell extends Spell {

    public LifeDrainSpell() {
        super(Elements.NECROMANCY, SpellTiers.APPRENTICE, 15, 30, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        HitResult hit = SpellRayHelper.rayTrace(level, caster, 12.0);
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_LIFE_DRAIN_START.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity target) {
            float damage = 5.0f;
            target.hurt(level.damageSources().indirectMagic(caster, caster), damage);
            caster.heal(damage * 0.75f);

            if (level.isClientSide) {
                for (int i = 0; i < 8; i++) {
                    level.addParticle(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getY() + 0.5, target.getZ(), 0, 0, 0);
                    level.addParticle(ParticleTypes.HEART, caster.getX(), caster.getY() + 1.0, caster.getZ(), 0, 0, 0);
                }
            }
            return true;
        }
        return false;
    }
}
