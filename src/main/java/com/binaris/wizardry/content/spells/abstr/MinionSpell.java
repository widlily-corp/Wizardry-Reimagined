package com.binaris.wizardry.content.spells.abstr;

import com.binaris.wizardry.api.content.spell.internal.*;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.util.BlockUtil;
import com.binaris.wizardry.content.spells.DefaultProperties;
import com.binaris.wizardry.registry.Spell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class MinionSpell<T extends Mob> extends Spell {
    protected final Function<Level, T> minionFactory;
    protected boolean flying = false;
    protected boolean shouldFollowOwner = true;
    protected boolean shouldDeleteBaseGoals = false;
    private boolean searchNearbyTargets = true;

    public MinionSpell(Function<Level, T> minionFactory) {
        this.minionFactory = minionFactory;
    }

    public MinionSpell<T> setShouldFollowOwner(boolean shouldFollowOwner) {
        this.shouldFollowOwner = shouldFollowOwner;
        return this;
    }

    public MinionSpell<T> setShouldDeleteGoals(boolean shouldDeleteGoals) {
        this.shouldDeleteBaseGoals = shouldDeleteGoals;
        return this;
    }

    public MinionSpell<T> setSearchNearbyTargets(boolean searchNearbyTargets) {
        this.searchNearbyTargets = searchNearbyTargets;
        return this;
    }

    public MinionSpell<T> flying(boolean flying) {
        this.flying = flying;
        return this;
    }

    @Override
    public boolean canCastByLocation() {
        return true;
    }

    @Override
    public boolean canCastByEntity() {
        return true;
    }

    @Override
    public boolean cast(PlayerCastContext ctx) {
        if (!extraConditions(ctx, BlockPos.containing(ctx.caster().xo, ctx.caster().yo, ctx.caster().zo))) {
            return false;
        }
        if (!this.spawnMinions(ctx)) return false;
        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean cast(EntityCastContext ctx) {
        if (!extraConditions(ctx, BlockPos.containing(ctx.caster().xo, ctx.caster().yo, ctx.caster().zo))) {
            return false;
        }
        if (!this.spawnMinions(ctx)) return false;
        this.playSound(ctx.world(), ctx.caster(), ctx.castingTicks(), -1);
        return true;
    }

    @Override
    public boolean cast(LocationCastContext ctx) {
        if (!extraConditions(ctx, ctx.pos())) return false;

        if (!ctx.world().isClientSide) {
            int count = hasProperty(DefaultProperties.MINION_COUNT) ? property(DefaultProperties.MINION_COUNT) : 1;
            for (int i = 0; i < count; i++) {
                T minion = minionFactory.apply(ctx.world());
                minion.setPos(ctx.pos().getX() + 0.5, ctx.pos().getY(), ctx.pos().getZ() + 0.5);
                setLifetime(minion, (int) (ctx.modifiers().get(SpellModifiers.DURATION, (float) (hasProperty(DefaultProperties.MINION_LIFETIME) ? property(DefaultProperties.MINION_LIFETIME) : 600))));
                this.addMinionExtras(minion, ctx, i);
                ctx.world().addFreshEntity(minion);
            }
        }

        this.playSoundAt(ctx.world(), ctx.x() - ctx.direction().getStepX(), ctx.y() - ctx.direction().getStepY(), ctx.z() - ctx.direction().getStepZ(), ctx.castingTicks(), (float) ctx.duration());
        return true;
    }

    protected boolean spawnMinions(CastContext ctx) {
        if (ctx.world().isClientSide) return true;

        int count = hasProperty(DefaultProperties.MINION_COUNT) ? property(DefaultProperties.MINION_COUNT) : 1;
        int range = hasProperty(DefaultProperties.SUMMON_RADIUS) ? property(DefaultProperties.SUMMON_RADIUS) : 4;

        for (int i = 0; i < count; i++) {
            BlockPos pos = BlockUtil.findNearbyFloorSpace(ctx.caster(), range, range * 2);

            if (flying) {
                if (pos != null) {
                    pos = pos.atY(pos.getY() + 2);
                } else {
                    pos = BlockPos.containing(ctx.caster().position().relative(Direction.NORTH, ctx.world().random.nextInt(range * 2) - range)
                            .relative(Direction.EAST, ctx.world().random.nextInt(range * 2) - range));
                }
            } else {
                if (pos == null) return false;
            }

            T minion = createMinion(ctx.world(), ctx.caster(), ctx.modifiers());
            minion.setCustomName(Component.translatable("entity.ebwizardry.minion_name", ctx.caster().getDisplayName(), minion.getDisplayName()));

            if (minion instanceof TamableAnimal tamable && ctx.caster() instanceof Player player) {
                tamable.tame(player);
            }

            minion.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            setLifetime(minion, (int) (ctx.modifiers().get(SpellModifiers.DURATION, (float) (hasProperty(DefaultProperties.MINION_LIFETIME) ? property(DefaultProperties.MINION_LIFETIME) : 600))));

            if (minion.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
                minion.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath("ebwizardry", "potency"),
                                ctx.modifiers().getFactor(SpellModifiers.POTENCY) - 1.0f,
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
            if (minion.getAttribute(Attributes.MAX_HEALTH) != null) {
                minion.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath("ebwizardry", "health"),
                                ctx.modifiers().getFactor(SpellModifiers.HEALTH_MODIFIER) - 1.0f,
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }

            minion.setHealth(minion.getMaxHealth());
            minion.finalizeSpawn((ServerLevelAccessor) ctx.world(), ctx.world().getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null);
            this.addMinionExtras(minion, ctx, i);
            ctx.world().addFreshEntity(minion);
        }
        return true;
    }

    protected void setLifetime(T minion, int lifetime) {
        minion.getPersistentData().putInt("ebwizardry:lifetime", lifetime);
    }

    protected T createMinion(Level world, @Nullable LivingEntity caster, SpellModifiers modifiers) {
        return minionFactory.apply(world);
    }

    protected void addMinionExtras(T minion, CastContext ctx, int alreadySpawned) {
    }

    protected boolean extraConditions(CastContext ctx, BlockPos containing) {
        return true;
    }

    @Override
    public boolean requiresPacket() {
        return false;
    }
}
