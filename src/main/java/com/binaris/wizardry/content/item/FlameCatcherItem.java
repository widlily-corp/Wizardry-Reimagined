package com.binaris.wizardry.content.item;

import com.binaris.wizardry.content.entity.projectile.FlamecatcherArrow;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FlameCatcherItem extends BowItem {
    public FlameCatcherItem() {
        super(new Properties().durability(1200).rarity(Rarity.EPIC));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;
        if (level.isClientSide) return;

        int charge = this.getUseDuration(stack, entity) - timeLeft;
        if (charge < 0) return;

        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();
        if (tag.contains("ShotsLeft")) {
            int shotsLeft = tag.getInt("ShotsLeft");
            if (shotsLeft <= 0) {
                stack.shrink(1);
                return;
            }
            tag.putInt("ShotsLeft", shotsLeft - 1);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }

        float velocity = getPowerForTime(charge);
        if ((double) velocity < 0.1D) return;

        FlamecatcherArrow arrow = new FlamecatcherArrow(level);
        arrow.setOwner(player);
        arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
        arrow.aim(player, FlamecatcherArrow.SPEED * velocity);
        level.addFreshEntity(arrow);

        if (EBSounds.ITEM_FLAMECATCHER_SHOOT != null && EBSounds.ITEM_FLAMECATCHER_SHOOT.get() != null) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), EBSounds.ITEM_FLAMECATCHER_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        if (EBSounds.ITEM_FLAMECATCHER_FLAME != null && EBSounds.ITEM_FLAMECATCHER_FLAME.get() != null) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), EBSounds.ITEM_FLAMECATCHER_FLAME.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack stack, @NotNull ItemStack stack1) {
        return false;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }
}
