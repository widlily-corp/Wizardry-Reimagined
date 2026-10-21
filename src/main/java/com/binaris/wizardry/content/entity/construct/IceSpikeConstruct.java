package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBMobEffects;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class IceSpikeConstruct extends ScaledConstructEntity {
    private Direction facing = Direction.UP;

    public IceSpikeConstruct(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setBaseSize(0.5f, 1.0f);
    }

    public IceSpikeConstruct(Level world) {
        this(EBEntities.ICE_SPIKE.get(), world);
    }

    @Override
    public void tick() {
        super.tick();
        if (lifetime - this.tickCount == 30 && EBSounds.ENTITY_ICE_SPIKE_EXTEND != null && EBSounds.ENTITY_ICE_SPIKE_EXTEND.get() != null) {
            this.playSound(EBSounds.ENTITY_ICE_SPIKE_EXTEND.get(), 1.0f, 2.5f);
        }
        if (level().isClientSide) {
            if (this.tickCount <= 6) {
                for (int i = 0; i < 3; i++) {
                    level().addParticle(ParticleTypes.ITEM_SNOWBALL, getX(), getY() + 0.4, getZ(),
                            (random.nextDouble() - 0.5) * 0.2, 0.25, (random.nextDouble() - 0.5) * 0.2);
                    level().addParticle(ParticleTypes.SNOWFLAKE, getX(), getY() + 0.4, getZ(),
                            (random.nextDouble() - 0.5) * 0.15, 0.15, (random.nextDouble() - 0.5) * 0.15);
                }
            }
            return;
        }
        double extensionSpeed = 0;

        if (lifetime - this.tickCount < 15) {
            extensionSpeed = -0.01 * (this.tickCount - (lifetime - 15));
        } else if (lifetime - this.tickCount < 25) {
            extensionSpeed = 0;
        } else if (lifetime - this.tickCount < 28) {
            extensionSpeed = 0.25;
        }

        if (facing != null) {
            this.move(MoverType.SELF, new Vec3(this.facing.getStepX() * extensionSpeed,
                    this.facing.getStepY() * extensionSpeed, this.facing.getStepZ() * extensionSpeed));
        }

        for (Object entity : this.level().getEntities(this, this.getBoundingBox())) {
            if (entity instanceof LivingEntity livingEntity && this.isValidTarget(livingEntity)) {
                if (MagicDamageSource.causeMagicDamage(this, livingEntity, 5.0f * this.damageMultiplier, EBDamageSources.FROST)) {
                    int dur = EBSpells.ICE_SPIKES != null && EBSpells.ICE_SPIKES.hasProperty(DefaultProperties.EFFECT_DURATION)
                            ? EBSpells.ICE_SPIKES.property(DefaultProperties.EFFECT_DURATION) : 100;
                    int str = EBSpells.ICE_SPIKES != null && EBSpells.ICE_SPIKES.hasProperty(DefaultProperties.EFFECT_STRENGTH)
                            ? EBSpells.ICE_SPIKES.property(DefaultProperties.EFFECT_STRENGTH) : 0;
                    if (EBMobEffects.FROST != null && EBMobEffects.FROST.get() != null) {
                        livingEntity.addEffect(new MobEffectInstance(EBMobEffects.FROST, dur, str));
                    }
                }
            }
        }
    }

    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Facing")) {
            Direction dir = Direction.byName(tag.getString("Facing"));
            if (dir != null) {
                this.facing = dir;
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (facing != null) {
            tag.putString("Facing", facing.getName());
        }
    }
}
