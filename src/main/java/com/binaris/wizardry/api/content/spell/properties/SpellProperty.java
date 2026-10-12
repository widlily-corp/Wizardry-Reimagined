package com.binaris.wizardry.api.content.spell.properties;

import com.mojang.serialization.Codec;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SpellProperty<T> {
    private static final Set<SpellProperty<?>> PROPERTIES = new HashSet<>();
    protected String identifier;
    protected T value;
    protected T defaultValue;
    protected Codec<T> codec;

    protected SpellProperty() {
    }

    public static SpellProperty<Byte> byteProperty(String id) {
        return byteProperty(id, (byte) 0);
    }

    public static SpellProperty<Short> shortProperty(String id) {
        return shortProperty(id, (short) 0);
    }

    public static SpellProperty<Integer> intProperty(String id) {
        return intProperty(id, 0);
    }

    public static SpellProperty<Long> longProperty(String id) {
        return longProperty(id, 0L);
    }

    public static SpellProperty<Float> floatProperty(String id) {
        return floatProperty(id, 0f);
    }

    public static SpellProperty<Double> doubleProperty(String id) {
        return doubleProperty(id, 0d);
    }

    public static SpellProperty<Boolean> booleanProperty(String id) {
        return booleanProperty(id, false);
    }

    public static SpellProperty<String> stringProperty(String id) {
        return stringProperty(id, "");
    }

    public static SpellProperty<Byte> byteProperty(String id, byte value) {
        return createProperty(id, value, Codec.BYTE);
    }

    public static SpellProperty<Short> shortProperty(String id, short value) {
        return createProperty(id, value, Codec.SHORT);
    }

    public static SpellProperty<Integer> intProperty(String id, int value) {
        return createProperty(id, value, Codec.INT);
    }

    public static SpellProperty<Long> longProperty(String id, long value) {
        return createProperty(id, value, Codec.LONG);
    }

    public static SpellProperty<Float> floatProperty(String id, float value) {
        return createProperty(id, value, Codec.FLOAT);
    }

    public static SpellProperty<Double> doubleProperty(String id, double value) {
        return createProperty(id, value, Codec.DOUBLE);
    }

    public static SpellProperty<Boolean> booleanProperty(String id, boolean value) {
        return createProperty(id, value, Codec.BOOL);
    }

    public static SpellProperty<String> stringProperty(String id, String value) {
        return createProperty(id, value, Codec.STRING);
    }

    public static <T> SpellProperty<T> createProperty(String id, T value, Codec<T> codec) {
        SpellProperty<T> property = new SpellProperty<>();
        property.identifier = id;
        property.value = value;
        property.defaultValue = value;
        property.codec = codec;
        PROPERTIES.add(property);
        return property;
    }

    public static <T> SpellProperty<T> copyOf(SpellProperty<T> property) {
        SpellProperty<T> copy = new SpellProperty<>();
        copy.identifier = property.identifier;
        copy.value = property.value;
        copy.defaultValue = property.defaultValue;
        copy.codec = property.codec;
        return copy;
    }

    public SpellProperty<T> copyOf() {
        return copyOf(this);
    }

    public String identifier() {
        return identifier;
    }

    public T value() {
        return value != null ? value : defaultValue;
    }

    public T defaultValue() {
        return defaultValue;
    }

    public SpellProperty<T> value(T value) {
        this.value = value;
        return this;
    }

    public SpellProperty<T> defaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        return this;
    }

    public Codec<T> codec() {
        return codec;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpellProperty<?> that = (SpellProperty<?>) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return "SpellProperty{" + identifier + "=" + value() + "}";
    }
}
