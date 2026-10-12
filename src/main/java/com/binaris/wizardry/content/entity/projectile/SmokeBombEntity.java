package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBItems;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SmokeBombEntity extends BombEntity {
    public SmokeBombEntity(EntityType<? extends ThrowableProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public SmokeBombEntity(Level world) {
        this(EBEntities.SMOKE_BOMB.get(), world);
    }

    public SmokeBombEntity(LivingEntity shooter, Level world) {
        super(EBEntities.SMOKE_BOMB.get(), shooter, world);
    }

    @Override
    public int getLifetime() {
        return -1;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, getX(), getY() + 0.1, getZ(), 0, 0.02, 0);
            level().addParticle(ParticleTypes.SMOKE, getX(), getY() + 0.1, getZ(), 0, 0, 0);
        }
    }

    protected void splashEffect() {
        if (level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, getX(), getY() + 0.5, getZ(), 90, 2.0, 1.2, 2.0, 0.04);
            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, getX(), getY() + 0.5, getZ(), 60, 2.5, 1.0, 2.5, 0.05);
            serverLevel.playSound(null, getX(), getY(), getZ(), net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH, net.minecraft.sounds.SoundSource.PLAYERS, 1.5f, 0.8f);
        } else if (level().isClientSide) {
            for (int i = 0; i < 60; i++) {
                double vx = (random.nextDouble() - 0.5) * 0.4;
                double vy = random.nextDouble() * 0.2;
                double vz = (random.nextDouble() - 0.5) * 0.4;
                level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, getX(), getY(), getZ(), vx, vy, vz);
            }
            return;
        }

        double radius = EBSpells.SMOKE_BOMB.hasProperty(DefaultProperties.EFFECT_RADIUS) ? EBSpells.SMOKE_BOMB.property(DefaultProperties.EFFECT_RADIUS) : 4.0;
        int duration = EBSpells.SMOKE_BOMB.hasProperty(DefaultProperties.EFFECT_DURATION) ? EBSpells.SMOKE_BOMB.property(DefaultProperties.EFFECT_DURATION) : 160;
        List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(radius, getX(), getY(), getZ(), level());

        for (LivingEntity target : targets) {
            if (isValidTarget(target)) {
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration, 0));
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration / 2, 0));
            }
        }
    }

    @Override
    public void onHitTargetExtraEffects(@NotNull EntityHitResult hitResult) {
        splashEffect();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        splashEffect();
    }

    @Override
    public float getDamage(@NotNull EntityHitResult hitResult) {
        return 0f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_SMOKE_BOMB_SMASH != null && EBSounds.ENTITY_SMOKE_BOMB_SMASH.get() != null
                ? EBSounds.ENTITY_SMOKE_BOMB_SMASH.get() : super.getHitSound(result);
    }


}
