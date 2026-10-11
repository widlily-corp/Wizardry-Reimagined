package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TelekinesisSpell extends Spell {

    public TelekinesisSpell() {
        super(Elements.SORCERY, SpellTiers.NOVICE, 5, 15, 0);
    }

    @Override
    public boolean cast(Level level, Player player) {
        return cast(level, (LivingEntity) player);
    }

    @Override
    public boolean cast(Level level, LivingEntity caster) {
        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), EBSounds.SPELL_TELEKINESIS.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        Vec3 casterEye = caster.getEyePosition();
        Vec3 look = caster.getLookAngle();
        AABB searchBox = caster.getBoundingBox().expandTowards(look.scale(16.0)).inflate(4.0);

        List<Entity> entities = level.getEntities(caster, searchBox, e -> e instanceof ItemEntity || (e instanceof LivingEntity && !caster.isAlliedTo(e)));
        for (Entity entity : entities) {
            Vec3 pullDir = casterEye.subtract(entity.position()).normalize().scale(0.8);
            entity.setDeltaMovement(entity.getDeltaMovement().add(pullDir));
            entity.hasImpulse = true;
            if (level.isClientSide) {
                level.addParticle(ParticleTypes.PORTAL, entity.getX(), entity.getY(), entity.getZ(), -pullDir.x, -pullDir.y, -pullDir.z);
            }
        }
        return true;
    }
}
