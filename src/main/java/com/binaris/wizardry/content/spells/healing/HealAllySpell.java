package com.binaris.wizardry.content.spells.healing;

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

public class HealAllySpell extends Spell {

    public HealAllySpell() {
        super(Elements.HEALING, SpellTiers.APPRENTICE, 15, 25, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        EntityHitResult hit = SpellRayHelper.rayTraceEntity(level, caster, 16.0);
        if (hit != null && hit.getEntity() instanceof LivingEntity ally) {
            ally.heal(8.0f);
            level.playSound(null, ally.getX(), ally.getY(), ally.getZ(), EBSounds.SPELL_HEAL_ALLY.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            if (level.isClientSide) {
                for (int i = 0; i < 10; i++) {
                    level.addParticle(ParticleTypes.HEART, ally.getX() + (level.random.nextDouble() - 0.5) * 0.8,
                            ally.getY() + 1.0 + level.random.nextDouble() * 0.5,
                            ally.getZ() + (level.random.nextDouble() - 0.5) * 0.8, 0, 0.1, 0);
                }
            }
            return true;
        }
        return false;
    }
}
