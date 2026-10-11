package com.binaris.wizardry.content.item.armor;

import com.binaris.wizardry.registry.Element;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import org.jetbrains.annotations.Nullable;

public class WizardBootsItem extends WizardArmorItem {
    public static final int DURABILITY = 180;

    public WizardBootsItem(Holder<ArmorMaterial> material, @Nullable java.util.function.Supplier<Element> elementSupplier, Properties properties) {
        super(material, ArmorItem.Type.BOOTS, elementSupplier, DURABILITY, properties);
    }

    public WizardBootsItem(Holder<ArmorMaterial> material, Properties properties) {
        this(material, (java.util.function.Supplier<Element>) null, properties);
    }
}
