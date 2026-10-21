package com.binaris.wizardry.api.content.spell;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.resources.ResourceLocation;

public class SpellAction {
    public final ResourceLocation location;

    public static final SpellAction POINT = new SpellAction("point");
    public static final SpellAction POINT_UP = new SpellAction("point_up");
    public static final SpellAction POINT_DOWN = new SpellAction("point_down");
    public static final SpellAction SUMMON = new SpellAction("summon");
    public static final SpellAction THRUST = new SpellAction("thrust");
    public static final SpellAction IMBUE = new SpellAction("imbue");
    public static final SpellAction GRAPPLE = new SpellAction("grapple");
    public static final SpellAction NONE = new SpellAction("none");

    public SpellAction(String name) {
        this(ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, name));
    }

    public SpellAction(ResourceLocation location) {
        this.location = location;
    }
}
