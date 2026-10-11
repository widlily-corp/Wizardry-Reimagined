package com.binaris.wizardry.content.item.armor;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class EBArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, WizardryMod.MODID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> WIZARD = ARMOR_MATERIALS.register("wizard", () -> {
        Map<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        defense.put(ArmorItem.Type.HELMET, 2);
        defense.put(ArmorItem.Type.CHESTPLATE, 5);
        defense.put(ArmorItem.Type.LEGGINGS, 4);
        defense.put(ArmorItem.Type.BOOTS, 2);

        List<ArmorMaterial.Layer> layers = List.of(
                new ArmorMaterial.Layer(WizardryMod.id("wizard"))
        );

        return new ArmorMaterial(
                defense,
                15,
                SoundEvents.ARMOR_EQUIP_GENERIC,
                () -> Ingredient.EMPTY,
                layers,
                0.0F,
                0.0F
        );
    });

    public static void register(IEventBus modBus) {
        ARMOR_MATERIALS.register(modBus);
    }

    private EBArmorMaterials() {}
}
