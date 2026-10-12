package com.binaris.wizardry.content.spells.lightning;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.spell.properties.SpellProperty;
import com.binaris.wizardry.api.content.util.MagicDamageSource;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBDamageSources;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Charge extends Spell {
    public static final SpellProperty<Float> CHARGE_SPEED = SpellProperty.floatProperty("charge_speed", 2.0f);

    public Charge() {
        this.soundValues(0.6f, 1, 0);
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        Vec3 look = ctx.caster().getLookAngle();
        float speed = ctx.modifiers().get(SpellModifiers.RANGE, property(CHARGE_SPEED));

        ctx.caster().setDeltaMovement(look.x * speed, 0.2, look.z * speed);
        ctx.caster().hurtMarked = true;

        if (ctx.world().isClientSide) {
            ctx.world().addParticle(ParticleTypes.EXPLOSION_EMITTER, ctx.caster().getX(), ctx.caster().getY() + ctx.caster().getBbHeight() / 2, ctx.caster().getZ(), 0, 0, 0);
            for (int i = 0; i < 5; i++) {
                ParticleBuilder.create(EBParticles.SPARK.get()).pos(ctx.caster().getX(), ctx.caster().getY() + 0.5, ctx.caster().getZ()).spawn(ctx.world());
            }
        } else {
            List<LivingEntity> collided = ctx.world().getEntitiesOfClass(LivingEntity.class, ctx.caster().getBoundingBox().inflate(1.5));
            collided.remove(ctx.caster());
            float damage = ctx.modifiers().get(SpellModifiers.POTENCY, property(DefaultProperties.DAMAGE));
            float knockback = property(DefaultProperties.KNOCKBACK);

            for (LivingEntity e : collided) {
                e.hurt(MagicDamageSource.causeDirectMagicDamage(ctx.caster(), EBDamageSources.SHOCK), damage);
                e.push(look.x * knockback, 0.3f, look.z * knockback);
            }
        }

        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.LIGHTNING, SpellTypes.ATTACK, SpellAction.POINT, 20, 0, 50)
                .add(CHARGE_SPEED, 2.0F)
                .add(DefaultProperties.DURATION, 10)
                .add(DefaultProperties.DAMAGE, 8F)
                .add(DefaultProperties.KNOCKBACK, 1.0F)
                .build();
    }
}
