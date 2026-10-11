package com.binaris.wizardry.content.spells.magic;

import com.binaris.wizardry.content.entity.projectile.MagicMissileEntity;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class MagicMissileSpell extends Spell {

    public MagicMissileSpell() {
        super(Elements.MAGIC, SpellTiers.NOVICE, 5, 10, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_MAGIC_MISSILE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (!level.isClientSide) {
            MagicMissileEntity missile = new MagicMissileEntity(level, caster);
            level.addFreshEntity(missile);
        }
        return true;
    }
}
