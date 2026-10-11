package com.binaris.wizardry.content.spells.magic;

import com.binaris.wizardry.content.spells.SpellRayHelper;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class DetonateSpell extends Spell {

    public DetonateSpell() {
        super(Elements.MAGIC, SpellTiers.ADVANCED, 25, 40, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        HitResult hit = SpellRayHelper.rayTrace(level, caster, 16.0);
        Vec3 hitPos = hit.getLocation();

        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_DETONATE.get(), SoundSource.PLAYERS, 1.2f, 1.0f);
        if (!level.isClientSide) {
            level.explode(caster, hitPos.x, hitPos.y, hitPos.z, 3.0f, Level.ExplosionInteraction.NONE);
        }
        return true;
    }
}
