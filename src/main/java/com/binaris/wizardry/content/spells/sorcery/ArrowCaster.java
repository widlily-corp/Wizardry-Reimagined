package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.entity.construct.ArrowOrbConstruct;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Master-tier Sorcery spell "Заклинатель стрел" (Arrow Caster).
 * Conjures a hovering sphere of magical arrows above the caster that seeks out and shoots nearby enemies.
 */
public class ArrowCaster extends Spell {

    @Override
    public boolean cast(PlayerCastContext ctx) {
        if (ctx.world().isClientSide) {
            // Burst of initial arcane sparks
            for (int i = 0; i < 24; i++) {
                double ox = (ctx.world().random.nextDouble() - 0.5) * 1.6;
                double oy = ctx.caster().getBbHeight() + 1.2 + (ctx.world().random.nextDouble() - 0.5) * 0.8;
                double oz = (ctx.world().random.nextDouble() - 0.5) * 1.6;
                ParticleBuilder.create(EBParticles.SPARKLE)
                        .pos(ctx.caster().getX() + ox, ctx.caster().getY() + oy, ctx.caster().getZ() + oz)
                        .velocity(0, 0.04, 0)
                        .spawn(ctx.world());
            }
            return true;
        }

        // Replace any existing orb for this caster
        AABB searchBox = ctx.caster().getBoundingBox().inflate(6.0);
        List<ArrowOrbConstruct> existing = ctx.world().getEntitiesOfClass(ArrowOrbConstruct.class, searchBox,
                c -> c.getOwner() == ctx.caster());
        for (ArrowOrbConstruct construct : existing) {
            construct.despawn();
        }

        ArrowOrbConstruct orb = new ArrowOrbConstruct(ctx.world());
        orb.setCaster(ctx.caster());
        orb.setPos(ctx.caster().getX(), ctx.caster().getY() + ctx.caster().getBbHeight() + 1.2, ctx.caster().getZ());

        float dmg = hasProperty(DefaultProperties.DAMAGE) ? property(DefaultProperties.DAMAGE) : 7.0f;
        float range = hasProperty(DefaultProperties.RANGE) ? property(DefaultProperties.RANGE) : 22.0f;
        int count = hasProperty(DefaultProperties.ENTITIES) ? property(DefaultProperties.ENTITIES) : 32;
        int dur = hasProperty(DefaultProperties.DURATION) ? property(DefaultProperties.DURATION) : 300;

        orb.setup(dmg, range, count, dur);
        ctx.world().addFreshEntity(orb);

        ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                SoundEvents.ILLUSIONER_PREPARE_MIRROR, SoundSource.PLAYERS, 1.2f, 1.2f);
        ctx.world().playSound(null, ctx.caster().getX(), ctx.caster().getY(), ctx.caster().getZ(),
                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f, 0.7f);

        return true;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.MASTER, Elements.SORCERY, SpellTypes.ATTACK, SpellAction.POINT, 85, 10, 400)
                .add(DefaultProperties.DAMAGE, 7.0f)
                .add(DefaultProperties.ENTITIES, 32)
                .add(DefaultProperties.RANGE, 22.0f)
                .add(DefaultProperties.DURATION, 300)
                .build();
    }
}
