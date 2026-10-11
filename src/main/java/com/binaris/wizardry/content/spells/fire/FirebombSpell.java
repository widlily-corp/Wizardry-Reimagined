package com.binaris.wizardry.content.spells.fire;

import com.binaris.wizardry.content.entity.projectile.FirebombEntity;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FirebombSpell extends Spell {

    public FirebombSpell() {
        super(Elements.FIRE, SpellTiers.APPRENTICE, 15, 25, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.ENTITY_FIREBOMB_THROW.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (!level.isClientSide) {
            FirebombEntity firebomb = new FirebombEntity(level, caster);
            level.addFreshEntity(firebomb);
        }
        return true;
    }
}
