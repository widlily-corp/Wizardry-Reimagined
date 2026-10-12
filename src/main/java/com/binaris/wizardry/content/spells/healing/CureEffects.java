package com.binaris.wizardry.content.spells.healing;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.abstr.BuffSpell;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CureEffects extends BuffSpell {
    public CureEffects() {
        super(0.8f, 0.8f, 1);
        this.soundValues(0.7f, 1.2f, 0.4f);
    }

    @Override
    protected boolean applyEffects(CastContext ctx, LivingEntity caster) {
        if (caster.getActiveEffects().isEmpty()) return false;

        boolean flag = false;
        List<MobEffectInstance> effectsCopy = new ArrayList<>(caster.getActiveEffects());
        for (MobEffectInstance effect : effectsCopy) {
            caster.removeEffect(effect.getEffect());
            flag = true;
        }
        return flag;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.HEALING, SpellTypes.DEFENCE, SpellAction.POINT_UP, 25, 10, 40)
                .build();
    }
}
