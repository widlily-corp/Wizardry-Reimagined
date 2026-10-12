package com.binaris.wizardry.content.spells.necromancy;

import com.binaris.wizardry.api.client.ParticleBuilder;
import com.binaris.wizardry.api.content.spell.SpellAction;
import com.binaris.wizardry.api.content.spell.SpellTypes;
import com.binaris.wizardry.api.content.spell.internal.CastContext;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

public class Ojospocos extends Spell {

    @Override
    public boolean cast(PlayerCastContext ctx) {
        Level world = ctx.world();
        if (world.isClientSide) {
            ParticleBuilder.create(EBParticles.SPARKLE)
                    .pos(ctx.caster().position().add(0, 1, 0))
                    .color(0.5f, 0.1f, 0.8f)
                    .count(20)
                    .spread(1.0, 1.0, 1.0)
                    .spawn(world);
            return true;
        }

        // Find floor space with at least 3 blocks of vertical clearance for Enderman
        BlockPos casterPos = ctx.caster().blockPosition();
        BlockPos spawnPos = null;
        for (int tries = 0; tries < 25; tries++) {
            int dx = world.random.nextInt(7) - 3;
            int dz = world.random.nextInt(7) - 3;
            for (int dy = 3; dy >= -3; dy--) {
                BlockPos check = casterPos.offset(dx, dy, dz);
                if (world.getBlockState(check).blocksMotion()
                        && world.getBlockState(check.above()).canBeReplaced()
                        && world.getBlockState(check.above(2)).canBeReplaced()
                        && world.getBlockState(check.above(3)).canBeReplaced()) {
                    spawnPos = check.above();
                    break;
                }
            }
            if (spawnPos != null) break;
        }

        if (spawnPos == null) {
            spawnPos = casterPos.relative(ctx.caster().getDirection(), 2);
        }

        EnderMan enderman = new EnderMan(EntityType.ENDERMAN, world);
        enderman.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
        enderman.setCustomName(Component.translatable("entity.ebwizardry.minion_name",
                ctx.caster().getDisplayName(), Component.literal("Enderman")));
        enderman.getPersistentData().putInt("ebwizardry:lifetime", 800);
        enderman.getPersistentData().putUUID("ebwizardry:owner", ctx.caster().getUUID());

        // Loyal minion targeting: attack hostile monsters near caster
        enderman.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(enderman, Monster.class, false,
                (target) -> target != enderman && !(target instanceof EnderMan)));

        enderman.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(spawnPos),
                MobSpawnType.MOB_SUMMONED, null);
        world.addFreshEntity(enderman);

        // Sound and particles
        world.playSound(null, spawnPos.getX() + 0.5, spawnPos.getY() + 1.0, spawnPos.getZ() + 0.5,
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.2f, 0.9f);

        for (int i = 0; i < 30; i++) {
            world.addParticle(ParticleTypes.PORTAL,
                    spawnPos.getX() + 0.5 + (world.random.nextDouble() - 0.5) * 1.5,
                    spawnPos.getY() + world.random.nextDouble() * 3.0,
                    spawnPos.getZ() + 0.5 + (world.random.nextDouble() - 0.5) * 1.5,
                    (world.random.nextDouble() - 0.5) * 0.5,
                    (world.random.nextDouble() - 0.5) * 0.5,
                    (world.random.nextDouble() - 0.5) * 0.5);
        }

        return true;
    }

    @Override
    protected @NotNull SpellProperties properties() {
        return SpellProperties.builder()
                .assignBaseProperties(SpellTiers.MASTER, Elements.NECROMANCY, SpellTypes.MINION, SpellAction.SUMMON, 80, 20, 300)
                .add(DefaultProperties.MINION_COUNT, 1)
                .add(DefaultProperties.MINION_LIFETIME, 800)
                .add(DefaultProperties.SUMMON_RADIUS, 3)
                .build();
    }
}
