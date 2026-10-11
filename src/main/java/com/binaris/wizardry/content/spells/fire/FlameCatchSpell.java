package com.binaris.wizardry.content.spells.fire;

import com.binaris.wizardry.content.entity.projectile.FireballEntity;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FlameCatchSpell extends Spell {

    public FlameCatchSpell() {
        super(Elements.FIRE, SpellTiers.MASTER, 45, 80, 10);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_FIRESTORM.get(), SoundSource.PLAYERS, 1.2f, 1.0f);
        if (!level.isClientSide) {
            Vec3 look = caster.getLookAngle();
            for (int i = -2; i <= 2; i++) {
                FireballEntity fireball = new FireballEntity(level, caster);
                fireball.damageMultiplier = 1.5f;
                double spreadYaw = Math.toRadians(i * 12.0);
                double cos = Math.cos(spreadYaw);
                double sin = Math.sin(spreadYaw);
                double vx = look.x * cos - look.z * sin;
                double vz = look.x * sin + look.z * cos;
                fireball.shoot(vx, look.y + 0.05 * (level.random.nextDouble() - 0.5), vz, 1.5f, 1.0f);
                level.addFreshEntity(fireball);
            }
        }
        return true;
    }
}
