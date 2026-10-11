package com.binaris.wizardry.content.recipe;

import com.binaris.wizardry.content.component.SpellData;
import com.binaris.wizardry.content.item.scroll.BlankSpellBookItem;
import com.binaris.wizardry.content.item.scroll.SpellBookItem;
import com.binaris.wizardry.registry.EBCraftingItems;
import com.binaris.wizardry.registry.EBDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SpellBookCloningRecipe extends CustomRecipe {
    public SpellBookCloningRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        int blankBooks = 0;
        ItemStack sourceBook = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof SpellBookItem) {
                if (!sourceBook.isEmpty()) return false;
                SpellData spellData = stack.get(EBDataComponents.SPELL_DATA.get());
                if (spellData == null || spellData.isEmpty()) return false;
                sourceBook = stack;
            } else if (stack.getItem() instanceof BlankSpellBookItem) {
                blankBooks++;
            } else {
                return false;
            }
        }

        return !sourceBook.isEmpty() && blankBooks > 0;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input, @NotNull HolderLookup.Provider registries) {
        int blankBooks = 0;
        ItemStack sourceBook = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof SpellBookItem) {
                if (!sourceBook.isEmpty()) return ItemStack.EMPTY;
                sourceBook = stack;
            } else if (stack.getItem() instanceof BlankSpellBookItem) {
                blankBooks++;
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (sourceBook.isEmpty() || blankBooks < 1) return ItemStack.EMPTY;

        SpellData spellData = sourceBook.get(EBDataComponents.SPELL_DATA.get());
        if (spellData == null || spellData.isEmpty()) return ItemStack.EMPTY;

        ItemStack result = new ItemStack(EBCraftingItems.SPELL_BOOK.get(), blankBooks);
        result.set(EBDataComponents.SPELL_DATA.get(), spellData);
        return result;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.getItem() instanceof SpellBookItem) {
                remaining.set(i, stack.copyWithCount(1));
                break;
            }
        }

        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return EBRecipeSerializers.SPELL_BOOK_CLONING.get();
    }
}
