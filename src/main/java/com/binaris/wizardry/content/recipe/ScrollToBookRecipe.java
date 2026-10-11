package com.binaris.wizardry.content.recipe;

import com.binaris.wizardry.content.component.SpellData;
import com.binaris.wizardry.content.item.scroll.BlankSpellBookItem;
import com.binaris.wizardry.content.item.scroll.ScrollItem;
import com.binaris.wizardry.registry.EBCraftingItems;
import com.binaris.wizardry.registry.EBDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ScrollToBookRecipe extends CustomRecipe {
    public ScrollToBookRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        ItemStack blankBook = ItemStack.EMPTY;
        ItemStack scroll = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof BlankSpellBookItem) {
                if (!blankBook.isEmpty()) return false;
                blankBook = stack;
            } else if (stack.getItem() instanceof ScrollItem) {
                if (!scroll.isEmpty()) return false;
                SpellData spellData = stack.get(EBDataComponents.SPELL_DATA.get());
                if (spellData == null || spellData.isEmpty()) return false;
                scroll = stack;
            } else {
                return false;
            }
        }

        return !blankBook.isEmpty() && !scroll.isEmpty();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input, @NotNull HolderLookup.Provider registries) {
        ItemStack scroll = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.getItem() instanceof ScrollItem) {
                scroll = stack;
                break;
            }
        }

        if (scroll.isEmpty()) return ItemStack.EMPTY;

        SpellData spellData = scroll.get(EBDataComponents.SPELL_DATA.get());
        if (spellData == null || spellData.isEmpty()) return ItemStack.EMPTY;

        ItemStack book = new ItemStack(EBCraftingItems.SPELL_BOOK.get());
        book.set(EBDataComponents.SPELL_DATA.get(), spellData);
        return book;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return EBRecipeSerializers.SCROLL_TO_BOOK.get();
    }
}
