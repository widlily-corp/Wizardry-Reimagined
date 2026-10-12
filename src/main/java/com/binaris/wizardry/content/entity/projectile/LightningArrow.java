package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LightningArrow extends MagicArrowEntity {
    public LightningArrow(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
        setLifetime(40);
    }

    public LightningArrow(Level world) {
        this(EBEntities.LIGHTNING_ARROW.get(), world);
    }

    @Override
    public float getDamageAmount(@NotNull EntityHitResult hitResult) {
        return EBSpells.LIGHTNING_ARROW.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.LIGHTNING_ARROW.property(DefaultProperties.DAMAGE) : 7.0f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_LIGHTNING_ARROW_HIT != null && EBSounds.ENTITY_LIGHTNING_ARROW_HIT.get() != null
                ? EBSounds.ENTITY_LIGHTNING_ARROW_HIT.get() : super.getHitSound(result);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide && tickCount > 1) {
            Vec3 cur = position();
            Vec3 prev = new Vec3(xo, yo, zo);
            Vec3 delta = cur.subtract(prev);
            for (int i = 0; i < 4; i++) {
                double pct = (double) i / 4.0;
                double px = prev.x + delta.x * pct + (random.nextDouble() - 0.5) * 0.1;
                double py = prev.y + delta.y * pct + (random.nextDouble() - 0.5) * 0.1;
                double pz = prev.z + delta.z * pct + (random.nextDouble() - 0.5) * 0.1;
                level().addParticle(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK, px, py, pz, 0, 0, 0);
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        if (level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(), 20, 0.4, 0.4, 0.4, 0.05);
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.FLASH, getX(), getY(), getZ(), 1, 0, 0, 0, 0);
        }
    }

    @Override
    protected void onHitBlock(@NotNull net.minecraft.world.phys.BlockHitResult result) {
        super.onHitBlock(result);
        if (level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(), 15, 0.3, 0.3, 0.3, 0.05);
        }
    }
}
