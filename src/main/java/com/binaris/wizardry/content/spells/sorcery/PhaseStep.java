package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.spell.properties.SpellProperty;
import com.binaris.wizardry.api.content.util.BlockUtil;
import com.binaris.wizardry.api.content.util.EntityUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PhaseStep extends Spell {
    public static final SpellProperty<Integer> WALL_THICKNESS = SpellProperty.intProperty("wall_thickness", 1);

    @Override
    public boolean cast(PlayerCastContext ctx) {
        Player caster = ctx.caster();
        Level world = ctx.world();

        double range = property(DefaultProperties.RANGE);
        int maxThickness = property(WALL_THICKNESS);

        Vec3 start = caster.getEyePosition();
        Vec3 direction = caster.getLookAngle();

        if (world.isClientSide) {
            for (int i = 0; i < 10; i++) {
                double x = caster.getX();
                double y = caster.getY() + 2 * world.random.nextFloat();
                double z = caster.getZ();
                world.addParticle(ParticleTypes.PORTAL, x, y, z,
                        world.random.nextDouble() - 0.5,
                        world.random.nextDouble() - 0.5,
                        world.random.nextDouble() - 0.5);
            }
        }

        Entity subject = caster;
        BlockPos lastChecked = null;
        int wallThickness = 0;
        boolean inWall = false;

        for (double dist = 1.0; dist <= range; dist += 0.5) {
            Vec3 checkPoint = start.add(direction.scale(dist));
            BlockPos checkPos = BlockPos.containing(checkPoint);

            if (lastChecked != null && lastChecked.equals(checkPos)) continue;
            lastChecked = checkPos;

            boolean isBlocked = !BlockUtil.isBlockPassable(world, checkPos) || !BlockUtil.isBlockPassable(world, checkPos.above());

            if (isBlocked) {
                if (!inWall) {
                    inWall = true;
                    wallThickness = 1;
                } else {
                    wallThickness++;
                }

                if (wallThickness > maxThickness) break;
            } else if (inWall) {
                Vec3 dest = new Vec3(checkPos.getX() + 0.5, checkPos.getY(), checkPos.getZ() + 0.5);
                if (attemptTeleport(world, subject, dest, caster)) return true;
                inWall = false;
                wallThickness = 0;
            } else if (dist >= range - 0.5) {
                Vec3 destination = new Vec3(checkPos.getX() + 0.5, checkPos.getY(), checkPos.getZ() + 0.5);
                return attemptTeleport(world, subject, destination, caster);
            }
        }

        return false;
    }

    protected boolean attemptTeleport(Level world, Entity toTeleport, Vec3 destination, Player caster) {
        Vec3 resolved = EntityUtil.findSpaceForTeleport(toTeleport, destination, false);
        if (resolved == null) return false;

        playSound(world, caster, 0, -1);
        if (caster.getVehicle() != null) caster.stopRiding();

        if (!world.isClientSide) {
            if (toTeleport instanceof ServerPlayer sp) sp.teleportTo(resolved.x, resolved.y, resolved.z);
            else toTeleport.setPos(resolved.x, resolved.y, resolved.z);
        }

        playSound(world, caster, 0, -1);
        return true;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.ADVANCED, Elements.SORCERY, SpellTypes.UTILITY, SpellAction.POINT, 35, 0, 40)
                .add(WALL_THICKNESS)
                .add(DefaultProperties.RANGE, 8F)
                .build();
    }
}
