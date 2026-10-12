package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TornadoConstruct extends ScaledConstructEntity {
    private static final EntityDataAccessor<Float> VEL_X = SynchedEntityData.defineId(TornadoConstruct.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> VEL_Z = SynchedEntityData.defineId(TornadoConstruct.class, EntityDataSerializers.FLOAT);

    public TornadoConstruct(EntityType<?> type, Level world) {
        super(type, world);
        float rad = EBSpells.TORNADO != null && EBSpells.TORNADO.hasProperty(DefaultProperties.EFFECT_RADIUS)
                ? EBSpells.TORNADO.property(DefaultProperties.EFFECT_RADIUS).floatValue() : 4.0f;
        this.setBaseSize(rad, 8);
    }

    public TornadoConstruct(Level world) {
        this(EBEntities.TORNADO.get(), world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VEL_X, 0.0F);
        builder.define(VEL_Z, 0.0F);
    }

    @Override
    protected boolean shouldScaleHeight() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount % 60 == 1 && EBSounds.ENTITY_TORNADO_AMBIENT != null && EBSounds.ENTITY_TORNADO_AMBIENT.get() != null) {
            this.playSound(EBSounds.ENTITY_TORNADO_AMBIENT.get(), 1.0f, 1.0f);
        }

        if (!level().isClientSide) {
            Vec3 delta = this.getDeltaMovement();
            if (delta.lengthSqr() > 0.0001) {
                this.move(net.minecraft.world.entity.MoverType.SELF, delta);
            }
        }

        if (level().isClientSide) {
            for (int i = 0; i < 10; i++) {
                double height = random.nextDouble() * getBbHeight();
                double progress = height / getBbHeight();
                double radius = 0.5 + progress * (getBbWidth() / 2);
                double angle = (this.tickCount * 0.3) + (random.nextDouble() * Math.PI * 2);
                double px = getX() + radius * Math.cos(angle);
                double pz = getZ() + radius * Math.sin(angle);
                double vx = -Math.sin(angle) * 0.2;
                double vz = Math.cos(angle) * 0.2;
                if (random.nextBoolean()) {
                    level().addParticle(net.minecraft.core.particles.ParticleTypes.CLOUD, px, getY() + height, pz, vx, 0.08, vz);
                } else {
                    level().addParticle(net.minecraft.core.particles.ParticleTypes.POOF, px, getY() + height, pz, vx * 0.5, 0.05, vz * 0.5);
                }
            }
            return;
        }

        List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(getBbWidth() / 2, getX(), getY(), getZ(), level());
        for (LivingEntity target : targets) {
            if (!isValidTarget(target)) continue;

            Vec3 diff = new Vec3(getX() - target.getX(), 0, getZ() - target.getZ()).normalize();
            target.setDeltaMovement(target.getDeltaMovement().add(diff.x * 0.15 - diff.z * 0.1, 0.25, diff.z * 0.15 + diff.x * 0.1));
            target.hurtMarked = true;
            if (target instanceof net.minecraft.server.level.ServerPlayer player) {
                player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(target));
            }

            if (tickCount % 10 == 0) {
                float dmg = EBSpells.TORNADO != null && EBSpells.TORNADO.hasProperty(DefaultProperties.DAMAGE)
                        ? EBSpells.TORNADO.property(DefaultProperties.DAMAGE) : 2.0f;
                MagicDamageSource.causeMagicDamage(this, target, dmg * damageMultiplier, EBDamageSources.SORCERY);
            }
        }
    }
}
