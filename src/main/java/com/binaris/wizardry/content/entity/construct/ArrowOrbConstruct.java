package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.content.entity.projectile.SwarmArrowEntity;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Hovering magical orb of conjured arrows above the caster.
 * Rapidly fires homing ricocheting arrows at nearby hostile targets.
 */
public class ArrowOrbConstruct extends MagicConstructEntity {
    private static final EntityDataAccessor<Integer> CASTER_ID =
            SynchedEntityData.defineId(ArrowOrbConstruct.class, EntityDataSerializers.INT);

    private int arrowsRemaining = 32;
    private int shootCooldown = 0;
    private float damage = 7.0f;
    private float range = 22.0f;

    public ArrowOrbConstruct(EntityType<?> type, Level world) {
        super(type, world);
        this.setLifetime(300);
        this.noCulling = true;
    }

    public ArrowOrbConstruct(Level world) {
        this(EBEntities.ARROW_ORB.get(), world);
    }

    public void setup(float damage, float range, int count, int lifetime) {
        this.damage = damage;
        this.range = range;
        this.arrowsRemaining = count;
        this.setLifetime(lifetime);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CASTER_ID, -1);
    }

    @Override
    public void setCaster(@Nullable LivingEntity caster) {
        super.setCaster(caster);
        this.entityData.set(CASTER_ID, caster != null ? caster.getId() : -1);
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity caster = null;
        if (this.level().isClientSide) {
            int id = this.entityData.get(CASTER_ID);
            if (id != -1) {
                Entity e = this.level().getEntity(id);
                if (e instanceof LivingEntity living) {
                    caster = living;
                    this.setPos(caster.getX(), caster.getY() + caster.getBbHeight() + 1.2, caster.getZ());
                }
            }
        } else {
            caster = this.getCaster();
            if (caster == null || !caster.isAlive() || caster.isRemoved()) {
                this.despawn();
                return;
            }
            this.setPos(caster.getX(), caster.getY() + caster.getBbHeight() + 1.2, caster.getZ());
        }

        // Visuals of the arrow sphere
        if (this.level().isClientSide) {
            double time = (double) this.tickCount * 0.15;
            int points = 8;
            double radius = 0.95;
            for (int i = 0; i < points; i++) {
                double phi = Math.acos(1.0 - 2.0 * (i + 0.5) / points);
                double theta = Math.PI * (1.0 + Math.sqrt(5.0)) * (i + 0.5) + time;
                double px = getX() + radius * Math.sin(phi) * Math.cos(theta);
                double py = getY() + radius * Math.cos(phi);
                double pz = getZ() + radius * Math.sin(phi) * Math.sin(theta);

                ParticleBuilder.create(EBParticles.SPARKLE)
                        .pos(px, py, pz)
                        .velocity((getX() - px) * 0.04, (getY() - py) * 0.04, (getZ() - pz) * 0.04)
                        .spawn(this.level());

                if (this.tickCount % 2 == 0) {
                    this.level().addParticle(ParticleTypes.ENCHANTED_HIT, px, py, pz, 0, 0, 0);
                }
            }

            if (this.tickCount % 3 == 0) {
                this.level().addParticle(ParticleTypes.CRIT, getX(), getY(), getZ(),
                        (random.nextDouble() - 0.5) * 0.2,
                        (random.nextDouble() - 0.5) * 0.2,
                        (random.nextDouble() - 0.5) * 0.2);
            }
        }

        // Shooting logic on server
        if (!this.level().isClientSide && caster != null) {
            shootCooldown--;
            if (shootCooldown <= 0 && arrowsRemaining > 0) {
                shootCooldown = 3; // Rapid continuous stream

                AABB searchBox = this.getBoundingBox().inflate(range);
                List<LivingEntity> targets = new ArrayList<>(this.level().getEntitiesOfClass(LivingEntity.class, searchBox, this::isValidTarget));

                if (!targets.isEmpty()) {
                    targets.sort(Comparator.comparingDouble(this::distanceToSqr));
                    int targetIndex = this.random.nextInt(Math.min(3, targets.size()));
                    LivingEntity target = targets.get(targetIndex);

                    // Launch Swarm Arrow
                    SwarmArrowEntity arrow = new SwarmArrowEntity(this.level(), caster);
                    Vec3 toTarget = target.getEyePosition().subtract(position()).normalize();
                    Vec3 spawnPos = position().add(toTarget.scale(0.85));
                    arrow.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                    arrow.setTarget(target);
                    arrow.setBaseDamage(damage * damageMultiplier);
                    arrow.setDeltaMovement(toTarget.scale(1.5));
                    this.level().addFreshEntity(arrow);

                    this.level().playSound(null, getX(), getY(), getZ(),
                            SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                            0.75f, 1.3f + this.random.nextFloat() * 0.4f);

                    arrowsRemaining--;

                    if (arrowsRemaining <= 0) {
                        // Finale pop
                        this.level().playSound(null, getX(), getY(), getZ(),
                                SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1.2f, 1.5f);
                        this.despawn();
                    }
                }
            }
        }
    }

    @Override
    public void despawn() {
        if (!this.level().isClientSide) {
            this.level().playSound(null, getX(), getY(), getZ(),
                    SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.PLAYERS, 1.0f, 1.4f);
        }
        super.despawn();
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ArrowsRemaining")) this.arrowsRemaining = tag.getInt("ArrowsRemaining");
        if (tag.contains("Damage")) this.damage = tag.getFloat("Damage");
        if (tag.contains("Range")) this.range = tag.getFloat("Range");
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ArrowsRemaining", this.arrowsRemaining);
        tag.putFloat("Damage", this.damage);
        tag.putFloat("Range", this.range);
    }
}
