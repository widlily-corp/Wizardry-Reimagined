package com.binaris.wizardry.content.menu;

import com.binaris.wizardry.content.block.workbench.IWorkbenchItem;
import com.binaris.wizardry.content.item.material.CrystalItem;
import com.binaris.wizardry.content.item.wand.IManaItem;
import com.binaris.wizardry.content.item.wand.WandUpgrades;
import com.binaris.wizardry.registry.EBDataComponents;
import com.binaris.wizardry.registry.EBMenuTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArcaneWorkbenchMenu extends AbstractContainerMenu {
    public static final int CRYSTAL_SLOT = 8;
    public static final int CENTRE_SLOT = 9;
    public static final int UPGRADE_SLOT = 10;
    public static final int TOTAL_SLOTS = 11;
    public static final int SLOT_RADIUS = 42;
    public static final int PLAYER_INVENTORY_SIZE = 36;

    public final Container container;
    private final Player player;
    public boolean needsRefresh;

    public ArcaneWorkbenchMenu(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(TOTAL_SLOTS));
    }

    public ArcaneWorkbenchMenu(int id, Inventory playerInv, Container container) {
        super(EBMenuTypes.ARCANE_WORKBENCH.get(), id);
        this.container = container;
        this.player = playerInv.player;
        ItemStack wand = container.getItem(CENTRE_SLOT);

        // 0-7: Spell Book Slots (initially positioned offscreen)
        for (int i = 0; i < 8; i++) {
            this.addSlot(new SlotSpellBook(container, i, -999, -999));
        }

        // 8: Crystal Slot
        this.addSlot(new SlotItemList(container, CRYSTAL_SLOT, 13, 101, 64,
                stack -> stack.getItem() instanceof CrystalItem || stack.getItem() instanceof IManaItem));

        // 9: Centre/Wand Slot
        this.addSlot(new SlotWorkbenchItem(container, CENTRE_SLOT, 80, 64, this));

        // 10: Upgrade Slot
        this.addSlot(new SlotItemList(container, UPGRADE_SLOT, 147, 17, 1, WandUpgrades::isUpgradeOrTome));

        // Player Inventory: Hotbar (slots 11 - 19)
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInv, x, 8 + x * 18, 196));
        }

        // Player Inventory: Main (slots 20 - 46)
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInv, 9 + x + y * 9, 8 + x * 18, 138 + y * 18));
            }
        }

        onSlotChanged(CENTRE_SLOT, wand, this.player);
    }

    public static int getBookSlotXOffset(int i, int bookSlotCount) {
        float angle = i * (2 * (float) Math.PI) / bookSlotCount;
        return Math.round(SLOT_RADIUS * Mth.sin(angle));
    }

    public static int getBookSlotYOffset(int i, int bookSlotCount) {
        float angle = i * (2 * (float) Math.PI) / bookSlotCount;
        return Math.round(SLOT_RADIUS * -Mth.cos(angle));
    }

    public void onSlotChanged(int slotNumber, ItemStack stack) {
        onSlotChanged(slotNumber, stack, this.player);
    }

    public void onSlotChanged(int slotNumber, ItemStack stack, @NotNull Player player) {
        if (slotNumber != CENTRE_SLOT) return;

        if (stack.isEmpty()) {
            for (int i = 0; i < CRYSTAL_SLOT; i++) {
                hideSlot(i, player);
            }
            return;
        }

        if (stack.getItem() instanceof IWorkbenchItem workbenchItem) {
            int spellSlots = workbenchItem.getSpellSlotCount(stack);
            int centreX = this.getSlot(CENTRE_SLOT).x;
            int centreY = this.getSlot(CENTRE_SLOT).y;

            for (int i = 0; i < spellSlots; i++) {
                int x = centreX + getBookSlotXOffset(i, spellSlots);
                int y = centreY + getBookSlotYOffset(i, spellSlots);
                showSlot(i, x, y);
            }

            for (int i = spellSlots; i < CRYSTAL_SLOT; i++) {
                hideSlot(i, player);
            }
        }
    }

    private static final sun.misc.Unsafe UNSAFE;
    private static final long X_OFFSET;
    private static final long Y_OFFSET;

    static {
        try {
            java.lang.reflect.Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (sun.misc.Unsafe) field.get(null);
            X_OFFSET = UNSAFE.objectFieldOffset(Slot.class.getDeclaredField("x"));
            Y_OFFSET = UNSAFE.objectFieldOffset(Slot.class.getDeclaredField("y"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Slot offset in ArcaneWorkbenchMenu", e);
        }
    }

    private void showSlot(int index, int x, int y) {
        Slot slot = this.getSlot(index);
        UNSAFE.putInt(slot, X_OFFSET, x);
        UNSAFE.putInt(slot, Y_OFFSET, y);
    }

    private void hideSlot(int index, Player player) {
        Slot slot = this.getSlot(index);
        UNSAFE.putInt(slot, X_OFFSET, -999);
        UNSAFE.putInt(slot, Y_OFFSET, -999);

        this.quickMoveStack(player, index);
        ItemStack remaining = slot.getItem();

        if (remaining.isEmpty()) return;

        slot.set(ItemStack.EMPTY);
        if (player.addItem(remaining)) return;

        ItemEntity itemEntity = player.drop(remaining, false);
        if (itemEntity != null) {
            itemEntity.setNoPickUpDelay();
            itemEntity.setTarget(player.getUUID());
        }
    }

    public void onApplyButtonPressed(Player player) {
        if (player.level().isClientSide) return;

        Slot centre = this.getSlot(CENTRE_SLOT);
        if (centre.getItem().getItem() instanceof IWorkbenchItem workbenchItem) {
            Slot[] spellBooks = new Slot[8];
            for (int i = 0; i < 8; i++) {
                spellBooks[i] = this.getSlot(i);
            }

            workbenchItem.onApplyButtonPressed(player, centre, this.getSlot(CRYSTAL_SLOT), this.getSlot(UPGRADE_SLOT), spellBooks);
        }
    }

    public void onClearButtonPressed(Player player) {
        Slot centre = this.getSlot(CENTRE_SLOT);
        if (centre.getItem().getItem() instanceof IWorkbenchItem workbenchItem) {
            Slot[] spellBooks = new Slot[8];
            for (int i = 0; i < 8; i++) {
                spellBooks[i] = this.getSlot(i);
            }
            workbenchItem.onClearButtonPressed(player, centre, this.getSlot(CRYSTAL_SLOT), this.getSlot(UPGRADE_SLOT), spellBooks);
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (!slot.hasItem()) return itemstack;

        ItemStack itemstack1 = slot.getItem();
        itemstack = itemstack1.copy();

        if (index < TOTAL_SLOTS) {
            if (!this.moveItemStackTo(itemstack1, TOTAL_SLOTS, TOTAL_SLOTS + PLAYER_INVENTORY_SIZE, true)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TOTAL_SLOTS + PLAYER_INVENTORY_SIZE) {
            int[] slotRange = findSlotRangeForItem(itemstack1);
            if (slotRange != null) {
                if (!this.moveItemStackTo(itemstack1, slotRange[0], slotRange[1] + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (itemstack1.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (itemstack1.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, itemstack1);
        return itemstack;
    }

    public int @Nullable [] findSlotRangeForItem(ItemStack stack) {
        if (stack.has(EBDataComponents.SPELL_DATA.get())) {
            ItemStack centreStack = getSlot(CENTRE_SLOT).getItem();
            if (centreStack.getItem() instanceof IWorkbenchItem workbenchItem) {
                int spellSlots = workbenchItem.getSpellSlotCount(centreStack);
                if (spellSlots > 0) return new int[]{0, spellSlots - 1};
            }
        } else if (getSlot(CRYSTAL_SLOT).mayPlace(stack)) {
            return new int[]{CRYSTAL_SLOT, CRYSTAL_SLOT};
        } else if (getSlot(CENTRE_SLOT).mayPlace(stack)) {
            return new int[]{CENTRE_SLOT, CENTRE_SLOT};
        } else if (getSlot(UPGRADE_SLOT).mayPlace(stack)) {
            return new int[]{UPGRADE_SLOT, UPGRADE_SLOT};
        }
        return null;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.container.stillValid(player);
    }
}
