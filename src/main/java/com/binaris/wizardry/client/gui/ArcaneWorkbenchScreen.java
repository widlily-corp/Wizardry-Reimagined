package com.binaris.wizardry.client.gui;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.block.workbench.IWorkbenchItem;
import com.binaris.wizardry.content.menu.ArcaneWorkbenchMenu;
import com.binaris.wizardry.network.EBNetwork;
import com.binaris.wizardry.network.PacketArcaneWorkbenchSync;
import com.binaris.wizardry.registry.EBSounds;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArcaneWorkbenchScreen extends AbstractContainerScreen<ArcaneWorkbenchMenu> {
    public static final int MAIN_GUI_WIDTH = 176;
    public static final int RUNE_LEFT = 38;
    public static final int RUNE_TOP = 22;
    public static final int RUNE_WIDTH = 100;
    public static final int RUNE_HEIGHT = 100;
    public static final int HALO_DIAMETER = 156;
    public static final int TEXTURE_WIDTH = 512;
    public static final int TEXTURE_HEIGHT = 512;

    public static final int TOOLTIP_WIDTH = 144;
    public static final int TOOLTIP_BORDER = 6;
    public static final int PROGRESSION_BAR_WIDTH = 131;
    public static final int PROGRESSION_BAR_HEIGHT = 3;

    public static final ResourceLocation ARCANE_WORKBENCH_CONTAINER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/container/arcane_workbench.png");

    public static final int LINE_SPACING_WIDE = 5;
    public static final int LINE_SPACING_NARROW = 1;
    public static final int ANIMATION_DURATION = 20;

    private final Inventory playerInventory;
    private final ArcaneWorkbenchMenu menu;
    private final List<TooltipElement> tooltipElements = new ArrayList<>();
    private AbstractWidget applyBtn;
    private AbstractWidget clearBtn;
    private int animationTimer = 0;

    public ArcaneWorkbenchScreen(ArcaneWorkbenchMenu menu, Inventory playerInventory, Component name) {
        super(menu, playerInventory, name);
        this.menu = menu;
        this.playerInventory = playerInventory;
        this.imageWidth = MAIN_GUI_WIDTH;
        this.imageHeight = 220;
    }

    @Override
    protected void init() {
        super.init();
        if (this.minecraft == null || this.minecraft.player == null) return;
        this.minecraft.player.containerMenu = this.menu;

        this.leftPos = (this.width - MAIN_GUI_WIDTH) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.clearWidgets();

        this.addRenderableWidget(this.applyBtn = new GuiButtonApply(this.width / 2 + 64, this.height / 2 + 3, button -> {
            if (button.active) {
                EBNetwork.sendToServer(new PacketArcaneWorkbenchSync(PacketArcaneWorkbenchSync.ACTION_APPLY));
                if (this.minecraft != null) {
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(EBSounds.BLOCK_ARCANE_WORKBENCH_SPELLBIND.get(), 1.0F));
                }
                animationTimer = ANIMATION_DURATION;
            }
        }));

        this.addRenderableWidget(this.clearBtn = new GuiButtonClear(this.width / 2 + 64, this.height / 2 - 16, button -> {
            if (button.active) {
                EBNetwork.sendToServer(new PacketArcaneWorkbenchSync(PacketArcaneWorkbenchSync.ACTION_CLEAR));
                if (this.minecraft != null) {
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(EBSounds.BLOCK_ARCANE_WORKBENCH_SPELLBIND.get(), 0.8F));
                }
                animationTimer = ANIMATION_DURATION;
            }
        }));

        this.tooltipElements.clear();
        this.tooltipElements.add(new TooltipElementText.TooltipElementItemName(Style.EMPTY.withColor(ChatFormatting.WHITE), LINE_SPACING_WIDE));
        this.tooltipElements.add(new TooltipElementText.TooltipElementManaReadout(LINE_SPACING_WIDE));
        this.tooltipElements.add(new TooltipElementProgressionBar(imageHeight, LINE_SPACING_WIDE));
        this.tooltipElements.add(new TooltipElementSpellList(LINE_SPACING_WIDE, generateSpellEntries()));
        this.tooltipElements.add(new TooltipElementUpgradeList(this, LINE_SPACING_WIDE));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        Slot centreSlot = this.menu.getSlot(ArcaneWorkbenchMenu.CENTRE_SLOT);
        imageWidth = MAIN_GUI_WIDTH;
        leftPos = (this.width - MAIN_GUI_WIDTH) / 2;

        if (centreSlot.hasItem() && centreSlot.getItem().getItem() instanceof IWorkbenchItem workbenchItem && workbenchItem.showTooltip(centreSlot.getItem())) {
            imageWidth += TOOLTIP_WIDTH;
        }

        this.applyBtn.active = centreSlot.hasItem();
        this.clearBtn.active = centreSlot.hasItem() && centreSlot.getItem().getItem() instanceof IWorkbenchItem workbenchItem && workbenchItem.isClearable();

        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        ResourceLocation texture = ARCANE_WORKBENCH_CONTAINER_TEXTURE;
        int left = leftPos;
        int top = topPos;

        // Gray background
        guiGraphics.blit(texture, left + RUNE_LEFT, top + RUNE_TOP, MAIN_GUI_WIDTH + TOOLTIP_WIDTH, 0,
                RUNE_WIDTH, RUNE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);

        // Yellow halo
        if (animationTimer > 0) {
            float scale = (animationTimer + partialTick) / ANIMATION_DURATION;
            scale = (float) (1 - Math.pow(1 - scale, 1.4f));

            int x = left + RUNE_LEFT + RUNE_WIDTH / 2;
            int y = top + RUNE_TOP + RUNE_HEIGHT / 2;
            float halfDiameter = HALO_DIAMETER / 2.0F;

            PoseStack pose = guiGraphics.pose();
            pose.pushPose();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            pose.scale(scale, scale, 1);
            pose.translate(x / scale, y / scale, 0);
            guiGraphics.blit(texture, (int) -halfDiameter, (int) -halfDiameter, MAIN_GUI_WIDTH + TOOLTIP_WIDTH,
                    RUNE_HEIGHT, HALO_DIAMETER, HALO_DIAMETER, TEXTURE_WIDTH, TEXTURE_HEIGHT);
            RenderSystem.disableBlend();
            pose.popPose();
        }

        // Main inventory frame
        guiGraphics.blit(texture, left, top, 0, 0, MAIN_GUI_WIDTH, this.imageHeight, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        float opacity = (animationTimer + partialTick) / ANIMATION_DURATION;

        // Spell book slots
        for (int i = 0; i < ArcaneWorkbenchMenu.CRYSTAL_SLOT; i++) {
            Slot slot = this.menu.getSlot(i);
            if (slot.x < 0 || slot.y < 0) continue;

            guiGraphics.blit(texture, leftPos + slot.x - 10, topPos + slot.y - 10, 0, 220, 36, 36, TEXTURE_WIDTH, TEXTURE_HEIGHT);

            if (animationTimer > 0 && slot.hasItem()) {
                blitWithOpacity(guiGraphics, texture, leftPos + slot.x - 10, topPos + slot.y - 10, 36, 220, 36, 36, opacity);
            }
        }

        // Crystal + upgrade slot animations
        if (animationTimer > 0) {
            int glowU = MAIN_GUI_WIDTH + TOOLTIP_WIDTH + RUNE_WIDTH;

            Slot crystals = this.menu.getSlot(ArcaneWorkbenchMenu.CRYSTAL_SLOT);
            if (crystals.hasItem()) {
                blitWithOpacity(guiGraphics, texture, leftPos + crystals.x - 8, topPos + crystals.y - 8, glowU, 0, 32, 32, opacity);
            }

            Slot upgrades = this.menu.getSlot(ArcaneWorkbenchMenu.UPGRADE_SLOT);
            if (upgrades.hasItem()) {
                blitWithOpacity(guiGraphics, texture, leftPos + upgrades.x - 8, topPos + upgrades.y - 8, glowU, 0, 32, 32, opacity);
            }
        }

        // Tooltip panel on right
        if (this.menu.getSlot(ArcaneWorkbenchMenu.CENTRE_SLOT).hasItem()) {
            ItemStack stack = this.menu.getSlot(ArcaneWorkbenchMenu.CENTRE_SLOT).getItem();
            if (stack.getItem() instanceof IWorkbenchItem workbenchItem && workbenchItem.showTooltip(stack)) {
                int tooltipHeight = tooltipElements.stream().mapToInt(e -> e.getTotalHeight(stack)).sum()
                        - tooltipElements.get(tooltipElements.size() - 1).spaceAfter;

                guiGraphics.blit(texture, left + MAIN_GUI_WIDTH, top, MAIN_GUI_WIDTH, 0, TOOLTIP_WIDTH,
                        TOOLTIP_BORDER + tooltipHeight, TEXTURE_WIDTH, TEXTURE_HEIGHT);

                guiGraphics.blit(texture, left + MAIN_GUI_WIDTH, top + TOOLTIP_BORDER + tooltipHeight, MAIN_GUI_WIDTH,
                        imageHeight - TOOLTIP_BORDER, TOOLTIP_WIDTH, TOOLTIP_BORDER, TEXTURE_WIDTH, TEXTURE_HEIGHT);

                int x = left + MAIN_GUI_WIDTH + TOOLTIP_BORDER;
                int y = top + TOOLTIP_BORDER;
                for (TooltipElement element : this.tooltipElements) {
                    y = element.drawBackgroundLayer(guiGraphics, x, y, stack, partialTick, mouseX, mouseY);
                }
            }
        }

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    private void blitWithOpacity(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int u, int v, int w, int h, float opacity) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1, 1, 1, opacity);
        guiGraphics.blit(texture, x, y, u, v, w, h, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        int left = 0;

        guiGraphics.drawString(this.font, getTitle(), left + 8, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventory.getName().getString(), left + 8, imageHeight - 96 + 2, 4210752, false);

        if (!this.menu.getSlot(ArcaneWorkbenchMenu.CENTRE_SLOT).hasItem()) return;
        ItemStack stack = this.menu.getSlot(ArcaneWorkbenchMenu.CENTRE_SLOT).getItem();

        if (stack.getItem() instanceof IWorkbenchItem workbenchItem && workbenchItem.showTooltip(stack)) {
            int x = left + MAIN_GUI_WIDTH + TOOLTIP_BORDER;
            int y = TOOLTIP_BORDER;
            for (TooltipElement element : this.tooltipElements) {
                y = element.drawForegroundLayer(guiGraphics, x, y, stack, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void containerTick() {
        if (animationTimer > 0) animationTimer--;
        if (menu.needsRefresh) menu.needsRefresh = false;
    }

    private TooltipElement[] generateSpellEntries() {
        TooltipElement[] entries = new TooltipElement[8];
        for (int i = 0; i < 8; i++) {
            entries[i] = new TooltipElementSpellEntry(this, i);
        }
        return entries;
    }

    @Override
    public @NotNull ArcaneWorkbenchMenu getMenu() {
        return menu;
    }
}
