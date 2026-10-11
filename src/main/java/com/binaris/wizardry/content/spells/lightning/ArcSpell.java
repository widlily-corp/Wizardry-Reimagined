package com.binaris.wizardry.content.spells.lightning;

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

public class ArcSpell extends Spell {

    public ArcSpell() {
        super(Elements.LIGHTNING, SpellTiers.ADVANCED, 20, 30, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_ARC.get(), SoundSource.PLAYERS, 1.0f, 1.2f);
        HitResult hit = SpellRayHelper.rayTrace(level, caster, 12.0);

        if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity target) {
            target.hurt(level.damageSources().lightningBolt(), 8.0f);
            if (level.isClientSide) {
                for (int i = 0; i < 10; i++) {
                    level.addParticle(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY() + 0.5, target.getZ(),
                            (level.random.nextDouble() - 0.5) * 0.4, (level.random.nextDouble() - 0.5) * 0.4, (level.random.nextDouble() - 0.5) * 0.4);
                }
            }
            return true;
        }
        return false;
    }
}
