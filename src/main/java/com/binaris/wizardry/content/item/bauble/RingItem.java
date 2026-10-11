package com.binaris.wizardry.content.item.bauble;

import net.minecraft.world.item.Rarity;

public class RingItem extends BaubleItem {
    public RingItem(Rarity rarity, Properties properties) {
        super(BaubleType.RING, rarity, properties);
    }

    public RingItem(Rarity rarity) {
        this(rarity, new Properties());
    }
}
