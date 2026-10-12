package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.api.content.spell.internal.*;
import com.binaris.wizardry.api.content.spell.properties.SpellProperties;
import com.binaris.wizardry.api.content.spell.properties.SpellProperty;
import com.binaris.wizardry.content.spells.DefaultProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class Spell {
    protected float volume = 1.0f;
    protected float pitch = 1.0f;
    protected float pitchVariation = 0.0f;

    private Supplier<Element> elementSupplier;
    private Supplier<SpellTier> tierSupplier;
    private int cost;
    private int cooldown;
    private int chargeup;
    private ResourceLocation location;
    private String descriptionId;
    private ResourceLocation icon;
    private SpellProperties properties = SpellProperties.empty();

    public Spell() {
        this.properties = properties();
    }

    public Spell(Supplier<Element> element, Supplier<SpellTier> tier, int cost, int cooldown, int chargeup) {
        this();
        this.elementSupplier = element;
        this.tierSupplier = tier;
        this.cost = cost;
        this.cooldown = cooldown;
        this.chargeup = chargeup;
    }

    public Spell(Supplier<Element> element, Supplier<SpellTier> tier) {
        this(element, tier, 0, 0, 0);
    }

    protected @NotNull SpellProperties properties() {
        return SpellProperties.empty();
    }

    public Spell assignProperties(SpellProperties properties) {
        this.properties = properties != null ? properties : SpellProperties.empty();
        return this;
    }

    public SpellProperties getProperties() {
        return properties;
    }

    public <T> T property(SpellProperty<T> prop) {
        return properties.get(prop);
    }

    public <T> boolean hasProperty(SpellProperty<T> prop) {
        return properties.has(prop);
    }

    public Spell soundValues(float volume, float pitch, float pitchVariation) {
        this.volume = volume;
        this.pitch = pitch;
        this.pitchVariation = pitchVariation;
        return this;
    }

    public Element getElement() {
        if (elementSupplier != null) return elementSupplier.get();
        if (hasProperty(DefaultProperties.ELEMENT)) {
            String elemName = property(DefaultProperties.ELEMENT);
            Element elem = Elements.fromName(elemName);
            if (elem != null) return elem;
        }
        return Elements.MAGIC.get();
    }

    public SpellTier getTier() {
        if (tierSupplier != null) return tierSupplier.get();
        if (hasProperty(DefaultProperties.TIER)) {
            String tierName = property(DefaultProperties.TIER);
            SpellTier tier = SpellTiers.fromName(tierName);
            if (tier != null) return tier;
        }
        return SpellTiers.NOVICE.get();
    }

    public int getCost() {
        if (hasProperty(DefaultProperties.COST)) return property(DefaultProperties.COST);
        return cost;
    }

    public int getCooldown() {
        if (hasProperty(DefaultProperties.COOLDOWN)) return property(DefaultProperties.COOLDOWN);
        return cooldown;
    }

    public int getChargeup() {
        if (hasProperty(DefaultProperties.CHARGEUP)) return property(DefaultProperties.CHARGEUP);
        return chargeup;
    }

    // ==========================================
    // Core Cast Entry Points
    // ==========================================

    public boolean cast(PlayerCastContext ctx) {
        return cast(ctx.world(), (Player) ctx.caster());
    }

    public boolean cast(EntityCastContext ctx) {
        return cast(ctx.world(), ctx.caster());
    }

    public boolean cast(LocationCastContext ctx) {
        return false;
    }

    public void endCast(CastContext ctx) {}

    public boolean isInstantCast() {
        return true;
    }

    public boolean canCastByEntity() {
        return false;
    }

    public boolean canCastByLocation() {
        return false;
    }

    public boolean requiresPacket() {
        return true;
    }

    // Backward compatibility for existing code
    public boolean cast(Level level, Player player) {
        return cast(new PlayerCastContext(level, player, InteractionHand.MAIN_HAND, 0, new SpellModifiers()));
    }

    public boolean cast(Level level, LivingEntity caster) {
        return cast(new EntityCastContext(level, caster, null, 0, new SpellModifiers()));
    }

    // ==========================================
    // Audio & Visual Helpers
    // ==========================================

    public void playSound(Level level, LivingEntity entity, int castingTicks, float customVolume) {
        if (entity == null || level == null) return;
        SoundEvent sound = getSpellSound();
        if (sound != null) {
            float v = customVolume > 0 ? customVolume : volume;
            float p = pitch + (level.random.nextFloat() - 0.5f) * pitchVariation;
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.PLAYERS, v, p);
        }
    }

    public SoundEvent getSpellSound() {
        ResourceLocation loc = getLocation();
        String soundPath = "spell." + loc.getPath();
        var holder = EBSounds.SOUNDS.getEntries().stream()
                .filter(h -> h.getId().getPath().equals(soundPath))
                .findFirst()
                .orElse(null);
        return holder != null ? holder.get() : null;
    }

    // ==========================================
    // Metadata & Formatting
    // ==========================================

    public ResourceLocation getLocation() {
        if (location == null) {
            ResourceLocation key = EBRegistries.SPELL_REGISTRY.getKey(this);
            location = key != null ? key : ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "none");
        }
        return location;
    }

    public String getDescriptionId() {
        if (descriptionId == null) {
            descriptionId = "spell." + getLocation().getNamespace() + "." + getLocation().getPath();
        }
        return descriptionId;
    }

    public Component getDescriptionFormatted() {
        return Component.translatable(getDescriptionId()).withStyle(getElement().getColor());
    }

    public ResourceLocation getIcon() {
        if (icon == null) {
            icon = ResourceLocation.fromNamespaceAndPath(getLocation().getNamespace(), "textures/spells/" + getLocation().getPath() + ".png");
        }
        return icon;
    }

    public boolean is(ResourceLocation otherLocation) {
        return getLocation().equals(otherLocation);
    }

    @Override
    public String toString() {
        return getLocation().toString();
    }
}
