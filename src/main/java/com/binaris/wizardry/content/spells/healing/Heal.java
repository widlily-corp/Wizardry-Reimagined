package com.binaris.wizardry.content.spells.healing;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.BuffSpell;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class Heal extends BuffSpell {
    public Heal() {
        super(1, 1, 0.3f);
    }

    public static void heal(LivingEntity entity, float health) {
        entity.heal(health);
    }

    @Override
    protected boolean applyEffects(CastContext ctx, LivingEntity caster) {
        if (caster.getHealth() < caster.getMaxHealth() && caster.getHealth() > 0) {
            heal(caster, ctx.modifiers().get(SpellModifiers.POTENCY, property(DefaultProperties.HEALTH)));
            return true;
        }
        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.NOVICE, Elements.HEALING, SpellTypes.DEFENCE, SpellAction.POINT_UP, 15, 0, 60)
                .add(DefaultProperties.HEALTH, 4F).build();
    }
}
