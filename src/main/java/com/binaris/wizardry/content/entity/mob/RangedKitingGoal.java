package com.binaris.wizardry.content.entity.mob;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RangedKitingGoal extends Goal {
    private final PathfinderMob mob;
    private final double moveSpeed;
    private final double safeDistance;
    private final double fleeDistance;
    private final double followRange;
    private LivingEntity target;

    public RangedKitingGoal(PathfinderMob mob, double moveSpeed, double safeDistance, double fleeDistance, double followRange) {
        this.mob = mob;
        this.moveSpeed = moveSpeed;
        this.safeDistance = safeDistance;
        this.fleeDistance = fleeDistance;
        this.followRange = followRange;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public RangedKitingGoal(PathfinderMob mob, double moveSpeed) {
        this(mob, moveSpeed, 10.0, 6.0, 32.0);
    }

    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        if (this.target == null || !this.target.isAlive()) return false;
        double distanceSq = this.mob.distanceToSqr(this.target);
        return distanceSq <= this.followRange * this.followRange;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public void tick() {
        if (this.target == null) return;

        double distance = this.mob.distanceTo(this.target);
        if (distance < this.fleeDistance) {
            retreatFromTarget();
        } else if (distance > this.safeDistance) {
            advanceToTarget();
        } else {
            stopMovement();
        }

        handleStepUp();
    }

    private void handleStepUp() {
        if (mob.horizontalCollision && mob.onGround()) {
            AABB checkBox = mob.getBoundingBox().move(0.0, 0.42, 0.0);
            if (mob.level().noCollision(mob, checkBox)) {
                mob.setDeltaMovement(mob.getDeltaMovement().x, 0.42f, mob.getDeltaMovement().z);
            }
        }
    }

    private void retreatFromTarget() {
        if (this.target == null) return;
        Vec3 mobPos = this.mob.position();
        Vec3 targetPos = this.target.position();

        double dx = mobPos.x - targetPos.x;
        double dz = mobPos.z - targetPos.z;
        double distance = Math.sqrt(dx * dx + dz * dz);
        if (distance > 0) {
            dx /= distance;
            dz /= distance;
        }

        this.mob.setDeltaMovement(
                this.mob.getDeltaMovement().x() + dx * this.moveSpeed * 0.1,
                this.mob.getDeltaMovement().y(),
                this.mob.getDeltaMovement().z() + dz * this.moveSpeed * 0.1
        );
    }

    private void advanceToTarget() {
        if (this.target == null) return;
        Vec3 mobPos = this.mob.position();
        Vec3 targetPos = this.target.position();

        double dx = targetPos.x - mobPos.x;
        double dz = targetPos.z - mobPos.z;
        double distance = Math.sqrt(dx * dx + dz * dz);
        if (distance > 0) {
            dx /= distance;
            dz /= distance;
        }

        this.mob.setDeltaMovement(
                this.mob.getDeltaMovement().x() + dx * this.moveSpeed * 0.1,
                this.mob.getDeltaMovement().y(),
                this.mob.getDeltaMovement().z() + dz * this.moveSpeed * 0.1
        );
    }

    private void stopMovement() {
        if (this.target == null) return;
        this.mob.setDeltaMovement(
                this.mob.getDeltaMovement().x() * 0.9,
                this.mob.getDeltaMovement().y(),
                this.mob.getDeltaMovement().z() * 0.9
        );
    }
}
