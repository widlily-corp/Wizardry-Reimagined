package com.binaris.wizardry.content.block.crystal;

import com.binaris.wizardry.registry.Element;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CrystalBlock extends DropExperienceBlock {
    @Nullable
    private final Supplier<Element> elementSupplier;

    public CrystalBlock(MapColor color, @Nullable Supplier<Element> elementSupplier) {
        super(UniformInt.of(0, 2), BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(5.0f, 10.0f)
                .sound(SoundType.AMETHYST)
                .requiresCorrectToolForDrops());
        this.elementSupplier = elementSupplier;
    }

    public CrystalBlock(MapColor color, @Nullable Element element) {
        this(color, element != null ? () -> element : null);
    }

    @Nullable
    public Element getElement() {
        return elementSupplier != null ? elementSupplier.get() : null;
    }
}
