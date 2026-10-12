package com.binaris.wizardry.compat.jei;

import com.binaris.wizardry.content.item.scroll.SpellBookItem;
import com.binaris.wizardry.content.item.wand.WandItem;
import com.binaris.wizardry.content.item.wand.WandUpgrades;
import com.binaris.wizardry.registry.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class ArcaneWorkbenchRecipeMaker {

    public static List<ArcaneWorkbenchJeiRecipe> createRecipes() {
        List<ArcaneWorkbenchJeiRecipe> recipes = new ArrayList<>();

        createTierUpgradeRecipes(recipes);
        createSpecialUpgradeRecipes(recipes);
        createSpellBindingRecipes(recipes);
        createRechargeRecipes(recipes);

        return recipes;
    }

    private static void createTierUpgradeRecipes(List<ArcaneWorkbenchJeiRecipe> recipes) {
        Element[] elements = new Element[] {
                Elements.MAGIC.get(),
                Elements.EARTH.get(),
                Elements.FIRE.get(),
                Elements.HEALING.get(),
                Elements.ICE.get(),
                Elements.LIGHTNING.get(),
                Elements.NECROMANCY.get(),
                Elements.SORCERY.get()
        };

        for (Element element : elements) {
            WandItem noviceWand = EBWandItems.getWand(SpellTiers.NOVICE.get(), element);
            WandItem apprenticeWand = EBWandItems.getWand(SpellTiers.APPRENTICE.get(), element);
            WandItem advancedWand = EBWandItems.getWand(SpellTiers.ADVANCED.get(), element);
            WandItem masterWand = EBWandItems.getWand(SpellTiers.MASTER.get(), element);

            // Novice -> Apprentice
            recipes.add(new ArcaneWorkbenchJeiRecipe(
                    new ItemStack(noviceWand),
                    new ItemStack(EBCraftingItems.APPRENTICE_TOME.get()),
                    new ItemStack(apprenticeWand),
                    ArcaneWorkbenchJeiRecipe.RecipeType.TIER_UPGRADE,
                    Component.translatable("jei.ebwizardry.arcane_workbench.tier_apprentice")
            ));

            // Apprentice -> Advanced
            recipes.add(new ArcaneWorkbenchJeiRecipe(
                    new ItemStack(apprenticeWand),
                    new ItemStack(EBCraftingItems.ADVANCED_TOME.get()),
                    new ItemStack(advancedWand),
                    ArcaneWorkbenchJeiRecipe.RecipeType.TIER_UPGRADE,
                    Component.translatable("jei.ebwizardry.arcane_workbench.tier_advanced")
            ));

            // Advanced -> Master
            recipes.add(new ArcaneWorkbenchJeiRecipe(
                    new ItemStack(advancedWand),
                    new ItemStack(EBCraftingItems.MASTER_TOME.get()),
                    new ItemStack(masterWand),
                    ArcaneWorkbenchJeiRecipe.RecipeType.TIER_UPGRADE,
                    Component.translatable("jei.ebwizardry.arcane_workbench.tier_master")
            ));
        }
    }

    private static void createSpecialUpgradeRecipes(List<ArcaneWorkbenchJeiRecipe> recipes) {
        List<ItemStack> allWands = getAllNoviceWands();

        var upgrades = List.of(
                new UpgradeEntry(EBCraftingItems.STORAGE_UPGRADE.get(), WandUpgrades.STORAGE, "jei.ebwizardry.arcane_workbench.upgrade_storage"),
                new UpgradeEntry(EBCraftingItems.SIPHON_UPGRADE.get(), WandUpgrades.SIPHON, "jei.ebwizardry.arcane_workbench.upgrade_siphon"),
                new UpgradeEntry(EBCraftingItems.RANGE_UPGRADE.get(), WandUpgrades.RANGE, "jei.ebwizardry.arcane_workbench.upgrade_range"),
                new UpgradeEntry(EBCraftingItems.BLAST_UPGRADE.get(), WandUpgrades.BLAST, "jei.ebwizardry.arcane_workbench.upgrade_blast"),
                new UpgradeEntry(EBCraftingItems.COOLDOWN_UPGRADE.get(), WandUpgrades.COOLDOWN, "jei.ebwizardry.arcane_workbench.upgrade_cooldown"),
                new UpgradeEntry(EBCraftingItems.CONDENSER_UPGRADE.get(), WandUpgrades.CONDENSER, "jei.ebwizardry.arcane_workbench.upgrade_condenser"),
                new UpgradeEntry(EBCraftingItems.ATTUNEMENT_UPGRADE.get(), WandUpgrades.ATTUNEMENT, "jei.ebwizardry.arcane_workbench.upgrade_attunement"),
                new UpgradeEntry(EBCraftingItems.DURATION_UPGRADE.get(), WandUpgrades.DURATION, "jei.ebwizardry.arcane_workbench.upgrade_duration")
        );

        for (UpgradeEntry entry : upgrades) {
            ItemStack baseWand = new ItemStack(EBWandItems.NOVICE_WAND.get());
            ItemStack upgradeStack = new ItemStack(entry.item);
            ItemStack result = ((WandItem) baseWand.getItem()).applyUpgrade(null, baseWand.copy(), upgradeStack.copy());

            recipes.add(new ArcaneWorkbenchJeiRecipe(
                    allWands,
                    List.of(upgradeStack),
                    result,
                    ArcaneWorkbenchJeiRecipe.RecipeType.SPECIAL_UPGRADE,
                    Component.translatable(entry.langKey)
            ));
        }
    }

    private static void createSpellBindingRecipes(List<ArcaneWorkbenchJeiRecipe> recipes) {
        List<ItemStack> allWands = getAllNoviceWands();

        var spells = List.of(
                EBSpells.MAGIC_MISSILE.get(),
                EBSpells.FIREBALL.get(),
                EBSpells.ICE_SHARD.get(),
                EBSpells.THUNDERBOLT.get(),
                EBSpells.HEAL.get(),
                EBSpells.SUMMON_ZOMBIE.get(),
                EBSpells.DART.get(),
                EBSpells.TELEKINESIS.get()
        );

        for (Spell spell : spells) {
            ItemStack baseWand = new ItemStack(EBWandItems.NOVICE_WAND.get());
            ItemStack book = SpellBookItem.createWithSpell(EBCraftingItems.SPELL_BOOK.get(), spell);

            ItemStack resultWand = baseWand.copy();
            WandItem wandItem = (WandItem) resultWand.getItem();
            var wandData = wandItem.getWandData(resultWand);
            List<net.minecraft.resources.ResourceLocation> spellList = new ArrayList<>(wandData.spells());
            if (spellList.isEmpty()) {
                spellList.add(spell.getLocation());
            } else {
                spellList.set(0, spell.getLocation());
            }
            wandItem.setWandData(resultWand, wandData.withSpells(spellList));

            recipes.add(new ArcaneWorkbenchJeiRecipe(
                    allWands,
                    List.of(book),
                    resultWand,
                    ArcaneWorkbenchJeiRecipe.RecipeType.SPELL_BINDING,
                    Component.translatable("jei.ebwizardry.arcane_workbench.bind_spell", spell.getDescriptionFormatted())
            ));
        }
    }

    private static void createRechargeRecipes(List<ArcaneWorkbenchJeiRecipe> recipes) {
        List<ItemStack> allDepletedWands = new ArrayList<>();
        for (ItemStack wand : getAllNoviceWands()) {
            ItemStack depleted = wand.copy();
            ((WandItem) depleted.getItem()).setMana(depleted, 0);
            allDepletedWands.add(depleted);
        }

        List<ItemStack> crystals = List.of(
                new ItemStack(EBItems.MAGIC_CRYSTAL.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_GRAND.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_SHARD.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_FIRE.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_ICE.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_LIGHTNING.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_EARTH.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_HEALING.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_NECROMANCY.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_SORCERY.get())
        );

        ItemStack rechargedWand = new ItemStack(EBWandItems.NOVICE_WAND.get());
        ((WandItem) rechargedWand.getItem()).setMana(rechargedWand, ((WandItem) rechargedWand.getItem()).getManaCapacity(rechargedWand));

        recipes.add(new ArcaneWorkbenchJeiRecipe(
                allDepletedWands,
                crystals,
                rechargedWand,
                ArcaneWorkbenchJeiRecipe.RecipeType.RECHARGE,
                Component.translatable("jei.ebwizardry.arcane_workbench.recharge")
        ));
    }

    private static List<ItemStack> getAllNoviceWands() {
        return List.of(
                new ItemStack(EBWandItems.NOVICE_WAND.get()),
                new ItemStack(EBWandItems.NOVICE_EARTH_WAND.get()),
                new ItemStack(EBWandItems.NOVICE_FIRE_WAND.get()),
                new ItemStack(EBWandItems.NOVICE_HEALING_WAND.get()),
                new ItemStack(EBWandItems.NOVICE_ICE_WAND.get()),
                new ItemStack(EBWandItems.NOVICE_LIGHTNING_WAND.get()),
                new ItemStack(EBWandItems.NOVICE_NECROMANCY_WAND.get()),
                new ItemStack(EBWandItems.NOVICE_SORCERY_WAND.get())
        );
    }

    private record UpgradeEntry(net.minecraft.world.item.Item item, String upgradeId, String langKey) {}

    private ArcaneWorkbenchRecipeMaker() {}
}
