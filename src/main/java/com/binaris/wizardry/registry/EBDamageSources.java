package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.ArrayList;

public final class EBDamageSources {
    public static final ArrayList<ResourceKey<DamageType>> TYPES = new ArrayList<>();
    public static final ResourceKey<DamageType> MAGIC = createType("magic");
    public static final ResourceKey<DamageType> SORCERY = createType("sorcery");
    public static final ResourceKey<DamageType> FIRE = createType("fire");
    public static final ResourceKey<DamageType> FROST = createType("frost");
    public static final ResourceKey<DamageType> SHOCK = createType("shock");
    public static final ResourceKey<DamageType> WITHER = createType("wither");
    public static final ResourceKey<DamageType> POISON = createType("poison");
    public static final ResourceKey<DamageType> FORCE = createType("force");
    public static final ResourceKey<DamageType> BLAST = createType("blast");
    public static final ResourceKey<DamageType> RADIANT = createType("radiant");
    public static final ResourceKey<DamageType> EARTH = createType("earth");
    public static final ResourceKey<DamageType> LIGHTNING = SHOCK;
    public static final ResourceKey<DamageType> ICE = FROST;

    private EBDamageSources() {
    }

    public static void init() {
    }

    private static ResourceKey<DamageType> createType(String name) {
        var key = ResourceKey.create(Registries.DAMAGE_TYPE, WizardryMod.id(name));
        TYPES.add(key);
        return key;
    }

    public static ResourceKey<DamageType> createType(ResourceLocation location) {
        var key = ResourceKey.create(Registries.DAMAGE_TYPE, location);
        TYPES.add(key);
        return key;
    }

    public static boolean isMagic(DamageSource source) {
        return TYPES.stream().anyMatch(source::is) || source.is(DamageTypes.MAGIC);
    }
}
