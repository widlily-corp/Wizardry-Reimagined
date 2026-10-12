package com.binaris.wizardry.content.spells.lightning;

import com.binaris.wizardry.content.spells.SpellRayHelper;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class LightningBoltSpell extends Spell {

    public LightningBoltSpell() {
        super(Elements.LIGHTNING, SpellTiers.APPRENTICE, 15, 30, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        HitResult hit = SpellRayHelper.rayTrace(level, caster, 25.0);
        if (hit.getType() == HitResult.Type.MISS) return false;

        Vec3 pos = hit.getLocation();
        if (level instanceof ServerLevel serverLevel) {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
            if (bolt != null) {
                bolt.moveTo(pos);
                bolt.setVisualOnly(false);
                serverLevel.addFreshEntity(bolt);
            }
        }

        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_LIGHTNING_BOLT.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        return true;
    }
}
