package com.binaris.wizardry.content.item.armor;

import com.binaris.wizardry.registry.Element;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import org.jetbrains.annotations.Nullable;

public class WizardRobeItem extends WizardArmorItem {
    public static final int DURABILITY = 300;

    public WizardRobeItem(Holder<ArmorMaterial> material, @Nullable java.util.function.Supplier<Element> elementSupplier, Properties properties) {
        super(material, ArmorItem.Type.CHESTPLATE, elementSupplier, DURABILITY, properties);
    }

    public WizardRobeItem(Holder<ArmorMaterial> material, Properties properties) {
        this(material, (java.util.function.Supplier<Element>) null, properties);
    }
}
