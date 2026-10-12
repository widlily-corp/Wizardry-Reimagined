package com.binaris.wizardry.api.content.spell;

import net.minecraft.resources.ResourceLocation;

public interface SpellType {
    ResourceLocation getLocation();
    default String getDisplayName() {
        return "spell_type." + getLocation().getNamespace() + "." + getLocation().getPath();
    }
}
