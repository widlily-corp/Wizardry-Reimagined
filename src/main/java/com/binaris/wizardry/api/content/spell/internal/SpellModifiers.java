package com.binaris.wizardry.api.content.spell.internal;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;

public final class SpellModifiers {
    public static final String POTENCY = "ebwizardry.potency";
    public static final String COST = "ebwizardry.cost";
    public static final String CHARGEUP = "ebwizardry.chargeup";
    public static final String PROGRESSION = "ebwizardry.progression";
    public static final String DURATION = "ebwizardry.duration";
    public static final String BLAST = "ebwizardry.blast";
    public static final String RANGE = "ebwizardry.range";
    public static final String COOLDOWN = "ebwizardry.cooldown";
    public static final String HEALTH_MODIFIER = "ebwizardry.health_modifier";

    private final Map<String, Float> baseModifiers = new HashMap<>();

    public SpellModifiers() {}

    public float get(String key, float base) {
        float factor = baseModifiers.getOrDefault(key, 1.0f);
        return base * factor;
    }

    public float getFactor(String key) {
        return baseModifiers.getOrDefault(key, 1.0f);
    }

    public SpellModifiers set(String key, float value) {
        baseModifiers.put(key, value);
        return this;
    }

    public SpellModifiers add(String key, float amount) {
        float current = baseModifiers.getOrDefault(key, 1.0f);
        baseModifiers.put(key, current + amount);
        return this;
    }

    public SpellModifiers multiply(String key, float factor) {
        float current = baseModifiers.getOrDefault(key, 1.0f);
        baseModifiers.put(key, current * factor);
        return this;
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        baseModifiers.forEach(tag::putFloat);
        return tag;
    }

    public static SpellModifiers fromTag(CompoundTag tag) {
        SpellModifiers modifiers = new SpellModifiers();
        if (tag != null) {
            for (String key : tag.getAllKeys()) {
                modifiers.set(key, tag.getFloat(key));
            }
        }
        return modifiers;
    }
}
