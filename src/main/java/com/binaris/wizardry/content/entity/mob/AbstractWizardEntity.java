package com.binaris.wizardry.content.entity.mob;

import com.binaris.wizardry.registry.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWizardEntity extends PathfinderMob {
    private static final EntityDataAccessor<Integer> HEAL_COOLDOWN =
            SynchedEntityData.defineId(AbstractWizardEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> ELEMENT =
            SynchedEntityData.defineId(AbstractWizardEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> TEXTURE_INDEX =
            SynchedEntityData.defineId(AbstractWizardEntity.class, EntityDataSerializers.INT);

    protected final List<Spell> spells = new ArrayList<>();

    public AbstractWizardEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RangedKitingGoal(this, 0.6D));
        this.goalSelector.addGoal(3, new HardLookAtTargetGoal(this, 10.0F, 10.0F));
        this.goalSelector.addGoal(4, new MobAttackSpellGoal(this, 16.0F, 30));
        this.goalSelector.addGoal(5, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(6, new MoveTowardsRestrictionGoal(this, 0.6D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HEAL_COOLDOWN, 50);
        builder.define(ELEMENT, "");
        builder.define(TEXTURE_INDEX, 0);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        handleSelfHealing();
    }

    private void handleSelfHealing() {
        int healCooldown = getHealCooldown();
        if (healCooldown <= 0) {
            if (getHealth() < getMaxHealth() && getHealth() > 0) {
                float healAmount = (getElement() == Elements.HEALING.get()) ? 8.0F : 4.0F;
                this.heal(healAmount);
                setHealCooldown(200);

                if (level().isClientSide) {
                    for (int i = 0; i < 8; i++) {
                        this.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                                this.getRandomX(0.8D), this.getRandomY(), this.getRandomZ(0.8D),
                                0.0D, 0.1D, 0.0D);
                    }
                } else {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            EBSounds.SPELL_HEAL.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
        } else {
            setHealCooldown(healCooldown - 1);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
                                        @NotNull MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnData) {
        this.setTextureIndex(this.random.nextInt(6));

        if (this.entityData.get(ELEMENT).isEmpty()) {
            this.setElement(chooseElement());
        }

        equipWizardGear();
        populateSpells();

        this.setHealCooldown(50);
        return super.finalizeSpawn(level, difficulty, mobSpawnType, spawnData);
    }

    private Element chooseElement() {
        if (this.random.nextBoolean()) {
            List<Element> pool = List.of(
                    Elements.EARTH.get(),
                    Elements.FIRE.get(),
                    Elements.HEALING.get(),
                    Elements.ICE.get(),
                    Elements.LIGHTNING.get(),
                    Elements.NECROMANCY.get(),
                    Elements.SORCERY.get()
            );
            return pool.get(this.random.nextInt(pool.size()));
        }
        return Elements.MAGIC.get();
    }

    public void equipWizardGear() {
        Element el = getElement();
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            Item armor = EBArmorItems.getArmorPiece(el, slot);
            this.setItemSlot(slot, new ItemStack(armor));
            this.setDropChance(slot, 0.0F);
        }

        Item wand = EBWandItems.getWand(SpellTiers.NOVICE.get(), el);
        if (wand != null) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(wand));
            this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        }
    }

    public void populateSpells() {
        this.spells.clear();
        Element el = getElement();

        this.spells.add(EBSpells.MAGIC_MISSILE.get());

        if (el == Elements.FIRE.get()) {
            this.spells.add(EBSpells.IGNITE.get());
            this.spells.add(EBSpells.FIREBALL.get());
            this.spells.add(EBSpells.FIREBOMB.get());
        } else if (el == Elements.ICE.get()) {
            this.spells.add(EBSpells.FREEZE.get());
            this.spells.add(EBSpells.ICE_SHARD.get());
            this.spells.add(EBSpells.BLIZZARD.get());
        } else if (el == Elements.LIGHTNING.get()) {
            this.spells.add(EBSpells.SPARK.get());
            this.spells.add(EBSpells.THUNDERBOLT.get());
            this.spells.add(EBSpells.ARC.get());
        } else if (el == Elements.EARTH.get()) {
            this.spells.add(EBSpells.DART.get());
            this.spells.add(EBSpells.SNARE.get());
            this.spells.add(EBSpells.POISON_BOMB.get());
        } else if (el == Elements.NECROMANCY.get()) {
            this.spells.add(EBSpells.SUMMON_ZOMBIE.get());
            this.spells.add(EBSpells.LIFE_DRAIN.get());
            this.spells.add(EBSpells.DECAY.get());
        } else if (el == Elements.SORCERY.get()) {
            this.spells.add(EBSpells.TELEKINESIS.get());
            this.spells.add(EBSpells.BLINK.get());
            this.spells.add(EBSpells.FORCEFIELD.get());
        } else if (el == Elements.HEALING.get()) {
            this.spells.add(EBSpells.HEAL.get());
            this.spells.add(EBSpells.CURE_EFFECTS.get());
            this.spells.add(EBSpells.GREATER_HEAL.get());
        } else {
            this.spells.add(EBSpells.DETONATE.get());
            this.spells.add(EBSpells.LIGHT.get());
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        if (this.hasCustomName() || this.getElement() == null) {
            return super.getDisplayName();
        }
        return this.getElement().getWizardName();
    }

    public @Nullable Element getElement() {
        String elKey = this.entityData.get(ELEMENT);
        if (elKey.isEmpty()) return Elements.MAGIC.get();
        ResourceLocation loc = ResourceLocation.tryParse(elKey);
        if (loc != null) {
            Element el = EBRegistries.ELEMENT_REGISTRY.get(loc);
            if (el != null) return el;
        }
        return Elements.MAGIC.get();
    }

    public void setElement(Element element) {
        this.entityData.set(ELEMENT, element.getLocation().toString());
    }

    public int getTextureIndex() {
        return this.entityData.get(TEXTURE_INDEX);
    }

    public void setTextureIndex(int index) {
        this.entityData.set(TEXTURE_INDEX, index);
    }

    public int getHealCooldown() {
        return this.entityData.get(HEAL_COOLDOWN);
    }

    public void setHealCooldown(int cooldown) {
        this.entityData.set(HEAL_COOLDOWN, cooldown);
    }

    public List<Spell> getSpells() {
        return this.spells;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        Element el = getElement();
        if (el != null) {
            tag.putString("Element", el.getLocation().toString());
        }
        tag.putInt("Skin", getTextureIndex());
        tag.putInt("HealCooldown", getHealCooldown());

        ListTag spellsTag = new ListTag();
        for (Spell s : this.spells) {
            spellsTag.add(StringTag.valueOf(s.getLocation().toString()));
        }
        tag.put("Spells", spellsTag);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Element")) {
            ResourceLocation loc = ResourceLocation.tryParse(tag.getString("Element"));
            if (loc != null) {
                Element el = EBRegistries.ELEMENT_REGISTRY.get(loc);
                if (el != null) setElement(el);
            }
        }
        if (tag.contains("Skin")) {
            setTextureIndex(tag.getInt("Skin"));
        }
        if (tag.contains("HealCooldown")) {
            setHealCooldown(tag.getInt("HealCooldown"));
        }

        if (tag.contains("Spells", Tag.TAG_LIST)) {
            this.spells.clear();
            ListTag list = tag.getList("Spells", Tag.TAG_STRING);
            for (int i = 0; i < list.size(); i++) {
                ResourceLocation spellLoc = ResourceLocation.tryParse(list.getString(i));
                if (spellLoc != null) {
                    Spell spell = EBRegistries.SPELL_REGISTRY.get(spellLoc);
                    if (spell != null) this.spells.add(spell);
                }
            }
        }
    }
}
