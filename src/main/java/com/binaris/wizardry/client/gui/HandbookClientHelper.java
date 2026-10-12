package com.binaris.wizardry.client.gui;

import net.minecraft.client.Minecraft;

/**
 * Client-only caller to open the handbook GUI, preventing dedicated server crashes.
 */
public final class HandbookClientHelper {

    public static void openHandbook() {
        Minecraft.getInstance().setScreen(new WizardHandbookScreen());
    }

    private HandbookClientHelper() {}
}
