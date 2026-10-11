package com.binaris.wizardry.content.item.bauble;

import com.binaris.wizardry.compat.curios.CuriosCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BaubleItem extends Item {
    public enum BaubleType {
        RING,
        AMULET
    }

    private final BaubleType baubleType;

    public BaubleItem(BaubleType baubleType, Rarity rarity, Properties properties) {
        super(properties.stacksTo(1).rarity(rarity));
        this.baubleType = baubleType;
    }

    public BaubleType getBaubleType() {
        return baubleType;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            onWornTick(stack, player, level);
        }
    }

    public void onWornTick(ItemStack stack, Player player, Level level) {
    }

    public static boolean isEquippedOrInInventory(Player player, Item item) {
        if (player == null || item == null) return false;

        // 1. Curios integration check
        if (CuriosCompat.isEquipped(player, item)) {
            return true;
        }

        // 2. Inventory check (active in player's inventory as decided by /grill-me)
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasBauble(Player player, Item item) {
        return isEquippedOrInInventory(player, item);
    }
}
