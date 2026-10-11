package com.binaris.wizardry.content.recipe;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, WizardryMod.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<ScrollToBookRecipe>> SCROLL_TO_BOOK =
            RECIPE_SERIALIZERS.register("crafting_special_scroll_to_book", () -> new SimpleCraftingRecipeSerializer<>(ScrollToBookRecipe::new));

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<SpellBookCloningRecipe>> SPELL_BOOK_CLONING =
            RECIPE_SERIALIZERS.register("crafting_special_spell_book_cloning", () -> new SimpleCraftingRecipeSerializer<>(SpellBookCloningRecipe::new));

    public static void register(IEventBus modBus) {
        RECIPE_SERIALIZERS.register(modBus);
    }

    private EBRecipeSerializers() {}
}
