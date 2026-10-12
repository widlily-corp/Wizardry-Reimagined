package com.binaris.wizardry.compat.jei;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.registry.EBWorkbenchBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ArcaneWorkbenchRecipeCategory implements IRecipeCategory<ArcaneWorkbenchJeiRecipe> {
    public static final RecipeType<ArcaneWorkbenchJeiRecipe> RECIPE_TYPE =
            RecipeType.create(WizardryMod.MODID, "arcane_workbench", ArcaneWorkbenchJeiRecipe.class);

    public static final int WIDTH = 150;
    public static final int HEIGHT = 72;

    private final IDrawable icon;
    private final Component title;
    private final IDrawableStatic arrow;
    private final IDrawableStatic plus;

    public ArcaneWorkbenchRecipeCategory(IGuiHelper guiHelper) {
        this.title = Component.translatable("jei.ebwizardry.category.arcane_workbench");
        this.icon = guiHelper.createDrawableItemLike(EBWorkbenchBlocks.ARCANE_WORKBENCH.get());
        this.arrow = guiHelper.getRecipeArrow();
        this.plus = guiHelper.getRecipePlusSign();
    }

    @Override
    public @NotNull RecipeType<ArcaneWorkbenchJeiRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ArcaneWorkbenchJeiRecipe recipe, @NotNull IFocusGroup focuses) {
        // Slot 1: Base wand in the center slot
        builder.addInputSlot(16, 18)
                .addItemStacks(recipe.getWandInputs())
                .setStandardSlotBackground();

        // Slot 2: Modifier / Tome / Upgrade / Spell Book / Crystal
        builder.addInputSlot(54, 18)
                .addItemStacks(recipe.getModifierInputs())
                .setStandardSlotBackground();

        // Slot 3: Resulting wand
        builder.addOutputSlot(112, 18)
                .addItemStack(recipe.getResult())
                .setOutputSlotBackground();
    }

    @Override
    public void draw(ArcaneWorkbenchJeiRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        plus.draw(guiGraphics, 38, 21);
        arrow.draw(guiGraphics, 78, 18);

        if (recipe.getDescription() != null) {
            Minecraft minecraft = Minecraft.getInstance();
            guiGraphics.drawString(minecraft.font, recipe.getDescription(), 10, 50, 0x555555, false);
        }
    }
}
