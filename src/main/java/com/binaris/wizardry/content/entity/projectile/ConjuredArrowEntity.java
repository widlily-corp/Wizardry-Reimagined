package com.binaris.wizardry.content.entity.projectile;

import com.binaris.wizardry.registry.EBEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConjuredArrowEntity extends AbstractArrow {
    public ConjuredArrowEntity(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
    }

    public ConjuredArrowEntity(Level level) {
        this(EBEntities.CONJURED_ARROW.get(), level);
    }

    public ConjuredArrowEntity(double x, double y, double z, Level level) {
        this(EBEntities.CONJURED_ARROW.get(), level);
        this.setPos(x, y, z);
    }

    public ConjuredArrowEntity(EntityType<? extends AbstractArrow> type, LivingEntity entity, Level level) {
        super(type, entity, level, ItemStack.EMPTY, null);
    }

    public ConjuredArrowEntity(Level level, LivingEntity entity) {
        this(EBEntities.CONJURED_ARROW.get(), entity, level);
    }

    public ConjuredArrowEntity(EntityType<? extends AbstractArrow> type, LivingEntity entity, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(type, entity, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 60) this.discard();
        super.tick();
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean tryPickup(@NotNull Player player) {
        return false;
    }
}
