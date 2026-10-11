package com.binaris.wizardry.client.gui;

import com.binaris.wizardry.content.component.SpellData;
import com.binaris.wizardry.content.item.wand.ICastItem;
import com.binaris.wizardry.registry.EBDataComponents;
import com.binaris.wizardry.registry.EBRegistries;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.Spells;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.binaris.wizardry.client.gui.ArcaneWorkbenchScreen.LINE_SPACING_NARROW;

public class TooltipElementSpellEntry extends TooltipElementText {
    private final int index;
    private final ArcaneWorkbenchScreen screen;

    public TooltipElementSpellEntry(ArcaneWorkbenchScreen screen, int index) {
        super(null, Style.EMPTY.withColor(ChatFormatting.BLUE), LINE_SPACING_NARROW);
        this.index = index;
        this.screen = screen;
    }

    private boolean shouldFlash(ItemStack stack) {
        ItemStack spellBook = screen.getMenu().getSlot(index).getItem();
        Spell bookSpell = getSpellFromBook(spellBook);
        if (bookSpell == null || bookSpell == Spells.NONE.get()) return false;
        if (stack.getItem() instanceof ICastItem castItem) {
            Spell[] spells = castItem.getSpells(stack);
            return index < spells.length && bookSpell != spells[index];
        }
        return false;
    }

    private float getAlpha(float partialTicks) {
        Player player = Minecraft.getInstance().player;
        int tick = player != null ? player.tickCount : 0;
        return (Mth.sin(0.2f * (tick + partialTicks)) + 1) / 4 + 0.5f;
    }

    @Override
    protected boolean isVisible(ItemStack stack) {
        return stack.getItem() instanceof ICastItem castItem && index < castItem.getSpells(stack).length;
    }

    @Override
    protected int getColour(ItemStack stack) {
        Spell spell = getSpell(stack);
        if (spell != null && spell.getElement() != null && spell.getElement().getColor() != null) {
            Integer color = spell.getElement().getColor().getColor();
            if (color != null) return color;
        }
        return ChatFormatting.BLUE.getColor();
    }

    @Override
    protected Component getText(ItemStack stack) {
        Spell spell = getSpell(stack);
        if (spell == null || spell == Spells.NONE.get()) {
            return Component.translatable("spell.ebwizardry.none").withStyle(ChatFormatting.DARK_GRAY);
        }
        return spell.getDescriptionFormatted();
    }

    @Override
    protected void drawBackground(GuiGraphics guiGraphics, int x, int y, ItemStack stack, float partialTicks, int mouseX, int mouseY) {
        Spell spell = getSpell(stack);
        if (spell != null && spell.getElement() != null) {
            if (shouldFlash(stack)) {
                RenderSystem.setShaderColor(1, 1, 1, getAlpha(partialTicks));
            }
            guiGraphics.blit(spell.getElement().getIconId(), x, y, 0, 0, 8, 8, 8, 8);
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }

    private Spell getSpellFromBook(ItemStack bookStack) {
        if (!bookStack.isEmpty() && bookStack.has(EBDataComponents.SPELL_DATA.get())) {
            SpellData data = bookStack.get(EBDataComponents.SPELL_DATA.get());
            if (data != null && !data.isEmpty()) {
                return EBRegistries.SPELL_REGISTRY.get(data.spellId());
            }
        }
        return null;
    }

    private Spell getSpell(ItemStack stack) {
        ItemStack spellBook = screen.getMenu().getSlot(index).getItem();
        Spell fromBook = getSpellFromBook(spellBook);
        if (fromBook != null) {
            return fromBook;
        }

        if (stack.getItem() instanceof ICastItem castItem) {
            Spell[] spells = castItem.getSpells(stack);
            if (index < spells.length && spells[index] != null) {
                return spells[index];
            }
        }
        return Spells.NONE.get();
    }

    @Override
    protected void drawForeground(GuiGraphics guiGraphics, int x, int y, ItemStack stack, int mouseX, int mouseY) {
        x += 11;
        int color = getColour(stack);
        guiGraphics.drawString(getFontRenderer(stack), getText(stack), x, y, color, true);
    }
}
