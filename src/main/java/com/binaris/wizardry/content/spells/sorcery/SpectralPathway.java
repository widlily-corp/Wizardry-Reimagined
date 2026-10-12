package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.spell.properties.SpellProperty;
import com.binaris.wizardry.api.content.util.BlockUtil;
import com.binaris.wizardry.registry.EBBlocks;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class SpectralPathway extends Spell {
    public static final SpellProperty<Integer> LENGTH = SpellProperty.intProperty("length", 12);

    @Override
    public boolean requiresPacket() {
        return false;
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        Player caster = ctx.caster();
        Level world = ctx.world();

        Block standingOn = world.getBlockState(caster.getOnPos()).getBlock();
        if (standingOn == Blocks.AIR) return false;

        Direction direction = caster.getDirection();
        boolean flag = false;

        if (!world.isClientSide) {
            BlockPos origin = new BlockPos(Mth.floor(caster.getX()), Mth.floor(caster.getY()) - 1, Mth.floor(caster.getZ()));

            int startPoint = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? -1 : 0;
            int length = (int) (property(LENGTH) * ctx.modifiers().get(SpellModifiers.RANGE, 1f));

            for (int i = 0; i < length; i++) {
                flag = placePathwayBlockIfPossible(world, origin.relative(direction, startPoint + i)) || flag;
                flag = placePathwayBlockIfPossible(world,
                        origin.relative(direction, startPoint + i)
                                .relative(Direction.get(Direction.AxisDirection.NEGATIVE, direction.getClockWise().getAxis()))) || flag;
            }
        }

        this.playSound(world, caster, ctx.castingTicks(), -1);
        return flag;
    }

    private boolean placePathwayBlockIfPossible(Level world, BlockPos pos) {
        if (!BlockUtil.canBlockBeReplaced(world, pos)) return false;
        return world.setBlockAndUpdate(pos, EBBlocks.SPECTRAL_BLOCK.get().defaultBlockState());
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.SORCERY, SpellTypes.CONSTRUCT, SpellAction.POINT, 20, 0, 40)
                .add(LENGTH)
                .build();
    }
}
