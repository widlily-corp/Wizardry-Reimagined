package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LeapSpell extends Spell {

    public LeapSpell() {
        super(Elements.SORCERY, SpellTiers.NOVICE, 5, 15, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        Vec3 look = caster.getLookAngle();
        Vec3 motion = new Vec3(look.x * 1.5, Math.max(0.6, look.y * 1.2), look.z * 1.5);
        caster.setDeltaMovement(motion);
        caster.hurtMarked = true;

        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_LEAP.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (level.isClientSide) {
            for (int i = 0; i < 12; i++) {
                level.addParticle(ParticleTypes.CLOUD,
                        caster.getX() + (level.random.nextDouble() - 0.5) * 0.8,
                        caster.getY() + 0.1,
                        caster.getZ() + (level.random.nextDouble() - 0.5) * 0.8, 0, 0.1, 0);
            }
        }
        return true;
    }
}
