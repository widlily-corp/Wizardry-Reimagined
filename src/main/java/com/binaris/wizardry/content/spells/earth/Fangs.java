package com.binaris.wizardry.content.spells.earth;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.*;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.BlockUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Fangs extends Spell {
    private static final double FANG_SPACING = 1.25;

    @Override
    public boolean cast(PlayerCastContext ctx) {
        Vec3 look = ctx.caster().getLookAngle();
        Vec3 horiz = new Vec3(look.x, 0, look.z).normalize();
        if (spawnFangs(ctx, ctx.caster().position(), horiz)) {
            return false;
        }
        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    protected boolean spawnFangs(CastContext ctx, Vec3 origin, Vec3 direction) {
        boolean flag = false;

        if (ctx.world().isClientSide) {
            double x = origin.x;
            double y = ctx.caster() != null ? origin.y + ctx.caster().getEyeHeight() : origin.y;
            double z = origin.z;

            for (int i = 0; i < 12; i++) {
                ParticleBuilder.create(EBParticles.DARK_MAGIC.get())
                        .pos(x, y, z)
                        .color(0.4f, 0.3f, 0.35f).spawn(ctx.world());
            }
        }

        int count = (int) (ctx.modifiers().get(SpellModifiers.RANGE, property(DefaultProperties.RANGE)));
        float yaw = (float) Mth.atan2(direction.z, direction.x);

        for (int i = 0; i < count; i++) {
            Vec3 vec = origin.add(direction.scale((i + 1) * FANG_SPACING));
            flag |= this.spawnFangsAt(ctx.world(), ctx.caster(), yaw, i, vec);
        }
        return !flag;
    }

    private boolean spawnFangsAt(Level world, LivingEntity caster, float yaw, int delay, Vec3 vec) {
        Integer y = BlockUtil.getNearestFloor(world, BlockPos.containing(vec), 5);
        if (y != null) {
            EvokerFangs fangs = new EvokerFangs(world, vec.x, y, vec.z, yaw, delay, caster);
            world.addFreshEntity(fangs);
            return true;
        }
        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.ADVANCED, Elements.EARTH, SpellTypes.ATTACK, SpellAction.SUMMON, 40, 5, 60)
                .add(DefaultProperties.RANGE, 10F).build();
    }
}
