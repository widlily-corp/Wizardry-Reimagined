package com.binaris.wizardry.content.spells.fire;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.NotNull;

public class FlamingWeapon extends Spell {
    @Override
    public boolean cast(PlayerCastContext ctx) {
        ItemStack stack = ctx.caster().getMainHandItem();
        if (stack.isEmpty() || !(stack.getItem() instanceof SwordItem)) {
            stack = ctx.caster().getOffhandItem();
        }
        if (stack.isEmpty() || !(stack.getItem() instanceof SwordItem)) {
            return false;
        }

        var enchantRegistry = ctx.world().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        var fireAspectHolder = enchantRegistry.getHolder(Enchantments.FIRE_ASPECT);

        if (fireAspectHolder.isPresent()) {
            ItemEnchantments enchants = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            int level = Math.max(1, (int) ctx.modifiers().getFactor(SpellModifiers.POTENCY));
            ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(enchants);
            mutable.set(fireAspectHolder.get(), level);
            stack.set(DataComponents.ENCHANTMENTS, mutable.toImmutable());

            if (ctx.world().isClientSide) {
                ParticleBuilder.create(EBParticles.SPARKLE.get())
                        .pos(ctx.caster().position().add(0, 1, 0))
                        .count(15)
                        .spread(0.5, 0.5, 0.5)
                        .spawn(ctx.world());
            }

            this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
            return true;
        }

        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.FIRE, SpellTypes.BUFF, SpellAction.POINT_UP, 25, 0, 50)
                .add(DefaultProperties.EFFECT_DURATION, 600)
                .build();
    }
}
