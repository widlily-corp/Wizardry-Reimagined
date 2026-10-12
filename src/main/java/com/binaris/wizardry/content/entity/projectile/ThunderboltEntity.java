package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ThunderboltEntity extends MagicProjectileEntity {
    public ThunderboltEntity(EntityType<? extends ThrowableProjectile> entityType, Level world) {
        super(entityType, world);
        setLifetime(30);
    }

    public ThunderboltEntity(Level world) {
        this(EBEntities.THUNDERBOLT.get(), world);
    }

    @Override
    public void onHitTargetExtraEffects(@NotNull EntityHitResult hitResult) {
        if (!(hitResult.getEntity() instanceof LivingEntity livingEntity)) return;
        float knockback = EBSpells.THUNDERBOLT.hasProperty(DefaultProperties.KNOCKBACK) ? EBSpells.THUNDERBOLT.property(DefaultProperties.KNOCKBACK) : 0.4f;
        livingEntity.knockback(knockback * 0.5F, Mth.sin(this.getYRot() * 0.017453292F), -Mth.cos(this.getYRot() * 0.017453292F));
        if (level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() / 2, livingEntity.getZ(), 20, 0.4, 0.4, 0.4, 0.05);
            serverLevel.sendParticles(ParticleTypes.FLASH, livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() / 2, livingEntity.getZ(), 1, 0, 0, 0, 0);
        }
    }

    @Override
    public float getDamage(@NotNull EntityHitResult hitResult) {
        return EBSpells.THUNDERBOLT.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.THUNDERBOLT.property(DefaultProperties.DAMAGE) : 6f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_THUNDERBOLT_HIT != null && EBSounds.ENTITY_THUNDERBOLT_HIT.get() != null
                ? EBSounds.ENTITY_THUNDERBOLT_HIT.get() : super.getHitSound(result);
    }

    @Override
    protected void onHitBlock(@NotNull net.minecraft.world.phys.BlockHitResult result) {
        super.onHitBlock(result);
        if (level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 1, 0, 0, 0, 0);
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(), 25, 0.5, 0.5, 0.5, 0.05);
            serverLevel.sendParticles(ParticleTypes.FLASH, getX(), getY(), getZ(), 1, 0, 0, 0, 0);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            Vec3 cur = position();
            Vec3 prev = new Vec3(xo, yo, zo);
            Vec3 delta = cur.subtract(prev);
            for (int i = 0; i < 4; i++) {
                double pct = (double) i / 4.0;
                double px = prev.x + delta.x * pct + (random.nextDouble() - 0.5) * 0.15;
                double py = prev.y + delta.y * pct + (random.nextDouble() - 0.5) * 0.15;
                double pz = prev.z + delta.z * pct + (random.nextDouble() - 0.5) * 0.15;
                this.level().addParticle(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 0, 0, 0);
            }
            if (this.random.nextInt(3) == 0) {
                this.level().addParticle(ParticleTypes.FLASH, getX(), getY(), getZ(), 0, 0, 0);
            }
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }
}
