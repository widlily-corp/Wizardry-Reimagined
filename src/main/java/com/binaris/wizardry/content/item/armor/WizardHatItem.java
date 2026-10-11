package com.binaris.wizardry.content.item.armor;

import com.binaris.wizardry.registry.Element;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import org.jetbrains.annotations.Nullable;

public class WizardHatItem extends WizardArmorItem {
    public static final int DURABILITY = 200;

    public WizardHatItem(Holder<ArmorMaterial> material, @Nullable java.util.function.Supplier<Element> elementSupplier, Properties properties) {
        super(material, ArmorItem.Type.HELMET, elementSupplier, DURABILITY, properties);
    }

    public WizardHatItem(Holder<ArmorMaterial> material, Properties properties) {
        this(material, (java.util.function.Supplier<Element>) null, properties);
    }
}
