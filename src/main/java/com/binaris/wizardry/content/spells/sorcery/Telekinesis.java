package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Telekinesis extends RaySpell {

    public Telekinesis() {
        this.aimAssist(0.4f);
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        Entity target = entityHit.getEntity();

        if (ctx.caster() instanceof Player && target instanceof Player playerTarget) {
            if (!ctx.world().isClientSide()) {
                ItemEntity item = playerTarget.spawnAtLocation(playerTarget.getMainHandItem(), 0);
                if (item != null) {
                    item.setDeltaMovement((origin.x - playerTarget.getX()) / 20, item.getDeltaMovement().y, (origin.z - playerTarget.getZ()) / 20);
                }
            }
            playerTarget.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            return true;
        }

        if (!(target instanceof ItemEntity item)) return false;

        if (!ctx.world().isClientSide() && ctx.caster() instanceof Player caster) {
            Vec3 toCaster = origin.subtract(item.position());
            double distSq = toCaster.lengthSqr();

            if (distSq < 4.0) {
                ItemStack stack = item.getItem();
                if (caster.getInventory().add(stack)) {
                    item.discard();
                } else {
                    item.setItem(stack);
                }
                return true;
            }

            double speed = 0.5;
            Vec3 motion = toCaster.normalize().scale(speed);
            item.setDeltaMovement(motion.x, motion.y, motion.z);
            return true;
        }

        return true;
    }

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
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
                .add(DefaultProperties.RANGE, 12F)
                .build();
    }
}
