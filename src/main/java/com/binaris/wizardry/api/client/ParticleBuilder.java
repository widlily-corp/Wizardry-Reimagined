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
    private int count = 1;
    private double spreadX = 0, spreadY = 0, spreadZ = 0;

    private ParticleBuilder() {}

    public static ParticleBuilder create(Supplier<? extends ParticleOptions> particleSupplier) {
        return create(particleSupplier != null ? particleSupplier.get() : ParticleTypes.CRIT);
    }

    public static ParticleBuilder create(Supplier<? extends ParticleOptions> type, Entity entity) {
        double x = entity.xo + (entity.level().random.nextDouble() - 0.5D) * (double) entity.getBbWidth();
        double y = entity.yo + entity.level().random.nextDouble() * (double) entity.getBbHeight();
        double z = entity.zo + (entity.level().random.nextDouble() - 0.5D) * (double) entity.getBbWidth();
        return create(type).pos(x, y, z);
    }

    public static ParticleBuilder create(Supplier<? extends ParticleOptions> type, net.minecraft.util.RandomSource random, double x, double y, double z, double radius) {
        double px = x + (random.nextDouble() * 2 - 1) * radius;
        double py = y + (random.nextDouble() * 2 - 1) * radius;
        double pz = z + (random.nextDouble() * 2 - 1) * radius;
        return create(type).pos(px, py, pz);
    }

    public static ParticleBuilder create(Supplier<? extends ParticleOptions> type, net.minecraft.util.RandomSource random, double x, double y, double z, double radius, boolean move) {
        double px = x + (random.nextDouble() * 2 - 1) * radius;
        double py = y + (random.nextDouble() * 2 - 1) * radius;
        double pz = z + (random.nextDouble() * 2 - 1) * radius;
        if (move) {
            return create(type).pos(px, py, pz).velocity(px - x, py - y, pz - z);
        }
        return create(type).pos(px, py, pz);
    }

    public static void spawnShockParticles(Level world, double x, double y, double z) {
        double px, py, pz;
        for (int i = 0; i < 8; i++) {
            px = x + world.random.nextDouble() - 0.5;
            py = y + world.random.nextDouble() - 0.5;
            pz = z + world.random.nextDouble() - 0.5;
            ParticleBuilder.create(EBParticles.SPARK).pos(px, py, pz).spawn(world);

            px = x + world.random.nextDouble() - 0.5;
            py = y + world.random.nextDouble() - 0.5;
            pz = z + world.random.nextDouble() - 0.5;
            world.addParticle(ParticleTypes.LARGE_SMOKE, px, py, pz, 0, 0, 0);
        }
    }

    public static void spawnHealParticles(Level world, net.minecraft.world.entity.LivingEntity entity) {
        for (int i = 0; i < 10; i++) {
            double x = entity.getX() + world.random.nextDouble() * 2 - 1;
            double y = entity.getY() + entity.getDimensions(entity.getPose()).height() * 0.85F - 0.5 + world.random.nextDouble();
            double z = entity.getZ() + world.random.nextDouble() * 2 - 1;
            ParticleBuilder.create(EBParticles.SPARKLE).pos(x, y, z).velocity(0, 0.1, 0).color(1, 1, 0.3f).spawn(world);
        }
        ParticleBuilder.create(EBParticles.BUFF).entity(entity).color(1, 1, 0.3f).spawn(world);
    }

    public static ParticleBuilder create(ParticleOptions particle) {
        instance.particle = particle != null ? particle : ParticleTypes.CRIT;
        instance.x = instance.y = instance.z = 0;
        instance.vx = instance.vy = instance.vz = 0;
        instance.red = instance.green = instance.blue = 1;
        instance.scale = 1;
        instance.lifetime = 20;
        instance.entity = null;
        instance.count = 1;
        instance.spreadX = instance.spreadY = instance.spreadZ = 0;
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

    public ParticleBuilder color(int rgb) {
        this.red = ((rgb >> 16) & 0xFF) / 255.0f;
        this.green = ((rgb >> 8) & 0xFF) / 255.0f;
        this.blue = (rgb & 0xFF) / 255.0f;
        return this;
    }

    public ParticleBuilder spin(double radius, double speed) {
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

    public ParticleBuilder count(int count) {
        this.count = Math.max(1, count);
        return this;
    }

    public ParticleBuilder spread(double sx, double sy, double sz) {
        this.spreadX = sx;
        this.spreadY = sy;
        this.spreadZ = sz;
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
        double baseX = entity != null ? entity.getX() + x : x;
        double baseY = entity != null ? entity.getY() + y : y;
        double baseZ = entity != null ? entity.getZ() + z : z;

        ParticleOptions toSpawn = particle;
        if (toSpawn == null || toSpawn == EBParticles.BUFF.get() || toSpawn == EBParticles.SPARKLE.get()
                || toSpawn == EBParticles.DARK_MAGIC.get() || toSpawn == EBParticles.SPHERE.get()) {
            toSpawn = new DustParticleOptions(new Vector3f(red, green, blue), scale);
        }

        for (int i = 0; i < count; i++) {
            double spawnX = baseX + (spreadX > 0 ? (level.random.nextDouble() * 2 - 1) * spreadX : 0);
            double spawnY = baseY + (spreadY > 0 ? (level.random.nextDouble() * 2 - 1) * spreadY : 0);
            double spawnZ = baseZ + (spreadZ > 0 ? (level.random.nextDouble() * 2 - 1) * spreadZ : 0);

            if (level.isClientSide) {
                level.addParticle(toSpawn, spawnX, spawnY, spawnZ, vx, vy, vz);
            } else if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(toSpawn, spawnX, spawnY, spawnZ, 1, vx, vy, vz, 0.0);
            }
        }
    }
}
