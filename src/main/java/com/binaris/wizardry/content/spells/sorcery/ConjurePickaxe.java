package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.content.spells.abstr.ConjureItemSpell;
import com.binaris.wizardry.registry.EBItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class ConjurePickaxe extends ConjureItemSpell {
    public ConjurePickaxe() {
        super(EBItems.SPECTRAL_PICKAXE.get());
    }

    @Override
    protected ItemStack addItemExtras(PlayerCastContext ctx, ItemStack stack) {
        float potency = ctx.modifiers().getFactor(SpellModifiers.POTENCY);
        int tierLevel = (int) Math.min(3, (potency - 1.0f) * 2 + 1);
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putInt("ebwizardry:tier_level", Math.max(0, tierLevel));
        });
        return super.addItemExtras(ctx, stack);
    }
}
