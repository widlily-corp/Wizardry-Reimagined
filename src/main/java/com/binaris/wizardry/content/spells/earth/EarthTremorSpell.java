package com.binaris.wizardry.content.spells.earth;

import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class EarthTremorSpell extends Spell {

    public EarthTremorSpell() {
        super(Elements.EARTH, SpellTiers.ADVANCED, 30, 60, 5);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_EARTHQUAKE.get(), SoundSource.PLAYERS, 1.2f, 0.9f);
        AABB area = caster.getBoundingBox().inflate(8.0);

        if (!level.isClientSide) {
            List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area,
                    e -> e != caster && !caster.isAlliedTo(e) && e.onGround());
            for (LivingEntity target : targets) {
                target.hurt(level.damageSources().indirectMagic(caster, caster), 8.0f);
                target.push(0.0, 0.65, 0.0);
            }
        } else {
            for (int i = 0; i < 30; i++) {
                double px = caster.getX() + (level.random.nextDouble() - 0.5) * 16.0;
                double py = caster.getY();
                double pz = caster.getZ() + (level.random.nextDouble() - 0.5) * 16.0;
                level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DIRT.defaultBlockState()),
                        px, py + 0.1, pz, 0, 0.3, 0);
            }
        }
        return true;
    }
}
