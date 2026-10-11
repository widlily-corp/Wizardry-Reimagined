package com.binaris.wizardry.content.spells.earth;

import com.binaris.wizardry.content.entity.projectile.PoisonBombEntity;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PoisonSpraySpell extends Spell {

    public PoisonSpraySpell() {
        super(Elements.EARTH, SpellTiers.APPRENTICE, 15, 25, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.ENTITY_POISON_BOMB_THROW.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (!level.isClientSide) {
            PoisonBombEntity bomb = new PoisonBombEntity(level, caster);
            level.addFreshEntity(bomb);
        }
        return true;
    }
}
