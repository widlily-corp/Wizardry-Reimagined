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
        if (this.tickCount % 16 == 1 && level().isClientSide && EBSounds.ENTITY_METEOR_FALLING != null && EBSounds.ENTITY_METEOR_FALLING.get() != null) {
            level().playSound(null, getX(), getY(), getZ(), EBSounds.ENTITY_METEOR_FALLING.get(), SoundSource.PLAYERS, 3.0f, 1.0f);
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        ++this.time;
        this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.1d, this.getDeltaMovement().z);
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.98D, 0.98D, 0.98D));

        if (!this.onGround()) return;

        if (!this.level().isClientSide) {
            float damage = EBSpells.METEOR.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.METEOR.property(DefaultProperties.DAMAGE) : 10.0f;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(),
                    damage * blastMultiplier,
                    damageBlocks, damageBlocks ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE);
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
