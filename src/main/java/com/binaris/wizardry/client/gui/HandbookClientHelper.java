package com.binaris.wizardry.client.gui;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Client-only caller to open the handbook GUI, preventing dedicated server crashes.
 */
@OnlyIn(Dist.CLIENT)
public final class HandbookClientHelper {

    public static void openHandbook() {
        Minecraft.getInstance().setScreen(new WizardHandbookScreen());
    }

    private HandbookClientHelper() {}
}
