package com.binaris.wizardry.content.spells.abstr;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.internal.*;
import com.binaris.wizardry.api.content.spell.properties.SpellProperty;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Spell;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.*;
import java.util.function.Supplier;

public class BuffSpell extends Spell {
    protected final float r, g, b;
    protected final List<Supplier<MobEffect>> effectSuppliers = new ArrayList<>();
    protected float particleCount = 10;

    @SafeVarargs
    public BuffSpell(float r, float g, float b, Supplier<MobEffect>... effects) {
        this.r = r;
        this.g = g;
        this.b = b;
        Collections.addAll(effectSuppliers, effects);
    }

    public BuffSpell particleCount(int count) {
        this.particleCount = count;
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
        if (!applyEffects(ctx, ctx.caster())) return false;
        if (ctx.world().isClientSide) spawnParticles(ctx.world(), ctx.caster());
        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean cast(EntityCastContext ctx) {
        if (!applyEffects(ctx, ctx.caster())) return false;
        if (ctx.world().isClientSide) spawnParticles(ctx.world(), ctx.caster());
        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean cast(LocationCastContext ctx) {
        AABB boundingBox = new AABB(ctx.pos()).inflate(3.0);
        List<LivingEntity> entities = ctx.world().getEntitiesOfClass(LivingEntity.class, boundingBox);
        if (entities.isEmpty()) return false;
        LivingEntity nearest = entities.get(0);
        if (!applyEffects(ctx, nearest)) return false;
        if (ctx.world().isClientSide) spawnParticles(ctx.world(), nearest);
        return true;
    }

    protected boolean applyEffects(CastContext ctx, LivingEntity target) {
        if (ctx.world().isClientSide) return true;
        int bonusAmplifier = (int) ((ctx.modifiers().getFactor(SpellModifiers.POTENCY) - 1.0f) / 0.4f);

        for (Supplier<MobEffect> supplier : effectSuppliers) {
            MobEffect effect = supplier.get();
            if (effect == null) continue;
            Holder<MobEffect> holder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect);
            int duration = hasProperty(getEffectDurationProperty(effect))
                    ? (int) (property(getEffectDurationProperty(effect)) * ctx.modifiers().getFactor(SpellModifiers.DURATION))
                    : 600;
            int strength = hasProperty(getEffectStrengthProperty(effect))
                    ? property(getEffectStrengthProperty(effect)) + bonusAmplifier
                    : bonusAmplifier;

            target.addEffect(new MobEffectInstance(holder, Math.max(20, duration), Math.max(0, strength), false, true));
        }
        return true;
    }

    protected void spawnParticles(Level world, LivingEntity target) {
        for (int i = 0; i < particleCount; i++) {
            double px = target.getX() + world.random.nextDouble() * 2 - 1;
            double py = target.getY() + target.getEyeHeight() - 0.5 + world.random.nextDouble();
            double pz = target.getZ() + world.random.nextDouble() * 2 - 1;
            ParticleBuilder.create(EBParticles.SPARKLE).pos(px, py, pz).velocity(0, 0.1, 0).color(r, g, b).spawn(world);
        }
        ParticleBuilder.create(EBParticles.BUFF).entity(target).color(r, g, b).spawn(world);
    }

    public static SpellProperty<Integer> getEffectDurationProperty(MobEffect effect) {
        return SpellProperty.intProperty(effect.getDescriptionId() + "_duration", 600);
    }

    public static SpellProperty<Integer> getEffectStrengthProperty(MobEffect effect) {
        return SpellProperty.intProperty(effect.getDescriptionId() + "_strength", 0);
    }

    public static SpellProperty<Integer> getEffectDurationProperty(Holder<MobEffect> effect) {
        return getEffectDurationProperty(effect.value());
    }

    public static SpellProperty<Integer> getEffectStrengthProperty(Holder<MobEffect> effect) {
        return getEffectStrengthProperty(effect.value());
    }
}
