package com.binaris.wizardry.content.spells.necromancy;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.MinionSpell;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SummonZombie extends MinionSpell<Zombie> {
    public SummonZombie() {
        super(Zombie::new);
        this.soundValues(7, 0.6f, 0);
    }

    @Override
    protected Zombie createMinion(Level world, @Nullable LivingEntity caster, SpellModifiers modifiers) {
        return super.createMinion(world, caster, modifiers);
    }

    @Override
    protected void addMinionExtras(Zombie minion, CastContext ctx, int alreadySpawned) {
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.NOVICE, Elements.NECROMANCY, SpellTypes.MINION, SpellAction.SUMMON, 10, 0, 40)
                .add(DefaultProperties.MINION_LIFETIME, 600)
                .add(DefaultProperties.MINION_COUNT, 1)
                .add(DefaultProperties.SUMMON_RADIUS, 2)
                .build();
    }
}
