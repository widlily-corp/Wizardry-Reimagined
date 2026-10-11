package com.binaris.wizardry.worldgen;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;

public final class EBWorldGen {
    // Biome Tags
    public static final TagKey<Biome> SPAWNS_CRYSTAL_ORE = TagKey.create(Registries.BIOME, WizardryMod.id("spawns_crystal_ore"));
    public static final TagKey<Biome> SPAWNS_CRYSTAL_FLOWER = TagKey.create(Registries.BIOME, WizardryMod.id("spawns_crystal_flower"));
    public static final TagKey<Biome> SPAWNS_EVIL_WIZARD = TagKey.create(Registries.BIOME, WizardryMod.id("spawns_evil_wizard"));
    public static final TagKey<Biome> HAS_WIZARD_TOWER = TagKey.create(Registries.BIOME, WizardryMod.id("has_structure/wizard_tower"));
    public static final TagKey<Biome> HAS_SHRINE = TagKey.create(Registries.BIOME, WizardryMod.id("has_structure/shrine"));
    public static final TagKey<Biome> HAS_OBELISK = TagKey.create(Registries.BIOME, WizardryMod.id("has_structure/obelisk"));

    // Placed Features
    public static final ResourceKey<PlacedFeature> CRYSTAL_ORE = ResourceKey.create(Registries.PLACED_FEATURE, WizardryMod.id("crystal_ore"));
    public static final ResourceKey<PlacedFeature> CRYSTAL_FLOWER = ResourceKey.create(Registries.PLACED_FEATURE, WizardryMod.id("crystal_flower"));

    // Structures
    public static final ResourceKey<Structure> WIZARD_TOWER = ResourceKey.create(Registries.STRUCTURE, WizardryMod.id("wizard_tower"));
    public static final ResourceKey<Structure> SHRINE = ResourceKey.create(Registries.STRUCTURE, WizardryMod.id("shrine"));
    public static final ResourceKey<Structure> OBELISK = ResourceKey.create(Registries.STRUCTURE, WizardryMod.id("obelisk"));
    public static final ResourceKey<Structure> LIBRARY_RUINS = ResourceKey.create(Registries.STRUCTURE, WizardryMod.id("library_ruins"));

    private EBWorldGen() {}
}
