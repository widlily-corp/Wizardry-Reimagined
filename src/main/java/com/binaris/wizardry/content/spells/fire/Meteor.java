package com.binaris.wizardry.content.spells.fire;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.content.entity.MeteorEntity;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Meteor extends RaySpell {
    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        if (ctx.world().canSeeSky(blockHit.getBlockPos().above())) {
            if (!ctx.world().isClientSide()) {
                MeteorEntity meteor = new MeteorEntity(ctx.world(),
                        blockHit.getBlockPos().getX() + 0.5,
                        blockHit.getBlockPos().getY() + 40,
                        blockHit.getBlockPos().getZ() + 0.5,
                        ctx.modifiers().getFactor(SpellModifiers.BLAST),
                        EntityUtil.canDamageBlocks(ctx.caster(), ctx.world()));
                ctx.world().addFreshEntity(meteor);
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        BlockPos pos = entityHit.getEntity().blockPosition();
        if (ctx.world().canSeeSky(pos.above())) {
            if (!ctx.world().isClientSide()) {
                MeteorEntity meteor = new MeteorEntity(ctx.world(),
                        pos.getX() + 0.5,
                        pos.getY() + 40,
                        pos.getZ() + 0.5,
                        ctx.modifiers().getFactor(SpellModifiers.BLAST),
                        EntityUtil.canDamageBlocks(ctx.caster(), ctx.world()));
                ctx.world().addFreshEntity(meteor);
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.MASTER, Elements.FIRE, SpellTypes.ATTACK, SpellAction.POINT, 80, 20, 250)
                .add(DefaultProperties.RANGE, 30F)
                .add(DefaultProperties.DAMAGE, 15F)
                .add(DefaultProperties.BLAST_RADIUS, 4F)
                .build();
    }
}
