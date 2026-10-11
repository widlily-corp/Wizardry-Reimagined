package com.binaris.wizardry.content.block.crystal;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class CrystalOreBlock extends DropExperienceBlock {
    public CrystalOreBlock(boolean deepslate) {
        super(UniformInt.of(4, 8), BlockBehaviour.Properties.of()
                .mapColor(deepslate ? MapColor.DEEPSLATE : MapColor.STONE)
                .strength(deepslate ? 4.5f : 3.0f, 3.0f)
                .sound(deepslate ? SoundType.DEEPSLATE : SoundType.STONE)
                .requiresCorrectToolForDrops());
    }
}
