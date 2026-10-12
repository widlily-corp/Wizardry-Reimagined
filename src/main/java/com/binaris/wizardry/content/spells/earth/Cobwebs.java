package com.binaris.wizardry.content.spells.earth;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.BlockUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Cobwebs extends RaySpell {

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        boolean flag = false;
        BlockPos pos = blockHit.getBlockPos().relative(blockHit.getDirection());
        float radius = ctx.modifiers().get(SpellModifiers.BLAST, property(DefaultProperties.BLAST_RADIUS));
        List<BlockPos> sphere = BlockUtil.getBlockSphere(pos, radius);

        for (BlockPos pos1 : sphere) {
            if (!ctx.world().isEmptyBlock(pos1)) continue;

            if (!ctx.world().isClientSide) {
                if (BlockUtil.canPlaceBlock(ctx.caster(), ctx.world(), pos1)) {
                    ctx.world().setBlockAndUpdate(pos1, Blocks.COBWEB.defaultBlockState());
                }
            }
            flag = true;
        }

        return flag;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return false;
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.EARTH, SpellTypes.CONSTRUCT, SpellAction.POINT, 15, 0, 30)
                .add(DefaultProperties.RANGE, 12F)
                .add(DefaultProperties.BLAST_RADIUS, 1.5F)
                .add(DefaultProperties.DURATION, 400)
                .build();
    }
}
