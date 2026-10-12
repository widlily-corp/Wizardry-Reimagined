package com.binaris.wizardry.api.client;

import com.binaris.wizardry.registry.EBParticles;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.function.Supplier;

public final class ParticleBuilder {
    private static final ParticleBuilder instance = new ParticleBuilder();

    private ParticleOptions particle;
    private double x, y, z;
    private double vx, vy, vz;
    private float red = 1, green = 1, blue = 1;
    private float scale = 1;
    private int lifetime = 20;
    private Entity entity;

    private ParticleBuilder() {}

    public static ParticleBuilder create(Supplier<? extends ParticleOptions> particleSupplier) {
        return create(particleSupplier != null ? particleSupplier.get() : ParticleTypes.CRIT);
    }

    public static ParticleBuilder create(ParticleOptions particle) {
        instance.particle = particle != null ? particle : ParticleTypes.CRIT;
        instance.x = instance.y = instance.z = 0;
        instance.vx = instance.vy = instance.vz = 0;
        instance.red = instance.green = instance.blue = 1;
        instance.scale = 1;
        instance.lifetime = 20;
        instance.entity = null;
        return instance;
    }

    public ParticleBuilder pos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public ParticleBuilder pos(Vec3 vec) {
        return pos(vec.x, vec.y, vec.z);
    }

    public ParticleBuilder entity(Entity entity) {
        this.entity = entity;
        return this;
    }

    public ParticleBuilder velocity(double vx, double vy, double vz) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        return this;
    }

    public ParticleBuilder color(float r, float g, float b) {
        this.red = r;
        this.green = g;
        this.blue = b;
        return this;
    }

    public ParticleBuilder scale(float scale) {
        this.scale = scale;
        return this;
    }

    public ParticleBuilder time(int lifetime) {
        this.lifetime = lifetime;
        return this;
    }

    public ParticleBuilder target(double tx, double ty, double tz) {
        this.vx = (tx - this.x) * 0.1;
        this.vy = (ty - this.y) * 0.1;
        this.vz = (tz - this.z) * 0.1;
        return this;
    }

    public ParticleBuilder target(Entity target) {
        if (target != null) {
            return target(target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ());
        }
        return this;
    }

    public ParticleBuilder radius(double radius) {
        return this;
    }

    public ParticleBuilder rpt(double rpt) {
        return this;
    }

    public ParticleBuilder collide(boolean collide) {
        return this;
    }

    public ParticleBuilder shaded(boolean shaded) {
        return this;
    }

    public ParticleBuilder gravity(boolean gravity) {
        return this;
    }

    public ParticleBuilder fadeColor(float r, float g, float b) {
        return this;
    }

    public ParticleBuilder length(double length) {
        return this;
    }

    public ParticleBuilder seed(long seed) {
        return this;
    }

    public ParticleBuilder yaw(float yaw) {
        return this;
    }

    public ParticleBuilder pitch(float pitch) {
        return this;
    }

    public ParticleBuilder sound(boolean sound) {
        return this;
    }

    public ParticleBuilder soundVolume(float soundVolume) {
        return this;
    }

    public ParticleBuilder soundPitch(float soundPitch) {
        return this;
    }

    public ParticleBuilder speed(double speed) {
        this.vx *= speed;
        this.vy *= speed;
        this.vz *= speed;
        return this;
    }

    public void spawn(Level level) {
        if (level == null) return;
        double spawnX = entity != null ? entity.getX() + x : x;
        double spawnY = entity != null ? entity.getY() + y : y;
        double spawnZ = entity != null ? entity.getZ() + z : z;

        ParticleOptions toSpawn = particle;
        if (toSpawn == null || toSpawn == EBParticles.BUFF.get() || toSpawn == EBParticles.SPARKLE.get()) {
            toSpawn = new DustParticleOptions(new Vector3f(red, green, blue), scale);
        }

        if (level.isClientSide) {
            level.addParticle(toSpawn, spawnX, spawnY, spawnZ, vx, vy, vz);
        } else if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(toSpawn, spawnX, spawnY, spawnZ, 1, vx, vy, vz, 0.0);
        }
    }
}
