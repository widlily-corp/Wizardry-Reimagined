package com.binaris.wizardry.content.spells.necromancy;

import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SummonZombieSpell extends Spell {

    public SummonZombieSpell() {
        super(Elements.NECROMANCY, SpellTiers.NOVICE, 10, 40, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_SUMMON_ZOMBIE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (!level.isClientSide) {
            Vec3 spawnPos = caster.position().add(caster.getLookAngle().scale(2.0));
            Zombie zombie = new Zombie(EntityType.ZOMBIE, level);
            zombie.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            zombie.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
            level.addFreshEntity(zombie);
        }
        return true;
    }
}
