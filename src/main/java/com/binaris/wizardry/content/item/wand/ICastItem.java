package com.binaris.wizardry.content.item.wand;

import com.binaris.wizardry.registry.Spell;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface ICastItem {
    Spell getCurrentSpell(ItemStack stack);

    Spell[] getSpells(ItemStack stack);

    boolean selectSpell(ItemStack stack, int index);

    void selectNextSpell(ItemStack stack);

    void selectPreviousSpell(ItemStack stack);

    int getCurrentCooldown(ItemStack stack, Level level);

    int getCurrentMaxCooldown(ItemStack stack);

    default boolean showSpellsInWorkbench(@Nullable Player player, ItemStack stack) {
        return true;
    }
}
