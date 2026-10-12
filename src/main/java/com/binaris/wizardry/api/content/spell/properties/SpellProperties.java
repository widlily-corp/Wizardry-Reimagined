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
