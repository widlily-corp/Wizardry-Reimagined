package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.spell.properties.SpellProperty;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SpeedTime extends Spell {
    public static final SpellProperty<Float> TIME_INCREMENT = SpellProperty.floatProperty("time_increment", 30f);
    public static final SpellProperty<Integer> EXTRA_TICKS = SpellProperty.intProperty("extra_ticks", 1);

    public SpeedTime() {
        super();
        this.soundValues(0.4f, 1, 0);
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        Level world = ctx.world();
        float blastMod = ctx.modifiers().getFactor(SpellModifiers.BLAST);

        if (!world.isClientSide && world instanceof ServerLevel serverLevel) {
            long advance = (long) (property(TIME_INCREMENT) * blastMod);
            serverLevel.setDayTime(world.getDayTime() + advance);
        }

        this.playSound(world, ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean isInstantCast() {
        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.MASTER, Elements.SORCERY, SpellTypes.UTILITY, SpellAction.NONE, 2, 0, 0)
                .add(TIME_INCREMENT)
                .add(EXTRA_TICKS)
                .add(DefaultProperties.EFFECT_RADIUS, 16)
                .build();
    }
}
