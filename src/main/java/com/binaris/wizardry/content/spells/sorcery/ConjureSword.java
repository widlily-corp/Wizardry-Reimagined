package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.content.spells.abstr.ConjureItemSpell;
import com.binaris.wizardry.registry.EBItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.resources.ResourceLocation;

public class ConjureSword extends ConjureItemSpell {
    private static final ResourceLocation SWORD_DAMAGE_ID = ResourceLocation.fromNamespaceAndPath("ebwizardry", "spectral_sword_bonus_damage");
    private static final double BASE_DAMAGE = 5.0D;

    public ConjureSword() {
        super(EBItems.SPECTRAL_SWORD);
    }

    @Override
    protected ItemStack addItemExtras(PlayerCastContext ctx, ItemStack stack) {
        float potency = ctx.modifiers().getFactor(SpellModifiers.POTENCY);
        if (potency > 1.0F) {
            double bonusDamage = BASE_DAMAGE * (potency - 1.0F);
            ItemAttributeModifiers existing = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
            ItemAttributeModifiers updated = existing.withModifierAdded(
                    Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(SWORD_DAMAGE_ID, bonusDamage, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND
            );
            stack.set(DataComponents.ATTRIBUTE_MODIFIERS, updated);
        }
        return super.addItemExtras(ctx, stack);
    }
}
