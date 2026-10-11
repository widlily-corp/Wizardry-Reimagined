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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChainLightningSpell extends Spell {

    public ChainLightningSpell() {
        super(Elements.LIGHTNING, SpellTiers.MASTER, 45, 90, 5);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        HitResult hit = SpellRayHelper.rayTrace(level, caster, 16.0);
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_CHAIN_LIGHTNING.get(), SoundSource.PLAYERS, 1.2f, 1.0f);

        if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity primary) {
            Set<LivingEntity> hitSet = new HashSet<>();
            hitSet.add(primary);
            primary.hurt(level.damageSources().lightningBolt(), 12.0f);

            LivingEntity current = primary;
            for (int i = 0; i < 4; i++) {
                AABB chainBox = current.getBoundingBox().inflate(8.0);
                List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, chainBox,
                        e -> e != caster && !caster.isAlliedTo(e) && !hitSet.contains(e));
                if (nearby.isEmpty()) break;

                LivingEntity next = nearby.get(0);
                hitSet.add(next);
                next.hurt(level.damageSources().lightningBolt(), 8.0f);

                if (level.isClientSide) {
                    for (int p = 0; p < 8; p++) {
                        level.addParticle(ParticleTypes.ELECTRIC_SPARK, next.getX(), next.getY() + 0.5, next.getZ(),
                                (level.random.nextDouble() - 0.5) * 0.3, 0.1, (level.random.nextDouble() - 0.5) * 0.3);
                    }
                }
                current = next;
            }
            return true;
        }
        return false;
    }
}
