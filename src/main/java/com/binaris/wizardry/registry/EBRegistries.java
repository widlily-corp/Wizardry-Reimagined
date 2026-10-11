package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBRegistries {
    public static final ResourceKey<Registry<Element>> ELEMENT_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "elements"));
    public static final ResourceKey<Registry<SpellTier>> TIER_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "tiers"));
    public static final ResourceKey<Registry<Spell>> SPELL_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "spells"));

    public static final DeferredRegister<Element> ELEMENTS =
            DeferredRegister.create(ELEMENT_REGISTRY_KEY, WizardryMod.MODID);
    public static final Registry<Element> ELEMENT_REGISTRY =
            ELEMENTS.makeRegistry(builder -> builder.sync(true));

    public static final DeferredRegister<SpellTier> TIERS =
            DeferredRegister.create(TIER_REGISTRY_KEY, WizardryMod.MODID);
    public static final Registry<SpellTier> TIER_REGISTRY =
            TIERS.makeRegistry(builder -> builder.sync(true));

    public static final DeferredRegister<Spell> SPELLS =
            DeferredRegister.create(SPELL_REGISTRY_KEY, WizardryMod.MODID);
    public static final Registry<Spell> SPELL_REGISTRY =
            SPELLS.makeRegistry(builder -> builder.sync(true));

    public static void register(IEventBus modBus) {
        ELEMENTS.register(modBus);
        TIERS.register(modBus);
        SPELLS.register(modBus);
    }

    private EBRegistries() {}
}
