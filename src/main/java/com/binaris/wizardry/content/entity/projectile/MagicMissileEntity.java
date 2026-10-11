package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class MagicMissileEntity extends MagicArrowEntity {

    public MagicMissileEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.setLifetime(40);
    }

    public MagicMissileEntity(Level level, LivingEntity shooter) {
        super(EBEntities.MAGIC_MISSILE.get(), level);
        this.setLifetime(40);
        this.aim(shooter, 1.6f);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public float getDamageAmount(@NotNull EntityHitResult hitResult) {
        return 5.0f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_MAGIC_MISSILE_HIT.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            double x = getX();
            double y = getY() + 0.1;
            double z = getZ();
            this.level().addParticle(ParticleTypes.ENCHANTED_HIT, x, y, z, 0.0, 0.0, 0.0);
            this.level().addParticle(ParticleTypes.WITCH, x, y, z, 0.0, 0.0, 0.0);
        }
    }
}
