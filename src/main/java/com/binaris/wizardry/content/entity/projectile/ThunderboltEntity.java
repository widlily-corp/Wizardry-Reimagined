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
import org.jetbrains.annotations.NotNull;

public class ThunderboltEntity extends MagicProjectileEntity {
    public ThunderboltEntity(EntityType<? extends ThrowableProjectile> entityType, Level world) {
        super(entityType, world);
        setLifetime(8);
    }

    public ThunderboltEntity(Level world) {
        this(EBEntities.THUNDERBOLT.get(), world);
    }

    @Override
    public void onHitTargetExtraEffects(@NotNull EntityHitResult hitResult) {
        if (!(hitResult.getEntity() instanceof LivingEntity livingEntity)) return;
        float knockback = EBSpells.THUNDERBOLT.hasProperty(DefaultProperties.KNOCKBACK) ? EBSpells.THUNDERBOLT.property(DefaultProperties.KNOCKBACK) : 0.2f;
        livingEntity.knockback(knockback * 0.5F, Mth.sin(this.getYRot() * 0.017453292F), -Mth.cos(this.getYRot() * 0.017453292F));
    }

    @Override
    public float getDamage(@NotNull EntityHitResult hitResult) {
        return EBSpells.THUNDERBOLT.hasProperty(DefaultProperties.DAMAGE) ? EBSpells.THUNDERBOLT.property(DefaultProperties.DAMAGE) : 3f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_THUNDERBOLT_HIT != null && EBSounds.ENTITY_THUNDERBOLT_HIT.get() != null
                ? EBSounds.ENTITY_THUNDERBOLT_HIT.get() : super.getHitSound(result);
    }

    @Override
    protected void onHitBlock(@NotNull net.minecraft.world.phys.BlockHitResult result) {
        super.onHitBlock(result);
        this.level().addParticle(ParticleTypes.EXPLOSION, this.xo, this.yo, this.zo, 0, 0, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            ParticleBuilder.create(EBParticles.SPARK.get()).pos(xo, yo + getBbHeight() / 2, zo).time(8).spawn(level());
            for (int i = 0; i < 2; i++) {
                this.level().addParticle(ParticleTypes.LARGE_SMOKE,
                        this.xo + random.nextFloat() * 0.2 - 0.1,
                        this.yo + this.getBbHeight() / 2 + random.nextFloat() * 0.2 - 0.1,
                        this.zo + random.nextFloat() * 0.2 - 0.1, 0, 0, 0);
            }
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }
}
