package com.binaris.wizardry.content.item;

import com.binaris.wizardry.content.entity.projectile.BombEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BombItem extends Item {
    private final Supplier<? extends EntityType<?>> entityType;
    private final Supplier<SoundEvent> sound;

    public BombItem(Supplier<? extends EntityType<?>> entityType, Supplier<SoundEvent> sound) {
        super(new Properties().stacksTo(16));
        this.entityType = entityType;
        this.sound = sound;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        player.getCooldowns().addCooldown(this, 20);
        if (sound != null && sound.get() != null) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), sound.get(), player.getSoundSource(), 0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
        }

        if (!level.isClientSide && entityType != null && entityType.get() != null) {
            Entity created = entityType.get().create(level);
            if (created instanceof BombEntity bombEntity) {
                bombEntity.setOwner(player);
                bombEntity.setPos(player.getX(), player.getY() + player.getEyeHeight() - 0.1, player.getZ());
                bombEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.7F, 1.0F);
                level.addFreshEntity(bombEntity);
            }
        }

        if (!player.isCreative()) {
            stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
