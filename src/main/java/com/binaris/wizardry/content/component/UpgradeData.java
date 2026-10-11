package com.binaris.wizardry.content.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public record UpgradeData(Map<String, Integer> upgrades) {
    public static final UpgradeData EMPTY = new UpgradeData(Map.of());

    public static final Codec<UpgradeData> CODEC = Codec.unboundedMap(Codec.STRING, Codec.INT)
            .xmap(UpgradeData::new, UpgradeData::upgrades);

    public static final StreamCodec<ByteBuf, UpgradeData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public int getUpgradeLevel(String upgradeId) {
        return upgrades.getOrDefault(upgradeId, 0);
    }

    public int getTotalUpgrades() {
        return upgrades.values().stream().mapToInt(Integer::intValue).sum();
    }

    public UpgradeData withUpgrade(String upgradeId, int level) {
        Map<String, Integer> map = new HashMap<>(upgrades);
        if (level <= 0) {
            map.remove(upgradeId);
        } else {
            map.put(upgradeId, level);
        }
        return new UpgradeData(Collections.unmodifiableMap(map));
    }
}
