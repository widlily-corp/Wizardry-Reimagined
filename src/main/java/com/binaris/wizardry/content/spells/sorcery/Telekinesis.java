package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Telekinesis extends RaySpell {

    public Telekinesis() {
        this.aimAssist(0.6f);
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        boolean hit = super.cast(ctx);
        pullNearbyItems(ctx);
        return true;
    }

    private void pullNearbyItems(PlayerCastContext ctx) {
        if (ctx.caster() == null) return;
        Player caster = ctx.caster();
        Vec3 look = caster.getLookAngle();
        Vec3 start = caster.getEyePosition();
        double range = property(DefaultProperties.RANGE);

        AABB area = new AABB(start, start.add(look.scale(range))).inflate(4.0);
        List<Entity> pulls = new ArrayList<>(ctx.world().getEntitiesOfClass(Entity.class, area,
                e -> (e instanceof ItemEntity || e instanceof ExperienceOrb) && e.isAlive()));

        for (Entity e : pulls) {
            Vec3 toCaster = caster.position().add(0, 0.8, 0).subtract(e.position());
            double distSq = toCaster.lengthSqr();

            if (distSq < 3.5 && !ctx.world().isClientSide) {
                if (e instanceof ItemEntity item) {
                    ItemStack stack = item.getItem();
                    if (caster.getInventory().add(stack)) {
                        item.discard();
                    }
                } else if (e instanceof ExperienceOrb orb) {
                    orb.playerTouch(caster);
                }
            } else {
                Vec3 motion = toCaster.normalize().scale(0.85);
                e.setDeltaMovement(motion.x, motion.y, motion.z);
                e.hasImpulse = true;
            }

            if (ctx.world().isClientSide) {
                ParticleBuilder.create(EBParticles.SPARKLE)
                        .pos(e.position().add(0, 0.3, 0))
                        .color(0.8f, 0.4f, 1.0f)
                        .spawn(ctx.world());
            }
        }
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        Entity target = entityHit.getEntity();

        if (ctx.caster() instanceof Player caster) {
            if (target instanceof Player playerTarget) {
                if (!ctx.world().isClientSide()) {
                    ItemEntity item = playerTarget.spawnAtLocation(playerTarget.getMainHandItem(), 0);
                    if (item != null) {
                        item.setDeltaMovement((origin.x - playerTarget.getX()) / 20, item.getDeltaMovement().y, (origin.z - playerTarget.getZ()) / 20);
                    }
                    playerTarget.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                }
                return true;
            }

            if (target instanceof ItemEntity item) {
                if (!ctx.world().isClientSide()) {
                    Vec3 toCaster = caster.position().add(0, 0.8, 0).subtract(item.position());
                    if (toCaster.lengthSqr() < 3.5) {
                        if (caster.getInventory().add(item.getItem())) {
                            item.discard();
                        }
                    } else {
                        Vec3 motion = toCaster.normalize().scale(0.9);
                        item.setDeltaMovement(motion.x, motion.y, motion.z);
                    }
                }
                return true;
            }

            if (target instanceof LivingEntity living && living != caster) {
                if (!ctx.world().isClientSide()) {
                    Vec3 toCaster = caster.position().subtract(living.position()).normalize().scale(0.6);
                    living.setDeltaMovement(toCaster.x, 0.25, toCaster.z);
                    living.hasImpulse = true;
                }
                return true;
            }
        }

        return true;
    }

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        if (ctx.caster() instanceof Player player) {
            BlockPos pos = blockHit.getBlockPos();
            BlockState state = ctx.world().getBlockState(pos);

            InteractionResult result = state.useWithoutItem(ctx.world(), player, blockHit);
            if (result.consumesAction()) {
                if (ctx.world().isClientSide) {
                    ParticleBuilder.create(EBParticles.SPARKLE)
                            .pos(Vec3.atCenterOf(pos))
                            .count(8)
                            .color(0.8f, 0.4f, 1.0f)
                            .spawn(ctx.world());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.NOVICE, Elements.SORCERY, SpellTypes.UTILITY, SpellAction.POINT, 10, 0, 15)
                .add(DefaultProperties.RANGE, 14F)
                .build();
    }
}
