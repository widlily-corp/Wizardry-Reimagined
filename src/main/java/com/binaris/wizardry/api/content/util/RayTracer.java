package com.binaris.wizardry.api.content.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class RayTracer {

    public static @NotNull HitResult rayTrace(Level world, Entity caster, Vec3 origin, Vec3 endpoint, float aimAssist, boolean hitLiquids, Class<? extends Entity> entityType, Predicate<? super Entity> filter) {
        ClipContext.Fluid fluidMode = hitLiquids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE;
        HitResult blockHit = world.clip(new ClipContext(origin, endpoint, ClipContext.Block.COLLIDER, fluidMode, caster));

        Vec3 traceEnd = blockHit.getLocation();
        float searchRadius = 1 + aimAssist;
        AABB searchVolume = new AABB(origin.x, origin.y, origin.z, traceEnd.x, traceEnd.y, traceEnd.z).inflate(searchRadius, searchRadius, searchRadius);

        List<? extends Entity> entities = world.getEntitiesOfClass(entityType, searchVolume);
        if (filter != null) {
            entities = entities.stream().filter(filter.negate()).toList();
        }

        Entity closestEntity = null;
        double closestDistance = origin.distanceTo(traceEnd);

        for (Entity entity : entities) {
            if (entity == caster) continue;
            Vec3 intercept = calculateIntercept(entity, origin, traceEnd, aimAssist);
            if (intercept == null) continue;

            double distance = origin.distanceTo(intercept);
            if (distance < closestDistance) {
                closestEntity = entity;
                closestDistance = distance;
            }
        }

        return closestEntity != null ? new EntityHitResult(closestEntity) : blockHit;
    }

    public static @Nullable Vec3 calculateIntercept(Entity entity, Vec3 origin, Vec3 endpoint, float aimAssist) {
        AABB aabb = entity.getBoundingBox().inflate(aimAssist);
        Optional<Vec3> hit = aabb.clip(origin, endpoint);
        return hit.orElse(null);
    }

    private RayTracer() {}
}
