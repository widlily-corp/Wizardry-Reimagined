package com.binaris.wizardry.content.spells.necromancy;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.AreaEffectSpell;
import com.binaris.wizardry.content.spells.abstr.BuffSpell;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PlagueOfDarkness extends AreaEffectSpell {

    public PlagueOfDarkness() {
        this.alwaysSucceed(true);
        this.soundValues(1, 1.1f, 0.2f);
    }

    @Override
    protected boolean affectEntity(CastContext ctx, Vec3 origin, LivingEntity target, int targetCount) {
        if (ctx.world().isClientSide) return true;
        DamageSource src = ctx.caster() != null
                ? ctx.world().damageSources().indirectMagic(ctx.caster(), ctx.caster())
                : ctx.world().damageSources().wither();
        target.hurt(src, ctx.modifiers().get(SpellModifiers.POTENCY, property(DefaultProperties.DAMAGE)));

        int duration = (int) (ctx.modifiers().get(SpellModifiers.DURATION, property(DefaultProperties.EFFECT_DURATION)));
        int amplifier = property(DefaultProperties.EFFECT_STRENGTH) + BuffSpell.getStandardBonusAmplifier(ctx.modifiers().getFactor(SpellModifiers.POTENCY));

        target.addEffect(new MobEffectInstance(MobEffects.WITHER, duration, amplifier));
        target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, Math.min(160, duration), 0));
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 1));

        return true;
    }

    @Override
    protected void spawnParticleEffect(CastContext ctx, Vec3 origin, double radius) {
        double particleX, particleZ;

        for (int i = 0; i < 40; i++) {
            particleX = origin.x - 1.0d + 2 * ctx.world().random.nextDouble();
            particleZ = origin.z - 1.0d + 2 * ctx.world().random.nextDouble();
            ParticleBuilder.create(EBParticles.DARK_MAGIC).pos(particleX, origin.y, particleZ)
                    .velocity(particleX - origin.x, 0, particleZ - origin.z).color(0.1f, 0, 0).spawn(ctx.world());

            particleX = origin.x - 1.0d + 2 * ctx.world().random.nextDouble();
            particleZ = origin.z - 1.0d + 2 * ctx.world().random.nextDouble();
            ParticleBuilder.create(EBParticles.SPARKLE).pos(particleX, origin.y, particleZ)
                    .velocity(particleX - origin.x, 0, particleZ - origin.z).time(30).color(0.1f, 0, 0.05f).spawn(ctx.world());

            particleX = origin.x - 1.0d + 2 * ctx.world().random.nextDouble();
            particleZ = origin.z - 1.0d + 2 * ctx.world().random.nextDouble();

            BlockPos bpos = BlockPos.containing(origin.x, origin.y - 0.5, origin.z);
            BlockState state = ctx.world().getBlockState(bpos);

            if (state.getRenderShape() != RenderShape.INVISIBLE) {
                if (ctx.world().isClientSide) {
                    ctx.world().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), particleX, origin.y, particleZ, particleX - origin.x, 0, particleZ - origin.z);
                } else if (ctx.world() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), particleX, origin.y, particleZ, 1, particleX - origin.x, 0, particleZ - origin.z, 0.0);
                }
            }
        }

        ParticleBuilder.create(EBParticles.SPHERE).pos(origin.add(0, 0.1, 0)).scale((float) radius * 0.8f).color(0.8f, 0, 0.05f).spawn(ctx.world());
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.MASTER, Elements.NECROMANCY, SpellTypes.ATTACK, SpellAction.POINT_DOWN, 75, 15, 200)
                .add(DefaultProperties.EFFECT_RADIUS, 5)
                .add(DefaultProperties.DAMAGE, 8F)
                .add(DefaultProperties.EFFECT_DURATION, 140)
                .add(DefaultProperties.EFFECT_STRENGTH, 2)
                .build();
    }
}
