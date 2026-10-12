package com.binaris.wizardry.content.spells.fire;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.*;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.AreaEffectSpell;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Firestorm extends AreaEffectSpell {
    public Firestorm() {
        this.alwaysSucceed(true);
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        burnNearbyBlocks(ctx, ctx.caster().position());
        return super.cast(ctx);
    }

    @Override
    public boolean cast(EntityCastContext ctx) {
        burnNearbyBlocks(ctx, ctx.caster().position());
        return super.cast(ctx);
    }

    @Override
    public boolean cast(LocationCastContext ctx) {
        burnNearbyBlocks(ctx, ctx.vec3());
        return super.cast(ctx);
    }



    @Override
    protected boolean affectEntity(CastContext ctx, Vec3 origin, LivingEntity target, int targetCount) {
        int duration = property(DefaultProperties.EFFECT_DURATION);
        target.igniteForSeconds(duration);
        float damage = ctx.modifiers().get(SpellModifiers.POTENCY, property(DefaultProperties.DAMAGE));
        target.hurt(ctx.caster() != null ? ctx.world().damageSources().indirectMagic(ctx.caster(), ctx.caster()) : ctx.world().damageSources().magic(), damage);
        return true;
    }

    protected void burnNearbyBlocks(CastContext ctx, Vec3 origin) {
        if (ctx.world().isClientSide) return;
        int radius = (int) ctx.modifiers().get(SpellModifiers.BLAST, (float) property(DefaultProperties.EFFECT_RADIUS));
        BlockPos center = BlockPos.containing(origin);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius && ctx.world().random.nextFloat() < 0.3f) {
                    BlockPos p = center.offset(x, 0, z);
                    BlockPos above = p.above();
                    if (ctx.world().isEmptyBlock(above) && ctx.world().getBlockState(p).isFaceSturdy(ctx.world(), p, Direction.UP)) {
                        ctx.world().setBlockAndUpdate(above, Blocks.FIRE.defaultBlockState());
                    }
                }
            }
        }
    }

    @Override
    protected void spawnParticle(net.minecraft.world.level.Level world, double x, double y, double z) {
        ParticleBuilder.create(EBParticles.MAGIC_FIRE.get()).pos(x, y + 0.5, z).time(20).spawn(world);
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.MASTER, Elements.FIRE, SpellTypes.ATTACK, SpellAction.POINT_DOWN, 60, 20, 200)
                .add(DefaultProperties.EFFECT_RADIUS, 8)
                .add(DefaultProperties.DAMAGE, 10F)
                .add(DefaultProperties.EFFECT_DURATION, 10)
                .build();
    }
}
