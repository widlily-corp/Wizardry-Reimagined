package com.binaris.wizardry.content.item.scroll;

import com.binaris.wizardry.content.block.workbench.IWorkbenchItem;
import com.binaris.wizardry.content.component.SpellData;
import com.binaris.wizardry.content.item.wand.ICastItem;
import com.binaris.wizardry.registry.EBDataComponents;
import com.binaris.wizardry.registry.EBRegistries;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.Spells;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScrollItem extends Item implements ICastItem, IWorkbenchItem {
    public static final int CASTING_DURATION = 72000;

    public ScrollItem(Properties properties) {
        super(properties.stacksTo(16));
    }

    public static ItemStack createWithSpell(Item item, Spell spell) {
        ItemStack stack = new ItemStack(item);
        stack.set(EBDataComponents.SPELL_DATA.get(), new SpellData(spell.getLocation()));
        return stack;
    }

    public static ItemStack createWithSpell(Item item, ResourceLocation spellId) {
        ItemStack stack = new ItemStack(item);
        stack.set(EBDataComponents.SPELL_DATA.get(), new SpellData(spellId));
        return stack;
    }

    @Override
    public Spell getCurrentSpell(ItemStack stack) {
        SpellData data = stack.get(EBDataComponents.SPELL_DATA.get());
        if (data == null || data.isEmpty()) return Spells.NONE.get();
        Spell spell = EBRegistries.SPELL_REGISTRY.get(data.spellId());
        if (spell == null && data.spellId() != null) {
            spell = com.binaris.wizardry.registry.EBSpells.SPELLS.get(data.spellId().getPath());
        }
        return spell != null ? spell : Spells.NONE.get();
    }

    @Override
    public Spell[] getSpells(ItemStack stack) {
        return new Spell[]{getCurrentSpell(stack)};
    }

    @Override
    public boolean selectSpell(ItemStack stack, int index) {
        return false;
    }

    @Override
    public void selectNextSpell(ItemStack stack) {}

    @Override
    public void selectPreviousSpell(ItemStack stack) {}

    @Override
    public int getCurrentCooldown(ItemStack stack, Level level) {
        return 0;
    }

    @Override
    public int getCurrentMaxCooldown(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean showSpellsInWorkbench(@Nullable Player player, ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Spell spell = getCurrentSpell(stack);
        if (spell == Spells.NONE.get()) {
            return InteractionResultHolder.fail(stack);
        }

        if (spell.getChargeup() > 0) {
            player.startUsingItem(hand);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        boolean success = castInstant(level, player, stack, spell);
        return success ? InteractionResultHolder.sidedSuccess(stack, level.isClientSide()) : InteractionResultHolder.fail(stack);
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity livingEntity, @NotNull ItemStack stack, int timeLeft) {
        if (!(livingEntity instanceof Player player)) return;

        Spell spell = getCurrentSpell(stack);
        if (spell == Spells.NONE.get()) {
            player.stopUsingItem();
            return;
        }

        int useTick = getUseDuration(stack, livingEntity) - timeLeft;
        if (useTick >= spell.getChargeup()) {
            castInstant(level, player, stack, spell);
            player.stopUsingItem();
        }
    }

    protected boolean castInstant(Level level, Player player, ItemStack stack, Spell spell) {
        boolean success = spell.cast(level, player);
        if (success && !player.isCreative()) {
            stack.shrink(1);
            player.getCooldowns().addCooldown(this, Math.max(10, spell.getCooldown()));
        }
        return success;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return CASTING_DURATION;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        Spell spell = getCurrentSpell(stack);
        if (spell == Spells.NONE.get()) {
            return Component.translatableWithFallback("item.ebwizardry.scroll.generic", "Scroll");
        }
        return Component.translatable("item.ebwizardry.scroll", spell.getDescriptionFormatted());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        Spell spell = getCurrentSpell(stack);
        if (spell != Spells.NONE.get()) {
            tooltip.add(spell.getTier().getDescriptionFormatted());
            tooltip.add(Component.translatable(spell.getElement().getDescriptionId()).withStyle(spell.getElement().getColor()));
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public boolean canPlace(ItemStack stack) {
        return false;
    }

    @Override
    public int getSpellSlotCount(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean showTooltip(ItemStack stack) {
        return false;
    }

    @Override
    public boolean onApplyButtonPressed(Player player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
        return false;
    }
}
