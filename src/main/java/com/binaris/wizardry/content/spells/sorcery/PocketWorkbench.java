package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import org.jetbrains.annotations.NotNull;

public class PocketWorkbench extends Spell {
    @Override
    public boolean cast(PlayerCastContext ctx) {
        ctx.caster().openMenu(new SimpleMenuProvider(
                (containerId, playerInventory, player) -> new CraftingMenu(containerId, playerInventory, ContainerLevelAccess.create(ctx.world(), ctx.caster().blockPosition())),
                Component.translatable("container.crafting")
        ));
        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean requiresPacket() {
        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.SORCERY, SpellTypes.UTILITY, SpellAction.POINT_UP, 30, 0, 40)
                .build();
    }
}
