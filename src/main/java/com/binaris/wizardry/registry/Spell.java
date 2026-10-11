package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public abstract class Spell {
    private final Supplier<Element> elementSupplier;
    private final Supplier<SpellTier> tierSupplier;
    private final int cost;
    private final int cooldown;
    private final int chargeup;
    private ResourceLocation location;
    private String descriptionId;
    private ResourceLocation icon;

    public Spell(Supplier<Element> element, Supplier<SpellTier> tier, int cost, int cooldown, int chargeup) {
        this.elementSupplier = element;
        this.tierSupplier = tier;
        this.cost = cost;
        this.cooldown = cooldown;
        this.chargeup = chargeup;
    }

    public Spell(Supplier<Element> element, Supplier<SpellTier> tier) {
        this(element, tier, 0, 0, 0);
    }

    public Element getElement() {
        return elementSupplier.get();
    }

    public SpellTier getTier() {
        return tierSupplier.get();
    }

    public int getCost() {
        return cost;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getChargeup() {
        return chargeup;
    }

    public boolean cast(Level level, Player player) {
        return false;
    }

    public boolean cast(Level level, LivingEntity caster) {
        return false;
    }

    public boolean requiresPacket() {
        return true;
    }

    public ResourceLocation getLocation() {
        if (location == null) {
            ResourceLocation key = EBRegistries.SPELL_REGISTRY.getKey(this);
            location = key != null ? key : ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "none");
        }
        return location;
    }

    public String getDescriptionId() {
        if (descriptionId == null) {
            descriptionId = "spell." + getLocation().getNamespace() + "." + getLocation().getPath();
        }
        return descriptionId;
    }

    public Component getDescriptionFormatted() {
        return Component.translatable(getDescriptionId()).withStyle(getElement().getColor());
    }

    public ResourceLocation getIcon() {
        if (icon == null) {
            icon = ResourceLocation.fromNamespaceAndPath(getLocation().getNamespace(), "textures/spells/" + getLocation().getPath() + ".png");
        }
        return icon;
    }

    public boolean is(ResourceLocation otherLocation) {
        return getLocation().equals(otherLocation);
    }

    @Override
    public String toString() {
        return getLocation().toString();
    }
}
