package com.binaris.wizardry.content.item.armor;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.block.workbench.IWorkbenchItem;
import com.binaris.wizardry.content.block.workbench.WorkbenchUtils;
import com.binaris.wizardry.content.item.wand.IManaItem;
import com.binaris.wizardry.registry.EBDataComponents;
import com.binaris.wizardry.registry.Element;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WizardArmorItem extends ArmorItem implements IManaItem, IWorkbenchItem {
    public static final float DEFAULT_COST_REDUCTION = 0.10F;

    private final @Nullable java.util.function.Supplier<Element> elementSupplier;
    private final float costReduction;

    public WizardArmorItem(Holder<ArmorMaterial> material, Type type, @Nullable java.util.function.Supplier<Element> elementSupplier, int durability, Properties properties) {
        super(material, type, properties.durability(durability));
        this.elementSupplier = elementSupplier;
        this.costReduction = DEFAULT_COST_REDUCTION;
    }

    public WizardArmorItem(Holder<ArmorMaterial> material, Type type, @Nullable Element element, int durability, Properties properties) {
        this(material, type, element != null ? () -> element : null, durability, properties);
    }

    public @Nullable Element getElement() {
        return elementSupplier != null ? elementSupplier.get() : null;
    }

    public float getCostReduction() {
        return costReduction;
    }

    // ==========================================
    // IManaItem Implementation
    // ==========================================

    @Override
    public int getMana(ItemStack stack) {
        if (!stack.has(EBDataComponents.MANA.get())) {
            return getManaCapacity(stack);
        }
        return Math.min(stack.getOrDefault(EBDataComponents.MANA.get(), getManaCapacity(stack)), getManaCapacity(stack));
    }

    @Override
    public void setMana(ItemStack stack, int mana) {
        int clamped = Math.max(0, Math.min(mana, getManaCapacity(stack)));
        stack.set(EBDataComponents.MANA.get(), clamped);
        // Also synchronize vanilla damage value so broken armor reflects visually if needed
        stack.setDamageValue(getManaCapacity(stack) - clamped);
    }

    @Override
    public int getManaCapacity(ItemStack stack) {
        return stack.getMaxDamage();
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return getMana(stack) < getManaCapacity(stack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        int capacity = getManaCapacity(stack);
        return capacity > 0 ? Math.round(13.0F * getMana(stack) / capacity) : 13;
    }

    @Override
    public int getSpellSlotCount(ItemStack stack) {
        return 0;
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        Element elem = getElement();
        if (elem != null && elem.getColors().length > 0) {
            return elem.getColors()[0];
        }
        return 0xa811ce;
    }

    // ==========================================
    // IWorkbenchItem Implementation
    // ==========================================

    @Override
    public boolean showTooltip(ItemStack stack) {
        return true;
    }

    @Override
    public boolean onApplyButtonPressed(Player player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
        return WorkbenchUtils.rechargeManaFromCrystals(centre, crystals);
    }

    // ==========================================
    // Tooltip and Name
    // ==========================================

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        Element elem = getElement();
        if (elem != null) {
            return Component.literal(super.getName(stack).getString()).withStyle(elem.getColor());
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.ebwizardry.mana", getMana(stack), getManaCapacity(stack))
                .withStyle(ChatFormatting.AQUA));

        Element elem = getElement();
        if (elem != null) {
            tooltip.add(Component.translatable(
                    "item." + WizardryMod.MODID + ".wizard_armor.element_cost_reduction",
                    (int) (this.costReduction * 100),
                    elem.getDescriptionFormatted().getString()
            ).withStyle(ChatFormatting.GRAY));
        }
    }

    // ==========================================
    // Mana Cost Reduction Helper
    // ==========================================

    public static float getCostMultiplier(LivingEntity entity, @Nullable Element spellElement) {
        if (spellElement == null) return 1.0F;

        float totalReduction = 0.0F;
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (stack.getItem() instanceof WizardArmorItem armor) {
                if (armor.getElement() == spellElement && armor.getMana(stack) > 0) {
                    totalReduction += armor.getCostReduction();
                }
            }
        }

        totalReduction = Math.min(0.80F, totalReduction);
        return Math.max(0.20F, 1.0F - totalReduction);
    }
}
