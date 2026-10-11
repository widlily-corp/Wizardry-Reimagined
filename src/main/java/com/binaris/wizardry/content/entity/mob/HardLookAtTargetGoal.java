package com.binaris.wizardry.content.entity.mob;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HardLookAtTargetGoal extends Goal {
    private final PathfinderMob mob;
    private final float maxYawRotationPerTick;
    private final float maxPitchRotationPerTick;
    private LivingEntity target;

    public HardLookAtTargetGoal(PathfinderMob mob, float maxYawRotationPerTick, float maxPitchRotationPerTick) {
        this.mob = mob;
        this.maxYawRotationPerTick = maxYawRotationPerTick;
        this.maxPitchRotationPerTick = maxPitchRotationPerTick;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        return this.target != null && this.target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
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

        double dx = this.target.getX() - this.mob.getX();
        double dy = this.target.getY() + this.target.getEyeHeight() - (this.mob.getY() + this.mob.getEyeHeight());
        double dz = this.target.getZ() - this.mob.getZ();

        double distanceXZ = Math.sqrt(dx * dx + dz * dz);

        float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
        float targetPitch = (float) (-(Mth.atan2(dy, distanceXZ) * (180.0 / Math.PI)));

        float yawDifference = Mth.wrapDegrees(targetYaw - this.mob.getYRot());
        float pitchDifference = targetPitch - this.mob.getXRot();

        yawDifference = Mth.clamp(yawDifference, -this.maxYawRotationPerTick, this.maxYawRotationPerTick);
        pitchDifference = Mth.clamp(pitchDifference, -this.maxPitchRotationPerTick, this.maxPitchRotationPerTick);

        float newYaw = this.mob.getYRot() + yawDifference;
        float newPitch = this.mob.getXRot() + pitchDifference;

        this.mob.setYRot(newYaw);
        this.mob.setXRot(newPitch);
        this.mob.yHeadRot = newYaw;
        this.mob.yBodyRot = newYaw;
    }
}
