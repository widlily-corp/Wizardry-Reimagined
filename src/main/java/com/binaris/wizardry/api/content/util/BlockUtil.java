package com.binaris.wizardry.api.content.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    private BlockUtil() {}
}
