package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.*;
import com.binaris.wizardry.registry.EBParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
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

public class FirebombEntity extends BombEntity {
    public FirebombEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public FirebombEntity(LivingEntity livingEntity, Level world) {
        super(EBEntities.FIREBOMB.get(), livingEntity, world);
    }

    public FirebombEntity(Level world, LivingEntity livingEntity) {
        super(EBEntities.FIREBOMB.get(), livingEntity, world);
        this.aim(livingEntity, 1.25f);
    }

    public FirebombEntity(Level world) {
        super(EBEntities.FIREBOMB.get(), world);
    }

    @Override
    public int getLifetime() {
        return -1;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            level().addParticle(ParticleTypes.FLAME, getX(), getY() + 0.1, getZ(), 0, 0.01, 0);
            if (this.random.nextBoolean()) {
                level().addParticle(ParticleTypes.SMOKE, getX(), getY() + 0.1, getZ(), 0, 0.01, 0);
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
        return EBSpells.FIREBOMB != null && EBSpells.FIREBOMB.hasProperty(DefaultProperties.DAMAGE)
                ? EBSpells.FIREBOMB.property(DefaultProperties.DAMAGE) : 6.0f;
    }

    @Override
    public DamageSource getDamageSource(@NotNull EntityHitResult hitResult) {
        return level().damageSources().inFire();
    }

    public void splashEffect() {
        float radius = EBSpells.FIREBOMB != null && EBSpells.FIREBOMB.hasProperty(DefaultProperties.EFFECT_RADIUS)
                ? EBSpells.FIREBOMB.property(DefaultProperties.EFFECT_RADIUS) : 3.0f;
        float splashDamage = EBSpells.FIREBOMB != null && EBSpells.FIREBOMB.hasProperty(DefaultProperties.SPLASH_DAMAGE)
                ? EBSpells.FIREBOMB.property(DefaultProperties.SPLASH_DAMAGE) : 3.0f;
        int burnDuration = EBSpells.FIREBOMB != null && EBSpells.FIREBOMB.hasProperty(DefaultProperties.EFFECT_DURATION)
                ? EBSpells.FIREBOMB.property(DefaultProperties.EFFECT_DURATION) : 7;

        if (level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLAME, getX(), getY() + 0.5, getZ(), 45, 1.2, 0.8, 1.2, 0.05);
            serverLevel.sendParticles(ParticleTypes.LAVA, getX(), getY() + 0.5, getZ(), 15, 0.8, 0.5, 0.8, 0.05);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, getX(), getY() + 0.5, getZ(), 1, 0, 0, 0, 0);
        }

        List<LivingEntity> livingEntities = EntityUtil.getLivingEntitiesInRange(level(), getX(), getY(), getZ(), radius);

        for (LivingEntity entity : livingEntities) {
            if (!isValidTarget(entity)) continue;
            MagicDamageSource.causeMagicDamage(this, entity, splashDamage * blastMultiplier, EBDamageSources.FIRE);
            entity.igniteForSeconds(burnDuration);
        }
    }

    @Override
    protected void playHitSound(HitResult result) {
        this.playSound(EBSounds.ENTITY_FIREBOMB_SMASH.get(), 1.5F, random.nextFloat() * 0.4F + 0.6F);
        this.playSound(EBSounds.ENTITY_FIREBOMB_FIRE.get(), 1, 1);
    }

    protected void spawnHitParticles(HitResult.Type type) {
        ParticleBuilder.create(EBParticles.FLASH).pos(this.position()).scale(5 * blastMultiplier).color(1, 0.6f, 0).spawn(level());

        for (int i = 0; i < 60 * blastMultiplier; i++) {
            ParticleBuilder.create(EBParticles.MAGIC_FIRE, level().getRandom(), xo, yo, zo, 2 * blastMultiplier, false)
                    .time(10 + random.nextInt(4)).scale(1 + random.nextFloat()).spawn(level());

            ParticleBuilder.create(EBParticles.DARK_MAGIC, level().getRandom(), xo, yo, zo, 2 * blastMultiplier, false)
                    .color(1.0f, 0.2f + random.nextFloat() * 0.4f, 0.0f).spawn(level());
        }
        level().addParticle(ParticleTypes.EXPLOSION, xo, yo, zo, 0, 0, 0);
    }

    public int getRemainingFireTicks() {
        return -1;
    }

    public Item getItem() {
        return EBItems.FIREBOMB.get();
    }
}
