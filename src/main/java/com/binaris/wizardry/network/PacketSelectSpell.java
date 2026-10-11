package com.binaris.wizardry.network;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.wand.WandItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketSelectSpell(int slotIndex) implements CustomPacketPayload {
    public static final Type<PacketSelectSpell> TYPE = new Type<>(WizardryMod.id("select_spell"));

    public static final int NEXT_SPELL = -1;
    public static final int PREVIOUS_SPELL = -2;

    public static final StreamCodec<ByteBuf, PacketSelectSpell> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            PacketSelectSpell::slotIndex,
            PacketSelectSpell::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketSelectSpell payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ItemStack wand = player.getMainHandItem();
            if (!(wand.getItem() instanceof WandItem)) {
                wand = player.getOffhandItem();
            }

            if (wand.getItem() instanceof WandItem wandItem) {
                if (payload.slotIndex() == NEXT_SPELL) {
                    wandItem.selectNextSpell(wand);
                } else if (payload.slotIndex() == PREVIOUS_SPELL) {
                    wandItem.selectPreviousSpell(wand);
                } else {
                    wandItem.selectSpell(wand, payload.slotIndex());
                }
                player.stopUsingItem();
            }
        });
    }
}
