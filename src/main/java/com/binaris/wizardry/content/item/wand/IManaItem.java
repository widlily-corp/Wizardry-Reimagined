package com.binaris.wizardry.content.item.wand;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IManaItem {
    int getMana(ItemStack stack);

    int getManaCapacity(ItemStack stack);

    void setMana(ItemStack stack, int mana);

    default void consumeMana(ItemStack stack, int amount, @Nullable Player player) {
        if (player != null && player.isCreative()) return;
        setMana(stack, getMana(stack) - amount);
    }

    default void rechargeMana(ItemStack stack, int amount) {
        setMana(stack, getMana(stack) + amount);
    }

    default boolean isManaFull(ItemStack stack) {
        return getMana(stack) >= getManaCapacity(stack);
    }

    default boolean showManaInWorkbench(@Nullable Player player, ItemStack stack) {
        return true;
    }
}
