package com.binaris.wizardry.client.gui;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.component.UpgradeData;
import com.binaris.wizardry.content.item.wand.WandItem;
import com.binaris.wizardry.content.item.wand.WandUpgrades;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.Spells;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = WizardryMod.MODID, value = Dist.CLIENT)
public final class SpellTooltipClientEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof WandItem wand) {
            List<Component> tooltip = event.getToolTip();
            if (Screen.hasShiftDown()) {
                tooltip.add(Component.empty());
                Spell[] spells = wand.getSpells(stack);
                int selected = wand.getWandData(stack).selectedSpell();
                int boundCount = 0;
                for (Spell s : spells) {
                    if (s != Spells.NONE.get()) boundCount++;
                }

                tooltip.add(Component.translatable("tooltip.ebwizardry.wand.spells_bound", boundCount, wand.getSpellSlotCount(stack))
                        .withStyle(ChatFormatting.YELLOW));
                for (int i = 0; i < spells.length; i++) {
                    Spell s = spells[i];
                    boolean isSelected = (i == selected);
                    Component prefix = Component.literal(" [" + (i + 1) + "] ").withStyle(isSelected ? ChatFormatting.GREEN : ChatFormatting.GRAY);
                    Component spellName = (s != null && s != Spells.NONE.get())
                            ? s.getDescriptionFormatted()
                            : Component.translatable("tooltip.ebwizardry.wand.empty_slot").withStyle(ChatFormatting.DARK_GRAY);
                    if (isSelected) {
                        tooltip.add(prefix.copy().append(spellName).append(Component.literal(" ◄").withStyle(ChatFormatting.GREEN)));
                    } else {
                        tooltip.add(prefix.copy().append(spellName));
                    }
                }

                UpgradeData upgradeData = wand.getUpgradeData(stack);
                int totalUpgrades = wand.getTotalUpgrades(stack);
                int maxUpgrades = wand.getTier().getUpgradeLimit() + (wand.getElement() == Elements.MAGIC.get() ? WandItem.NON_ELEMENTAL_UPGRADE_BONUS : 0);
                tooltip.add(Component.empty());
                tooltip.add(Component.translatable("tooltip.ebwizardry.wand.upgrades", totalUpgrades, maxUpgrades)
                        .withStyle(ChatFormatting.AQUA));
                if (totalUpgrades > 0) {
                    for (String upId : WandUpgrades.ALL_UPGRADE_IDS) {
                        int lvl = upgradeData.getUpgradeLevel(upId);
                        if (lvl > 0) {
                            tooltip.add(Component.literal(" • ")
                                    .append(Component.translatable("item.ebwizardry." + upId + "_upgrade"))
                                    .append(Component.literal(" " + toRoman(lvl)).withStyle(ChatFormatting.YELLOW))
                                    .withStyle(ChatFormatting.DARK_AQUA));
                        }
                    }
                } else {
                    tooltip.add(Component.literal(" • ").append(Component.translatable("tooltip.ebwizardry.wand.no_upgrades")).withStyle(ChatFormatting.DARK_GRAY));
                }
            } else {
                tooltip.add(Component.translatable("tooltip.ebwizardry.hold_shift_for_details").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            }
        }
    }

    private static String toRoman(int n) {
        return switch (n) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> String.valueOf(n);
        };
    }

    private SpellTooltipClientEvents() {}
}
