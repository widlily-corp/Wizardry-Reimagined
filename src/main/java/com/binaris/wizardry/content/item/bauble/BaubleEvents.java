package com.binaris.wizardry.content.item.bauble;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.registry.EBBaubleItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = WizardryMod.MODID)
public final class BaubleEvents {

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        // Amulet of Warding - 10% magic damage reduction
        if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO) || event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)) {
            if (BaubleItem.hasBauble(player, EBBaubleItems.AMULET_WARDING.get())) {
                event.setAmount(event.getAmount() * 0.90F);
            }
        }

        // Amulet of Fire Protection - 30% fire damage reduction
        if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
            if (BaubleItem.hasBauble(player, EBBaubleItems.AMULET_FIRE_PROTECTION.get())) {
                event.setAmount(event.getAmount() * 0.70F);
            }
        }

        // Amulet of Ice Immunity / Protection & Ring of Ice Protection
        if (event.getSource().is(DamageTypeTags.IS_FREEZING)) {
            if (BaubleItem.hasBauble(player, EBBaubleItems.AMULET_ICE_IMMUNITY.get())) {
                event.setCanceled(true);
                return;
            }
            if (BaubleItem.hasBauble(player, EBBaubleItems.AMULET_ICE_PROTECTION.get()) ||
                BaubleItem.hasBauble(player, EBBaubleItems.RING_ICE_PROTECTION.get())) {
                event.setAmount(event.getAmount() * 0.70F);
            }
        }

        // Curse of Soulbinding - transfer 50% damage to bound targets nearby
        if (!player.level().isClientSide) {
            var boundTargets = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(16.0),
                    e -> e != player && e.hasEffect(com.binaris.wizardry.registry.EBMobEffects.CURSE_OF_SOULBINDING));
            if (!boundTargets.isEmpty()) {
                float sharedDamage = event.getAmount() * 0.5F;
                event.setAmount(event.getAmount() * 0.5F);
                for (LivingEntity bound : boundTargets) {
                    bound.hurt(player.damageSources().magic(), sharedDamage);
                    ((net.minecraft.server.level.ServerLevel) player.level()).sendParticles(
                            net.minecraft.core.particles.ParticleTypes.SOUL,
                            bound.getX(), bound.getY() + bound.getBbHeight() / 2, bound.getZ(),
                            6, 0.2, 0.2, 0.2, 0.05);
                }
            }
        }

        // Retaliation effects when hit by living attacker
        if (event.getSource().getEntity() instanceof LivingEntity attacker && attacker != player) {
            // Static Aura retaliation shock
            if (player.hasEffect(com.binaris.wizardry.registry.EBMobEffects.STATIC_AURA)) {
                attacker.hurt(player.damageSources().magic(), 3.5F);
                if (!player.level().isClientSide) {
                    ((net.minecraft.server.level.ServerLevel) player.level()).sendParticles(
                            net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK,
                            attacker.getX(), attacker.getY() + attacker.getBbHeight() / 2, attacker.getZ(),
                            10, 0.3, 0.3, 0.3, 0.1);
                    if (com.binaris.wizardry.registry.EBSounds.SPELL_STATIC_AURA_RETALIATE != null && com.binaris.wizardry.registry.EBSounds.SPELL_STATIC_AURA_RETALIATE.get() != null) {
                        player.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                                com.binaris.wizardry.registry.EBSounds.SPELL_STATIC_AURA_RETALIATE.get(), net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }

            // Amulet of Potential - 15% chance to strike attacker with lightning
            if (BaubleItem.hasBauble(player, EBBaubleItems.AMULET_POTENTIAL.get()) && player.getRandom().nextFloat() < 0.15F) {
                if (!player.level().isClientSide) {
                    LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(player.level());
                    if (bolt != null) {
                        bolt.moveTo(attacker.position());
                        player.level().addFreshEntity(bolt);
                    }
                }
            }

            // Amulet of Banishing - 15% chance to teleport melee attacker away
            if (BaubleItem.hasBauble(player, EBBaubleItems.AMULET_BANISHING.get()) && player.getRandom().nextFloat() < 0.15F) {
                if (!player.level().isClientSide) {
                    double offsetX = (player.getRandom().nextDouble() - 0.5D) * 16.0D;
                    double offsetZ = (player.getRandom().nextDouble() - 0.5D) * 16.0D;
                    attacker.teleportTo(attacker.getX() + offsetX, attacker.getY(), attacker.getZ() + offsetZ);
                }
            }
        }

        // Amulet of Transience - triggers when critically wounded
        if (BaubleItem.hasBauble(player, EBBaubleItems.AMULET_TRANSIENCE.get()) && (player.getHealth() - event.getAmount()) <= 4.0F) {
            if (!player.getCooldowns().isOnCooldown(EBBaubleItems.AMULET_TRANSIENCE.get())) {
                player.getCooldowns().addCooldown(EBBaubleItems.AMULET_TRANSIENCE.get(), 1200);
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 2));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1));
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
            }
        }
    }

    @SubscribeEvent
    public static void onDamageDealt(LivingDamageEvent.Post event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        LivingEntity target = event.getEntity();

        // Ring of Leeching - 30% chance to restore health on attack
        if (BaubleItem.hasBauble(player, EBBaubleItems.RING_LEECHING.get()) && player.getRandom().nextFloat() < 0.30F) {
            player.heal(2.0F);
        }

        // Ring of Poison - poisons target
        if (BaubleItem.hasBauble(player, EBBaubleItems.RING_POISON.get())) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
        }

        // Ring of Combustion - ignites target
        if (BaubleItem.hasBauble(player, EBBaubleItems.RING_COMBUSTION.get())) {
            target.igniteForSeconds(4.0F);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(net.neoforged.neoforge.event.tick.PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (!player.isCreative() && !player.isSpectator()) {
            if (player.getAbilities().mayfly && !player.hasEffect(com.binaris.wizardry.registry.EBMobEffects.FLIGHT)) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0));
            }
        }
    }

    private BaubleEvents() {}
}
