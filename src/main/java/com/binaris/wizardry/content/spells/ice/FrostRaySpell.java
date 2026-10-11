package com.binaris.wizardry.content.spells.ice;

import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FrostRaySpell extends Spell {

    public FrostRaySpell() {
        super(Elements.ICE, SpellTiers.ADVANCED, 25, 40, 5);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_FROST_RAY_START.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        Vec3 origin = caster.getEyePosition();
        Vec3 look = caster.getLookAngle();
        double range = 20.0;

        for (double d = 1.0; d <= range; d += 0.5) {
            Vec3 point = origin.add(look.scale(d));
            if (level.isClientSide) {
                level.addParticle(ParticleTypes.SNOWFLAKE, point.x, point.y, point.z, 0, 0, 0);
            } else {
                AABB box = new AABB(point.x - 0.75, point.y - 0.75, point.z - 0.75, point.x + 0.75, point.y + 0.75, point.z + 0.75);
                List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, box, e -> e != caster && !caster.isAlliedTo(e));
                for (LivingEntity target : targets) {
                    target.hurt(level.damageSources().freeze(), 7.0f);
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 2));
                    target.setTicksFrozen(Math.min(target.getTicksRequiredToFreeze() + 160, target.getTicksFrozen() + 160));
                }
            }
        }
        return true;
    }
}
