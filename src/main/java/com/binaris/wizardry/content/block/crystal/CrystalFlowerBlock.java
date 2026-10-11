package com.binaris.wizardry.content.block.crystal;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

public class CrystalFlowerBlock extends FlowerBlock implements BonemealableBlock {
    public static final MapCodec<CrystalFlowerBlock> CODEC = simpleCodec(CrystalFlowerBlock::new);

    public CrystalFlowerBlock(BlockBehaviour.Properties properties) {
        super(MobEffects.REGENERATION, 7.0f, properties);
    }

    public CrystalFlowerBlock() {
        this(BlockBehaviour.Properties.of()
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)
                .lightLevel(state -> 15)
                .pushReaction(PushReaction.DESTROY));
    }

    @Override
    public MapCodec<? extends FlowerBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (world.isClientSide && random.nextBoolean()) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble() * 0.5 + 0.5;
            double z = pos.getZ() + random.nextDouble();
            world.addParticle(ParticleTypes.ENCHANT, x, y, z, 0.0, 0.05, 0.0);
        }
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        BlockPos targetPos = pos.offset(
                random.nextInt(7) - 3,
                random.nextInt(3) - 1,
                random.nextInt(7) - 3
        );

        if (level.isEmptyBlock(targetPos) && state.canSurvive(level, targetPos)) {
            level.setBlock(targetPos, state, 3);
        }
    }
}
