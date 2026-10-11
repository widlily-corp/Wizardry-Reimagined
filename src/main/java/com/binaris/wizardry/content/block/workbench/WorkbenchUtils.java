package com.binaris.wizardry.content.block.workbench;

import com.binaris.wizardry.content.component.SpellData;
import com.binaris.wizardry.content.component.WandData;
import com.binaris.wizardry.content.item.material.CrystalItem;
import com.binaris.wizardry.content.item.wand.IManaItem;
import com.binaris.wizardry.content.item.wand.WandItem;
import com.binaris.wizardry.registry.EBDataComponents;
import com.binaris.wizardry.registry.EBRegistries;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class WorkbenchUtils {

    public static boolean rechargeManaFromCrystals(Slot centre, Slot crystals) {
        ItemStack wandStack = centre.getItem();
        if (!(wandStack.getItem() instanceof IManaItem manaItem)) return false;
        if (!crystals.hasItem() || manaItem.isManaFull(wandStack)) return false;

        int chargeDepleted = manaItem.getManaCapacity(wandStack) - manaItem.getMana(wandStack);
        int manaPerItem = getManaValuePerCrystal(crystals.getItem());
        if (manaPerItem <= 0) return false;

        int totalAvailableMana = crystals.getItem().getCount() * manaPerItem;

        if (totalAvailableMana <= chargeDepleted) {
            manaItem.rechargeMana(wandStack, totalAvailableMana);
            crystals.set(ItemStack.EMPTY);
        } else {
            manaItem.setMana(wandStack, manaItem.getManaCapacity(wandStack));
            int countNeeded = (int) Math.ceil((double) chargeDepleted / manaPerItem);
            crystals.remove(countNeeded);
        }

        centre.setChanged();
        crystals.setChanged();
        return true;
    }

    public static int getManaValuePerCrystal(ItemStack crystal) {
        if (crystal.getItem() instanceof CrystalItem crystalItem) {
            return crystalItem.getManaCapacity();
        }
        if (crystal.getItem() instanceof IManaItem manaItem) {
            return manaItem.getMana(crystal);
        }
        return 0;
    }

    public static boolean applySpellBooks(Slot centre, Slot[] spellBooks, SpellTier wandTier) {
        ItemStack wandStack = centre.getItem();
        if (!(wandStack.getItem() instanceof WandItem wandItem)) return false;

        WandData wandData = wandItem.getWandData(wandStack);
        int slotCount = wandItem.getSpellSlotCount(wandStack);
        List<ResourceLocation> spells = new ArrayList<>(wandData.spells());
        while (spells.size() < slotCount) {
            spells.add(ResourceLocation.fromNamespaceAndPath("ebwizardry", "none"));
        }

        boolean changed = false;

        for (int i = 0; i < slotCount && i < spellBooks.length; i++) {
            if (!spellBooks[i].hasItem()) continue;

            ItemStack bookStack = spellBooks[i].getItem();
            SpellData spellData = bookStack.get(EBDataComponents.SPELL_DATA.get());
            if (spellData == null || spellData.isEmpty()) continue;

            ResourceLocation spellId = spellData.spellId();
            Spell spell = EBRegistries.SPELL_REGISTRY.get(spellId);
            if (spell == null) continue;

            if (spell.getTier().getLevel() <= wandTier.getLevel()) {
                if (!spellId.equals(spells.get(i))) {
                    spells.set(i, spellId);
                    changed = true;
                }
            }
        }

        if (changed) {
            wandItem.setWandData(wandStack, wandData.withSpells(spells));
            centre.setChanged();
        }

        return changed;
    }

    private WorkbenchUtils() {}
}
