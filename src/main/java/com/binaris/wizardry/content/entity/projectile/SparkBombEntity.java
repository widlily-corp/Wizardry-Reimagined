package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBItems;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.EBSpells;
import net.minecraft.sounds.SoundEvent;
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

public class SparkBombEntity extends BombEntity {
    public SparkBombEntity(EntityType<? extends ThrowableProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public SparkBombEntity(Level world) {
        this(EBEntities.SPARK_BOMB.get(), world);
    }

    public SparkBombEntity(LivingEntity shooter, Level world) {
        super(EBEntities.SPARK_BOMB.get(), shooter, world);
    }

    protected void splashEffect() {
        double range = EBSpells.SPARK_BOMB.hasProperty(DefaultProperties.EFFECT_RADIUS) ? EBSpells.SPARK_BOMB.property(DefaultProperties.EFFECT_RADIUS) : 5.0;
        int maxTargets = EBSpells.SPARK_BOMB.hasProperty(DefaultProperties.MAX_TARGETS) ? EBSpells.SPARK_BOMB.property(DefaultProperties.MAX_TARGETS) : 4;
        float splashDamage = EBSpells.SPARK_BOMB.hasProperty(DefaultProperties.SPLASH_DAMAGE) ? EBSpells.SPARK_BOMB.property(DefaultProperties.SPLASH_DAMAGE) : 4.0f;

        List<LivingEntity> targets = EntityUtil.getLivingWithinRadius(range, getX(), getY(), getZ(), level());
        int hit = 0;

        for (LivingEntity target : targets) {
            if (!this.isValidTarget(target)) continue;
            if (hit++ >= maxTargets) break;

            if (EBSounds.ENTITY_SPARK_BOMB_CHAIN != null && EBSounds.ENTITY_SPARK_BOMB_CHAIN.get() != null) {
                target.playSound(EBSounds.ENTITY_SPARK_BOMB_CHAIN.get(), 1.0F, random.nextFloat() * 0.4F + 1.5F);
            }
            target.hurt(level().damageSources().lightningBolt(), splashDamage * damageMultiplier);

            ParticleBuilder.create(EBParticles.SPARK.get())
                    .pos(position()).target(target).time(10).spawn(level());
        }
    }

    @Override
    public int getLifetime() {
        return -1;
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
        return EBSpells.SPARK_BOMB.hasProperty(DefaultProperties.DIRECT_DAMAGE) ? EBSpells.SPARK_BOMB.property(DefaultProperties.DIRECT_DAMAGE) : 6.0f;
    }

    @Override
    public @NotNull SoundEvent getHitSound(HitResult result) {
        return EBSounds.ENTITY_SPARK_BOMB_HIT != null && EBSounds.ENTITY_SPARK_BOMB_HIT.get() != null
                ? EBSounds.ENTITY_SPARK_BOMB_HIT.get() : super.getHitSound(result);
    }


}
