package com.binaris.wizardry.content.item;

import com.binaris.wizardry.content.entity.projectile.ConjuredArrowEntity;
import com.binaris.wizardry.registry.EBEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SpectralBowItem extends BowItem {
    public SpectralBowItem() {
        super(new Properties().rarity(Rarity.UNCOMMON).durability(1200));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, Level world, @NotNull LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;

        int i = this.getUseDuration(stack, entity) - timeLeft;
        if (i < 0) return;
        float f = getPowerForTime(i);
        if (f < 0.1F) return;

        if (!world.isClientSide && EBEntities.CONJURED_ARROW != null && EBEntities.CONJURED_ARROW.get() != null) {
            ConjuredArrowEntity arrow = new ConjuredArrowEntity(EBEntities.CONJURED_ARROW.get(), player, world);
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);

            if (f == 1.0F) arrow.setCritArrow(true);
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            world.addFreshEntity(arrow);
        }

        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, (float) (1.0F / (world.random.nextFloat() * 0.4F + 1.2F) + f * 0.5F));
        player.awardStat(Stats.ITEM_USED.get(this));
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
