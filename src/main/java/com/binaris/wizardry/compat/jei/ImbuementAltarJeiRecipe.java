package com.binaris.wizardry.compat.jei;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

/**
 * Represents a recipe displayed in the JEI Imbuement Altar category.
 */
public class ImbuementAltarJeiRecipe {
    private final ResourceLocation id;
    private final Ingredient center;
    private final List<Ingredient> receptacles;
    private final ItemStack result;

    public ImbuementAltarJeiRecipe(ResourceLocation id, Ingredient center, List<Ingredient> receptacles, ItemStack result) {
        this.id = id;
        this.center = center;
        this.receptacles = receptacles;
        this.result = result;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Ingredient getCenter() {
        return center;
    }

    public List<Ingredient> getReceptacles() {
        return receptacles;
    }

    public ItemStack getResult() {
        return result;
    }
}
