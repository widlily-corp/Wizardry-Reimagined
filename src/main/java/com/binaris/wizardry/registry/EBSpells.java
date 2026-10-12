package com.binaris.wizardry.registry;

import com.binaris.wizardry.content.spells.earth.*;
import com.binaris.wizardry.content.spells.fire.*;
import com.binaris.wizardry.content.spells.healing.*;
import com.binaris.wizardry.content.spells.ice.*;
import com.binaris.wizardry.content.spells.lightning.*;
import com.binaris.wizardry.content.spells.magic.*;
import com.binaris.wizardry.content.spells.necromancy.*;
import com.binaris.wizardry.content.spells.sorcery.*;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class EBSpells {

    // 1. Fire Spells
    public static final DeferredHolder<Spell, IgniteSpell> IGNITE =
            EBRegistries.SPELLS.register("ignite", IgniteSpell::new);

    public static final DeferredHolder<Spell, FireballSpell> FIREBALL =
            EBRegistries.SPELLS.register("fireball", FireballSpell::new);

    public static final DeferredHolder<Spell, FirebombSpell> FIREBOMB =
            EBRegistries.SPELLS.register("firebomb", FirebombSpell::new);

    public static final DeferredHolder<Spell, FlameCatchSpell> FLAMECATCHER =
            EBRegistries.SPELLS.register("flamecatcher", FlameCatchSpell::new);

    public static final DeferredHolder<Spell, FireResistanceSpell> FIRE_RESISTANCE =
            EBRegistries.SPELLS.register("fire_resistance", FireResistanceSpell::new);

    // 2. Ice Spells
    public static final DeferredHolder<Spell, FreezeSpell> FREEZE =
            EBRegistries.SPELLS.register("freeze", FreezeSpell::new);

    public static final DeferredHolder<Spell, IceShardsSpell> ICE_SHARD =
            EBRegistries.SPELLS.register("ice_shard", IceShardsSpell::new);

    public static final DeferredHolder<Spell, FrostRaySpell> FROST_RAY =
            EBRegistries.SPELLS.register("frost_ray", FrostRaySpell::new);

    public static final DeferredHolder<Spell, BlizzardSpell> BLIZZARD =
            EBRegistries.SPELLS.register("blizzard", BlizzardSpell::new);

    // 3. Lightning Spells
    public static final DeferredHolder<Spell, SparkSpell> SPARK =
            EBRegistries.SPELLS.register("spark", SparkSpell::new);

    public static final DeferredHolder<Spell, ThunderboltSpell> THUNDERBOLT =
            EBRegistries.SPELLS.register("thunderbolt", ThunderboltSpell::new);

    public static final DeferredHolder<Spell, ArcSpell> ARC =
            EBRegistries.SPELLS.register("arc", ArcSpell::new);

    public static final DeferredHolder<Spell, ChainLightningSpell> CHAIN_LIGHTNING =
            EBRegistries.SPELLS.register("chain_lightning", ChainLightningSpell::new);

    public static final DeferredHolder<Spell, LightningBoltSpell> LIGHTNING_BOLT =
            EBRegistries.SPELLS.register("lightning_bolt", LightningBoltSpell::new);

    // 4. Necromancy Spells
    public static final DeferredHolder<Spell, SummonZombieSpell> SUMMON_ZOMBIE =
            EBRegistries.SPELLS.register("summon_zombie", SummonZombieSpell::new);

    public static final DeferredHolder<Spell, SummonSkeletonSpell> SUMMON_SKELETON =
            EBRegistries.SPELLS.register("summon_skeleton", SummonSkeletonSpell::new);

    public static final DeferredHolder<Spell, LifeDrainSpell> LIFE_DRAIN =
            EBRegistries.SPELLS.register("life_drain", LifeDrainSpell::new);

    public static final DeferredHolder<Spell, DecaySpell> DECAY =
            EBRegistries.SPELLS.register("decay", DecaySpell::new);

    public static final DeferredHolder<Spell, WitherSpell> WITHER =
            EBRegistries.SPELLS.register("wither", WitherSpell::new);

    public static final DeferredHolder<Spell, CurseOfEnfeeblementSpell> CURSE_OF_ENFEEBLEMENT =
            EBRegistries.SPELLS.register("curse_of_enfeeblement", CurseOfEnfeeblementSpell::new);

    // 5. Earth Spells
    public static final DeferredHolder<Spell, DartSpell> DART =
            EBRegistries.SPELLS.register("dart", DartSpell::new);

    public static final DeferredHolder<Spell, SnareSpell> SNARE =
            EBRegistries.SPELLS.register("snare", SnareSpell::new);

    public static final DeferredHolder<Spell, PoisonSpraySpell> POISON_BOMB =
            EBRegistries.SPELLS.register("poison_bomb", PoisonSpraySpell::new);

    public static final DeferredHolder<Spell, EarthTremorSpell> EARTH_TREMOR =
            EBRegistries.SPELLS.register("earth_tremor", EarthTremorSpell::new);

    public static final DeferredHolder<Spell, OakfleshSpell> OAKFLESH =
            EBRegistries.SPELLS.register("oakflesh", OakfleshSpell::new);

    public static final DeferredHolder<Spell, IronfleshSpell> IRONFLESH =
            EBRegistries.SPELLS.register("ironflesh", IronfleshSpell::new);

    public static final DeferredHolder<Spell, DiamondfleshSpell> DIAMONDFLESH =
            EBRegistries.SPELLS.register("diamondflesh", DiamondfleshSpell::new);

    public static final DeferredHolder<Spell, GrowthAuraSpell> GROWTH_AURA =
            EBRegistries.SPELLS.register("growth_aura", GrowthAuraSpell::new);

    // 6. Sorcery Spells
    public static final DeferredHolder<Spell, TelekinesisSpell> TELEKINESIS =
            EBRegistries.SPELLS.register("telekinesis", TelekinesisSpell::new);

    public static final DeferredHolder<Spell, BlinkSpell> BLINK =
            EBRegistries.SPELLS.register("blink", BlinkSpell::new);

    public static final DeferredHolder<Spell, ForcefieldSpell> FORCEFIELD =
            EBRegistries.SPELLS.register("forcefield", ForcefieldSpell::new);

    public static final DeferredHolder<Spell, PhaseStepSpell> PHASE_STEP =
            EBRegistries.SPELLS.register("phase_step", PhaseStepSpell::new);

    public static final DeferredHolder<Spell, GlideSpell> GLIDE =
            EBRegistries.SPELLS.register("glide", GlideSpell::new);

    public static final DeferredHolder<Spell, AgilitySpell> AGILITY =
            EBRegistries.SPELLS.register("agility", AgilitySpell::new);

    public static final DeferredHolder<Spell, InvisibilitySpell> INVISIBILITY =
            EBRegistries.SPELLS.register("invisibility", InvisibilitySpell::new);

    public static final DeferredHolder<Spell, DarkVisionSpell> DARK_VISION =
            EBRegistries.SPELLS.register("dark_vision", DarkVisionSpell::new);

    public static final DeferredHolder<Spell, LeapSpell> LEAP =
            EBRegistries.SPELLS.register("leap", LeapSpell::new);

    // 7. Healing Spells
    public static final DeferredHolder<Spell, HealSpell> HEAL =
            EBRegistries.SPELLS.register("heal", HealSpell::new);

    public static final DeferredHolder<Spell, CureEffectsSpell> CURE_EFFECTS =
            EBRegistries.SPELLS.register("cure_effects", CureEffectsSpell::new);

    public static final DeferredHolder<Spell, HealAllySpell> HEAL_ALLY =
            EBRegistries.SPELLS.register("heal_ally", HealAllySpell::new);

    public static final DeferredHolder<Spell, GreaterHealSpell> GREATER_HEAL =
            EBRegistries.SPELLS.register("greater_heal", GreaterHealSpell::new);

    public static final DeferredHolder<Spell, ReplenishHungerSpell> REPLENISH_HUNGER =
            EBRegistries.SPELLS.register("replenish_hunger", ReplenishHungerSpell::new);

    public static final DeferredHolder<Spell, WaterBreathingSpell> WATER_BREATHING =
            EBRegistries.SPELLS.register("water_breathing", WaterBreathingSpell::new);

    // 8. Magic (General) Spells
    public static final DeferredHolder<Spell, MagicMissileSpell> MAGIC_MISSILE =
            EBRegistries.SPELLS.register("magic_missile", MagicMissileSpell::new);

    public static final DeferredHolder<Spell, LightSpell> LIGHT =
            EBRegistries.SPELLS.register("light", LightSpell::new);

    public static final DeferredHolder<Spell, DetonateSpell> DETONATE =
            EBRegistries.SPELLS.register("detonate", DetonateSpell::new);

    public static void init() {}

    private EBSpells() {}
}
