package com.binaris.wizardry.content.block.crystal;

import com.binaris.wizardry.registry.Element;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class CrystalBlock extends DropExperienceBlock {
    @Nullable
    private final Element element;

    public CrystalBlock(MapColor color, @Nullable Element element) {
        super(UniformInt.of(0, 2), BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(5.0f, 10.0f)
                .sound(SoundType.AMETHYST)
                .requiresCorrectToolForDrops());
        this.element = element;
    }

    @Nullable
    public Element getElement() {
        return element;
    }
}
