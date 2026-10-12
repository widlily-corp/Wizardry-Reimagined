package com.binaris.wizardry.content.spells.abstr;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Spell;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.HashSet;
import java.util.Set;

public class ConjureItemSpell extends Spell {
    public static final Set<Item> SUPPORTED_ITEMS = new HashSet<>();
    private final Item item;

    public ConjureItemSpell(Item item) {
        this.item = item;
        SUPPORTED_ITEMS.add(item);
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        if (conjureItem(ctx)) {
            if (ctx.world().isClientSide) spawnParticles(ctx);
            this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
            return true;
        }
        return false;
    }

    protected boolean conjureItem(PlayerCastContext ctx) {
        ItemStack stack = new ItemStack(item);
        stack = addItemExtras(ctx, stack);

        int duration = hasProperty(DefaultProperties.ITEM_LIFETIME) ? property(DefaultProperties.ITEM_LIFETIME) : 1200;
        float durationMultiplier = ctx.modifiers().getFactor(SpellModifiers.DURATION);
        long currentGameTime = ctx.world().getGameTime();
        long expireTime = currentGameTime + (long) (duration * durationMultiplier);

        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putBoolean("ebwizardry:conjured", true);
            tag.putLong("ebwizardry:expire_time", expireTime);
            tag.putInt("ebwizardry:duration", (int) (duration * durationMultiplier));
        });

        if (!ctx.caster().addItem(stack)) {
            ctx.caster().sendSystemMessage(Component.translatable("spell.ebwizardry.conjure_item.no_space"));
            return false;
        }
        return true;
    }

    protected ItemStack addItemExtras(PlayerCastContext ctx, ItemStack stack) {
        return stack;
    }

    protected void spawnParticles(PlayerCastContext ctx) {
        ParticleBuilder.create(EBParticles.SPARKLE)
                .pos(ctx.caster().position().add(0, 1, 0))
                .count(15)
                .spread(0.5, 0.5, 0.5)
                .spawn(ctx.world());
    }

    public static boolean isSummoned(ItemStack stack) {
        if (stack.isEmpty() || !SUPPORTED_ITEMS.contains(stack.getItem())) return false;
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null && customData.copyTag().getBoolean("ebwizardry:conjured");
    }
}
