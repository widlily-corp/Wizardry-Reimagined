package com.binaris.wizardry.client.gui;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.wand.IManaItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;

public class TooltipElementText extends TooltipElement {
    private final String text;
    private final Style style;

    public TooltipElementText(String text, Style style, int spaceAfter, TooltipElement... children) {
        super(spaceAfter, children);
        this.text = text;
        this.style = style;
    }

    @Override
    protected int getHeight(ItemStack stack) {
        return getFontRenderer(stack).split(getText(stack), ArcaneWorkbenchScreen.TOOLTIP_WIDTH - 2 * ArcaneWorkbenchScreen.TOOLTIP_BORDER).size() * getFontRenderer(stack).lineHeight;
    }

    @Override
    protected void drawBackground(GuiGraphics guiGraphics, int x, int y, ItemStack stack, float partialTicks, int mouseX, int mouseY) {
    }

    @Override
    protected void drawForeground(GuiGraphics guiGraphics, int x, int y, ItemStack stack, int mouseX, int mouseY) {
        int color = getColour(stack);
        guiGraphics.drawString(getFontRenderer(stack), getText(stack), x, y, color, true);
    }

    protected Component getText(ItemStack stack) {
        return Component.literal(this.text != null ? this.text : "").withStyle(this.style);
    }

    protected Font getFontRenderer(ItemStack stack) {
        return Minecraft.getInstance().font;
    }

    protected int getColour(ItemStack stack) {
        TextColor color = getText(stack).getStyle().getColor();
        return color != null ? color.getValue() : ChatFormatting.WHITE.getColor();
    }

    protected Style getStyle() {
        return this.style;
    }

    @Override
    protected boolean isVisible(ItemStack stack) {
        return true;
    }

    public static class TooltipElementItemName extends TooltipElementText {
        public TooltipElementItemName(Style style, int spaceAfter) {
            super(null, style, spaceAfter);
        }

        @Override
        protected Component getText(ItemStack stack) {
            return stack.getItem().getName(stack);
        }

        @Override
        protected int getColour(ItemStack stack) {
            TextColor textColor = stack.getItem().getName(stack).getStyle().getColor();
            return textColor == null ? super.getColour(stack) : textColor.getValue();
        }
    }

    public static class TooltipElementManaReadout extends TooltipElementText {
        public TooltipElementManaReadout(int spaceAfter) {
            super(null, Style.EMPTY.withColor(ChatFormatting.BLUE), spaceAfter);
        }

        @Override
        protected Component getText(ItemStack stack) {
            if (stack.getItem() instanceof IManaItem manaItem) {
                return Component.translatable("container." + WizardryMod.MODID + ".arcane_workbench.mana",
                        manaItem.getMana(stack), manaItem.getManaCapacity(stack)).withStyle(this.getStyle());
            }
            return Component.empty();
        }

        @Override
        protected int getColour(ItemStack stack) {
            TextColor color = getStyle().getColor();
            return color == null ? super.getColour(stack) : color.getValue();
        }

        @Override
        protected boolean isVisible(ItemStack stack) {
            return stack.getItem() instanceof IManaItem;
        }
    }
}
