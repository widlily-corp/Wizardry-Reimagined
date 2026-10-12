package com.binaris.wizardry.api.content.util;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class EntityUtil {

    public static int getDefaultAimingError(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 5;
            case NORMAL -> 3;
            case HARD -> 0;
            default -> 4;
        };
    }

    public static <T extends Entity> List<T> getEntitiesWithinRadius(double radius, double x, double y, double z, Level world, Class<T> entityType) {
        AABB aabb = new AABB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
        List<T> entityList = world.getEntitiesOfClass(entityType, aabb);
        double radiusSq = radius * radius;
        return entityList.stream().filter(entity -> entity.distanceToSqr(x, y, z) <= radiusSq).toList();
    }

    public static boolean canDamageBlocks(LivingEntity entity, Level world) {
        if (entity instanceof Player player) {
            return player.mayBuild() && !player.isSpectator();
        }
        return true;
    }

    private EntityUtil() {}
}
