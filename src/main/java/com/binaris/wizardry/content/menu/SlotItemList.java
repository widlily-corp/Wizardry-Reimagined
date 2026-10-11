package com.binaris.wizardry.content.menu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SlotItemList extends Slot {
    private final int maxStackSize;
    private final Predicate<ItemStack> validator;

    public SlotItemList(Container container, int slot, int x, int y, int maxStackSize, Predicate<ItemStack> validator) {
        super(container, slot, x, y);
        this.maxStackSize = maxStackSize;
        this.validator = validator;
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return validator.test(stack);
    }
}
