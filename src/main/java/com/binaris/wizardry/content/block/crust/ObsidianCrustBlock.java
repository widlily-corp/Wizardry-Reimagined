package com.binaris.wizardry.content.block.crust;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class ObsidianCrustBlock extends Block {
    public static final MapCodec<ObsidianCrustBlock> CODEC = simpleCodec(ObsidianCrustBlock::new);
    public static final IntegerProperty CRUSH_LEVEL = IntegerProperty.create("crush_level", 0, 3);

    public ObsidianCrustBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CRUSH_LEVEL, 0));
    }

    public ObsidianCrustBlock() {
        this(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BLACK)
                .strength(3.0F, 10.0F)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops());
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide && entity instanceof net.minecraft.world.entity.LivingEntity living && living.onGround() && !living.isSteppingCarefully()) {
            if (!(living instanceof net.minecraft.world.entity.player.Player player && player.isCreative())) {
                if (level.getRandom().nextInt(30) == 0) {
                    int crushLevel = state.getValue(CRUSH_LEVEL);
                    if (crushLevel < 3) {
                        level.setBlock(pos, state.setValue(CRUSH_LEVEL, crushLevel + 1), Block.UPDATE_ALL);
                    } else {
                        level.destroyBlock(pos, false);
                    }
                }
            }
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CRUSH_LEVEL);
    }
}
