package com.binaris.wizardry.compat.jei;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * Represents a recipe displayed in the JEI Arcane Workbench category.
 */
public class ArcaneWorkbenchJeiRecipe {
    public enum RecipeType {
        TIER_UPGRADE,
        SPECIAL_UPGRADE,
        SPELL_BINDING,
        RECHARGE
    }

    private final List<ItemStack> wandInputs;
    private final List<ItemStack> modifierInputs;
    private final ItemStack result;
    private final RecipeType recipeType;
    private final Component description;

    public ArcaneWorkbenchJeiRecipe(List<ItemStack> wandInputs, List<ItemStack> modifierInputs, ItemStack result, RecipeType recipeType, Component description) {
        this.wandInputs = wandInputs;
        this.modifierInputs = modifierInputs;
        this.result = result;
        this.recipeType = recipeType;
        this.description = description;
    }

    public ArcaneWorkbenchJeiRecipe(ItemStack wandInput, ItemStack modifierInput, ItemStack result, RecipeType recipeType, Component description) {
        this(Collections.singletonList(wandInput), Collections.singletonList(modifierInput), result, recipeType, description);
    }

    public List<ItemStack> getWandInputs() {
        return wandInputs;
    }

    public List<ItemStack> getModifierInputs() {
        return modifierInputs;
    }

    public ItemStack getResult() {
        return result;
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public Component getDescription() {
        return description;
    }
}
