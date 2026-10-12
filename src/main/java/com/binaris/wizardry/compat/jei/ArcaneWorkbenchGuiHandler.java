package com.binaris.wizardry.compat.jei;

import com.binaris.wizardry.client.gui.ArcaneWorkbenchScreen;
import com.binaris.wizardry.content.block.workbench.IWorkbenchItem;
import com.binaris.wizardry.content.menu.ArcaneWorkbenchMenu;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ArcaneWorkbenchGuiHandler implements IGuiContainerHandler<ArcaneWorkbenchScreen> {

    @Override
    public @NotNull List<Rect2i> getGuiExtraAreas(@NotNull ArcaneWorkbenchScreen screen) {
        Slot centreSlot = screen.getMenu().getSlot(ArcaneWorkbenchMenu.CENTRE_SLOT);
        if (centreSlot.hasItem() && centreSlot.getItem().getItem() instanceof IWorkbenchItem workbenchItem && workbenchItem.showTooltip(centreSlot.getItem())) {
            return List.of(new Rect2i(
                    screen.getGuiLeft() + ArcaneWorkbenchScreen.MAIN_GUI_WIDTH,
                    screen.getGuiTop(),
                    ArcaneWorkbenchScreen.TOOLTIP_WIDTH,
                    screen.getImageHeight()
            ));
        }
        return Collections.emptyList();
    }
}
