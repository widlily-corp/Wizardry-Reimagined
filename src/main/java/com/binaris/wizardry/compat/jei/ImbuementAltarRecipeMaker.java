package com.binaris.wizardry.compat.jei;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.registry.EBArmorItems;
import com.binaris.wizardry.registry.EBCraftingItems;
import com.binaris.wizardry.registry.EBItems;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class ImbuementAltarRecipeMaker {
    private static final Gson GSON = new Gson();

    private static final String[] ELEMENTS = {
            "earth", "fire", "healing", "ice", "lightning", "necromancy", "sorcery"
    };

    private static final String[] ARMOR_SETS = {
            "battlemage_boots", "battlemage_chestplate", "battlemage_helmet", "battlemage_leggings",
            "sage_boots", "sage_hat", "sage_leggings", "sage_robe",
            "warlock_boots", "warlock_hood", "warlock_leggings", "warlock_robe",
            "wizard_boots", "wizard_hat", "wizard_leggings", "wizard_robe"
    };

    public static List<ImbuementAltarJeiRecipe> createRecipes() {
        List<ImbuementAltarJeiRecipe> recipes = new ArrayList<>();

        // 1. Try loading from JSON recipe files
        loadFromJson(recipes);

        // 2. If JSON loading failed or was empty, use procedural generation fallback
        if (recipes.isEmpty()) {
            generateProceduralRecipes(recipes);
        }

        return recipes;
    }

    private static void loadFromJson(List<ImbuementAltarJeiRecipe> recipes) {
        List<String> fileNames = new ArrayList<>();

        // Generate expected filenames
        for (String armor : ARMOR_SETS) {
            for (String elem : ELEMENTS) {
                fileNames.add(armor + "_" + elem + ".json");
            }
        }
        for (String elem : ELEMENTS) {
            fileNames.add("magic_crystal_" + elem + ".json");
        }
        fileNames.add("fix_ruined_spell_book.json");

        for (String fileName : fileNames) {
            String path = "/data/ebwizardry/recipes/imbuement/" + fileName;
            InputStream is = ImbuementAltarRecipeMaker.class.getResourceAsStream(path);
            if (is == null) {
                is = ImbuementAltarRecipeMaker.class.getClassLoader().getResourceAsStream("data/ebwizardry/recipes/imbuement/" + fileName);
            }
            if (is == null) continue;

            try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                if (json == null) continue;

                if (fileName.equals("fix_ruined_spell_book.json")) {
                    parseRuinedSpellBookRecipe(fileName, json, recipes);
                } else {
                    parseStandardRecipe(fileName, json, recipes);
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void parseStandardRecipe(String fileName, JsonObject json, List<ImbuementAltarJeiRecipe> recipes) {
        if (!json.has("center") || !json.has("result")) return;

        Ingredient center = parseIngredient(json.getAsJsonObject("center"));
        if (center.isEmpty()) return;

        List<Ingredient> receptacles = new ArrayList<>();
        if (json.has("receptacles")) {
            JsonArray recArray = json.getAsJsonArray("receptacles");
            for (JsonElement el : recArray) {
                if (el.isJsonObject()) {
                    receptacles.add(parseIngredient(el.getAsJsonObject()));
                }
            }
        }

        ItemStack result = parseItemStack(json.getAsJsonObject("result"));
        if (result.isEmpty()) return;

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "imbuement/" + fileName.replace(".json", ""));
        recipes.add(new ImbuementAltarJeiRecipe(id, center, receptacles, result));
    }

    private static void parseRuinedSpellBookRecipe(String fileName, JsonObject json, List<ImbuementAltarJeiRecipe> recipes) {
        Ingredient center = parseIngredient(json.getAsJsonObject("center"));
        if (center.isEmpty()) return;

        if (json.has("poll") && json.has("results")) {
            JsonArray poll = json.getAsJsonArray("poll");
            JsonArray results = json.getAsJsonArray("results");

            for (int i = 0; i < poll.size() && i < results.size(); i++) {
                Ingredient dust = parseIngredient(poll.get(i).getAsJsonObject());
                List<Ingredient> receptacles = List.of(dust, dust, dust, dust);
                ItemStack result = new ItemStack(EBCraftingItems.SPELL_BOOK.get());

                ResourceLocation id = ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "imbuement/" + fileName.replace(".json", "") + "_" + i);
                recipes.add(new ImbuementAltarJeiRecipe(id, center, receptacles, result));
            }
        }
    }

    private static Ingredient parseIngredient(JsonObject obj) {
        if (obj.has("item")) {
            ResourceLocation loc = ResourceLocation.parse(obj.get("item").getAsString());
            Item item = BuiltInRegistries.ITEM.get(loc);
            return Ingredient.of(item);
        } else if (obj.has("tag")) {
            ResourceLocation loc = ResourceLocation.parse(obj.get("tag").getAsString());
            TagKey<Item> tagKey = TagKey.create(Registries.ITEM, loc);
            return Ingredient.of(tagKey);
        }
        return Ingredient.EMPTY;
    }

    private static ItemStack parseItemStack(JsonObject obj) {
        if (obj.has("item")) {
            ResourceLocation loc = ResourceLocation.parse(obj.get("item").getAsString());
            Item item = BuiltInRegistries.ITEM.get(loc);
            int count = obj.has("count") ? obj.get("count").getAsInt() : 1;
            return new ItemStack(item, count);
        }
        return ItemStack.EMPTY;
    }

    private static void generateProceduralRecipes(List<ImbuementAltarJeiRecipe> recipes) {
        // Armor imbuements
        for (String armor : ARMOR_SETS) {
            Item baseArmor = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, armor));
            if (baseArmor == null) continue;
            Ingredient center = Ingredient.of(baseArmor);

            for (String elem : ELEMENTS) {
                Item resultItem = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, armor + "_" + elem));
                TagKey<Item> dustTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, elem + "_elemental_dust"));
                Ingredient dustIngredient = Ingredient.of(dustTag);

                List<Ingredient> receptacles = List.of(dustIngredient, dustIngredient, dustIngredient, dustIngredient);
                ResourceLocation id = ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "imbuement/" + armor + "_" + elem);
                recipes.add(new ImbuementAltarJeiRecipe(id, center, receptacles, new ItemStack(resultItem)));
            }
        }

        // Crystal imbuements
        Item magicCrystal = EBItems.MAGIC_CRYSTAL.get();
        Ingredient crystalCenter = Ingredient.of(magicCrystal);
        for (String elem : ELEMENTS) {
            Item resultCrystal = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "magic_crystal_" + elem));
            Item spectralDust = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "spectral_dust_" + elem));
            Ingredient dustIngredient = Ingredient.of(spectralDust);

            List<Ingredient> receptacles = List.of(dustIngredient, dustIngredient, dustIngredient, dustIngredient);
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "imbuement/magic_crystal_" + elem);
            recipes.add(new ImbuementAltarJeiRecipe(id, crystalCenter, receptacles, new ItemStack(resultCrystal)));
        }

        // Ruined Spell Book repair
        Item ruinedBook = EBItems.RUINED_SPELL_BOOK.get();
        Ingredient bookCenter = Ingredient.of(ruinedBook);
        for (String elem : ELEMENTS) {
            TagKey<Item> dustTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, elem + "_elemental_dust"));
            Ingredient dustIngredient = Ingredient.of(dustTag);

            List<Ingredient> receptacles = List.of(dustIngredient, dustIngredient, dustIngredient, dustIngredient);
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "imbuement/ruined_spell_book_" + elem);
            recipes.add(new ImbuementAltarJeiRecipe(id, bookCenter, receptacles, new ItemStack(EBCraftingItems.SPELL_BOOK.get())));
        }
    }

    private ImbuementAltarRecipeMaker() {}
}
