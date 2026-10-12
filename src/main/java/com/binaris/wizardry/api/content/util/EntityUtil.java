package com.binaris.wizardry.api.content.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public final class EntityUtil {
    public static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

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

    public static List<LivingEntity> getLivingWithinRadius(double radius, double x, double y, double z, Level world) {
        return getEntitiesWithinRadius(radius, x, y, z, world, LivingEntity.class);
    }

    public static List<LivingEntity> getLivingEntitiesInRange(Level world, double x, double y, double z, double radius) {
        return getLivingWithinRadius(radius, x, y, z, world);
    }

    public static boolean attackEntityWithoutKnockback(Entity entity, net.minecraft.world.damagesource.DamageSource source, float amount) {
        Vec3 originalVec = entity.getDeltaMovement();
        boolean succeeded = entity.hurt(source, amount);
        entity.setDeltaMovement(originalVec);
        return succeeded;
    }

    public static Entity getRider(Entity entity) {
        return !entity.getPassengers().isEmpty() ? entity.getPassengers().get(0) : null;
    }

    public static <T extends Entity> List<T> getEntitiesWithinCylinder(double radius, double x, double y, double z, double height, Level world, Class<T> entityType) {
        AABB aabb = new AABB(x - radius, y, z - radius, x + radius, y + height, z + radius);
        List<T> entityList = new java.util.ArrayList<>(world.getEntitiesOfClass(entityType, aabb));
        double radiusSq = radius * radius;
        entityList.removeIf(entity -> entity.distanceToSqr(x, entity.getY(), z) > radiusSq);
        return entityList;
    }

    public static List<LivingEntity> getLivingWithinCylinder(double radius, double x, double y, double z, double height, Level world) {
        return getEntitiesWithinCylinder(radius, x, y, z, height, world, LivingEntity.class);
    }

    public static boolean canDamageBlocks(LivingEntity entity, Level world) {
        if (entity instanceof Player player) {
            return player.mayBuild() && !player.isSpectator();
        }
        return true;
    }

    public static Vec3 findSpaceForTeleport(Entity entity, Vec3 destination, boolean accountForPassengers) {
        Level world = entity.level();
        AABB box = entity.getBoundingBox();

        if (accountForPassengers) {
            for (Entity passenger : entity.getPassengers()) {
                box = box.minmax(passenger.getBoundingBox());
            }
        }

        box = box.move(destination.subtract(entity.getX(), entity.getY(), entity.getZ()));

        Iterable<BlockPos> cuboid = BlockPos.betweenClosed(Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ), Mth.floor(box.maxX), Mth.floor(box.maxY), Mth.floor(box.maxZ));

        boolean allNoCollision = true;
        for (BlockPos b : cuboid) {
            if (!world.noCollision(new AABB(b))) {
                allNoCollision = false;
                break;
            }
        }

        if (allNoCollision) {
            return destination;
        } else {
            double dx = box.maxX - box.minX;
            double dy = box.maxY - box.minY;
            double dz = box.maxZ - box.minZ;

            int nx = Mth.ceil(dx) / 2;
            int px = Mth.ceil(dx) - nx;
            int ny = Mth.ceil(dy) / 2;
            int py = Mth.ceil(dy) - ny;
            int nz = Mth.ceil(dz) / 2;
            int pz = Mth.ceil(dz) - nz;

            List<BlockPos> nearby = new java.util.ArrayList<>();
            for (BlockPos p : BlockPos.betweenClosed(Mth.floor(box.minX) - 1, Mth.floor(box.minY) - 1, Mth.floor(box.minZ) - 1, Mth.floor(box.maxX) + 1, Mth.floor(box.maxY) + 1, Mth.floor(box.maxZ) + 1)) {
                nearby.add(p.immutable());
            }

            List<BlockPos> possiblePositions = new java.util.ArrayList<>();
            for (BlockPos p : cuboid) {
                possiblePositions.add(p.immutable());
            }

            while (!nearby.isEmpty()) {
                BlockPos pos = nearby.remove(0);

                if (!world.noCollision(new AABB(pos))) {
                    Predicate<BlockPos> nearSolidBlock = b -> b.getX() >= pos.getX() - nx && b.getX() <= pos.getX() + px && b.getY() >= pos.getY() - ny && b.getY() <= pos.getY() + py && b.getZ() >= pos.getZ() - nz && b.getZ() <= pos.getZ() + pz;
                    nearby.removeIf(nearSolidBlock);
                    possiblePositions.removeIf(nearSolidBlock);
                }
            }

            if (possiblePositions.isEmpty()) return null;

            BlockPos nearest = possiblePositions.stream().min(Comparator.comparingDouble(b -> destination.distanceToSqr(b.getX() + 0.5, b.getY() + 0.5, b.getZ() + 0.5))).orElse(null);
            if (nearest == null) return null;

            return VecUtils.getFaceCentre(nearest, Direction.DOWN);
        }
    }

    private EntityUtil() {}
}
