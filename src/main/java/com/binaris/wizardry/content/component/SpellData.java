package com.binaris.wizardry.content.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record SpellData(ResourceLocation spellId) {
    public static final ResourceLocation EMPTY_SPELL = ResourceLocation.fromNamespaceAndPath("ebwizardry", "none");
    public static final SpellData EMPTY = new SpellData(EMPTY_SPELL);

    public static final Codec<SpellData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("spell", EMPTY_SPELL).forGetter(SpellData::spellId)
    ).apply(instance, SpellData::new));

    public static final StreamCodec<ByteBuf, SpellData> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, SpellData::spellId,
            SpellData::new
    );

    public boolean isEmpty() {
        return EMPTY_SPELL.equals(spellId);
    }
}
