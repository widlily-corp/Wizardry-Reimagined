package com.binaris.wizardry.content.spells.fire;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.spell.properties.SpellProperty;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PocketFurnace extends Spell {
    public static final SpellProperty<Integer> ITEMS_SMELTED = SpellProperty.intProperty("items_smelted", 8);

    @Override
    public boolean cast(PlayerCastContext ctx) {
        int maxItems = (int) (ctx.modifiers().get(SpellModifiers.POTENCY, (float) property(ITEMS_SMELTED)));
        boolean smeltedAny = false;

        for (int i = 0; i < ctx.caster().getInventory().getContainerSize() && maxItems > 0; i++) {
            ItemStack stack = ctx.caster().getInventory().getItem(i);
            if (stack.isEmpty()) continue;

            SingleRecipeInput input = new SingleRecipeInput(stack);
            Optional<RecipeHolder<SmeltingRecipe>> recipeOpt = ctx.world().getRecipeManager().getRecipeFor(RecipeType.SMELTING, input, ctx.world());

            if (recipeOpt.isPresent()) {
                ItemStack result = recipeOpt.get().value().assemble(input, ctx.world().registryAccess());
                if (!result.isEmpty()) {
                    int countToSmelt = Math.min(stack.getCount(), maxItems);
                    stack.shrink(countToSmelt);
                    ItemStack resultStack = result.copyWithCount(result.getCount() * countToSmelt);

                    if (!ctx.caster().addItem(resultStack)) {
                        ctx.caster().drop(resultStack, false);
                    }
                    maxItems -= countToSmelt;
                    smeltedAny = true;
                }
            }
        }

        if (smeltedAny) {
            if (ctx.world().isClientSide) {
                ParticleBuilder.create(EBParticles.MAGIC_FIRE.get())
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
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.FIRE, SpellTypes.UTILITY, SpellAction.POINT_UP, 20, 0, 40)
                .add(ITEMS_SMELTED, 8)
                .build();
    }
}
