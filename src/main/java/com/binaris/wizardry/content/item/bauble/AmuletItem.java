package com.binaris.wizardry.content.item.bauble;

import net.minecraft.world.item.Rarity;

public class AmuletItem extends BaubleItem {
    public AmuletItem(Rarity rarity, Properties properties) {
        super(BaubleType.AMULET, rarity, properties);
    }

    public AmuletItem(Rarity rarity) {
        this(rarity, new Properties());
    }
}
