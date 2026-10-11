package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Element {
    private final String name;
    private final ChatFormatting color;
    private final int[] colors;
    private final ResourceLocation location;
    private final String descriptionId;
    private final ResourceLocation icon;

    public Element(String name, ChatFormatting colour, int... colors) {
        this.name = name;
        this.color = colour;
        this.colors = colors;
        this.location = ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, name);
        this.descriptionId = "element." + WizardryMod.MODID + "." + name;
        this.icon = ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/container/element_icon_" + name + ".png");
    }

    public int[] getColors() {
        return colors;
    }

    public Component getDescriptionFormatted() {
        return Component.translatable(descriptionId).withStyle(this.color);
    }

    public String getDescriptionId() {
        return this.descriptionId;
    }

    public ResourceLocation getLocation() {
        return this.location;
    }

    public final boolean is(ResourceLocation location) {
        return this.location.equals(location);
    }

    public final boolean is(String location) {
        return this.location.toString().equals(location);
    }

    public final boolean is(Element element) {
        return this == element;
    }

    public String getName() {
        return name;
    }

    public ChatFormatting getColor() {
        return color;
    }

    public Component getWizardName() {
        return Component.translatable(descriptionId + ".wizard");
    }

    public ResourceLocation getIconId() {
        return icon;
    }

    @Override
    public String toString() {
        return location.toString();
    }
}
