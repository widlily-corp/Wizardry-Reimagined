package com.binaris.wizardry.compat.jei;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.client.gui.ArcaneWorkbenchScreen;
import com.binaris.wizardry.content.item.bauble.AmuletItem;
import com.binaris.wizardry.content.item.bauble.RingItem;
import com.binaris.wizardry.registry.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class WizardryJeiPlugin implements IModPlugin {
    public static final ResourceLocation PLUGIN_UID =
            ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new ArcaneWorkbenchRecipeCategory(guiHelper),
                new ImbuementAltarRecipeCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // 1. Arcane Workbench recipes
        registration.addRecipes(ArcaneWorkbenchRecipeCategory.RECIPE_TYPE, ArcaneWorkbenchRecipeMaker.createRecipes());

        // 2. Imbuement Altar recipes
        registration.addRecipes(ImbuementAltarRecipeCategory.RECIPE_TYPE, ImbuementAltarRecipeMaker.createRecipes());

        // 3. Information descriptions
        registerItemDescriptions(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(EBWorkbenchBlocks.ARCANE_WORKBENCH.get(), ArcaneWorkbenchRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(EBBlocks.IMBUEMENT_ALTAR.get(), ImbuementAltarRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(ArcaneWorkbenchScreen.class, new ArcaneWorkbenchGuiHandler());
        registration.addRecipeClickArea(ArcaneWorkbenchScreen.class, 68, 52, 40, 40, ArcaneWorkbenchRecipeCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(ArcaneWorkbenchScreen.class, 146, 100, 24, 24, ArcaneWorkbenchRecipeCategory.RECIPE_TYPE);
    }

    private void registerItemDescriptions(IRecipeRegistration registration) {
        // 1. Scrolls & Spell Books
        List<ItemStack> scrollStacks = List.of(
                new ItemStack(EBCraftingItems.SCROLL.get()),
                new ItemStack(EBCraftingItems.BLANK_SCROLL.get())
        );
        registration.addIngredientInfo(scrollStacks, VanillaTypes.ITEM_STACK, Component.translatable("jei.ebwizardry.info.scroll"));

        // 2. Crystals
        List<ItemStack> crystalStacks = List.of(
                new ItemStack(EBItems.MAGIC_CRYSTAL.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_SHARD.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_GRAND.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_EARTH.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_FIRE.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_HEALING.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_ICE.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_LIGHTNING.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_NECROMANCY.get()),
                new ItemStack(EBItems.MAGIC_CRYSTAL_SORCERY.get())
        );
        registration.addIngredientInfo(crystalStacks, VanillaTypes.ITEM_STACK, Component.translatable("jei.ebwizardry.info.crystal"));

        // 3. Arcane Tomes
        List<ItemStack> tomeStacks = List.of(
                new ItemStack(EBCraftingItems.APPRENTICE_TOME.get()),
                new ItemStack(EBCraftingItems.ADVANCED_TOME.get()),
                new ItemStack(EBCraftingItems.MASTER_TOME.get()),
                new ItemStack(EBCraftingItems.ARCANE_TOME.get()),
                new ItemStack(EBCraftingItems.APPRENTICE_ARCANE_TOME.get()),
                new ItemStack(EBCraftingItems.ADVANCED_ARCANE_TOME.get()),
                new ItemStack(EBCraftingItems.MASTER_ARCANE_TOME.get())
        );
        registration.addIngredientInfo(tomeStacks, VanillaTypes.ITEM_STACK, Component.translatable("jei.ebwizardry.info.arcane_tome"));

        // 4. Rings
        List<ItemStack> ringStacks = new ArrayList<>();
        for (DeferredHolder<Item, ? extends Item> holder : EBBaubleItems.ITEMS.getEntries()) {
            if (holder.get() instanceof RingItem) {
                ringStacks.add(new ItemStack(holder.get()));
            }
        }
        if (!ringStacks.isEmpty()) {
            registration.addIngredientInfo(ringStacks, VanillaTypes.ITEM_STACK, Component.translatable("jei.ebwizardry.info.ring"));
        }

        // 5. Amulets
        List<ItemStack> amuletStacks = new ArrayList<>();
        for (DeferredHolder<Item, ? extends Item> holder : EBBaubleItems.ITEMS.getEntries()) {
            if (holder.get() instanceof AmuletItem) {
                amuletStacks.add(new ItemStack(holder.get()));
            }
        }
        if (!amuletStacks.isEmpty()) {
            registration.addIngredientInfo(amuletStacks, VanillaTypes.ITEM_STACK, Component.translatable("jei.ebwizardry.info.amulet"));
        }

        // 6. Wand Upgrades
        List<ItemStack> upgradeStacks = List.of(
                new ItemStack(EBCraftingItems.STORAGE_UPGRADE.get()),
                new ItemStack(EBCraftingItems.SIPHON_UPGRADE.get()),
                new ItemStack(EBCraftingItems.RANGE_UPGRADE.get()),
                new ItemStack(EBCraftingItems.BLAST_UPGRADE.get()),
                new ItemStack(EBCraftingItems.COOLDOWN_UPGRADE.get()),
                new ItemStack(EBCraftingItems.CONDENSER_UPGRADE.get()),
                new ItemStack(EBCraftingItems.ATTUNEMENT_UPGRADE.get()),
                new ItemStack(EBCraftingItems.DURATION_UPGRADE.get())
        );
        registration.addIngredientInfo(upgradeStacks, VanillaTypes.ITEM_STACK, Component.translatable("jei.ebwizardry.info.wand_upgrade"));
    }
}
