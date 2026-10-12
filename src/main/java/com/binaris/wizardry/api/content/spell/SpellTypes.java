package com.binaris.wizardry.api.content.spell;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.resources.ResourceLocation;

public enum SpellTypes implements SpellType {
    ATTACK("attack"),
    DEFENCE("defence"),
    UTILITY("utility"),
    MINION("minion"),
    BUFF("buff"),
    CONSTRUCT("construct"),
    PROJECTILE("projectile"),
    ALTERATION("alteration");

    private final String name;
    private final ResourceLocation location;

    SpellTypes(String name) {
        this.name = name;
        this.location = ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, name);
    }

    @Override
    public ResourceLocation getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
