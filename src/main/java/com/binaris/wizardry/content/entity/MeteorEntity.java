package com.binaris.wizardry.content.entity;

import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class MeteorEntity extends FallingBlockEntity {
    public float blastMultiplier = 1.0f;
    private boolean damageBlocks = false;

    public MeteorEntity(EntityType<? extends FallingBlockEntity> entityType, Level level) {
        super(entityType, level);
    }

    public MeteorEntity(Level level) {
        super(EBEntities.METEOR.get(), level);
    }

    public MeteorEntity(Level level, double x, double y, double z, BlockState state) {
        this(EBEntities.METEOR.get(), level);
        this.blocksBuilding = true;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    public MeteorEntity(Level world, double x, double y, double z, float blastMultiplier, boolean damageBlocks) {
        this(world, x, y, z, Blocks.MAGMA_BLOCK.defaultBlockState());
        this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.1D, this.getDeltaMovement().z);
        this.igniteForSeconds(200);
        this.blastMultiplier = blastMultiplier;
        this.damageBlocks = damageBlocks;
        this.noCulling = true;
    }

    @Override
    public void tick() {
        if (this.tickCount % 12 == 1 && level().isClientSide && EBSounds.ENTITY_METEOR_FALLING != null && EBSounds.ENTITY_METEOR_FALLING.get() != null) {
            level().playSound(null, getX(), getY(), getZ(), EBSounds.ENTITY_METEOR_FALLING.get(), SoundSource.PLAYERS, 3.0f, 0.9f);
        }

        if (this.level().isClientSide) {
            for (int i = 0; i < 4; i++) {
                double px = getX() + (random.nextDouble() - 0.5) * 0.8;
                double py = getY() + 0.5 + (random.nextDouble() - 0.5) * 0.8;
                double pz = getZ() + (random.nextDouble() - 0.5) * 0.8;
                level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, px, py, pz, -getDeltaMovement().x * 0.2, -getDeltaMovement().y * 0.2 + 0.05, -getDeltaMovement().z * 0.2);
                level().addParticle(ParticleTypes.FLAME, px, py, pz, (random.nextDouble() - 0.5) * 0.1, (random.nextDouble() - 0.5) * 0.1, (random.nextDouble() - 0.5) * 0.1);
            }
            if (random.nextInt(3) == 0) {
                level().addParticle(ParticleTypes.LAVA, getX(), getY() + 0.5, getZ(), 0, 0, 0);
            }
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        ++this.time;
        this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.06d, this.getDeltaMovement().z);
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.99D, 0.99D, 0.99D));

        boolean hitEntity = false;
        if (!this.level().isClientSide && this.time > 2) {
            hitEntity = !this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.5)).isEmpty();
        }

        if (!this.onGround() && !this.horizontalCollision && !this.verticalCollision && !hitEntity && this.time < 160) {
            return;
        }

        if (!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            float damage = EBSpells.METEOR.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.METEOR.property(DefaultProperties.DAMAGE) : 10.0f;
            float radius = damage * blastMultiplier;

            // Epic visual and sound burst
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ(), 2, 0.5, 0.5, 0.5, 0.0);
            serverLevel.sendParticles(ParticleTypes.FLAME, getX(), getY(), getZ(), 50, 2.0, 1.2, 2.0, 0.25);
            serverLevel.sendParticles(ParticleTypes.LAVA, getX(), getY(), getZ(), 20, 1.5, 1.0, 1.5, 0.2);
            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, getX(), getY(), getZ(), 25, 2.0, 1.0, 2.0, 0.1);
            serverLevel.playSound(null, getX(), getY(), getZ(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.WEATHER, 4.0f, 0.6f);

            this.level().explode(this, this.getX(), this.getY(), this.getZ(),
                    radius,
                    damageBlocks, damageBlocks ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE);

            if (damageBlocks) {
                net.minecraft.core.BlockPos basePos = this.blockPosition();
                for (int dx = -2; dx <= 2; dx++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        if (random.nextInt(3) == 0) {
                            net.minecraft.core.BlockPos firePos = basePos.offset(dx, 0, dz);
                            if (level().getBlockState(firePos).isAir() && level().getBlockState(firePos.below()).isSolid()) {
                                level().setBlockAndUpdate(firePos, Blocks.FIRE.defaultBlockState());
                            }
                        }
                    }
                }
            }

            this.discard();
        }
    }

    @Override
    public boolean causeFallDamage(float v1, float v, @NotNull DamageSource source) {
        return false;
    }

    @Override
    public boolean displayFireAnimation() {
        return true;
    }

    @Override
    public @NotNull BlockState getBlockState() {
        return Blocks.MAGMA_BLOCK.defaultBlockState();
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("blastMultiplier")) blastMultiplier = tag.getFloat("blastMultiplier");
        if (tag.contains("damageBlocks")) damageBlocks = tag.getBoolean("damageBlocks");
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("blastMultiplier", blastMultiplier);
        tag.putBoolean("damageBlocks", damageBlocks);
    }
}
