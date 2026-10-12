package com.binaris.wizardry.api.content.spell.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LocationCastContext extends CastContext {
    private final Vec3 pos;
    private final Direction direction;

    public LocationCastContext(Level world, Vec3 pos, Direction direction, int castingTicks, SpellModifiers modifiers) {
        super(world, castingTicks, modifiers);
        this.pos = pos;
        this.direction = direction != null ? direction : Direction.UP;
    }

    public Vec3 vec3() {
        return pos;
    }

    public BlockPos pos() {
        return BlockPos.containing(pos);
    }

    public double x() {
        return pos.x;
    }

    public double y() {
        return pos.y;
    }

    public double z() {
        return pos.z;
    }

    public Direction direction() {
        return direction;
    }
}
