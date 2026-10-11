package com.binaris.wizardry.content.item.armor;

import com.binaris.wizardry.registry.Element;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import org.jetbrains.annotations.Nullable;

public class WizardBootsItem extends WizardArmorItem {
    public static final int DURABILITY = 180;

    public WizardBootsItem(Holder<ArmorMaterial> material, @Nullable Element element, Properties properties) {
        super(material, ArmorItem.Type.BOOTS, element, DURABILITY, properties);
    }
}
