package com.binaris.wizardry.content.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;

public class MagicFireballEntity extends FireballEntity {
    public MagicFireballEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public MagicFireballEntity(Level level) {
        super(level);
    }

    public MagicFireballEntity(Level level, LivingEntity shooter) {
        super(level, shooter);
    }
}
