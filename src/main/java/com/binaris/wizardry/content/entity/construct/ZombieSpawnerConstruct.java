package com.binaris.wizardry.content.entity.construct;

import com.binaris.wizardry.registry.EBEntities;
import com.binaris.wizardry.registry.EBSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class ZombieSpawnerConstruct extends MagicConstructEntity {

    private static final double MAX_NUDGE_DISTANCE = 0.5;
    public boolean spawnHusks = false;
    private int spawnTimer = 10;

    public ZombieSpawnerConstruct(EntityType<?> type, Level level) {
        super(type, level);
    }

    public ZombieSpawnerConstruct(Level level) {
        this(EBEntities.ZOMBIE_SPAWNER.get(), level);
    }

    @Override
    public void tick() {
        super.tick();
        if (getCaster() == null) return;

        if (lifetime - tickCount > 10 && spawnTimer-- <= 0) {
            spawnTimer = 20;
            if (EBSounds.ENTITY_ZOMBIE_SPAWNER_SPAWN != null && EBSounds.ENTITY_ZOMBIE_SPAWNER_SPAWN.get() != null) {
                playSound(EBSounds.ENTITY_ZOMBIE_SPAWNER_SPAWN.get(), 1.0f, 1.0f);
            }

            if (!level().isClientSide) {
                Zombie zombie = spawnHusks ? new Husk(EntityType.HUSK, level()) : new Zombie(EntityType.ZOMBIE, level());
                zombie.setCustomName(Component.literal("Minion of " + getCaster().getName().getString()));

                double nx = getX() + (random.nextDouble() * 2 - 1) * MAX_NUDGE_DISTANCE;
                double nz = getZ() + (random.nextDouble() * 2 - 1) * MAX_NUDGE_DISTANCE;
                zombie.setPos(nx, getY(), nz);

                level().addFreshEntity(zombie);
            }
        }
    }
}
