package com.binaris.wizardry.content.spells.earth;

import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.BlockUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GrowthAura extends Spell {
    @Override
    public boolean cast(PlayerCastContext ctx) {
        if (ctx.caster().level().isClientSide) return true;

        boolean flag = false;
        Level level = ctx.caster().level();
        List<BlockPos> sphere = BlockUtil.getBlockSphere(ctx.caster().blockPosition(), ctx.modifiers().get(SpellModifiers.BLAST, property(DefaultProperties.EFFECT_RADIUS)));

        for (BlockPos pos : sphere) {
            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof BonemealableBlock plant) || !(plant.isBonemealSuccess(level, level.random, pos, state)))
                continue;

            if (!plant.isValidBonemealTarget(level, pos, state)) {
                continue;
            } else {
                flag = true;
            }

            plant.performBonemeal((ServerLevel) level, level.random, pos, state);
        }

        if (flag) this.playSound(level, ctx.caster(), ctx.castingTicks(), -1);
        return flag;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.EARTH, SpellTypes.UTILITY, SpellAction.POINT_DOWN, 20, 0, 50)
                .add(DefaultProperties.EFFECT_RADIUS, 5)
                .build();
    }
}
