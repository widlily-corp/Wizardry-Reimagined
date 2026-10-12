package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.effect.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, WizardryMod.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> ARCANE_JAMMER =
            MOB_EFFECTS.register("arcane_jammer", () -> new MagicMobEffect(MobEffectCategory.HARMFUL, 0x5500aa));

    public static final DeferredHolder<MobEffect, MobEffect> FROST =
            MOB_EFFECTS.register("frost", () -> {
                MagicMobEffect effect = new MagicMobEffect(MobEffectCategory.HARMFUL, 0x66ccff) {
                    @Override
                    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
                        entity.setTicksFrozen(Math.min(entity.getTicksRequiredToFreeze() * 3, entity.getTicksFrozen() + 30 + amplifier * 20));
                        if (entity.tickCount % 6 == 0) {
                            level.sendParticles(ParticleTypes.SNOWFLAKE, entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 3, 0.3, 0.3, 0.3, 0.01);
                        }
                        return true;
                    }
                };
                effect.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                        ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "effect.frost"),
                        -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                return effect;
            });

    public static final DeferredHolder<MobEffect, MobEffect> PARALYSIS =
            MOB_EFFECTS.register("paralysis", () -> {
                MagicMobEffect effect = new MagicMobEffect(MobEffectCategory.HARMFUL, 0xffff66);
                effect.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                        ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "effect.paralysis"),
                        -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                return effect;
            });

    public static final DeferredHolder<MobEffect, MobEffect> STATIC_AURA =
            MOB_EFFECTS.register("static_aura", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0x00ffff) {
                @Override
                public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
                    if (entity.tickCount % 5 == 0) {
                        for (int i = 0; i < 2; i++) {
                            double px = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * entity.getBbWidth() * 1.6;
                            double py = entity.getY() + entity.getRandom().nextDouble() * entity.getBbHeight();
                            double pz = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * entity.getBbWidth() * 1.6;
                            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 1, 0, 0.05, 0, 0.05);
                        }
                    }

                    if (entity.tickCount % 20 == 0) {
                        level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(3.5))
                                .forEach(target -> {
                                    if (target != entity && target.isAlive()) {
                                        target.hurt(level.damageSources().magic(), 2.5f + amplifier * 1.5f);
                                        level.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 8, 0.3, 0.3, 0.3, 0.1);
                                        level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 0.6f, 1.8f);
                                    }
                                });
                    }
                    return true;
                }
            });

    public static final DeferredHolder<MobEffect, MobEffect> WARD =
            MOB_EFFECTS.register("ward", () -> {
                MagicMobEffect effect = new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0xccffcc);
                effect.addAttributeModifier(Attributes.ARMOR,
                        ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "effect.ward"),
                        6.0, AttributeModifier.Operation.ADD_VALUE);
                return effect;
            });

    public static final DeferredHolder<MobEffect, MobEffect> FIRESKIN =
            MOB_EFFECTS.register("fireskin", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0xff6600) {
                @Override
                public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
                    if (entity.isOnFire()) entity.clearFire();
                    return true;
                }
            });

    public static final DeferredHolder<MobEffect, MobEffect> OAKFLESH =
            MOB_EFFECTS.register("oakflesh", () -> {
                MagicMobEffect effect = new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0x996633);
                effect.addAttributeModifier(Attributes.ARMOR,
                        ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "effect.oakflesh"),
                        4.0, AttributeModifier.Operation.ADD_VALUE);
                return effect;
            });

    public static final DeferredHolder<MobEffect, MobEffect> CURSE_OF_ENFEEBLEMENT =
            MOB_EFFECTS.register("curse_of_enfeeblement", () -> {
                MagicMobEffect effect = new MagicMobEffect(MobEffectCategory.HARMFUL, 0x442255);
                effect.addAttributeModifier(Attributes.ATTACK_DAMAGE,
                        ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "effect.curse_of_enfeeblement"),
                        -0.4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                return effect;
            });

    public static final DeferredHolder<MobEffect, MobEffect> CURSE_OF_UNDEATH =
            MOB_EFFECTS.register("curse_of_undeath", () -> new MagicMobEffect(MobEffectCategory.HARMFUL, 0x224422));

    public static final DeferredHolder<MobEffect, MobEffect> DECAY =
            MOB_EFFECTS.register("decay", () -> new MagicMobEffect(MobEffectCategory.HARMFUL, 0x331133) {
                @Override
                public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
                    entity.hurt(level.damageSources().wither(), 1.0f + amplifier);
                    return true;
                }
                @Override
                public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
                    return duration % 25 == 0;
                }
            });

    public static final DeferredHolder<MobEffect, MobEffect> CONTAINMENT =
            MOB_EFFECTS.register("containment", () -> new MagicMobEffect(MobEffectCategory.HARMFUL, 0xaa00aa));

    public static final DeferredHolder<MobEffect, MobEffect> FONT_OF_MANA =
            MOB_EFFECTS.register("font_of_mana", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0x3399ff));

    public static final DeferredHolder<MobEffect, MobEffect> CURSE_OF_SOULBINDING =
            MOB_EFFECTS.register("curse_of_soulbinding", () -> new MagicMobEffect(MobEffectCategory.HARMFUL, 0x0f000f));

    public static final DeferredHolder<MobEffect, MobEffect> FROST_STEP =
            MOB_EFFECTS.register("frost_step", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0x99ffff) {
                @Override
                public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
                    if (entity.isSpectator()) return true;
                    int radius = Math.min(16, 2 + amplifier);
                    BlockPos playerPos = entity.blockPosition();
                    BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
                    boolean frozeAny = false;
                    for (int x = -radius; x <= radius; x++) {
                        for (int z = -radius; z <= radius; z++) {
                            if (x * x + z * z <= radius * radius) {
                                mpos.set(playerPos.getX() + x, playerPos.getY() - 1, playerPos.getZ() + z);
                                BlockState state = level.getBlockState(mpos);
                                if (state.getBlock() == Blocks.WATER && state.getFluidState().isSource()) {
                                    level.setBlockAndUpdate(mpos, Blocks.FROSTED_ICE.defaultBlockState());
                                    level.scheduleTick(mpos, Blocks.FROSTED_ICE, level.random.nextInt(60) + 60);
                                    frozeAny = true;
                                }
                            }
                        }
                    }
                    if (frozeAny || entity.tickCount % 4 == 0) {
                        level.sendParticles(ParticleTypes.SNOWFLAKE, entity.getX(), entity.getY() + 0.1, entity.getZ(), 4, 0.3, 0.05, 0.3, 0.01);
                    }
                    return true;
                }
            });

    public static final DeferredHolder<MobEffect, MobEffect> FLIGHT =
            MOB_EFFECTS.register("flight", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0x55ffff) {
                @Override
                public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
                    if (entity instanceof Player player) {
                        if (!player.getAbilities().mayfly) {
                            player.getAbilities().mayfly = true;
                            player.onUpdateAbilities();
                        }
                        player.fallDistance = 0.0f;
                        if (player.level().random.nextInt(3) == 0) {
                            level.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY() + 0.1, player.getZ(), 1, 0.2, 0.0, 0.2, 0.02);
                        }
                    }
                    return true;
                }
            });

    public static void register(IEventBus modBus) {
        MOB_EFFECTS.register(modBus);
    }

    private EBMobEffects() {}
}
