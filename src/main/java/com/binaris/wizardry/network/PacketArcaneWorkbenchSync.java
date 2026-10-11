package com.binaris.wizardry.network;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.menu.ArcaneWorkbenchMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketArcaneWorkbenchSync(int action) implements CustomPacketPayload {
    public static final Type<PacketArcaneWorkbenchSync> TYPE = new Type<>(WizardryMod.id("arcane_workbench_sync"));

    public static final int ACTION_APPLY = 0;
    public static final int ACTION_CLEAR = 1;

    public static final StreamCodec<ByteBuf, PacketArcaneWorkbenchSync> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            PacketArcaneWorkbenchSync::action,
            PacketArcaneWorkbenchSync::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketArcaneWorkbenchSync payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player.containerMenu instanceof ArcaneWorkbenchMenu menu) {
                if (payload.action() == ACTION_APPLY) {
                    menu.onApplyButtonPressed(player);
                } else if (payload.action() == ACTION_CLEAR) {
                    menu.onClearButtonPressed(player);
                }
            }
        });
    }
}
