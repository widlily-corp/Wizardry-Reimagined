package com.binaris.wizardry.client.gui;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.wand.ICastItem;
import com.binaris.wizardry.content.item.wand.IManaItem;
import com.binaris.wizardry.registry.Element;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.Spells;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = WizardryMod.MODID, value = Dist.CLIENT)
public final class SpellHudOverlay {
    public static final ResourceLocation HUD_ID = WizardryMod.id("spell_hud");

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, HUD_ID, SpellHudOverlay::render);
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.player == null || mc.player.isSpectator()) return;

        Player player = mc.player;
        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof ICastItem)) {
            held = player.getOffhandItem();
        }
        if (!(held.getItem() instanceof ICastItem castItem)) return;

        Spell spell = castItem.getCurrentSpell(held);
        Font font = mc.font;
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // High-contrast, non-obstructive bottom-left placement
        int x = 12;
        int y = screenHeight - 56;
        int width = 148;
        int height = 48;

        Element element = (spell != null && spell != Spells.NONE.get()) ? spell.getElement() : null;
        int accentColor = 0xFF7A58A6;
        if (element != null && element.getColor() != null && element.getColor().getColor() != null) {
            accentColor = 0xFF000000 | element.getColor().getColor();
        }

        // Panel background & outline
        guiGraphics.fill(x, y, x + width, y + height, 0xAA0B0C14);
        guiGraphics.renderOutline(x, y, width, height, accentColor);

        // Spell Icon Box (26x26)
        int iconX = x + 5;
        int iconY = y + 5;
        guiGraphics.fill(iconX, iconY, iconX + 26, iconY + 26, 0x88000000);
        guiGraphics.renderOutline(iconX, iconY, 26, 26, 0x66FFFFFF);

        if (spell != null && spell != Spells.NONE.get()) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            guiGraphics.blit(spell.getIcon(), iconX + 1, iconY + 1, 0, 0, 24, 24, 24, 24);

            int currentCd = castItem.getCurrentCooldown(held, player.level());
            int maxCd = castItem.getCurrentMaxCooldown(held);
            if (currentCd > 0 && maxCd > 0) {
                float cdRatio = (float) currentCd / maxCd;
                int cdHeight = (int) (24 * cdRatio);
                guiGraphics.fill(iconX + 1, iconY + 1 + (24 - cdHeight), iconX + 25, iconY + 25, 0xAA000000);
            }

            Component spellName = spell.getDescriptionFormatted();
            guiGraphics.drawString(font, spellName, iconX + 32, iconY + 2, 0xFFFFFF, true);

            if (held.getItem() instanceof IManaItem manaItem) {
                int mana = manaItem.getMana(held);
                int cost = spell.getCost();
                int costColor = mana >= cost ? 0x55FFFF : 0xFF5555;
                guiGraphics.drawString(font, Component.translatable("gui.ebwizardry.hud.cost", cost), iconX + 32, iconY + 14, costColor, true);
            } else {
                guiGraphics.drawString(font, Component.translatable("gui.ebwizardry.hud.scroll"), iconX + 32, iconY + 14, 0xFFAA00, true);
            }

            if (currentCd > 0) {
                float sec = Math.round(currentCd / 2.0f) / 10.0f;
                guiGraphics.drawString(font, Component.translatable("gui.ebwizardry.hud.cooldown", String.format(java.util.Locale.US, "%.1f", sec)),
                        x + width - 42, iconY + 14, 0xFFAA00, true);
            } else {
                guiGraphics.drawString(font, Component.translatable("gui.ebwizardry.hud.ready"),
                        x + width - 40, iconY + 14, 0x55FF55, true);
            }
        } else {
            guiGraphics.drawString(font, Component.translatable("tooltip.ebwizardry.wand.no_spells"), iconX + 32, iconY + 8, 0x888888, true);
        }

        int barX = x + 5;
        int barY = y + 35;
        int barWidth = width - 10;
        int barHeight = 8;

        if (player.isUsingItem() && player.getUseItem() == held && spell != null && spell.getChargeup() > 0) {
            int useDuration = held.getItem().getUseDuration(held, player);
            int ticksHeld = useDuration - player.getUseItemRemainingTicks();
            float chargeProgress = Math.min(1.0f, (float) ticksHeld / spell.getChargeup());
            int fillW = (int) (barWidth * chargeProgress);

            guiGraphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF221100);
            guiGraphics.fill(barX, barY, barX + fillW, barY + barHeight, 0xFFFFCC00);
            guiGraphics.renderOutline(barX, barY, barWidth, barHeight, 0xFFFFAA00);

            String chargeText = ((int) (chargeProgress * 100)) + "%";
            guiGraphics.drawCenteredString(font, Component.translatable("gui.ebwizardry.hud.charging", chargeText),
                    barX + barWidth / 2, barY - 1, 0xFFFFFF);
        } else if (held.getItem() instanceof IManaItem manaItem) {
            int mana = manaItem.getMana(held);
            int maxMana = manaItem.getManaCapacity(held);
            float ratio = maxMana > 0 ? (float) mana / maxMana : 1.0f;
            int fillW = (int) (barWidth * Math.max(0.0f, Math.min(1.0f, ratio)));

            guiGraphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF151025);
            guiGraphics.fill(barX, barY, barX + fillW, barY + barHeight, 0xFF8A2BE2);
            guiGraphics.renderOutline(barX, barY, barWidth, barHeight, 0xFF5D24A6);

            String manaText = mana + " / " + maxMana;
            guiGraphics.drawCenteredString(font, manaText, barX + barWidth / 2, barY, 0xE0E0E0);
        }
    }

    private SpellHudOverlay() {}
}
