package com.binaris.wizardry.api.content.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public final class BlockUtil {

    public static boolean canBlockBeReplaced(Level world, BlockPos pos) {
        return canBlockBeReplaced(world, pos, false);
    }

    public static boolean canBlockBeReplaced(Level world, BlockPos pos, boolean excludeLiquids) {
        BlockState state = world.getBlockState(pos);
        return (world.isEmptyBlock(pos) || state.canBeReplaced()) && (!excludeLiquids || !state.liquid());
    }

    public static List<BlockPos> getBlockSphere(BlockPos centre, double radius) {
        List<BlockPos> sphere = new ArrayList<>((int) Math.pow(radius * 2, 3));
        int r = (int) Math.ceil(radius);

        for (int i = -r; i <= r; i++) {
            float r1 = Mth.sqrt((float) Math.max(0, radius * radius - i * i));
            for (int j = -(int) r1; j <= r1; j++) {
                float r2 = Mth.sqrt((float) Math.max(0, radius * radius - i * i - j * j));
                for (int k = -(int) r2; k <= r2; k++) {
                    sphere.add(centre.offset(i, j, k));
                }
            }
        }
        return sphere;
    }

    public static @Nullable Integer getNearestFloor(Level world, BlockPos pos, int range) {
        for (int y = 0; y <= range; y++) {
            BlockPos checkDown = pos.below(y);
            if (world.getBlockState(checkDown).blocksMotion() && world.getBlockState(checkDown.above()).canBeReplaced()) {
                return checkDown.getY() + 1;
            }
            BlockPos checkUp = pos.above(y);
            if (world.getBlockState(checkUp).blocksMotion() && world.getBlockState(checkUp.above()).canBeReplaced()) {
                return checkUp.getY() + 1;
            }
        }
        return null;
    }

    public static @Nullable BlockPos findNearbyFloorSpace(net.minecraft.world.entity.LivingEntity entity, int horizontalRange, int verticalRange) {
        Level world = entity.level();
        BlockPos basePos = entity.blockPosition();
        for (int tries = 0; tries < 20; tries++) {
            int dx = world.random.nextInt(horizontalRange * 2 + 1) - horizontalRange;
            int dz = world.random.nextInt(horizontalRange * 2 + 1) - horizontalRange;
            for (int dy = verticalRange; dy >= -verticalRange; dy--) {
                BlockPos check = basePos.offset(dx, dy, dz);
                if (world.getBlockState(check).blocksMotion() && world.getBlockState(check.above()).canBeReplaced() && world.getBlockState(check.above(2)).canBeReplaced()) {
                    return check.above();
                }
            }
        }
        return null;
    }

    public static @Nullable BlockPos findNearbyFloorSpace(Level world, BlockPos origin, int horizontalRange, int verticalRange, boolean lineOfSight) {
        for (int tries = 0; tries < 20; tries++) {
            int dx = world.random.nextInt(horizontalRange * 2 + 1) - horizontalRange;
            int dz = world.random.nextInt(horizontalRange * 2 + 1) - horizontalRange;
            for (int dy = verticalRange; dy >= -verticalRange; dy--) {
                BlockPos check = origin.offset(dx, dy, dz);
                if (world.getBlockState(check).blocksMotion() && world.getBlockState(check.above()).canBeReplaced() && world.getBlockState(check.above(2)).canBeReplaced()) {
                    return check.above();
                }
            }
        }
        return null;
    }

    public static boolean canPlaceBlock(LivingEntity placer, Level world, BlockPos pos) {
        if (world.isClientSide) return true;
        if (!EntityUtil.canDamageBlocks(placer, world)) return false;
        if (world.isOutsideBuildHeight(pos)) return false;
        return !(placer instanceof Player player) || world.mayInteract(player, pos);
    }

    public static boolean freeze(Level world, BlockPos pos, boolean freezeLava) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (state.getBlock() == Blocks.WATER && state.getValue(LiquidBlock.LEVEL) == 0) {
            world.setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
        } else if (freezeLava && state.getBlock() == Blocks.LAVA && state.getValue(LiquidBlock.LEVEL) == 0) {
            world.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
        } else if (freezeLava && (block == Blocks.LAVA)) {
            world.setBlockAndUpdate(pos, Blocks.COBBLESTONE.defaultBlockState());
        } else if (canBlockBeReplaced(world, pos.above()) && Blocks.SNOW.defaultBlockState().canSurvive(world, pos.above())) {
            world.setBlockAndUpdate(pos.above(), Blocks.SNOW.defaultBlockState());
        } else {
            return false;
        }

        return true;
    }

    public static boolean isBlockPassable(Level world, BlockPos checkPos) {
        return world.isEmptyBlock(checkPos) || world.getBlockState(checkPos).canBeReplaced();
    }

    public static boolean canBreak(Player player, Level level, BlockPos pos, boolean powerful) {
        if (level.isClientSide) return true;
        if (player.isCreative()) return true;
        if (!player.mayBuild()) return false;
        if (level.isOutsideBuildHeight(pos)) return false;

        BlockState state = level.getBlockState(pos);
        if (state.getDestroySpeed(level, pos) < 0) return false;
        if (player.isSpectator()) return false;

        if (state.getDestroySpeed(level, pos) > 3.0f && !powerful) {
            return false;
        } else {
            return level.mayInteract(player, pos);
        }
    }

    @Nullable
    public static Integer getNearestSurface(Level world, BlockPos pos, Direction direction, int range, boolean doubleSided, SurfaceCriteria criteria) {
        Integer surface = null;
        int currentBest = Integer.MAX_VALUE;

        for (int i = doubleSided ? -range : 0; i <= range && i < currentBest; i++) {
            BlockPos testPos = pos.relative(direction, i);

            if (criteria.test(world, testPos, direction)) {
                surface = (int) VecUtils.getFaceCentre(testPos, direction).get(direction.getAxis());
                currentBest = Math.abs(i);
            }
        }

        return surface;
    }

    public interface SurfaceCriteria {
        SurfaceCriteria COLLIDABLE = basedOn(BlockBehaviour.BlockStateBase::blocksMotion);

        SurfaceCriteria BUILDABLE = (world, pos, side) -> world.getBlockState(pos).isFaceSturdy(world, pos, side) && world.getBlockState(pos.relative(side)).canBeReplaced();

        SurfaceCriteria SOLID_LIQUID_TO_AIR = (world, pos, side) -> (world.getBlockState(pos).liquid() || world.getBlockState(pos).isFaceSturdy(world, pos, side) && world.isEmptyBlock(pos.relative(side)));

        SurfaceCriteria NOT_AIR_TO_AIR = basedOn(Level::isEmptyBlock).flip();

        static SurfaceCriteria basedOn(BiPredicate<Level, BlockPos> condition) {
            return (world, pos, side) -> condition.test(world, pos) && !condition.test(world, pos.relative(side));
        }

        static SurfaceCriteria basedOn(Predicate<BlockState> condition) {
            return (world, pos, side) -> condition.test(world.getBlockState(pos)) && !condition.test(world.getBlockState(pos.relative(side)));
        }

        boolean test(Level world, BlockPos pos, Direction side);

        default SurfaceCriteria flip() {
            return (world, pos, side) -> this.test(world, pos.relative(side), side.getOpposite());
        }
    }

    private BlockUtil() {}
}
