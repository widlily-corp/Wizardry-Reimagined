package com.binaris.wizardry.api.content.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TraceableEntity;

public final class MagicDamageSource {

    public static boolean causeMagicDamage(Entity directSource, Entity target, float damage) {
        if (target == null || directSource == null) return false;
        Entity owner = getOwnerIfPresent(directSource);
        DamageSource source;
        if (owner instanceof LivingEntity livingOwner) {
            source = target.level().damageSources().indirectMagic(directSource, livingOwner);
        } else if (directSource instanceof LivingEntity livingDirect) {
            source = target.level().damageSources().indirectMagic(directSource, livingDirect);
        } else {
            source = target.level().damageSources().magic();
        }
        return target.hurt(source, damage);
    }

    public static Entity getOwnerIfPresent(Entity entity) {
        if (entity instanceof OwnableEntity ownable && ownable.getOwner() != null) {
            return ownable.getOwner();
        }
        if (entity instanceof TraceableEntity traceable && traceable.getOwner() != null) {
            return traceable.getOwner();
        }
        return null;
    }

    private MagicDamageSource() {}
}
