package com.binaris.wizardry.content.spells.ice;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.BlockUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Permafrost extends RaySpell {
    public Permafrost() {
        this.particleVelocity(1);
        this.particleSpacing(0.5);
        soundValues(0.5f, 1, 0);
        this.ignoreLivingEntities(true);
    }

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        if (!ctx.world().isClientSide) {
            float radius = ctx.modifiers().get(SpellModifiers.BLAST, 2.0f);
            List<BlockPos> sphere = BlockUtil.getBlockSphere(blockHit.getBlockPos().above(), radius);
            for (BlockPos pos : sphere) {
                if (BlockUtil.canPlaceBlock(ctx.caster(), ctx.world(), pos)) {
                    ctx.world().setBlockAndUpdate(pos, Blocks.PACKED_ICE.defaultBlockState());
                }
            }
            return true;
        }
        return true;
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        return false;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return true;
    }

    @Override
    protected void spawnParticle(CastContext ctx, double x, double y, double z, double vx, double vy, double vz) {
        ParticleBuilder.create(EBParticles.SNOW.get()).pos(x, y, z).spawn(ctx.world());
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.ADVANCED, Elements.ICE, SpellTypes.CONSTRUCT, SpellAction.POINT, 30, 0, 80)
                .add(DefaultProperties.RANGE, 15F)
                .add(DefaultProperties.DURATION, 600)
                .build();
    }
}
