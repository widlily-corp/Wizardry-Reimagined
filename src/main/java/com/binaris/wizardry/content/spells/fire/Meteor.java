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
        if (!ctx.world().isClientSide()) {
            Vec3 target = blockHit.getLocation();
            spawnAngledMeteor(ctx, target);
        }
        return true;
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        if (!ctx.world().isClientSide()) {
            Vec3 target = entityHit.getEntity().position();
            spawnAngledMeteor(ctx, target);
        }
        return true;
    }

    private void spawnAngledMeteor(CastContext ctx, Vec3 target) {
        double spawnHeight = 35.0;
        double spawnOffsetX = -12.0;
        double spawnOffsetZ = -12.0;

        Vec3 spawnPos = target.add(spawnOffsetX, spawnHeight, spawnOffsetZ);
        Vec3 velocity = target.subtract(spawnPos).normalize().scale(1.8);

        MeteorEntity meteor = new MeteorEntity(ctx.world(),
                spawnPos.x, spawnPos.y, spawnPos.z,
                ctx.modifiers().getFactor(SpellModifiers.BLAST),
                EntityUtil.canDamageBlocks(ctx.caster(), ctx.world()));

        meteor.setDeltaMovement(velocity);
        ctx.world().addFreshEntity(meteor);
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        if (!ctx.world().isClientSide()) {
            Vec3 target = origin.add(direction.scale(25.0));
            spawnAngledMeteor(ctx, target);
        }
        return true;
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
