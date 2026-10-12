package com.binaris.wizardry.content.spells.healing;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.AreaEffectSpell;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import com.binaris.wizardry.registry.EBParticles;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class GroupHeal extends AreaEffectSpell {
    public GroupHeal() {
        this.targetAllies(true);
        this.alwaysSucceed(true);
    }

    @Override
    protected boolean affectEntity(CastContext ctx, Vec3 origin, LivingEntity target, int targetCount) {
        if (target.getHealth() < target.getMaxHealth() && target.getHealth() > 0) {
            Heal.heal(target, ctx.modifiers().get(SpellModifiers.POTENCY, property(DefaultProperties.HEALTH)));
            if (ctx.world().isClientSide) ParticleBuilder.spawnHealParticles(ctx.world(), target);
            playSound(ctx.world(), target, 0, -1);
            return true;
        }
        return false;
    }

    @Override
    protected void spawnParticle(Level world, double x, double y, double z) {
        ParticleBuilder.create(EBParticles.SPARKLE.get()).pos(x, y + 0.2, z)
                .velocity(0, 0.05, 0).time(30).color(0.3f, 1f, 0.4f).spawn(world);
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.ADVANCED, Elements.HEALING, SpellTypes.DEFENCE, SpellAction.POINT_UP, 35, 10, 150)
                .add(DefaultProperties.EFFECT_RADIUS, 5)
                .add(DefaultProperties.HEALTH, 6F)
                .build();
    }
}
