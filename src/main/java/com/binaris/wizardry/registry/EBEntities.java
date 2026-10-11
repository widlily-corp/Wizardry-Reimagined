package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
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
