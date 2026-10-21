package com.binaris.wizardry.content.entity.construct;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class MagicConstructEntity extends Entity implements OwnableEntity {
    public int lifetime = 600;
    public float damageMultiplier = 1.0f;
    private UUID casterUUID;

    public MagicConstructEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.noPhysics = true;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setCaster(@Nullable LivingEntity caster) {
        this.casterUUID = caster != null ? caster.getUUID() : null;
    }

    public @Nullable LivingEntity getCaster() {
        return casterUUID != null && level() instanceof net.minecraft.server.level.ServerLevel serverLevel
                && serverLevel.getEntity(casterUUID) instanceof LivingEntity living ? living : null;
    }

    @Override
    public @Nullable UUID getOwnerUUID() {
        return casterUUID;
    }

    @Override
    public @Nullable LivingEntity getOwner() {
        return getCaster();
    }

    public void despawn() {
        this.discard();
    }

    public boolean isValidTarget(Entity target) {
        if (target == null || target == this) return false;
        LivingEntity caster = getCaster();
        if (target == caster) return false;
        if (caster != null && caster.isAlliedTo(target)) return false;
        return target.isAlive();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && lifetime > 0 && tickCount >= lifetime) {
            this.despawn();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        if (tag.hasUUID("Caster")) casterUUID = tag.getUUID("Caster");
        if (tag.contains("Lifetime")) lifetime = tag.getInt("Lifetime");
        if (tag.contains("DamageMultiplier")) damageMultiplier = tag.getFloat("DamageMultiplier");
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        if (casterUUID != null) tag.putUUID("Caster", casterUUID);
        tag.putInt("Lifetime", lifetime);
        tag.putFloat("DamageMultiplier", damageMultiplier);
    }
}
