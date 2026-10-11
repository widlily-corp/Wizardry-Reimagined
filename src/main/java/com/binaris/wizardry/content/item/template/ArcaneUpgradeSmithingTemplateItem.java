package com.binaris.wizardry.content.item.template;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class ArcaneUpgradeSmithingTemplateItem extends SmithingTemplateItem {
    private static final Component APPLIES_TO = Component.translatable(
            Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "smithing_template.arcane_upgrade.applies_to"))
    ).withStyle(ChatFormatting.BLUE);

    private static final Component INGREDIENTS = Component.translatable(
            Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "smithing_template.arcane_upgrade.ingredients"))
    ).withStyle(ChatFormatting.BLUE);

    private static final Component UPGRADE_DESC = Component.translatable(
            Util.makeDescriptionId("upgrade", ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "arcane_upgrade"))
    ).withStyle(ChatFormatting.GRAY);

    private static final Component BASE_SLOT_DESC = Component.translatable(
            Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "smithing_template.arcane_upgrade.base_slot_description"))
    );

    private static final Component ADDITIONS_SLOT_DESC = Component.translatable(
            Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "smithing_template.arcane_upgrade.additions_slot_description"))
    );

    private static final List<ResourceLocation> BASE_SLOT_ICONS = List.of(
            ResourceLocation.withDefaultNamespace("item/empty_slot_sword")
    );

    private static final List<ResourceLocation> ADDITIONS_SLOT_ICONS = List.of(
            ResourceLocation.withDefaultNamespace("item/empty_slot_diamond"),
            ResourceLocation.withDefaultNamespace("item/empty_slot_ingot")
    );

    public ArcaneUpgradeSmithingTemplateItem() {
        super(APPLIES_TO, INGREDIENTS, UPGRADE_DESC, BASE_SLOT_DESC, ADDITIONS_SLOT_DESC, BASE_SLOT_ICONS, ADDITIONS_SLOT_ICONS);
    }
}
