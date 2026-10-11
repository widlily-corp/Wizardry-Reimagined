package com.binaris.wizardry.datagen;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.scroll.ScrollItem;
import com.binaris.wizardry.content.item.scroll.SpellBookItem;
import com.binaris.wizardry.content.recipe.ScrollToBookRecipe;
import com.binaris.wizardry.content.recipe.SpellBookCloningRecipe;
import com.binaris.wizardry.registry.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class WizardryRecipeProvider extends RecipeProvider {

    public WizardryRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(), new WizardryRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        buildBlankItemsRecipes(output);
        buildWandUpgradeRecipes(output);
        buildTomeRecipes(output);
        buildSmithingTemplateRecipes(output);
        buildSmithingWandUpgradeRecipes(output);
        buildSpellScrollsAndBooksRecipes(output);
        buildSpecialRecipes(output);
    }

    private void buildBlankItemsRecipes(RecipeOutput output) {
        // Blank Scroll: Paper + Gold Nugget + Magic Crystal
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EBCraftingItems.BLANK_SCROLL.get(), 2)
                .requires(Items.PAPER)
                .requires(Items.GOLD_NUGGET)
                .requires(EBItems.MAGIC_CRYSTAL.get())
                .unlockedBy("has_magic_crystal", has(EBItems.MAGIC_CRYSTAL.get()))
                .save(output, WizardryMod.id("blank_scroll"));

        // Blank Spell Book: Book + Gold Nugget + Magic Crystal
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EBCraftingItems.BLANK_SPELL_BOOK.get())
                .pattern(" G ")
                .pattern("CBC")
                .pattern(" C ")
                .define('G', Items.GOLD_NUGGET)
                .define('B', Items.BOOK)
                .define('C', EBItems.MAGIC_CRYSTAL.get())
                .unlockedBy("has_magic_crystal", has(EBItems.MAGIC_CRYSTAL.get()))
                .save(output, WizardryMod.id("blank_spell_book"));
    }

    private void buildWandUpgradeRecipes(RecipeOutput output) {
        registerUpgrade(output, EBCraftingItems.STORAGE_UPGRADE.get(), Items.CHEST, "storage_upgrade");
        registerUpgrade(output, EBCraftingItems.SIPHON_UPGRADE.get(), Items.FERMENTED_SPIDER_EYE, "siphon_upgrade");
        registerUpgrade(output, EBCraftingItems.RANGE_UPGRADE.get(), Items.FEATHER, "range_upgrade");
        registerUpgrade(output, EBCraftingItems.BLAST_UPGRADE.get(), Items.GUNPOWDER, "blast_upgrade");
        registerUpgrade(output, EBCraftingItems.COOLDOWN_UPGRADE.get(), Items.CLOCK, "cooldown_upgrade");
        registerUpgrade(output, EBCraftingItems.CONDENSER_UPGRADE.get(), Items.AMETHYST_SHARD, "condenser_upgrade");
        registerUpgrade(output, EBCraftingItems.ATTUNEMENT_UPGRADE.get(), Items.LAPIS_LAZULI, "attunement_upgrade");
        registerUpgrade(output, EBCraftingItems.DURATION_UPGRADE.get(), Items.REDSTONE, "duration_upgrade");
    }

    private void registerUpgrade(RecipeOutput output, Item result, Item catalyst, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
                .pattern(" C ")
                .pattern("IGI")
                .pattern(" C ")
                .define('C', EBItems.MAGIC_CRYSTAL.get())
                .define('I', Items.GOLD_INGOT)
                .define('G', catalyst)
                .unlockedBy("has_magic_crystal", has(EBItems.MAGIC_CRYSTAL.get()))
                .save(output, WizardryMod.id(name));
    }

    private void buildTomeRecipes(RecipeOutput output) {
        // Apprentice Tome: Blank Spell Book + 4 Magic Crystals + Blaze Powder
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EBCraftingItems.APPRENTICE_TOME.get())
                .pattern("PCP")
                .pattern("CBC")
                .pattern("PCP")
                .define('B', EBCraftingItems.BLANK_SPELL_BOOK.get())
                .define('C', EBItems.MAGIC_CRYSTAL.get())
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy("has_blank_spell_book", has(EBCraftingItems.BLANK_SPELL_BOOK.get()))
                .save(output, WizardryMod.id("apprentice_tome"));

        // Advanced Tome: Apprentice Tome + 4 Grand Magic Crystals + Ender Pearls
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EBCraftingItems.ADVANCED_TOME.get())
                .pattern("EGE")
                .pattern("GTG")
                .pattern("EGE")
                .define('T', EBCraftingItems.APPRENTICE_TOME.get())
                .define('G', EBItems.MAGIC_CRYSTAL_GRAND.get())
                .define('E', Items.ENDER_PEARL)
                .unlockedBy("has_apprentice_tome", has(EBCraftingItems.APPRENTICE_TOME.get()))
                .save(output, WizardryMod.id("advanced_tome"));

        // Master Tome: Advanced Tome + 4 Grand Magic Crystals + Netherite Ingots
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EBCraftingItems.MASTER_TOME.get())
                .pattern("NGN")
                .pattern("GTG")
                .pattern("NGN")
                .define('T', EBCraftingItems.ADVANCED_TOME.get())
                .define('G', EBItems.MAGIC_CRYSTAL_GRAND.get())
                .define('N', Items.NETHERITE_INGOT)
                .unlockedBy("has_advanced_tome", has(EBCraftingItems.ADVANCED_TOME.get()))
                .save(output, WizardryMod.id("master_tome"));
    }

    private void buildSmithingTemplateRecipes(RecipeOutput output) {
        // Arcane Upgrade Smithing Template crafted from scratch
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EBCraftingItems.ARCANE_UPGRADE_SMITHING_TEMPLATE.get())
                .pattern(" C ")
                .pattern("LDL")
                .pattern(" C ")
                .define('C', EBItems.MAGIC_CRYSTAL.get())
                .define('L', Items.LAPIS_LAZULI)
                .define('D', Items.DIAMOND)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(output, WizardryMod.id("arcane_upgrade_smithing_template"));

        // Duplication: 1 template + 7 lapis + 1 diamond -> 2 templates
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EBCraftingItems.ARCANE_UPGRADE_SMITHING_TEMPLATE.get(), 2)
                .pattern("LSL")
                .pattern("LDL")
                .pattern("LLL")
                .define('L', Items.LAPIS_LAZULI)
                .define('S', EBCraftingItems.ARCANE_UPGRADE_SMITHING_TEMPLATE.get())
                .define('D', Items.DIAMOND)
                .unlockedBy("has_arcane_template", has(EBCraftingItems.ARCANE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(output, WizardryMod.id("arcane_upgrade_smithing_template_duplication"));
    }

    private void buildSmithingWandUpgradeRecipes(RecipeOutput output) {
        Item template = EBCraftingItems.ARCANE_UPGRADE_SMITHING_TEMPLATE.get();
        Item grandCrystal = EBItems.MAGIC_CRYSTAL_GRAND.get();
        Item netheriteIngot = Items.NETHERITE_INGOT;

        // All 8 Elements: (Novice -> Apprentice -> Advanced -> Master)
        registerWandSmithingChain(output, template, grandCrystal, netheriteIngot,
                EBWandItems.NOVICE_WAND.get(), EBWandItems.APPRENTICE_WAND.get(), EBWandItems.ADVANCED_WAND.get(), EBWandItems.MASTER_WAND.get(), "magic");

        registerWandSmithingChain(output, template, grandCrystal, netheriteIngot,
                EBWandItems.NOVICE_EARTH_WAND.get(), EBWandItems.APPRENTICE_EARTH_WAND.get(), EBWandItems.ADVANCED_EARTH_WAND.get(), EBWandItems.MASTER_EARTH_WAND.get(), "earth");

        registerWandSmithingChain(output, template, grandCrystal, netheriteIngot,
                EBWandItems.NOVICE_FIRE_WAND.get(), EBWandItems.APPRENTICE_FIRE_WAND.get(), EBWandItems.ADVANCED_FIRE_WAND.get(), EBWandItems.MASTER_FIRE_WAND.get(), "fire");

        registerWandSmithingChain(output, template, grandCrystal, netheriteIngot,
                EBWandItems.NOVICE_HEALING_WAND.get(), EBWandItems.APPRENTICE_HEALING_WAND.get(), EBWandItems.ADVANCED_HEALING_WAND.get(), EBWandItems.MASTER_HEALING_WAND.get(), "healing");

        registerWandSmithingChain(output, template, grandCrystal, netheriteIngot,
                EBWandItems.NOVICE_ICE_WAND.get(), EBWandItems.APPRENTICE_ICE_WAND.get(), EBWandItems.ADVANCED_ICE_WAND.get(), EBWandItems.MASTER_ICE_WAND.get(), "ice");

        registerWandSmithingChain(output, template, grandCrystal, netheriteIngot,
                EBWandItems.NOVICE_LIGHTNING_WAND.get(), EBWandItems.APPRENTICE_LIGHTNING_WAND.get(), EBWandItems.ADVANCED_LIGHTNING_WAND.get(), EBWandItems.MASTER_LIGHTNING_WAND.get(), "lightning");

        registerWandSmithingChain(output, template, grandCrystal, netheriteIngot,
                EBWandItems.NOVICE_NECROMANCY_WAND.get(), EBWandItems.APPRENTICE_NECROMANCY_WAND.get(), EBWandItems.ADVANCED_NECROMANCY_WAND.get(), EBWandItems.MASTER_NECROMANCY_WAND.get(), "necromancy");

        registerWandSmithingChain(output, template, grandCrystal, netheriteIngot,
                EBWandItems.NOVICE_SORCERY_WAND.get(), EBWandItems.APPRENTICE_SORCERY_WAND.get(), EBWandItems.ADVANCED_SORCERY_WAND.get(), EBWandItems.MASTER_SORCERY_WAND.get(), "sorcery");
    }

    private void registerWandSmithingChain(RecipeOutput output, Item template, Item grandCrystal, Item netheriteIngot,
                                           Item novice, Item apprentice, Item advanced, Item master, String element) {
        // Novice -> Apprentice
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(template), Ingredient.of(novice), Ingredient.of(grandCrystal), RecipeCategory.COMBAT, apprentice)
                .unlocks("has_arcane_template", has(template))
                .save(output, WizardryMod.id("smithing_upgrade_" + element + "_wand_apprentice"));

        // Apprentice -> Advanced
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(template), Ingredient.of(apprentice), Ingredient.of(grandCrystal), RecipeCategory.COMBAT, advanced)
                .unlocks("has_arcane_template", has(template))
                .save(output, WizardryMod.id("smithing_upgrade_" + element + "_wand_advanced"));

        // Advanced -> Master
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(template), Ingredient.of(advanced), Ingredient.of(netheriteIngot), RecipeCategory.COMBAT, master)
                .unlocks("has_arcane_template", has(template))
                .save(output, WizardryMod.id("smithing_upgrade_" + element + "_wand_master"));
    }

    private void buildSpellScrollsAndBooksRecipes(RecipeOutput output) {
        registerSpellCrafting(output, "magic_missile", EBItems.MAGIC_CRYSTAL.get(), Items.AMETHYST_SHARD);
        registerSpellCrafting(output, "fireball", EBItems.MAGIC_CRYSTAL_FIRE.get(), Items.BLAZE_ROD);
        registerSpellCrafting(output, "ignite", EBItems.MAGIC_CRYSTAL_FIRE.get(), Items.BLAZE_POWDER);
        registerSpellCrafting(output, "freeze", EBItems.MAGIC_CRYSTAL_ICE.get(), Items.SNOWBALL);
        registerSpellCrafting(output, "frost_ray", EBItems.MAGIC_CRYSTAL_ICE.get(), Items.PACKED_ICE);
        registerSpellCrafting(output, "thunderbolt", EBItems.MAGIC_CRYSTAL_LIGHTNING.get(), Items.LIGHTNING_ROD);
        registerSpellCrafting(output, "arc", EBItems.MAGIC_CRYSTAL_LIGHTNING.get(), Items.COPPER_INGOT);
        registerSpellCrafting(output, "heal", EBItems.MAGIC_CRYSTAL_HEALING.get(), Items.GHAST_TEAR);
        registerSpellCrafting(output, "summon_zombie", EBItems.MAGIC_CRYSTAL_NECROMANCY.get(), Items.ROTTEN_FLESH);
        registerSpellCrafting(output, "dart", EBItems.MAGIC_CRYSTAL_EARTH.get(), Items.FLINT);
        registerSpellCrafting(output, "telekinesis", EBItems.MAGIC_CRYSTAL_SORCERY.get(), Items.ENDER_PEARL);
    }

    private void registerSpellCrafting(RecipeOutput output, String spellName, Item crystal, Item catalyst) {
        ResourceLocation spellId = WizardryMod.id(spellName);

        // Scroll
        ItemStack scrollStack = ScrollItem.createWithSpell(EBCraftingItems.SCROLL.get(), spellId);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, scrollStack)
                .requires(EBCraftingItems.BLANK_SCROLL.get())
                .requires(crystal)
                .requires(catalyst)
                .unlockedBy("has_blank_scroll", has(EBCraftingItems.BLANK_SCROLL.get()))
                .save(output, WizardryMod.id("scroll_" + spellName));

        // Spell Book
        ItemStack bookStack = SpellBookItem.createWithSpell(EBCraftingItems.SPELL_BOOK.get(), spellId);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, bookStack)
                .requires(EBCraftingItems.BLANK_SPELL_BOOK.get())
                .requires(crystal)
                .requires(catalyst)
                .unlockedBy("has_blank_spell_book", has(EBCraftingItems.BLANK_SPELL_BOOK.get()))
                .save(output, WizardryMod.id("spell_book_" + spellName));
    }

    private void buildSpecialRecipes(RecipeOutput output) {
        SpecialRecipeBuilder.special(ScrollToBookRecipe::new)
                .save(output, WizardryMod.id("scroll_to_spell_book"));

        SpecialRecipeBuilder.special(SpellBookCloningRecipe::new)
                .save(output, WizardryMod.id("spell_book_cloning"));
    }
}
