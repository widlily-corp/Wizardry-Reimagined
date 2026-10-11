package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.armor.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class EBArmorItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WizardryMod.MODID);

    // Neutral Wizard Armor
    public static final DeferredItem<WizardHatItem> WIZARD_HAT =
            ITEMS.register("wizard_hat", () -> new WizardHatItem(EBArmorMaterials.WIZARD, new Item.Properties()));
    public static final DeferredItem<WizardRobeItem> WIZARD_ROBE =
            ITEMS.register("wizard_robe", () -> new WizardRobeItem(EBArmorMaterials.WIZARD, new Item.Properties()));
    public static final DeferredItem<WizardLeggingsItem> WIZARD_LEGGINGS =
            ITEMS.register("wizard_leggings", () -> new WizardLeggingsItem(EBArmorMaterials.WIZARD, new Item.Properties()));
    public static final DeferredItem<WizardBootsItem> WIZARD_BOOTS =
            ITEMS.register("wizard_boots", () -> new WizardBootsItem(EBArmorMaterials.WIZARD, new Item.Properties()));

    // Earth Wizard Armor
    public static final DeferredItem<WizardHatItem> WIZARD_HAT_EARTH =
            ITEMS.register("wizard_hat_earth", () -> new WizardHatItem(EBArmorMaterials.WIZARD, Elements.EARTH, new Item.Properties()));
    public static final DeferredItem<WizardRobeItem> WIZARD_ROBE_EARTH =
            ITEMS.register("wizard_robe_earth", () -> new WizardRobeItem(EBArmorMaterials.WIZARD, Elements.EARTH, new Item.Properties()));
    public static final DeferredItem<WizardLeggingsItem> WIZARD_LEGGINGS_EARTH =
            ITEMS.register("wizard_leggings_earth", () -> new WizardLeggingsItem(EBArmorMaterials.WIZARD, Elements.EARTH, new Item.Properties()));
    public static final DeferredItem<WizardBootsItem> WIZARD_BOOTS_EARTH =
            ITEMS.register("wizard_boots_earth", () -> new WizardBootsItem(EBArmorMaterials.WIZARD, Elements.EARTH, new Item.Properties()));

    // Fire Wizard Armor
    public static final DeferredItem<WizardHatItem> WIZARD_HAT_FIRE =
            ITEMS.register("wizard_hat_fire", () -> new WizardHatItem(EBArmorMaterials.WIZARD, Elements.FIRE, new Item.Properties()));
    public static final DeferredItem<WizardRobeItem> WIZARD_ROBE_FIRE =
            ITEMS.register("wizard_robe_fire", () -> new WizardRobeItem(EBArmorMaterials.WIZARD, Elements.FIRE, new Item.Properties()));
    public static final DeferredItem<WizardLeggingsItem> WIZARD_LEGGINGS_FIRE =
            ITEMS.register("wizard_leggings_fire", () -> new WizardLeggingsItem(EBArmorMaterials.WIZARD, Elements.FIRE, new Item.Properties()));
    public static final DeferredItem<WizardBootsItem> WIZARD_BOOTS_FIRE =
            ITEMS.register("wizard_boots_fire", () -> new WizardBootsItem(EBArmorMaterials.WIZARD, Elements.FIRE, new Item.Properties()));

    // Healing Wizard Armor
    public static final DeferredItem<WizardHatItem> WIZARD_HAT_HEALING =
            ITEMS.register("wizard_hat_healing", () -> new WizardHatItem(EBArmorMaterials.WIZARD, Elements.HEALING, new Item.Properties()));
    public static final DeferredItem<WizardRobeItem> WIZARD_ROBE_HEALING =
            ITEMS.register("wizard_robe_healing", () -> new WizardRobeItem(EBArmorMaterials.WIZARD, Elements.HEALING, new Item.Properties()));
    public static final DeferredItem<WizardLeggingsItem> WIZARD_LEGGINGS_HEALING =
            ITEMS.register("wizard_leggings_healing", () -> new WizardLeggingsItem(EBArmorMaterials.WIZARD, Elements.HEALING, new Item.Properties()));
    public static final DeferredItem<WizardBootsItem> WIZARD_BOOTS_HEALING =
            ITEMS.register("wizard_boots_healing", () -> new WizardBootsItem(EBArmorMaterials.WIZARD, Elements.HEALING, new Item.Properties()));

    // Ice Wizard Armor
    public static final DeferredItem<WizardHatItem> WIZARD_HAT_ICE =
            ITEMS.register("wizard_hat_ice", () -> new WizardHatItem(EBArmorMaterials.WIZARD, Elements.ICE, new Item.Properties()));
    public static final DeferredItem<WizardRobeItem> WIZARD_ROBE_ICE =
            ITEMS.register("wizard_robe_ice", () -> new WizardRobeItem(EBArmorMaterials.WIZARD, Elements.ICE, new Item.Properties()));
    public static final DeferredItem<WizardLeggingsItem> WIZARD_LEGGINGS_ICE =
            ITEMS.register("wizard_leggings_ice", () -> new WizardLeggingsItem(EBArmorMaterials.WIZARD, Elements.ICE, new Item.Properties()));
    public static final DeferredItem<WizardBootsItem> WIZARD_BOOTS_ICE =
            ITEMS.register("wizard_boots_ice", () -> new WizardBootsItem(EBArmorMaterials.WIZARD, Elements.ICE, new Item.Properties()));

    // Lightning Wizard Armor
    public static final DeferredItem<WizardHatItem> WIZARD_HAT_LIGHTNING =
            ITEMS.register("wizard_hat_lightning", () -> new WizardHatItem(EBArmorMaterials.WIZARD, Elements.LIGHTNING, new Item.Properties()));
    public static final DeferredItem<WizardRobeItem> WIZARD_ROBE_LIGHTNING =
            ITEMS.register("wizard_robe_lightning", () -> new WizardRobeItem(EBArmorMaterials.WIZARD, Elements.LIGHTNING, new Item.Properties()));
    public static final DeferredItem<WizardLeggingsItem> WIZARD_LEGGINGS_LIGHTNING =
            ITEMS.register("wizard_leggings_lightning", () -> new WizardLeggingsItem(EBArmorMaterials.WIZARD, Elements.LIGHTNING, new Item.Properties()));
    public static final DeferredItem<WizardBootsItem> WIZARD_BOOTS_LIGHTNING =
            ITEMS.register("wizard_boots_lightning", () -> new WizardBootsItem(EBArmorMaterials.WIZARD, Elements.LIGHTNING, new Item.Properties()));

    // Necromancy Wizard Armor
    public static final DeferredItem<WizardHatItem> WIZARD_HAT_NECROMANCY =
            ITEMS.register("wizard_hat_necromancy", () -> new WizardHatItem(EBArmorMaterials.WIZARD, Elements.NECROMANCY, new Item.Properties()));
    public static final DeferredItem<WizardRobeItem> WIZARD_ROBE_NECROMANCY =
            ITEMS.register("wizard_robe_necromancy", () -> new WizardRobeItem(EBArmorMaterials.WIZARD, Elements.NECROMANCY, new Item.Properties()));
    public static final DeferredItem<WizardLeggingsItem> WIZARD_LEGGINGS_NECROMANCY =
            ITEMS.register("wizard_leggings_necromancy", () -> new WizardLeggingsItem(EBArmorMaterials.WIZARD, Elements.NECROMANCY, new Item.Properties()));
    public static final DeferredItem<WizardBootsItem> WIZARD_BOOTS_NECROMANCY =
            ITEMS.register("wizard_boots_necromancy", () -> new WizardBootsItem(EBArmorMaterials.WIZARD, Elements.NECROMANCY, new Item.Properties()));

    // Sorcery Wizard Armor
    public static final DeferredItem<WizardHatItem> WIZARD_HAT_SORCERY =
            ITEMS.register("wizard_hat_sorcery", () -> new WizardHatItem(EBArmorMaterials.WIZARD, Elements.SORCERY, new Item.Properties()));
    public static final DeferredItem<WizardRobeItem> WIZARD_ROBE_SORCERY =
            ITEMS.register("wizard_robe_sorcery", () -> new WizardRobeItem(EBArmorMaterials.WIZARD, Elements.SORCERY, new Item.Properties()));
    public static final DeferredItem<WizardLeggingsItem> WIZARD_LEGGINGS_SORCERY =
            ITEMS.register("wizard_leggings_sorcery", () -> new WizardLeggingsItem(EBArmorMaterials.WIZARD, Elements.SORCERY, new Item.Properties()));
    public static final DeferredItem<WizardBootsItem> WIZARD_BOOTS_SORCERY =
            ITEMS.register("wizard_boots_sorcery", () -> new WizardBootsItem(EBArmorMaterials.WIZARD, Elements.SORCERY, new Item.Properties()));

    public static Item getArmorPiece(@Nullable Element element, EquipmentSlot slot) {
        if (element != null && "earth".equals(element.getName())) {
            return switch (slot) {
                case HEAD -> WIZARD_HAT_EARTH.get();
                case CHEST -> WIZARD_ROBE_EARTH.get();
                case LEGS -> WIZARD_LEGGINGS_EARTH.get();
                case FEET -> WIZARD_BOOTS_EARTH.get();
                default -> WIZARD_ROBE_EARTH.get();
            };
        } else if (element != null && "fire".equals(element.getName())) {
            return switch (slot) {
                case HEAD -> WIZARD_HAT_FIRE.get();
                case CHEST -> WIZARD_ROBE_FIRE.get();
                case LEGS -> WIZARD_LEGGINGS_FIRE.get();
                case FEET -> WIZARD_BOOTS_FIRE.get();
                default -> WIZARD_ROBE_FIRE.get();
            };
        } else if (element != null && "healing".equals(element.getName())) {
            return switch (slot) {
                case HEAD -> WIZARD_HAT_HEALING.get();
                case CHEST -> WIZARD_ROBE_HEALING.get();
                case LEGS -> WIZARD_LEGGINGS_HEALING.get();
                case FEET -> WIZARD_BOOTS_HEALING.get();
                default -> WIZARD_ROBE_HEALING.get();
            };
        } else if (element != null && "ice".equals(element.getName())) {
            return switch (slot) {
                case HEAD -> WIZARD_HAT_ICE.get();
                case CHEST -> WIZARD_ROBE_ICE.get();
                case LEGS -> WIZARD_LEGGINGS_ICE.get();
                case FEET -> WIZARD_BOOTS_ICE.get();
                default -> WIZARD_ROBE_ICE.get();
            };
        } else if (element != null && "lightning".equals(element.getName())) {
            return switch (slot) {
                case HEAD -> WIZARD_HAT_LIGHTNING.get();
                case CHEST -> WIZARD_ROBE_LIGHTNING.get();
                case LEGS -> WIZARD_LEGGINGS_LIGHTNING.get();
                case FEET -> WIZARD_BOOTS_LIGHTNING.get();
                default -> WIZARD_ROBE_LIGHTNING.get();
            };
        } else if (element != null && "necromancy".equals(element.getName())) {
            return switch (slot) {
                case HEAD -> WIZARD_HAT_NECROMANCY.get();
                case CHEST -> WIZARD_ROBE_NECROMANCY.get();
                case LEGS -> WIZARD_LEGGINGS_NECROMANCY.get();
                case FEET -> WIZARD_BOOTS_NECROMANCY.get();
                default -> WIZARD_ROBE_NECROMANCY.get();
            };
        } else if (element != null && "sorcery".equals(element.getName())) {
            return switch (slot) {
                case HEAD -> WIZARD_HAT_SORCERY.get();
                case CHEST -> WIZARD_ROBE_SORCERY.get();
                case LEGS -> WIZARD_LEGGINGS_SORCERY.get();
                case FEET -> WIZARD_BOOTS_SORCERY.get();
                default -> WIZARD_ROBE_SORCERY.get();
            };
        } else {
            return switch (slot) {
                case HEAD -> WIZARD_HAT.get();
                case CHEST -> WIZARD_ROBE.get();
                case LEGS -> WIZARD_LEGGINGS.get();
                case FEET -> WIZARD_BOOTS.get();
                default -> WIZARD_ROBE.get();
            };
        }
    }

    public static void register(IEventBus modBus) {
        EBArmorMaterials.register(modBus);
        ITEMS.register(modBus);
    }

    private EBArmorItems() {}
}
