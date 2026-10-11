package com.binaris.wizardry.network;

import com.binaris.wizardry.WizardryMod;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class EBNetwork {
    public static void register(IEventBus modBus) {
        modBus.addListener(RegisterPayloadHandlersEvent.class, EBNetwork::onRegisterPayloads);
    }

    private static void onRegisterPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(WizardryMod.MODID).versioned("1.0.0");

        registrar.playToServer(
                PacketCastSpell.TYPE,
                PacketCastSpell.STREAM_CODEC,
                PacketCastSpell::handle
        );

        registrar.playToServer(
                PacketSelectSpell.TYPE,
                PacketSelectSpell.STREAM_CODEC,
                PacketSelectSpell::handle
        );

        registrar.playToServer(
                PacketArcaneWorkbenchSync.TYPE,
                PacketArcaneWorkbenchSync.STREAM_CODEC,
                PacketArcaneWorkbenchSync::handle
        );
    }

    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    private EBNetwork() {}
}
