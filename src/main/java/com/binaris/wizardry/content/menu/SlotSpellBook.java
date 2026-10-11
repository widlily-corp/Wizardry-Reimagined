package com.binaris.wizardry.content.menu;

import com.binaris.wizardry.registry.EBDataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotSpellBook extends Slot {
    public SlotSpellBook(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.has(EBDataComponents.SPELL_DATA.get());
    }
}
