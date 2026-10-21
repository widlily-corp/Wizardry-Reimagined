package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Underworld Portal Construct for the Zombie Apocalypse spell.
 * Hovering portal that continuously summons undead minions who drop from the abyss with particle/sound feedback.
 */
public class ZombieSpawnerConstruct extends MagicConstructEntity {

    private static final double MAX_NUDGE_DISTANCE = 0.6;
    public boolean spawnHusks = false;
    private int spawnTimer = 10;

    public ZombieSpawnerConstruct(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public ZombieSpawnerConstruct(Level level) {
        this(EBEntities.ZOMBIE_SPAWNER.get(), level);
    }

    @Override
    public void tick() {
        super.tick();

        // 1. Ongoing portal swirling particles & ambient hum
        spawnPortalParticles();

        if (!level().isClientSide && tickCount % 25 == 0) {
            level().playSound(null, getX(), getY(), getZ(), SoundEvents.PORTAL_AMBIENT, SoundSource.PLAYERS, 0.5f, 0.8f);
        }

        // 2. Periodic zombie spawn cycle
        if (lifetime - tickCount > 10 && spawnTimer-- <= 0) {
            spawnTimer = 20;

            if (!level().isClientSide) {
                if (EBSounds.ENTITY_ZOMBIE_SPAWNER_SPAWN != null && EBSounds.ENTITY_ZOMBIE_SPAWNER_SPAWN.get() != null) {
                    playSound(EBSounds.ENTITY_ZOMBIE_SPAWNER_SPAWN.get(), 1.0f, 1.0f);
                }
                level().playSound(null, getX(), getY(), getZ(), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.8f, 1.3f);
                level().playSound(null, getX(), getY(), getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS, 0.6f, 0.6f);

                if (level() instanceof ServerLevel serverLevel) {
                    // Portal opening flash & soul burst
                    serverLevel.sendParticles(ParticleTypes.FLASH, getX(), getY(), getZ(), 2, 0.2, 0.2, 0.2, 0);
                    serverLevel.sendParticles(ParticleTypes.SOUL, getX(), getY(), getZ(), 16, 0.6, 0.3, 0.6, 0.08);
                    serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, getX(), getY(), getZ(), 10, 0.5, 0.2, 0.5, 0.05);

                    Zombie zombie = spawnHusks ? new Husk(EntityType.HUSK, level()) : new Zombie(EntityType.ZOMBIE, level());
                    LivingEntity caster = getCaster();
                    if (caster != null) {
                        zombie.setCustomName(Component.literal("Minion of " + caster.getName().getString()));
                    } else {
                        zombie.setCustomName(Component.literal("Minion of the Underworld"));
                    }

                    double nx = getX() + (random.nextDouble() * 2 - 1) * MAX_NUDGE_DISTANCE;
                    double nz = getZ() + (random.nextDouble() * 2 - 1) * MAX_NUDGE_DISTANCE;
                    zombie.setPos(nx, getY() - 0.2, nz);

                    // Velocity: Drop down out of the portal
                    zombie.setDeltaMovement(0, -0.28, 0);

                    // Give slow falling so minions don't smash to death on ground impact
                    zombie.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 60, 0, false, false));

                    // Equip iron helmet so zombies don't burn if summoned during daytime
                    zombie.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                    zombie.setDropChance(EquipmentSlot.HEAD, 0.0f);

                    level().addFreshEntity(zombie);
                }
            }
        }
    }

    private void spawnPortalParticles() {
        if (level() instanceof ServerLevel serverLevel) {
            // Rotating portal rim particles
            double radius = 1.6;
            for (int i = 0; i < 4; i++) {
                double angle = (tickCount * 0.2) + (i * Math.PI / 2.0);
                double px = getX() + Math.cos(angle) * radius;
                double pz = getZ() + Math.sin(angle) * radius;

                serverLevel.sendParticles(ParticleTypes.PORTAL, px, getY(), pz, 1, 0, -0.05, 0, 0.1);
                serverLevel.sendParticles(ParticleTypes.FALLING_OBSIDIAN_TEAR, px, getY() - 0.1, pz, 1, 0, -0.1, 0, 0.02);
            }

            // Dark void center mist
            serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, getX(), getY(), getZ(), 2, 0.3, 0.1, 0.3, 0.02);
            if (random.nextFloat() < 0.3f) {
                serverLevel.sendParticles(ParticleTypes.SQUID_INK, getX(), getY() - 0.2, getZ(), 1, 0, -0.05, 0, 0.02);
            }
        }
    }

    @Override
    public void despawn() {
        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            level().playSound(null, getX(), getY(), getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.5f, 0.6f);
            serverLevel.sendParticles(ParticleTypes.FLASH, getX(), getY(), getZ(), 3, 0.3, 0.3, 0.3, 0);
            serverLevel.sendParticles(ParticleTypes.SQUID_INK, getX(), getY(), getZ(), 40, 1.5, 0.5, 1.5, 0.1);
        }
        super.despawn();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SpawnHusks")) {
            this.spawnHusks = tag.getBoolean("SpawnHusks");
        } else if (tag.contains("spawnHusks")) {
            this.spawnHusks = tag.getBoolean("spawnHusks");
        }
        if (tag.contains("SpawnTimer")) {
            this.spawnTimer = tag.getInt("SpawnTimer");
        } else if (tag.contains("spawnTimer")) {
            this.spawnTimer = tag.getInt("spawnTimer");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("SpawnHusks", this.spawnHusks);
        tag.putInt("SpawnTimer", this.spawnTimer);
    }
}
