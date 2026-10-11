package com.binaris.wizardry.content.spells.ice;

import com.binaris.wizardry.content.entity.projectile.IceShardEntity;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class IceShardsSpell extends Spell {

    public IceShardsSpell() {
        super(Elements.ICE, SpellTiers.APPRENTICE, 10, 15, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_ICE_SHARD.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (!level.isClientSide) {
            IceShardEntity shard = new IceShardEntity(level, caster);
            level.addFreshEntity(shard);
        }
        return true;
    }
}
