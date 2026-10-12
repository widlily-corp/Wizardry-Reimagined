package com.binaris.wizardry.content.spells.necromancy;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.PlayerCastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class BlockWithSurprise extends Spell {

    @Override
    public boolean cast(PlayerCastContext ctx) {
        HitResult hit = ctx.caster().pick(10.0, 1.0f, false);
        BlockPos targetPos;
        if (hit.getType() == HitResult.Type.BLOCK) {
            targetPos = ((BlockHitResult) hit).getBlockPos().above();
        } else {
            targetPos = ctx.caster().blockPosition().relative(ctx.caster().getDirection(), 2);
        }

        if (ctx.world().isClientSide) {
            ctx.world().addParticle(ParticleTypes.EXPLOSION_EMITTER,
                    targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5, 0, 0, 0);
            ParticleBuilder.create(EBParticles.SPARKLE)
                    .pos(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5)
                    .count(25)
                    .spread(0.8, 0.8, 0.8)
                    .spawn(ctx.world());
        } else {
            ctx.world().playSound(null, targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5,
                    SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1.0f, 1.2f);

            // Spawn a surprise magical treat
            ItemStack[] surpriseGifts = new ItemStack[]{
                    new ItemStack(Items.COOKIE, 3),
                    new ItemStack(Items.GOLD_NUGGET, 2),
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(Items.SWEET_BERRIES, 4),
                    new ItemStack(Items.FIREWORK_ROCKET, 1)
            };
            ItemStack gift = surpriseGifts[ctx.world().random.nextInt(surpriseGifts.length)].copy();
            ItemEntity entity = new ItemEntity(ctx.world(),
                    targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5, gift);
            entity.setDeltaMovement(0, 0.35, 0);
            ctx.world().addFreshEntity(entity);

            ctx.caster().sendSystemMessage(Component.translatable("spell.ebwizardry.block_surprise.success"));
        }
        return true;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.APPRENTICE, Elements.SORCERY, SpellTypes.UTILITY, SpellAction.POINT, 20, 0, 40)
                .add(DefaultProperties.RANGE, 10.0f)
                .build();
    }
}
