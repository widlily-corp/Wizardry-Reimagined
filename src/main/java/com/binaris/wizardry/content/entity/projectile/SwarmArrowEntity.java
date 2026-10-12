package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Magic arrow created by Arrow Caster (Заклинатель стрел).
 * Features homing steering and chaining ricochets ("туда-сюда") between targets.
 */
public class SwarmArrowEntity extends AbstractArrow {
    private UUID targetUUID;
    private int bouncesRemaining = 2; // Up to 3 hits total across targets
    private final Set<UUID> hitTargets = new HashSet<>();
    private int lifeTicks = 0;

    public SwarmArrowEntity(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
        this.setBaseDamage(7.0);
    }

    public SwarmArrowEntity(Level level) {
        this(EBEntities.SWARM_ARROW.get(), level);
    }

    public SwarmArrowEntity(Level level, LivingEntity shooter) {
        super(EBEntities.SWARM_ARROW.get(), shooter, level, ItemStack.EMPTY, null);
        this.setBaseDamage(7.0);
    }

    public void setTarget(@Nullable LivingEntity target) {
        this.targetUUID = target != null ? target.getUUID() : null;
    }

    @Override
    public void tick() {
        lifeTicks++;
        if (lifeTicks > 140) {
            this.discard();
            return;
        }

        if (this.inGroundTime > 20) {
            this.discard();
            return;
        }

        // Particle trail
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.CRIT, getX(), getY(), getZ(), 0, 0, 0);
            ParticleBuilder.create(EBParticles.SPARKLE)
                    .pos(getX(), getY(), getZ())
                    .velocity(0, 0, 0)
                    .spawn(this.level());
        } else {
            // Homing steering logic
            if (this.tickCount > 1) {
                LivingEntity target = getTargetEntity();
                if (target != null && target.isAlive()) {
                    Vec3 toTarget = target.getEyePosition().subtract(position()).normalize();
                    Vec3 currentMotion = getDeltaMovement();
                    double speed = Math.max(1.3, currentMotion.length());
                    Vec3 newMotion = currentMotion.add(toTarget.scale(0.32)).normalize().scale(speed);
                    setDeltaMovement(newMotion);
                    hasImpulse = true;
                } else {
                    findNewTarget(14.0);
                }
            }
        }

        super.tick();
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        if (entity == getOwner()) return;
        if (getOwner() instanceof LivingEntity caster && caster.isAlliedTo(entity)) return;

        if (!level().isClientSide) {
            float dmg = (float) getBaseDamage();
            DamageSource src = getOwner() instanceof LivingEntity caster
                    ? damageSources().indirectMagic(this, caster)
                    : damageSources().magic();
            entity.hurt(src, dmg);
            hitTargets.add(entity.getUUID());

            level().playSound(null, getX(), getY(), getZ(),
                    SoundEvents.ARROW_HIT, SoundSource.PLAYERS, 1.0f, 1.4f);

            // Ricochet / chain to next nearby enemy ("туда-сюда")
            if (bouncesRemaining > 0) {
                LivingEntity nextTarget = findNextTarget(entity, 14.0);
                if (nextTarget != null) {
                    bouncesRemaining--;
                    targetUUID = nextTarget.getUUID();
                    Vec3 toNext = nextTarget.getEyePosition().subtract(position()).normalize();
                    setDeltaMovement(toNext.scale(1.5));
                    hasImpulse = true;

                    level().playSound(null, getX(), getY(), getZ(),
                            SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.PLAYERS, 1.0f, 1.8f);
                    return;
                }
            }

            this.discard();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult hitResult) {
        if (!level().isClientSide && bouncesRemaining > 0) {
            LivingEntity nextTarget = findNewTarget(12.0);
            if (nextTarget != null) {
                bouncesRemaining--;
                targetUUID = nextTarget.getUUID();
                Vec3 toNext = nextTarget.getEyePosition().subtract(position()).normalize();
                setDeltaMovement(toNext.scale(1.4));
                hasImpulse = true;
                this.inGround = false;
                this.inGroundTime = 0;

                level().playSound(null, getX(), getY(), getZ(),
                        SoundEvents.ARROW_HIT, SoundSource.PLAYERS, 0.8f, 1.8f);
                return;
            }
        }
        super.onHitBlock(hitResult);
        if (!level().isClientSide) {
            this.discard();
        }
    }

    @Nullable
    private LivingEntity getTargetEntity() {
        if (targetUUID != null && level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(targetUUID);
            if (entity instanceof LivingEntity living && living.isAlive() && !hitTargets.contains(targetUUID)) {
                return living;
            }
        }
        return null;
    }

    @Nullable
    private LivingEntity findNextTarget(Entity currentHit, double radius) {
        AABB searchBox = getBoundingBox().inflate(radius);
        List<LivingEntity> candidates = level().getEntitiesOfClass(LivingEntity.class, searchBox, e ->
                e.isAlive()
                && e != currentHit
                && !hitTargets.contains(e.getUUID())
                && e != getOwner()
                && (getOwner() == null || !((LivingEntity) getOwner()).isAlliedTo(e)));

        if (candidates.isEmpty()) return null;
        candidates.sort(Comparator.comparingDouble(this::distanceToSqr));
        return candidates.get(0);
    }

    @Nullable
    private LivingEntity findNewTarget(double radius) {
        AABB searchBox = getBoundingBox().inflate(radius);
        List<LivingEntity> candidates = level().getEntitiesOfClass(LivingEntity.class, searchBox, e ->
                e.isAlive()
                && !hitTargets.contains(e.getUUID())
                && e != getOwner()
                && (getOwner() == null || !((LivingEntity) getOwner()).isAlliedTo(e)));

        if (candidates.isEmpty()) return null;
        candidates.sort(Comparator.comparingDouble(this::distanceToSqr));
        LivingEntity chosen = candidates.get(0);
        this.targetUUID = chosen.getUUID();
        return chosen;
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean tryPickup(@NotNull Player player) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (targetUUID != null) tag.putUUID("TargetUUID", targetUUID);
        tag.putInt("BouncesRemaining", bouncesRemaining);
        tag.putInt("LifeTicks", lifeTicks);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("TargetUUID")) targetUUID = tag.getUUID("TargetUUID");
        if (tag.contains("BouncesRemaining")) bouncesRemaining = tag.getInt("BouncesRemaining");
        if (tag.contains("LifeTicks")) lifeTicks = tag.getInt("LifeTicks");
    }
}
