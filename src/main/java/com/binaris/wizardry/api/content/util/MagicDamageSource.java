package com.binaris.wizardry.api.content.util;

import com.binaris.wizardry.registry.EBDamageSources;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TraceableEntity;
import org.jetbrains.annotations.Nullable;

public class MagicDamageSource extends DamageSource {

    public MagicDamageSource(Holder<DamageType> damageTypeHolder, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
        super(damageTypeHolder, directEntity, causingEntity);
    }

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

    public static boolean causeMagicDamage(Entity caster, Entity target, float damage, ResourceKey<DamageType> type) {
        Entity owner = getOwnerIfPresent(caster);
        DamageSource source = (owner != null)
                ? causeIndirectMagicDamage(caster, owner, type)
                : causeDirectMagicDamage(caster, type);
        return target.hurt(source, damage);
    }

    @Nullable
    public static Entity getOwnerIfPresent(Entity entity) {
        if (entity instanceof OwnableEntity ownable && ownable.getOwner() != null) return ownable.getOwner();
        if (entity instanceof TraceableEntity traceable && traceable.getOwner() != null) return traceable.getOwner();
        return null;
    }

    public static DamageSource causeDirectMagicDamage(Entity caster, ResourceKey<DamageType> type) {
        return createMagicDamage(caster, null, type);
    }

    public static DamageSource causeIndirectMagicDamage(Entity magic, Entity caster, ResourceKey<DamageType> type) {
        return createMagicDamage(magic, caster, type);
    }

    private static DamageSource createMagicDamage(Entity source, Entity indirect, ResourceKey<DamageType> type) {
        Holder<DamageType> holder = source.level().registryAccess()
                .lookupOrThrow(Registries.DAMAGE_TYPE)
                .getOrThrow(type);
        return new MagicDamageSource(holder, source, indirect);
    }
}
