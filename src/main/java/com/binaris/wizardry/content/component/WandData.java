package com.binaris.wizardry.content.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record WandData(
        List<ResourceLocation> spells,
        int selectedSpell,
        int progression,
        List<Long> cooldownEndTimes,
        List<Integer> maxCooldowns
) {
    public static final WandData EMPTY = new WandData(List.of(), 0, 0, List.of(), List.of());

    public static final Codec<WandData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.listOf().optionalFieldOf("spells", List.of()).forGetter(WandData::spells),
            Codec.INT.optionalFieldOf("selected_spell", 0).forGetter(WandData::selectedSpell),
            Codec.INT.optionalFieldOf("progression", 0).forGetter(WandData::progression),
            Codec.LONG.listOf().optionalFieldOf("cooldown_end_times", List.of()).forGetter(WandData::cooldownEndTimes),
            Codec.INT.listOf().optionalFieldOf("max_cooldowns", List.of()).forGetter(WandData::maxCooldowns)
    ).apply(instance, WandData::new));

    public static final StreamCodec<ByteBuf, WandData> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), WandData::spells,
            ByteBufCodecs.VAR_INT, WandData::selectedSpell,
            ByteBufCodecs.VAR_INT, WandData::progression,
            ByteBufCodecs.VAR_LONG.apply(ByteBufCodecs.list()), WandData::cooldownEndTimes,
            ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list()), WandData::maxCooldowns,
            WandData::new
    );

    public ResourceLocation getSelectedSpellId() {
        if (spells.isEmpty() || selectedSpell < 0 || selectedSpell >= spells.size()) {
            return ResourceLocation.fromNamespaceAndPath("ebwizardry", "none");
        }
        return spells.get(selectedSpell);
    }

    public WandData withSpells(List<ResourceLocation> newSpells) {
        return new WandData(List.copyOf(newSpells), selectedSpell, progression, cooldownEndTimes, maxCooldowns);
    }

    public WandData withSelectedSpell(int newSelectedSpell) {
        return new WandData(spells, newSelectedSpell, progression, cooldownEndTimes, maxCooldowns);
    }

    public WandData withProgression(int newProgression) {
        return new WandData(spells, selectedSpell, newProgression, cooldownEndTimes, maxCooldowns);
    }

    public WandData withCooldownEndTimes(List<Long> newCooldownEndTimes) {
        return new WandData(spells, selectedSpell, progression, List.copyOf(newCooldownEndTimes), maxCooldowns);
    }

    public WandData withMaxCooldowns(List<Integer> newMaxCooldowns) {
        return new WandData(spells, selectedSpell, progression, cooldownEndTimes, List.copyOf(newMaxCooldowns));
    }
}
