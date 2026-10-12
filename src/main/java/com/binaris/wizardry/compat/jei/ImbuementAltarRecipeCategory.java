package com.binaris.wizardry.compat.jei;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.registry.EBBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImbuementAltarRecipeCategory implements IRecipeCategory<ImbuementAltarJeiRecipe> {
    public static final RecipeType<ImbuementAltarJeiRecipe> RECIPE_TYPE =
            RecipeType.create(WizardryMod.MODID, "imbuement_altar", ImbuementAltarJeiRecipe.class);

    public static final int WIDTH = 148;
    public static final int HEIGHT = 86;

    private final IDrawable icon;
    private final Component title;
    private final IDrawableStatic arrow;

    public ImbuementAltarRecipeCategory(IGuiHelper guiHelper) {
        this.title = Component.translatable("jei.ebwizardry.category.imbuement_altar");
        this.icon = guiHelper.createDrawableItemLike(EBBlocks.IMBUEMENT_ALTAR.get());
        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public @NotNull RecipeType<ImbuementAltarJeiRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, ImbuementAltarJeiRecipe recipe, @NotNull IFocusGroup focuses) {
        // Central altar slot
        builder.addInputSlot(40, 34)
                .addIngredients(recipe.getCenter())
                .setStandardSlotBackground();

        // 4 surrounding receptacles (North, West, East, South)
        List<Ingredient> receptacles = recipe.getReceptacles();
        int[][] coords = new int[][] {
                {40, 8},   // North
                {14, 34},  // West
                {66, 34},  // East
                {40, 60}   // South
        };

        for (int i = 0; i < 4; i++) {
            var slotBuilder = builder.addInputSlot(coords[i][0], coords[i][1])
                    .setStandardSlotBackground();
            if (i < receptacles.size()) {
                slotBuilder.addIngredients(receptacles.get(i));
            }
        }

        // Output item
        builder.addOutputSlot(118, 34)
                .addItemStack(recipe.getResult())
                .setOutputSlotBackground();
    }

    @Override
    public void draw(ImbuementAltarJeiRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 90, 34);
    }
}
