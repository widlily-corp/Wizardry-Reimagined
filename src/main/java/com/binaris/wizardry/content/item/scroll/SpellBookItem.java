package com.binaris.wizardry.content.item.scroll;

import com.binaris.wizardry.content.component.SpellData;
import com.binaris.wizardry.registry.EBDataComponents;
import com.binaris.wizardry.registry.EBRegistries;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.Spells;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpellBookItem extends Item {
    public SpellBookItem(Properties properties) {
        super(properties.stacksTo(1).component(EBDataComponents.SPELL_DATA.get(), SpellData.EMPTY));
    }

    public static ResourceLocation getSpellId(ItemStack stack) {
        SpellData data = stack.get(EBDataComponents.SPELL_DATA.get());
        return data != null ? data.spellId() : SpellData.EMPTY_SPELL;
    }

    public static Spell getSpell(ItemStack stack) {
        ResourceLocation id = getSpellId(stack);
        Spell spell = EBRegistries.SPELL_REGISTRY.get(id);
        if (spell == null && id != null) {
            spell = com.binaris.wizardry.registry.EBSpells.SPELLS.get(id.getPath());
        }
        return spell != null ? spell : Spells.NONE.get();
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
    public @NotNull Component getName(@NotNull ItemStack stack) {
        Spell spell = getSpell(stack);
        if (spell == Spells.NONE.get()) {
            return Component.translatableWithFallback("item.ebwizardry.spell_book.empty", "Spell Book");
        }
        return Component.translatable("item.ebwizardry.spell_book", spell.getDescriptionFormatted());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        Spell spell = getSpell(stack);
        if (spell != Spells.NONE.get()) {
            tooltip.add(spell.getTier().getDescriptionFormatted());
            tooltip.add(Component.translatable(spell.getElement().getDescriptionId()).withStyle(spell.getElement().getColor()));

            tooltip.add(Component.translatable("tooltip.ebwizardry.spell.cost", spell.getCost()).withStyle(ChatFormatting.BLUE));
            if (spell.getCooldown() > 0) {
                float cdSec = Math.round(spell.getCooldown() / 2.0f) / 10.0f;
                tooltip.add(Component.translatable("tooltip.ebwizardry.spell.cooldown",
                        String.format(java.util.Locale.US, "%.1f", cdSec)).withStyle(ChatFormatting.DARK_GRAY));
            }
            if (spell.getChargeup() > 0) {
                float chargeSec = Math.round(spell.getChargeup() / 2.0f) / 10.0f;
                tooltip.add(Component.translatable("tooltip.ebwizardry.spell.chargeup",
                        String.format(java.util.Locale.US, "%.1f", chargeSec)).withStyle(ChatFormatting.DARK_GRAY));
            }
            String descKey = "spell." + spell.getLocation().getNamespace() + "." + spell.getLocation().getPath() + ".desc";
            tooltip.add(Component.translatable(descKey).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        } else {
            tooltip.add(Component.translatable("tooltip.ebwizardry.spell_book.empty").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
