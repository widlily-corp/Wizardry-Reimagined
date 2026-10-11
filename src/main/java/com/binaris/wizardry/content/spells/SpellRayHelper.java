package com.binaris.wizardry.content.spells;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class SpellRayHelper {

    public static HitResult rayTrace(Level level, LivingEntity caster, double range) {
        Vec3 eyePos = caster.getEyePosition();
        Vec3 lookVec = caster.getLookAngle();
        Vec3 endPos = eyePos.add(lookVec.scale(range));

        BlockHitResult blockHit = level.clip(new ClipContext(eyePos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, caster));
        double maxDist = blockHit.getType() != HitResult.Type.MISS ? blockHit.getLocation().distanceTo(eyePos) : range;

        AABB box = caster.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(1.0);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(level, caster, eyePos, endPos, box,
                e -> !e.isSpectator() && e.isPickable() && e != caster && (!caster.isAlliedTo(e)));

        if (entityHit != null && entityHit.getLocation().distanceTo(eyePos) <= maxDist) {
            return entityHit;
        }
        return blockHit;
    }

    public static EntityHitResult rayTraceEntity(Level level, LivingEntity caster, double range) {
        Vec3 eyePos = caster.getEyePosition();
        Vec3 lookVec = caster.getLookAngle();
        Vec3 endPos = eyePos.add(lookVec.scale(range));

        AABB box = caster.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(1.0);
        return ProjectileUtil.getEntityHitResult(level, caster, eyePos, endPos, box,
                e -> !e.isSpectator() && e.isPickable() && e != caster);
    }

    private SpellRayHelper() {}
}
