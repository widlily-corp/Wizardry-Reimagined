package com.binaris.wizardry.content.item.material;

import com.binaris.wizardry.registry.Element;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CrystalItem extends Item {
    private final int manaCapacity;
    @Nullable
    private final Supplier<Element> elementSupplier;

    public CrystalItem(int manaCapacity, @Nullable Supplier<Element> elementSupplier, Properties properties) {
        super(properties);
        this.manaCapacity = manaCapacity;
        this.elementSupplier = elementSupplier;
    }

    public CrystalItem(int manaCapacity, @Nullable Supplier<Element> elementSupplier) {
        this(manaCapacity, elementSupplier, new Properties());
    }

    public CrystalItem(int manaCapacity, @Nullable Element element, Properties properties) {
        this(manaCapacity, element != null ? () -> element : null, properties);
    }

    public CrystalItem(int manaCapacity, @Nullable Element element) {
        this(manaCapacity, element != null ? () -> element : null, new Properties());
    }

    public CrystalItem(int manaCapacity) {
        this(manaCapacity, (Supplier<Element>) null, new Properties());
    }

    public int getManaCapacity() {
        return manaCapacity;
    }

    @Nullable
    public Element getElement() {
        return elementSupplier != null ? elementSupplier.get() : null;
    }
}
