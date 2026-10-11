package com.binaris.wizardry.content.block.runestone;

import com.binaris.wizardry.registry.Element;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class RunestonePedestalBlock extends Block {
    @Nullable
    private final Supplier<Element> elementSupplier;

    public RunestonePedestalBlock(@Nullable Supplier<Element> elementSupplier) {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(4.0F, 6.0F)
                .sound(SoundType.STONE)
                .noOcclusion()
                .requiresCorrectToolForDrops());
        this.elementSupplier = elementSupplier;
    }

    public RunestonePedestalBlock() {
        this(null);
    }

    @Nullable
    public Element getElement() {
        return elementSupplier != null ? elementSupplier.get() : null;
    }
}
