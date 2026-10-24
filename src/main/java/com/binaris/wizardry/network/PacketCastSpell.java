package com.binaris.wizardry.network;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.wand.WandItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketCastSpell(InteractionHand hand) implements CustomPacketPayload {
    public static final Type<PacketCastSpell> TYPE = new Type<>(WizardryMod.id("cast_spell"));

    public static final StreamCodec<ByteBuf, PacketCastSpell> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            p -> p.hand() == InteractionHand.MAIN_HAND,
            isMain -> new PacketCastSpell(isMain ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND)
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketCastSpell payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null || player.isRemoved() || player.level() == null) return;
            ItemStack stack = player.getItemInHand(payload.hand());
            if (stack.getItem() instanceof WandItem wand) {
                wand.castCurrentSpell(player.level(), player, payload.hand(), stack);
            }
        });
    }
}
