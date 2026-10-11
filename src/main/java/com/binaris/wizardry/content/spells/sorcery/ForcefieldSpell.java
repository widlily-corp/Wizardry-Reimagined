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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ForcefieldSpell extends Spell {

    public ForcefieldSpell() {
        super(Elements.SORCERY, SpellTiers.ADVANCED, 25, 40, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_FORCEFIELD.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        AABB area = caster.getBoundingBox().inflate(6.0);

        if (!level.isClientSide) {
            List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area,
                    e -> e != caster && !caster.isAlliedTo(e));
            for (LivingEntity target : targets) {
                Vec3 pushDir = target.position().subtract(caster.position()).normalize().scale(1.5);
                target.push(pushDir.x, 0.4, pushDir.z);
                target.hurt(level.damageSources().indirectMagic(caster, caster), 4.0f);
            }
        } else {
            for (int i = 0; i < 20; i++) {
                double angle = i * (Math.PI * 2 / 20);
                double px = caster.getX() + Math.cos(angle) * 4.0;
                double pz = caster.getZ() + Math.sin(angle) * 4.0;
                level.addParticle(ParticleTypes.ENCHANT, px, caster.getY() + 1.0, pz, 0, 0.2, 0);
            }
        }
        return true;
    }
}
