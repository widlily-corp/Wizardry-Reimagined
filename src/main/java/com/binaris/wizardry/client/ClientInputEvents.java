package com.binaris.wizardry.client;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.wand.WandItem;
import com.binaris.wizardry.network.EBNetwork;
import com.binaris.wizardry.network.PacketSelectSpell;
import com.binaris.wizardry.registry.EBSounds;
import com.binaris.wizardry.registry.Spell;
import com.binaris.wizardry.registry.Spells;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public final class ClientInputEvents {

    public static final KeyMapping KEY_NEXT_SPELL = new KeyMapping(
            "key.ebwizardry.next_spell",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.categories.ebwizardry"
    );

    public static final KeyMapping KEY_PREVIOUS_SPELL = new KeyMapping(
            "key.ebwizardry.previous_spell",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "key.categories.ebwizardry"
    );

    @EventBusSubscriber(modid = WizardryMod.MODID, value = Dist.CLIENT)
    public static final class ModBusEvents {
        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(KEY_NEXT_SPELL);
            event.register(KEY_PREVIOUS_SPELL);
        }
    }

    @EventBusSubscriber(modid = WizardryMod.MODID, value = Dist.CLIENT)
    public static final class GameBusEvents {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            while (KEY_NEXT_SPELL.consumeClick()) {
                cycleSpell(mc.player, true);
            }
            while (KEY_PREVIOUS_SPELL.consumeClick()) {
                cycleSpell(mc.player, false);
            }
        }

        @SubscribeEvent
        public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.screen == null && mc.player.isShiftKeyDown()) {
                ItemStack held = mc.player.getMainHandItem();
                if (!(held.getItem() instanceof WandItem)) {
                    held = mc.player.getOffhandItem();
                }
                if (held.getItem() instanceof WandItem) {
                    double scrollDelta = event.getScrollDeltaY();
                    if (scrollDelta != 0) {
                        cycleSpell(mc.player, scrollDelta < 0);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    public static void cycleSpell(Player player, boolean next) {
        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof WandItem)) {
            held = player.getOffhandItem();
        }
        if (held.getItem() instanceof WandItem wand) {
            if (next) {
                wand.selectNextSpell(held);
                EBNetwork.sendToServer(new PacketSelectSpell(PacketSelectSpell.NEXT_SPELL));
            } else {
                wand.selectPreviousSpell(held);
                EBNetwork.sendToServer(new PacketSelectSpell(PacketSelectSpell.PREVIOUS_SPELL));
            }
            player.playSound(EBSounds.ITEM_WAND_SWITCH_SPELL.get(), 0.8f, 1.0f);
            Spell newSpell = wand.getCurrentSpell(held);
            if (newSpell != Spells.NONE.get()) {
                player.displayClientMessage(newSpell.getDescriptionFormatted(), true);
            }
        }
    }

    private ClientInputEvents() {}
}
