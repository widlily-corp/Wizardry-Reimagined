package com.binaris.wizardry.api.content.spell.properties;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellType;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Element;
import com.binaris.wizardry.registry.SpellTier;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellProperties {
    private final Map<String, SpellProperty<?>> properties = new HashMap<>();

    private SpellProperties(List<SpellProperty<?>> propertyList) {
        for (SpellProperty<?> prop : propertyList) {
            properties.put(prop.identifier(), prop);
        }
    }

    public static SpellProperties empty() {
        return new SpellProperties(new ArrayList<>());
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(SpellProperty<T> property) {
        SpellProperty<?> stored = properties.get(property.identifier());
        if (stored != null) {
            return (T) stored.value();
        }
        return property.defaultValue();
    }

    public <T> boolean has(SpellProperty<T> property) {
        return properties.containsKey(property.identifier());
    }

    public static class Builder {
        private final List<SpellProperty<?>> builder = new ArrayList<>();

        private Builder() {}

        public Builder assignBaseProperties(SpellTier tier, Element element, SpellType type, SpellAction action, int cost, int charge, int cooldown) {
            add(DefaultProperties.ELEMENT, element != null ? element.getName() : "magic");
            add(DefaultProperties.SPELL_TYPE, type.getLocation().toString());
            add(DefaultProperties.TIER, tier != null ? tier.getName() : "novice");
            add(DefaultProperties.SPELL_ACTION, action.location.toString());
            add(DefaultProperties.COST, cost);
            add(DefaultProperties.COOLDOWN, cooldown);
            add(DefaultProperties.CHARGEUP, charge);
            return this;
        }

        public Builder assignBaseProperties(java.util.function.Supplier<SpellTier> tier, java.util.function.Supplier<Element> element, SpellType type, SpellAction action, int cost, int charge, int cooldown) {
            String tierName = "novice";
            if (tier instanceof net.neoforged.neoforge.registries.DeferredHolder<?, ?> dh) {
                tierName = dh.getId().getPath();
            } else if (tier != null) {
                try {
                    SpellTier t = tier.get();
                    if (t != null) tierName = t.getName();
                } catch (Exception ignored) {}
            }

            String elementName = "magic";
            if (element instanceof net.neoforged.neoforge.registries.DeferredHolder<?, ?> dh) {
                elementName = dh.getId().getPath();
            } else if (element != null) {
                try {
                    Element e = element.get();
                    if (e != null) elementName = e.getName();
                } catch (Exception ignored) {}
            }

            add(DefaultProperties.ELEMENT, elementName);
            add(DefaultProperties.SPELL_TYPE, type != null && type.getLocation() != null ? type.getLocation().toString() : "ebwizardry:attack");
            add(DefaultProperties.TIER, tierName);
            add(DefaultProperties.SPELL_ACTION, action != null && action.location != null ? action.location.toString() : "ebwizardry:point");
            add(DefaultProperties.COST, cost);
            add(DefaultProperties.COOLDOWN, cooldown);
            add(DefaultProperties.CHARGEUP, charge);
            return this;
        }

        public <T> Builder add(SpellProperty<T> property) {
            if (property != null) {
                builder.add(property.copyOf());
            }
            return this;
        }

        public <T> Builder add(SpellProperty<T> property, T defaultValue) {
            if (property != null) {
                SpellProperty<T> cloned = property.copyOf();
                cloned.defaultValue(defaultValue);
                builder.add(cloned);
            }
            return this;
        }

        public SpellProperties build() {
            return new SpellProperties(new ArrayList<>(builder));
        }
    }
}
