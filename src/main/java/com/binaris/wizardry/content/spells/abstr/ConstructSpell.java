package com.binaris.wizardry.content.spells.abstr;

import com.binaris.wizardry.api.content.spell.internal.*;
import com.binaris.wizardry.api.content.util.BlockUtil;
import com.binaris.wizardry.content.entity.construct.MagicConstructEntity;
import com.binaris.wizardry.content.entity.construct.ScaledConstructEntity;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Spell;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class ConstructSpell<T extends MagicConstructEntity> extends Spell {
    protected final Function<Level, T> constructFactory;
    protected final boolean permanent;
    protected boolean requiresFloor = false;
    protected boolean allowOverlap = false;

    public ConstructSpell(Function<Level, T> constructFactory, boolean permanent) {
        this.constructFactory = constructFactory;
        this.permanent = permanent;
    }

    public ConstructSpell<T> floor(boolean requiresFloor) {
        this.requiresFloor = requiresFloor;
        return this;
    }

    public ConstructSpell<T> overlap(boolean allowOverlap) {
        this.allowOverlap = allowOverlap;
        return this;
    }

    @Override
    public boolean canCastByEntity() {
        return true;
    }

    @Override
    public boolean canCastByLocation() {
        return true;
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        Vec3 spawnPos = ctx.caster().position();
        if (requiresFloor) {
            Integer floorY = BlockUtil.getNearestFloor(ctx.world(), ctx.caster().blockPosition(), 3);
            if (floorY != null) {
                spawnPos = new Vec3(spawnPos.x, floorY, spawnPos.z);
            }
        }

        if (!ctx.world().isClientSide) {
            spawnConstruct(ctx, spawnPos);
        }

        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean cast(EntityCastContext ctx) {
        Vec3 spawnPos = ctx.caster().position();
        if (requiresFloor) {
            Integer floorY = BlockUtil.getNearestFloor(ctx.world(), ctx.caster().blockPosition(), 3);
            if (floorY != null) {
                spawnPos = new Vec3(spawnPos.x, floorY, spawnPos.z);
            }
        }

        if (!ctx.world().isClientSide) {
            spawnConstruct(ctx, spawnPos);
        }

        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    protected void spawnConstruct(CastContext ctx, Vec3 pos) {
        T construct = constructFactory.apply(ctx.world());
        construct.setPos(pos);
        construct.setCaster(ctx.caster());
        construct.damageMultiplier = ctx.modifiers().getFactor(SpellModifiers.POTENCY);

        if (construct instanceof ScaledConstructEntity scaled) {
            scaled.setSizeMultiplier(ctx.modifiers().getFactor(SpellModifiers.BLAST));
        }

        if (!permanent && hasProperty(DefaultProperties.DURATION)) {
            construct.lifetime = (int) (property(DefaultProperties.DURATION) * ctx.modifiers().getFactor(SpellModifiers.DURATION));
        } else if (permanent) {
            construct.lifetime = -1;
        }

        ctx.world().addFreshEntity(construct);
    }
}
