package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.lang.ref.WeakReference;

public class BubbleConstruct extends MagicConstructEntity {

    private static final EntityDataAccessor<Boolean> IS_DARK_ORB =
            SynchedEntityData.defineId(BubbleConstruct.class, EntityDataSerializers.BOOLEAN);

    private WeakReference<LivingEntity> rider;

    public BubbleConstruct(EntityType<?> type, Level world) {
        super(type, world);
    }

    public BubbleConstruct(Level world) {
        this(EBEntities.BUBBLE.get(), world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_DARK_ORB, false);
    }

    public boolean isDarkOrb() {
        return this.entityData.get(IS_DARK_ORB);
    }

    public void setDarkOrb(boolean darkOrb) {
        this.entityData.set(IS_DARK_ORB, darkOrb);
    }

    @Override
    public void tick() {
        super.tick();

        Entity currentRider = EntityUtil.getRider(this);
        if ((rider == null || rider.get() == null) && currentRider instanceof LivingEntity living && living.isAlive()) {
            rider = new WeakReference<>(living);
        }

        if (this.level().isClientSide) {
            for (int i = 0; i < 2; i++) {
                this.level().addParticle(ParticleTypes.BUBBLE,
                        this.getX() + (random.nextDouble() - 0.5) * 0.8,
                        this.getY() + random.nextDouble() * 0.8,
                        this.getZ() + (random.nextDouble() - 0.5) * 0.8,
                        0, 0.05, 0);
            }
            return;
        }

        if (rider != null && rider.get() != null) {
            LivingEntity living = rider.get();
            if (!living.isAlive() || living.getVehicle() != this) {
                this.discard();
                return;
            }
            this.setDeltaMovement(0, 0.05, 0);
            this.move(MoverType.SELF, this.getDeltaMovement());
        } else if (this.tickCount > 10 && this.getPassengers().isEmpty()) {
            this.discard();
        }
    }
}
