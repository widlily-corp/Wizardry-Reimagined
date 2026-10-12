package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.entity.MeteorEntity;
import com.binaris.wizardry.content.entity.construct.*;
import com.binaris.wizardry.content.entity.projectile.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, WizardryMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<MagicMissileEntity>> MAGIC_MISSILE =
            register("magic_missile", MagicMissileEntity::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<FireballEntity>> FIREBALL =
            register("fireball", FireballEntity::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<IceShardEntity>> ICE_SHARD =
            register("ice_shard", IceShardEntity::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<DartEntity>> DART =
            register("dart", DartEntity::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<SparkEntity>> SPARK =
            register("spark", SparkEntity::new, 0.3f, 0.3f);

    public static final DeferredHolder<EntityType<?>, EntityType<LightningBoltEntity>> LIGHTNING_BOLT =
            register("lightning_bolt", LightningBoltEntity::new, 0.3f, 0.3f);

    public static final DeferredHolder<EntityType<?>, EntityType<FirebombEntity>> FIREBOMB =
            register("firebomb", FirebombEntity::new, 0.4f, 0.4f);

    public static final DeferredHolder<EntityType<?>, EntityType<IceChargeEntity>> ICE_CHARGE =
            register("ice_charge", IceChargeEntity::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<ForceOrbEntity>> FORCE_ORB =
            register("force_orb", ForceOrbEntity::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<DarknessOrbEntity>> DARKNESS_ORB =
            register("darkness_orb", DarknessOrbEntity::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<PoisonBombEntity>> POISON_BOMB =
            register("poison_bomb", PoisonBombEntity::new, 0.4f, 0.4f);

    public static final DeferredHolder<EntityType<?>, EntityType<FireBoltEntity>> FIRE_BOLT =
            register("firebolt", FireBoltEntity::new, 0.25f, 0.25f);

    public static final DeferredHolder<EntityType<?>, EntityType<FlamecatcherArrow>> FLAME_CATCHER_ARROW =
            register("flamecatcher_arrow", FlamecatcherArrow::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<ThunderboltEntity>> THUNDERBOLT =
            register("thunderbolt", ThunderboltEntity::new, 0.25f, 0.25f);

    public static final DeferredHolder<EntityType<?>, EntityType<SparkBombEntity>> SPARK_BOMB =
            register("spark_bomb", SparkBombEntity::new, 0.25f, 0.25f);

    public static final DeferredHolder<EntityType<?>, EntityType<SmokeBombEntity>> SMOKE_BOMB =
            register("smoke_bomb", SmokeBombEntity::new, 0.25f, 0.25f);

    public static final DeferredHolder<EntityType<?>, EntityType<LightningArrow>> LIGHTNING_ARROW =
            register("lightning_arrow", LightningArrow::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<IceLanceEntity>> ICE_LANCE =
            register("ice_lance", IceLanceEntity::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<IceBall>> ICE_BALL =
            register("iceball", IceBall::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<ForceArrow>> FORCE_ARROW =
            register("force_arrow", ForceArrow::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<ConjuredArrowEntity>> CONJURED_ARROW =
            register("conjured_arrow", ConjuredArrowEntity::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<SwarmArrowEntity>> SWARM_ARROW =
            register("swarm_arrow", SwarmArrowEntity::new, 0.5f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<FireRingConstruct>> RING_OF_FIRE =
            register("ring_of_fire", FireRingConstruct::new, 3.0f, 1.0f);

    public static final DeferredHolder<EntityType<?>, EntityType<FireSigilConstruct>> FIRE_SIGIL =
            register("fire_sigil", FireSigilConstruct::new, 1.0f, 0.2f);

    public static final DeferredHolder<EntityType<?>, EntityType<CombustionRuneConstruct>> COMBUSTION_RUNE =
            register("combustion_rune", CombustionRuneConstruct::new, 2.0f, 0.2f);

    public static final DeferredHolder<EntityType<?>, EntityType<MeteorEntity>> METEOR =
            register("meteor", MeteorEntity::new, 0.98f, 0.98f);

    public static final DeferredHolder<EntityType<?>, EntityType<BlizzardConstruct>> BLIZZARD =
            register("blizzard", BlizzardConstruct::new, 6.0f, 3.0f);

    public static final DeferredHolder<EntityType<?>, EntityType<HailstormConstruct>> HAILSTORM =
            register("hailstorm", HailstormConstruct::new, 4.0f, 5.0f);

    public static final DeferredHolder<EntityType<?>, EntityType<FrostSigilConstruct>> FROST_SIGIL =
            register("frost_sigil", FrostSigilConstruct::new, 2.0f, 0.2f);

    public static final DeferredHolder<EntityType<?>, EntityType<IceSpikeConstruct>> ICE_SPIKE =
            register("ice_spikes", IceSpikeConstruct::new, 0.5f, 1.0f);

    public static final DeferredHolder<EntityType<?>, EntityType<IceBarrierConstruct>> ICE_BARRIER =
            register("ice_barrier", IceBarrierConstruct::new, 3.0f, 3.0f);

    public static final DeferredHolder<EntityType<?>, EntityType<LightningSigilConstruct>> LIGHTNING_SIGIL =
            register("lightning_sigil", LightningSigilConstruct::new, 2.0f, 0.2f);

    public static final DeferredHolder<EntityType<?>, EntityType<TornadoConstruct>> TORNADO =
            register("tornado", TornadoConstruct::new, 4.0f, 8.0f);

    public static final DeferredHolder<EntityType<?>, EntityType<HealAuraConstruct>> HEAL_AURA =
            register("heal_aura", HealAuraConstruct::new, 6.0f, 1.0f);

    public static final DeferredHolder<EntityType<?>, EntityType<BubbleConstruct>> BUBBLE =
            register("bubble", BubbleConstruct::new, 1.0f, 1.0f);

    public static final DeferredHolder<EntityType<?>, EntityType<BoulderConstruct>> BOULDER =
            register("boulder", BoulderConstruct::new, 2.375f, 2.375f);

    public static final DeferredHolder<EntityType<?>, EntityType<DecayConstruct>> DECAY =
            register("decay", DecayConstruct::new, 2.0f, 0.2f);

    public static final DeferredHolder<EntityType<?>, EntityType<ArrowRainConstruct>> ARROW_RAIN =
            register("arrow_rain", ArrowRainConstruct::new, 8.0f, 0.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<ArrowOrbConstruct>> ARROW_ORB =
            register("arrow_orb", ArrowOrbConstruct::new, 1.2f, 1.2f);

    public static final DeferredHolder<EntityType<?>, EntityType<ZombieSpawnerConstruct>> ZOMBIE_SPAWNER =
            register("zombie_spawner", ZombieSpawnerConstruct::new, 1.0f, 1.0f);

    public static final DeferredHolder<EntityType<?>, EntityType<com.binaris.wizardry.content.entity.mob.WizardEntity>> WIZARD =
            ENTITIES.register("wizard", () -> EntityType.Builder.of(com.binaris.wizardry.content.entity.mob.WizardEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.8f)
                    .clientTrackingRange(64)
                    .updateInterval(3)
                    .build("wizard"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.binaris.wizardry.content.entity.mob.EvilWizardEntity>> EVIL_WIZARD =
            ENTITIES.register("evil_wizard", () -> EntityType.Builder.of(com.binaris.wizardry.content.entity.mob.EvilWizardEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f)
                    .clientTrackingRange(64)
                    .updateInterval(3)
                    .build("evil_wizard"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.binaris.wizardry.content.entity.mob.EvilWizardEntity>> REMNANT =
            ENTITIES.register("remnant", () -> EntityType.Builder.of(com.binaris.wizardry.content.entity.mob.EvilWizardEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f)
                    .clientTrackingRange(64)
                    .updateInterval(3)
                    .build("remnant"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.binaris.wizardry.content.entity.construct.CelestialMonolithEntity>> CELESTIAL_MONOLITH =
            register("celestial_monolith", com.binaris.wizardry.content.entity.construct.CelestialMonolithEntity::new, 2.5f, 10.0f);

    public static final DeferredHolder<EntityType<?>, EntityType<com.binaris.wizardry.content.entity.construct.SupernovaEntity>> SUPERNOVA =
            register("supernova", com.binaris.wizardry.content.entity.construct.SupernovaEntity::new, 2.5f, 2.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<com.binaris.wizardry.content.entity.construct.GlacialOblivionEntity>> GLACIAL_OBLIVION =
            register("glacial_oblivion", com.binaris.wizardry.content.entity.construct.GlacialOblivionEntity::new, 2.5f, 6.0f);

    public static final DeferredHolder<EntityType<?>, EntityType<com.binaris.wizardry.content.entity.construct.TitansHammerEntity>> TITANS_HAMMER =
            register("titans_hammer", com.binaris.wizardry.content.entity.construct.TitansHammerEntity::new, 2.0f, 4.0f);

    public static final DeferredHolder<EntityType<?>, EntityType<com.binaris.wizardry.content.entity.construct.AbyssEyeEntity>> ABYSS_EYE =
            register("abyss_eye", com.binaris.wizardry.content.entity.construct.AbyssEyeEntity::new, 2.0f, 2.0f);

    public static final DeferredHolder<EntityType<?>, EntityType<com.binaris.wizardry.content.entity.construct.TectonicShatterEntity>> TECTONIC_SHATTER =
            register("tectonic_shatter", com.binaris.wizardry.content.entity.construct.TectonicShatterEntity::new, 4.0f, 4.5f);

    public static final DeferredHolder<EntityType<?>, EntityType<com.binaris.wizardry.content.entity.mob.IceGiant>> ICE_GIANT =
            ENTITIES.register("ice_giant", () -> EntityType.Builder.<com.binaris.wizardry.content.entity.mob.IceGiant>of(com.binaris.wizardry.content.entity.mob.IceGiant::new, MobCategory.CREATURE)
                    .sized(1.4f, 2.7f)
                    .clientTrackingRange(64)
                    .updateInterval(3)
                    .build("ice_giant"));

    public static final DeferredHolder<EntityType<?>, EntityType<com.binaris.wizardry.content.entity.mob.LightningWraith>> LIGHTNING_WRAITH =
            ENTITIES.register("lightning_wraith", () -> EntityType.Builder.<com.binaris.wizardry.content.entity.mob.LightningWraith>of(com.binaris.wizardry.content.entity.mob.LightningWraith::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f)
                    .clientTrackingRange(64)
                    .updateInterval(3)
                    .build("lightning_wraith"));

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(
            String name, EntityType.EntityFactory<T> factory, float width, float height) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(factory, MobCategory.MISC)
                .sized(width, height)
                .clientTrackingRange(64)
                .updateInterval(10)
                .build(name));
    }

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
    }

    private EBEntities() {}
}
