package com.binaris.wizardry.content.item.material;

import com.binaris.wizardry.registry.Element;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class CrystalItem extends Item {
    private final int manaCapacity;
    @Nullable
    private final Element element;

    public CrystalItem(int manaCapacity, @Nullable Element element, Properties properties) {
        super(properties);
        this.manaCapacity = manaCapacity;
        this.element = element;
    }

    public CrystalItem(int manaCapacity, @Nullable Element element) {
        this(manaCapacity, element, new Properties());
    }

    public CrystalItem(int manaCapacity) {
        this(manaCapacity, null, new Properties());
    }

    public int getManaCapacity() {
        return manaCapacity;
    }

    @Nullable
    public Element getElement() {
        return element;
    }
}
