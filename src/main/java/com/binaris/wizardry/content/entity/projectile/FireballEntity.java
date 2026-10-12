package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FireballEntity extends MagicProjectileEntity {

    public FireballEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        this.setLifetime(60);
    }

    public FireballEntity(Level level) {
        super(EBEntities.FIREBALL.get(), level);
        this.setLifetime(60);
    }

    public FireballEntity(Level level, LivingEntity shooter) {
        super(EBEntities.FIREBALL.get(), level);
        this.setLifetime(60);
        this.aim(shooter, 1.4f);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public float getDamage(@NotNull EntityHitResult result) {
        return 6.0f;
    }

    @Override
    public DamageSource getDamageSource(@NotNull EntityHitResult result) {
        return level().damageSources().inFire();
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_FIREBOLT_HIT.get();
    }

    @Override
    public void onHitTargetExtraEffects(@NotNull EntityHitResult result) {
        result.getEntity().igniteForSeconds(7);
        if (this.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLAME, getX(), getY() + 0.5, getZ(), 35, 0.8, 0.5, 0.8, 0.05);
            serverLevel.sendParticles(ParticleTypes.LAVA, getX(), getY() + 0.5, getZ(), 10, 0.5, 0.3, 0.5, 0.05);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, getX(), getY() + 0.5, getZ(), 1, 0, 0, 0, 0);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            if (this.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.FLAME, getX(), getY() + 0.5, getZ(), 30, 0.8, 0.5, 0.8, 0.05);
                serverLevel.sendParticles(ParticleTypes.LAVA, getX(), getY() + 0.5, getZ(), 8, 0.5, 0.3, 0.5, 0.05);
            }
            BlockPos firePos = result.getBlockPos().relative(result.getDirection());
            if (this.level().isEmptyBlock(firePos)) {
                this.level().setBlockAndUpdate(firePos, Blocks.FIRE.defaultBlockState());
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            Vec3 cur = position();
            Vec3 prev = new Vec3(xo, yo, zo);
            Vec3 delta = cur.subtract(prev);
            for (int i = 0; i < 3; i++) {
                double pct = (double) i / 3.0;
                double px = prev.x + delta.x * pct + (random.nextDouble() - 0.5) * 0.15;
                double py = prev.y + delta.y * pct + (random.nextDouble() - 0.5) * 0.15;
                double pz = prev.z + delta.z * pct + (random.nextDouble() - 0.5) * 0.15;
                this.level().addParticle(ParticleTypes.FLAME, px, py, pz, 0, 0.01, 0);
            }
            if (this.random.nextInt(2) == 0) {
                this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, getX(), getY(), getZ(), 0, 0.02, 0);
            }
        }
    }
}
