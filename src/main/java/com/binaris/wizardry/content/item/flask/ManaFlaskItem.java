package com.binaris.wizardry.content.item.flask;

import com.binaris.wizardry.content.item.wand.IManaItem;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ManaFlaskItem extends Item {
    private final int manaAmount;

    public ManaFlaskItem(int manaAmount, Properties properties) {
        super(properties);
        this.manaAmount = manaAmount;
    }

    public int getManaAmount() {
        return manaAmount;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        // Check offhand first if using main hand, or main hand if using offhand
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack otherStack = player.getItemInHand(otherHand);

        if (canRecharge(otherStack)) {
            return applyRecharge(level, player, heldStack, otherStack);
        }

        // Scan player inventory for an IManaItem needing recharge
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (canRecharge(stack)) {
                return applyRecharge(level, player, heldStack, stack);
            }
        }

        return InteractionResultHolder.pass(heldStack);
    }

    private boolean canRecharge(ItemStack stack) {
        return !stack.isEmpty()
                && stack.getItem() instanceof IManaItem manaItem
                && manaItem.getManaCapacity(stack) > 0
                && !manaItem.isManaFull(stack);
    }

    private InteractionResultHolder<ItemStack> applyRecharge(Level level, Player player, ItemStack heldStack, ItemStack targetStack) {
        if (!level.isClientSide) {
            if (targetStack.getItem() instanceof IManaItem manaItem) {
                manaItem.rechargeMana(targetStack, manaAmount);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    EBSounds.ITEM_MANA_FLASK_RECHARGE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            player.awardStat(Stats.ITEM_USED.get(this));

            if (!player.getAbilities().instabuild) {
                heldStack.shrink(1);
                net.minecraft.world.item.ItemStack bottle = new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.GLASS_BOTTLE);
                if (heldStack.isEmpty()) {
                    return InteractionResultHolder.sidedSuccess(bottle, false);
                }
                if (!player.getInventory().add(bottle)) {
                    player.drop(bottle, false);
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(heldStack, level.isClientSide);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.ebwizardry.mana_flask.desc", this.manaAmount).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
