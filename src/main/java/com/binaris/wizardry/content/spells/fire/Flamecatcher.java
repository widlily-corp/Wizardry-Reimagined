package com.binaris.wizardry.content.spells.fire;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperty;
import com.binaris.wizardry.content.spells.abstr.ConjureItemSpell;
import com.binaris.wizardry.registry.EBItems;
import com.binaris.wizardry.registry.EBParticles;
import net.minecraft.core.particles.ParticleTypes;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.SpellModifiers;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

public class Flamecatcher extends ConjureItemSpell {
    public static final SpellProperty<Integer> SHOT_COUNT = SpellProperty.intProperty("shot_count", 5);

    public Flamecatcher() {
        super(EBItems.FLAMECATCHER);
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        boolean success = super.cast(ctx);
        if (success && !ctx.world().isClientSide) {
            ctx.caster().displayClientMessage(Component.translatable("spell.ebwizardry.flamecatcher.conjured"), true);
        }
        return success;
    }

    @Override
    protected ItemStack addItemExtras(PlayerCastContext ctx, ItemStack stack) {
        int shots = (int) (property(SHOT_COUNT) * ctx.modifiers().getFactor(SpellModifiers.DURATION));
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putInt("ShotsLeft", shots);
            tag.putInt("MaxShots", shots);
        });
        return stack;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.ADVANCED, Elements.FIRE, SpellTypes.UTILITY, SpellAction.POINT, 40, 0, 100)
                .add(SHOT_COUNT)
                .add(DefaultProperties.DAMAGE, 16F)
                .add(DefaultProperties.EFFECT_DURATION, 15)
                .add(DefaultProperties.ITEM_LIFETIME, 1200)
                .build();
    }

    @Override
    protected void spawnParticles(PlayerCastContext ctx) {
        ParticleBuilder.create(EBParticles.BUFF.get()).pos(ctx.caster().position().add(0, 1, 0)).time(20).spawn(ctx.world());

        for (int i = 0; i < 10; i++) {
            double x = ctx.caster().getX() + ctx.world().random.nextDouble() * 2 - 1;
            double y = ctx.caster().getY() + ctx.caster().getEyeHeight() - 0.5 + ctx.world().random.nextDouble();
            double z = ctx.caster().getZ() + ctx.world().random.nextDouble() * 2 - 1;
            ctx.world().addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
        }
    }
}
