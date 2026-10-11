package com.binaris.wizardry.content.menu;

import com.binaris.wizardry.content.block.workbench.IWorkbenchItem;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotWorkbenchItem extends Slot {
    private final ArcaneWorkbenchMenu menu;

    public SlotWorkbenchItem(Container container, int slot, int x, int y, ArcaneWorkbenchMenu menu) {
        super(container, slot, x, y);
        this.menu = menu;
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        super.set(stack);
        this.menu.onSlotChanged(index, stack);
    }

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        super.onTake(player, stack);
        this.menu.onSlotChanged(index, ItemStack.EMPTY, player);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof IWorkbenchItem workbenchItem && workbenchItem.canPlace(stack);
    }
}
