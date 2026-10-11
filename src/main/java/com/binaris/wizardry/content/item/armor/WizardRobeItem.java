package com.binaris.wizardry.content.item.armor;

import com.binaris.wizardry.registry.Element;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import org.jetbrains.annotations.Nullable;

public class WizardRobeItem extends WizardArmorItem {
    public static final int DURABILITY = 300;

    public WizardRobeItem(Holder<ArmorMaterial> material, @Nullable Element element, Properties properties) {
        super(material, ArmorItem.Type.CHESTPLATE, element, DURABILITY, properties);
    }
}
