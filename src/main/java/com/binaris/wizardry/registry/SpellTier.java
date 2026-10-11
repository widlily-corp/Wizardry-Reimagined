package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public class SpellTier {
    private final String name;
    private final int maxCharge;
    private final int level;
    private final int upgradeLimit;
    private final int weight;
    private final int progression;
    private final ChatFormatting color;
    private final ResourceLocation location;
    private final String descriptionId;

    public SpellTier(String name, int maxCharge, int upgradeLimit, int weight, int level, ChatFormatting color, int progression) {
        this.name = name;
        this.maxCharge = maxCharge;
        this.level = level;
        this.upgradeLimit = upgradeLimit;
        this.weight = weight;
        this.color = color;
        this.progression = progression;
        this.location = ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, name);
        this.descriptionId = "tier." + WizardryMod.MODID + "." + name;
    }

    public static SpellTier getWeightedRandomTier(RandomSource random, List<SpellTier> tiers) {
        if (tiers.isEmpty()) {
            return SpellTiers.NOVICE.get();
        }
        int totalWeight = 0;
        for (SpellTier tier : tiers) totalWeight += tier.weight;
        if (totalWeight <= 0) return tiers.get(0);
        int randomiser = random.nextInt(totalWeight);
        int cumulativeWeight = 0;
        for (SpellTier tier : tiers) {
            cumulativeWeight += tier.weight;
            if (randomiser < cumulativeWeight) return tier;
        }
        return tiers.get(tiers.size() - 1);
    }

    public SpellTier next() {
        List<DeferredHolder<SpellTier, SpellTier>> tiers = SpellTiers.ALL;
        for (int i = 0; i < tiers.size(); i++) {
            if (tiers.get(i).get() == this && i < tiers.size() - 1) {
                return tiers.get(i + 1).get();
            }
        }
        return this;
    }

    public SpellTier previous() {
        List<DeferredHolder<SpellTier, SpellTier>> tiers = SpellTiers.ALL;
        for (int i = 0; i < tiers.size(); i++) {
            if (tiers.get(i).get() == this && i > 0) {
                return tiers.get(i - 1).get();
            }
        }
        return this;
    }

    public Component getDescriptionFormatted() {
        return Component.translatable(descriptionId).withStyle(this.color);
    }

    public String getDescriptionId() {
        return this.descriptionId;
    }

    public ResourceLocation getLocation() {
        return this.location;
    }

    public final boolean is(ResourceLocation location) {
        return this.location.equals(location);
    }

    public final boolean is(String location) {
        return this.location.toString().equals(location);
    }

    public final boolean is(SpellTier tier) {
        return this == tier;
    }

    public String getName() {
        return name;
    }

    public ChatFormatting getColor() {
        return color;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxCharge() {
        return maxCharge;
    }

    public int getUpgradeLimit() {
        return upgradeLimit;
    }

    public int getWeight() {
        return weight;
    }

    public int getProgression() {
        return progression;
    }

    @Override
    public String toString() {
        return location.toString();
    }
}
