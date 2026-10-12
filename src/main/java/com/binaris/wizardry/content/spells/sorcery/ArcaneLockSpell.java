package com.binaris.wizardry.content.spells.sorcery;

import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ArcaneLockSpell extends RaySpell {
    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        if (!(ctx.caster() instanceof Player player)) return false;

        if (!ctx.world().isClientSide) {
            BlockPos pos = blockHit.getBlockPos();
            BlockEntity be = ctx.world().getBlockEntity(pos);
            if (be instanceof BaseContainerBlockEntity container) {
                net.minecraft.nbt.CompoundTag tag = container.saveCustomOnly(ctx.world().registryAccess());
                String currentLock = tag.getString("Lock");
                if (currentLock.isEmpty()) {
                    tag.putString("Lock", player.getScoreboardName());
                    container.loadCustomOnly(tag, ctx.world().registryAccess());
                    container.setChanged();
                    player.displayClientMessage(Component.translatable("spell.ebwizardry.arcane_lock.locked").withStyle(ChatFormatting.GOLD), true);
                } else {
                    tag.remove("Lock");
                    container.loadCustomOnly(tag, ctx.world().registryAccess());
                    container.setChanged();
                    player.displayClientMessage(Component.translatable("spell.ebwizardry.arcane_lock.unlocked").withStyle(ChatFormatting.GREEN), true);
                }
                this.playSound(ctx.world(), player, ctx.castingTicks(), -1);
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
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        return false;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.SORCERY, SpellTypes.UTILITY, SpellAction.POINT, 20, 0, 40)
                .add(DefaultProperties.RANGE, 8F)
                .build();
    }
}
