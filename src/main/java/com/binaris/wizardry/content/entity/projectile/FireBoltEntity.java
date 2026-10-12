package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class FireBoltEntity extends MagicProjectileEntity {
    public FireBoltEntity(EntityType<? extends ThrowableProjectile> entityType, Level world) {
        super(entityType, world);
        setLifetime(6);
    }

    public FireBoltEntity(Level world) {
        this(EBEntities.FIRE_BOLT.get(), world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide || this.tickCount <= 1) return;
        ParticleBuilder.create(EBParticles.MAGIC_FIRE.get()).pos(position()).time(14).spawn(level());

        double x = xo - getDeltaMovement().x / 2 + random.nextFloat() * 0.2 - 0.1;
        double y = yo + getBbHeight() / 2 - getDeltaMovement().y / 2 + random.nextFloat() * 0.2 - 0.1;
        double z = zo - getDeltaMovement().z / 2 + random.nextFloat() * 0.2 - 0.1;
        ParticleBuilder.create(EBParticles.MAGIC_FIRE.get()).pos(x, y, z).time(14).spawn(level());
    }

    @Override
    protected void onHitBlock(@NotNull net.minecraft.world.phys.BlockHitResult result) {
        super.onHitBlock(result);
        for (int i = 0; i < 8; i++) {
            level().addParticle(ParticleTypes.LAVA, getX() + random.nextFloat() - 0.5, getY() + getBbHeight() / 2 + random.nextFloat() - 0.5, getZ() + random.nextFloat() - 0.5, 0, 0, 0);
        }
    }

    @Override
    public void onHitTargetExtraEffects(@NotNull EntityHitResult hitResult) {
        if (!(hitResult.getEntity() instanceof LivingEntity entity)) return;
        int duration = EBSpells.FIREBOLT.hasProperty(DefaultProperties.EFFECT_DURATION) ? EBSpells.FIREBOLT.property(DefaultProperties.EFFECT_DURATION) : 5;
        entity.igniteForSeconds(duration);
    }

    @Override
    public float getDamage(@NotNull EntityHitResult hitResult) {
        return EBSpells.FIREBOLT.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.FIREBOLT.property(DefaultProperties.DAMAGE) : 5f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_FIREBOLT_HIT != null && EBSounds.ENTITY_FIREBOLT_HIT.get() != null ? EBSounds.ENTITY_FIREBOLT_HIT.get() : super.getHitSound(result);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }
}
