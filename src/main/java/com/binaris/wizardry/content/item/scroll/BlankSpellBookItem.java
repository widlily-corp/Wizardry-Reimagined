package com.binaris.wizardry.content.item.scroll;

import com.binaris.wizardry.content.block.workbench.IWorkbenchItem;
import com.binaris.wizardry.content.block.workbench.WorkbenchUtils;
import com.binaris.wizardry.content.component.SpellData;
import com.binaris.wizardry.registry.EBCraftingItems;
import com.binaris.wizardry.registry.EBDataComponents;
import com.binaris.wizardry.registry.EBRegistries;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.Spells;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BlankSpellBookItem extends Item implements IWorkbenchItem {
    public BlankSpellBookItem(Properties properties) {
        super(properties.stacksTo(16));
    }

    @Override
    public int getSpellSlotCount(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean showTooltip(ItemStack stack) {
        return false;
    }

    @Override
    public boolean onApplyButtonPressed(Player player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
        if (!spellBooks[0].hasItem() || crystals.getItem().isEmpty()) return false;
        ItemStack bookOrScroll = spellBooks[0].getItem();
        SpellData spellData = bookOrScroll.get(EBDataComponents.SPELL_DATA.get());
        if (spellData == null || spellData.isEmpty()) return false;
        Spell spell = EBRegistries.SPELL_REGISTRY.get(spellData.spellId());
        if (spell == null || spell == Spells.NONE.get()) return false;

        int cost = Math.max(10, spell.getCost() * 2);
        int manaPerCrystal = WorkbenchUtils.getManaValuePerCrystal(crystals.getItem());
        if (manaPerCrystal <= 0) return false;

        int crystalsNeeded = Mth.ceil((float) cost / manaPerCrystal);
        if (crystals.getItem().getCount() >= crystalsNeeded) {
            crystals.remove(crystalsNeeded);
            ItemStack newBook = SpellBookItem.createWithSpell(EBCraftingItems.SPELL_BOOK.get(), spell);
            centre.remove(1);
            if (centre.getItem().isEmpty()) {
                centre.set(newBook);
            } else {
                if (!player.addItem(newBook)) {
                    player.drop(newBook, false);
                }
            }
            centre.setChanged();
            crystals.setChanged();
            return true;
        }
        return false;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatableWithFallback("item.ebwizardry.blank_spell_book", "Blank Spell Book");
    }
}
