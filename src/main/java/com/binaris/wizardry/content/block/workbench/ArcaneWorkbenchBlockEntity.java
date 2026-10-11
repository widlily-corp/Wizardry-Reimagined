package com.binaris.wizardry.content.block.workbench;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.material.CrystalItem;
import com.binaris.wizardry.content.item.wand.IManaItem;
import com.binaris.wizardry.content.item.wand.WandItem;
import com.binaris.wizardry.content.item.wand.WandUpgrades;
import com.binaris.wizardry.content.menu.ArcaneWorkbenchMenu;
import com.binaris.wizardry.registry.EBDataComponents;
import com.binaris.wizardry.registry.EBWorkbenchBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArcaneWorkbenchBlockEntity extends BaseContainerBlockEntity {
    public float timer = 0;
    public float rot;
    public float oRot;
    public float tRot;
    private NonNullList<ItemStack> inventory;

    public ArcaneWorkbenchBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EBWorkbenchBlocks.ARCANE_WORKBENCH_BE.get(), blockPos, blockState);
        this.inventory = NonNullList.withSize(ArcaneWorkbenchMenu.TOTAL_SLOTS, ItemStack.EMPTY);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ArcaneWorkbenchBlockEntity entity) {
        ItemStack stack = entity.getItem(ArcaneWorkbenchMenu.CENTRE_SLOT);
        if (stack.getItem() instanceof WandItem wandItem && !level.isClientSide && !wandItem.isManaFull(stack)
                && level.getGameTime() % WandItem.CONDENSER_TICK_INTERVAL == 0) {
            int condenserLevel = wandItem.getUpgradeLevel(stack, WandUpgrades.CONDENSER);
            if (condenserLevel > 0) {
                wandItem.rechargeMana(stack, condenserLevel);
                entity.setChanged();
            }
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, ArcaneWorkbenchBlockEntity entity) {
        if (!level.isClientSide) return;

        entity.timer++;
        entity.oRot = entity.rot;

        Player player = level.getNearestPlayer(
                (double) pos.getX() + 0.5D,
                (double) pos.getY() + 0.5D,
                (double) pos.getZ() + 0.5D,
                3.0D,
                false
        );

        if (player != null) {
            double dx = player.getX() - ((double) pos.getX() + 0.5D);
            double dz = player.getZ() - ((double) pos.getZ() + 0.5D);
            entity.tRot = (float) Math.atan2(dz, dx);
        } else {
            entity.tRot += 0.02F;
        }

        while (entity.rot >= (float) Math.PI) entity.rot -= ((float) Math.PI * 2F);
        while (entity.rot < -(float) Math.PI) entity.rot += ((float) Math.PI * 2F);
        while (entity.tRot >= (float) Math.PI) entity.tRot -= ((float) Math.PI * 2F);
        while (entity.tRot < -(float) Math.PI) entity.tRot += ((float) Math.PI * 2F);

        float rotDiff = entity.tRot - entity.rot;
        while (rotDiff >= (float) Math.PI) rotDiff -= ((float) Math.PI * 2F);
        while (rotDiff < -(float) Math.PI) rotDiff += ((float) Math.PI * 2F);

        entity.rot += rotDiff * 0.4F;
    }

    public void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.inventory = items;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ContainerHelper.removeItem(this.inventory, slot, amount);
        this.sync();
        return stack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = ContainerHelper.takeItem(this.inventory, slot);
        this.sync();
        return stack;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        inventory.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        this.sync();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return level != null && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) < 64.0;
    }

    @Override
    public boolean canPlaceItem(int slotNumber, @NotNull ItemStack itemstack) {
        if (itemstack.isEmpty()) return true;

        if (slotNumber >= 0 && slotNumber < ArcaneWorkbenchMenu.CRYSTAL_SLOT) {
            if (!itemstack.has(EBDataComponents.SPELL_DATA.get())) return false;
            ItemStack centreStack = getItem(ArcaneWorkbenchMenu.CENTRE_SLOT);
            if (centreStack.getItem() instanceof IWorkbenchItem workbenchItem) {
                return slotNumber < workbenchItem.getSpellSlotCount(centreStack);
            }
            return false;
        } else if (slotNumber == ArcaneWorkbenchMenu.CRYSTAL_SLOT) {
            return itemstack.getItem() instanceof CrystalItem || itemstack.getItem() instanceof IManaItem;
        } else if (slotNumber == ArcaneWorkbenchMenu.CENTRE_SLOT) {
            return itemstack.getItem() instanceof IWorkbenchItem;
        } else if (slotNumber == ArcaneWorkbenchMenu.UPGRADE_SLOT) {
            return WandUpgrades.isUpgradeOrTome(itemstack);
        }
        return true;
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (this.inventory == null) {
            this.inventory = NonNullList.withSize(ArcaneWorkbenchMenu.TOTAL_SLOTS, ItemStack.EMPTY);
        }
        this.inventory.clear();
        ContainerHelper.loadAllItems(tag, this.inventory, registries);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.inventory, registries);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
        return new ArcaneWorkbenchMenu(containerId, inventory, this);
    }

    @Override
    public void clearContent() {
        inventory.clear();
        this.sync();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container." + WizardryMod.MODID + ".arcane_workbench");
    }
}
