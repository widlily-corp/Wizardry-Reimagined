package com.binaris.wizardry.content.entity.mob;

import com.binaris.wizardry.content.item.scroll.SpellBookItem;
import com.binaris.wizardry.registry.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WizardEntity extends AbstractWizardEntity implements Npc, Merchant {
    private static final int[] XP_PER_LEVEL = new int[]{0, 10, 70, 150, 250};
    private static final int MAX_LEVEL = 5;

    private MerchantOffers trades;
    private @Nullable Player customer;
    private int wizardXp;
    private int wizardLevel = 1;

    public WizardEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new WizardTradeGoal(this));
        this.goalSelector.addGoal(1, new WizardLookAtTradePlayerGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(WizardEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false,
                target -> target instanceof Enemy && !(target instanceof WizardEntity)));
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (this.isAlive() && !this.isTrading() && !this.isBaby() && !player.isShiftKeyDown() && this.getTarget() != player) {
            if (!this.level().isClientSide) {
                this.setTradingPlayer(player);
                this.openTradingScreen(player, this.getDisplayName(), this.wizardLevel);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.customer != null && !(this.customer.containerMenu instanceof MerchantMenu)) {
            this.customer = null;
        }
    }

    @Override
    public void notifyTradeUpdated(@NotNull ItemStack stack) {
        if (!this.level().isClientSide) {
            SoundEvent sound = stack.isEmpty() ? EBSounds.ENTITY_WIZARD_NO.get() : EBSounds.ENTITY_WIZARD_YES.get();
            this.playSound(sound, this.getSoundVolume(), this.getVoicePitch());
        }
    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        offer.increaseUses();
        this.playSound(EBSounds.ENTITY_WIZARD_YES.get(), this.getSoundVolume(), this.getVoicePitch());
        this.wizardXp += offer.getXp();
        tryLevelUp();
    }

    private void tryLevelUp() {
        int nextLevel = Mth.clamp(this.wizardLevel + 1, 1, MAX_LEVEL);
        if (nextLevel > this.wizardLevel && this.wizardXp >= XP_PER_LEVEL[nextLevel - 1]) {
            this.wizardLevel = nextLevel;
            this.updateTrades();
        }
    }

    @Override
    public @NotNull MerchantOffers getOffers() {
        if (this.trades == null) {
            this.trades = new MerchantOffers();
            this.updateTrades();
        }
        return this.trades;
    }

    private void updateTrades() {
        if (this.trades == null) this.trades = new MerchantOffers();

        switch (this.wizardLevel) {
            case 1 -> addNoviceTrades();
            case 2 -> addApprenticeTrades();
            case 3 -> addAdvancedTrades();
            case 4 -> addMasterTrades(false);
            case 5 -> addMasterTrades(true);
        }
    }

    private void addNoviceTrades() {
        // Sell blank spell book for 4 magic crystals
        trades.add(new MerchantOffer(
                new ItemCost(EBCraftingItems.SPELL_BOOK.get(), 1),
                Optional.empty(),
                new ItemStack(EBItems.MAGIC_CRYSTAL.get(), 4),
                12, 2, 0.05F
        ));

        // 3-4 gold + magic crystal -> elemental crystal of wizard's element
        Element el = getElement();
        Item elCrystal = (el != null) ? getCrystalForElement(el) : EBItems.MAGIC_CRYSTAL.get();
        trades.add(new MerchantOffer(
                new ItemCost(Items.GOLD_INGOT, 3),
                Optional.of(new ItemCost(EBItems.MAGIC_CRYSTAL.get(), 1)),
                new ItemStack(elCrystal, 1),
                12, 5, 0.05F
        ));

        // 8 gold + 4 crystals -> Novice wand
        Item wand = EBWandItems.getWand(SpellTiers.NOVICE.get(), el);
        if (wand != null) {
            trades.add(new MerchantOffer(
                    new ItemCost(Items.GOLD_INGOT, 8),
                    Optional.of(new ItemCost(EBItems.MAGIC_CRYSTAL.get(), 4)),
                    new ItemStack(wand),
                    3, 10, 0.2F
            ));
        }

        // Novice Spell Book
        ItemStack missileBook = SpellBookItem.createWithSpell(EBCraftingItems.SPELL_BOOK.get(), EBSpells.MAGIC_MISSILE.get());
        trades.add(new MerchantOffer(
                new ItemCost(Items.GOLD_INGOT, 2),
                Optional.of(new ItemCost(EBItems.MAGIC_CRYSTAL.get(), 2)),
                missileBook,
                7, 2, 0.2F
        ));
    }

    private void addApprenticeTrades() {
        Element el = getElement();
        Item wand = EBWandItems.getWand(SpellTiers.APPRENTICE.get(), el);
        if (wand != null) {
            trades.add(new MerchantOffer(
                    new ItemCost(Items.GOLD_INGOT, 14),
                    Optional.of(new ItemCost(EBItems.MAGIC_CRYSTAL.get(), 8)),
                    new ItemStack(wand),
                    3, 20, 0.2F
            ));
        }

        trades.add(new MerchantOffer(
                new ItemCost(Items.GOLD_INGOT, 10),
                Optional.of(new ItemCost(Items.BOOK, 1)),
                new ItemStack(EBCraftingItems.APPRENTICE_TOME.get()),
                3, 30, 0.2F
        ));

        trades.add(new MerchantOffer(
                new ItemCost(Items.GOLD_INGOT, 12),
                Optional.of(new ItemCost(Items.PAPER, 4)),
                new ItemStack(EBCraftingItems.STORAGE_UPGRADE.get()),
                6, 15, 0.2F
        ));
    }

    private void addAdvancedTrades() {
        Element el = getElement();
        Item wand = EBWandItems.getWand(SpellTiers.ADVANCED.get(), el);
        if (wand != null) {
            trades.add(new MerchantOffer(
                    new ItemCost(Items.GOLD_INGOT, 18),
                    Optional.of(new ItemCost(EBItems.MAGIC_CRYSTAL.get(), 12)),
                    new ItemStack(wand),
                    3, 25, 0.2F
            ));
        }

        trades.add(new MerchantOffer(
                new ItemCost(Items.GOLD_INGOT, 14),
                Optional.of(new ItemCost(Items.BOOK, 1)),
                new ItemStack(EBCraftingItems.ADVANCED_TOME.get()),
                3, 30, 0.2F
        ));

        Item armor = EBArmorItems.getArmorPiece(el, EquipmentSlot.HEAD);
        trades.add(new MerchantOffer(
                new ItemCost(Items.GOLD_INGOT, 12),
                Optional.of(new ItemCost(EBItems.MAGIC_CRYSTAL.get(), 8)),
                new ItemStack(armor),
                3, 20, 0.2F
        ));
    }

    private void addMasterTrades(boolean isMax) {
        Element el = getElement();
        Item wand = EBWandItems.getWand(SpellTiers.MASTER.get(), el);
        if (wand != null) {
            trades.add(new MerchantOffer(
                    new ItemCost(Items.GOLD_INGOT, 22),
                    Optional.of(new ItemCost(EBItems.MAGIC_CRYSTAL.get(), 16)),
                    new ItemStack(wand),
                    3, 40, 0.2F
            ));
        }

        if (isMax) {
            trades.add(new MerchantOffer(
                    new ItemCost(Items.GOLD_INGOT, 24),
                    Optional.of(new ItemCost(Items.DIAMOND, 1)),
                    new ItemStack(EBCraftingItems.MASTER_TOME.get()),
                    3, 50, 0.2F
            ));
        }
    }

    private Item getCrystalForElement(Element el) {
        if (el == Elements.EARTH.get()) return EBItems.MAGIC_CRYSTAL_EARTH.get();
        if (el == Elements.FIRE.get()) return EBItems.MAGIC_CRYSTAL_FIRE.get();
        if (el == Elements.HEALING.get()) return EBItems.MAGIC_CRYSTAL_HEALING.get();
        if (el == Elements.ICE.get()) return EBItems.MAGIC_CRYSTAL_ICE.get();
        if (el == Elements.LIGHTNING.get()) return EBItems.MAGIC_CRYSTAL_LIGHTNING.get();
        if (el == Elements.NECROMANCY.get()) return EBItems.MAGIC_CRYSTAL_NECROMANCY.get();
        if (el == Elements.SORCERY.get()) return EBItems.MAGIC_CRYSTAL_SORCERY.get();
        return EBItems.MAGIC_CRYSTAL.get();
    }

    public boolean isTrading() {
        return this.customer != null;
    }

    @Override
    public @Nullable Player getTradingPlayer() {
        return this.customer;
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.customer = player;
    }

    @Override
    public @NotNull SoundEvent getNotifyTradeSound() {
        return EBSounds.ENTITY_WIZARD_YES.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return isTrading() ? EBSounds.ENTITY_WIZARD_TRADING.get() : EBSounds.ENTITY_WIZARD_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return EBSounds.ENTITY_WIZARD_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return EBSounds.ENTITY_WIZARD_DEATH.get();
    }

    @Override
    public void overrideOffers(@NotNull MerchantOffers offers) {
        this.trades = offers;
    }

    @Override
    public int getVillagerXp() {
        return this.wizardXp;
    }

    @Override
    public void overrideXp(int xp) {
    }

    @Override
    public boolean showProgressBar() {
        return true;
    }

    @Override
    public boolean canRestock() {
        return true;
    }

    @Override
    public boolean isClientSide() {
        return level().isClientSide();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.trades != null) {
            MerchantOffers.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.trades)
                    .result()
                    .ifPresent(tradesTag -> tag.put("Trades", tradesTag));
        }
        tag.putInt("WizardXp", this.wizardXp);
        tag.putInt("WizardLevel", this.wizardLevel);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Trades")) {
            MerchantOffers.CODEC.parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), tag.get("Trades"))
                    .result()
                    .ifPresent(offers -> this.trades = offers);
        }
        this.wizardXp = tag.getInt("WizardXp");
        this.wizardLevel = Math.max(1, tag.getInt("WizardLevel"));
    }
}
