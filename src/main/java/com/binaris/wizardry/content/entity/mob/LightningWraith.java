package com.binaris.wizardry.content.entity.mob;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.content.entity.projectile.SparkEntity;
import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class LightningWraith extends Blaze {
    private int attackCooldown;

    public LightningWraith(EntityType<? extends Blaze> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
        this.setPathfindingMalus(PathType.WATER, 0.0f);
        this.setPathfindingMalus(PathType.LAVA, 0.0f);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0f);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0f);
    }

    public LightningWraith(Level level) {
        this(EBEntities.LIGHTNING_WRAITH.get(), level);
    }

    public int getAttackCooldown() {
        return this.attackCooldown;
    }

    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("AttackCooldown", this.attackCooldown);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("AttackCooldown")) {
            this.attackCooldown = compound.getInt("AttackCooldown");
        }
    }

    @Override
    public void spawnAnim() {
        super.spawnAnim();
        spawnParticles();
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (this.deathTime == 1) this.spawnParticles();
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) ParticleBuilder.create(EBParticles.SPARK, this).spawn(level());
    }

    private void spawnParticles() {
        if (!level().isClientSide) return;

        for (int i = 0; i < 15; i++) {
            float brightness = 0.3f + (random.nextFloat() / 2);
            ParticleBuilder.create(EBParticles.SPARKLE, this).velocity(0, 0.05, 0)
                    .time(20 + random.nextInt(10))
                    .color(brightness, brightness + 0.2f, 1.0f).spawn(level());
        }
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.removeAllGoals(goal -> true);
        this.targetSelector.removeAllGoals(goal -> true);

        this.goalSelector.addGoal(4, new LightningWraithAttackGoal(this));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0F, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    static class LightningWraithAttackGoal extends Goal {
        private final LightningWraith wraith;
        private int seeTime;

        public LightningWraithAttackGoal(LightningWraith wraith) {
            this.wraith = wraith;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.wraith.getTarget();
            return target != null && target.isAlive() && this.wraith.canAttack(target);
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void start() {
            super.start();
            this.seeTime = 0;
            if (this.wraith.getAttackCooldown() <= 0) {
                this.wraith.setAttackCooldown(10);
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.seeTime = 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.wraith.getTarget();
            if (target == null) {
                return;
            }

            this.wraith.getLookControl().setLookAt(target, 30.0F, 30.0F);

            boolean hasLineOfSight = this.wraith.getSensing().hasLineOfSight(target);
            if (hasLineOfSight) {
                this.seeTime++;
            } else {
                this.seeTime = 0;
            }

            double distSq = this.wraith.distanceToSqr(target);
            double followRange = this.wraith.getAttributeValue(Attributes.FOLLOW_RANGE);

            if (distSq > 64.0 || (!hasLineOfSight && distSq > 9.0)) {
                this.wraith.getMoveControl().setWantedPosition(target.getX(), target.getY() + 1.0, target.getZ(), 1.0);
            } else if (distSq < 16.0) {
                double dx = this.wraith.getX() - target.getX();
                double dz = this.wraith.getZ() - target.getZ();
                double lenSq = dx * dx + dz * dz;
                if (lenSq > 1e-4) {
                    double len = Math.sqrt(lenSq);
                    this.wraith.getMoveControl().setWantedPosition(
                            this.wraith.getX() + (dx / len) * 3.0,
                            target.getY() + 1.5,
                            this.wraith.getZ() + (dz / len) * 3.0,
                            0.8
                    );
                }
            } else {
                this.wraith.getMoveControl().setWantedPosition(target.getX(), target.getY() + 1.5, target.getZ(), 0.6);
            }

            if (!this.wraith.level().isClientSide) {
                int cd = this.wraith.getAttackCooldown();
                if (cd > 0) {
                    this.wraith.setAttackCooldown(cd - 1);
                } else if (hasLineOfSight && distSq <= followRange * followRange) {
                    this.wraith.setAttackCooldown(20 + this.wraith.getRandom().nextInt(11));

                    double dx = target.getX() - this.wraith.getX();
                    double dy = target.getY(0.5) - this.wraith.getY(0.5);
                    double dz = target.getZ() - this.wraith.getZ();
                    double lenSq = dx * dx + dy * dy + dz * dz;

                    if (lenSq > 1e-4) {
                        SparkEntity spark = new SparkEntity(this.wraith.level(), this.wraith);
                        spark.shoot(dx, dy, dz, 1.2F, 1.0F);
                        this.wraith.level().addFreshEntity(spark);
                        this.wraith.playSound(EBSounds.SPELL_HOMING_SPARK.get(), 1.0F, 1.0F);
                    }
                }
            }
        }
    }
}
