package com.binaris.wizardry.content.spells.necromancy;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.content.spells.abstr.RaySpell;
import com.binaris.wizardry.registry.EBMobEffects;
import com.binaris.wizardry.registry.EBParticles;
import com.binaris.wizardry.registry.Elements;
import com.binaris.wizardry.registry.SpellTiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CurseOfSoulbinding extends RaySpell {
    public CurseOfSoulbinding() {
        this.soundValues(1, 1.1f, 0.2f);
    }

    @Override
    protected boolean onEntityHit(CastContext ctx, EntityHitResult entityHit, Vec3 origin) {
        if (entityHit.getEntity() instanceof LivingEntity livingTarget) {
            if (!ctx.world().isClientSide) {
                livingTarget.addEffect(new MobEffectInstance(EBMobEffects.CURSE_OF_SOULBINDING, 6000));
                if (ctx.caster() instanceof Player player) {
                    player.displayClientMessage(Component.literal("§d✦ Душа врага связана с вашей! ✦"), true);
                }
                net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) ctx.world();
                serverLevel.sendParticles(ParticleTypes.SOUL, livingTarget.getX(), livingTarget.getY() + 1.0, livingTarget.getZ(), 20, 0.4, 0.5, 0.4, 0.05);
                serverLevel.sendParticles(ParticleTypes.PORTAL, livingTarget.getX(), livingTarget.getY() + 1.0, livingTarget.getZ(), 25, 0.5, 0.5, 0.5, 0.1);
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean onMiss(CastContext ctx, Vec3 origin, Vec3 direction) {
        return true;
    }

    @Override
    protected boolean onBlockHit(CastContext ctx, BlockHitResult blockHit, Vec3 origin) {
        return false;
    }

    @Override
    protected void spawnParticle(CastContext ctx, double x, double y, double z, double vx, double vy, double vz) {
        ParticleBuilder.create(EBParticles.DARK_MAGIC).pos(x, y, z).color(0.4f, 0, 0).spawn(ctx.world());
        ParticleBuilder.create(EBParticles.DARK_MAGIC).pos(x, y, z).color(0.1f, 0, 0).spawn(ctx.world());
        ParticleBuilder.create(EBParticles.SPARKLE).pos(x, y, z).time(12 + ctx.world().random.nextInt(8)).color(1, 0.8f, 1).spawn(ctx.world());
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.ADVANCED, Elements.NECROMANCY, SpellTypes.ALTERATION, SpellAction.POINT, 35, 10, 100)
                .add(DefaultProperties.RANGE, 10F)
                .build();
    }
}
