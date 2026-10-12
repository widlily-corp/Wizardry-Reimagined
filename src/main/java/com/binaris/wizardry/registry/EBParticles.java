package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, WizardryMod.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BEAM = particle("beam");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GUARDIAN_BEAM = particle("guardian_beam");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BUFF = particle("buff");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MAGIC_FIRE = particle("magic_fire");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPARKLE = particle("sparkle");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DARK_MAGIC = particle("dark_magic");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SNOW = particle("snow");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LEAF = particle("leaf");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ICE = particle("ice");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CLOUD = particle("cloud");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MAGIC_BUBBLE = particle("magic_bubble");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPARK = particle("spark");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DUST = particle("dust");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LIGHTNING_PULSE = particle("lightning_pulse");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPHERE = particle("sphere");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLASH = particle("flash");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LIGHTNING = particle("lightning");

    private static DeferredHolder<ParticleType<?>, SimpleParticleType> particle(String name) {
        return PARTICLES.register(name, () -> new SimpleParticleType(false));
    }

    public static void register(IEventBus modBus) {
        PARTICLES.register(modBus);
    }

    private EBParticles() {}
}
