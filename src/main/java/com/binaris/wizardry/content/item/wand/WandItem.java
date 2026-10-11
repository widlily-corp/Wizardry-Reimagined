package com.binaris.wizardry.content.item.wand;

import com.binaris.wizardry.content.block.workbench.IWorkbenchItem;
import com.binaris.wizardry.content.block.workbench.WorkbenchUtils;
import com.binaris.wizardry.content.component.UpgradeData;
import com.binaris.wizardry.content.component.WandData;
import com.binaris.wizardry.registry.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WandItem extends Item implements ICastItem, IManaItem, IWorkbenchItem {
    public static final int BASE_SPELL_SLOTS = 5;
    public static final int MAX_USE_DURATION = 72000;
    public static final int CONDENSER_TICK_INTERVAL = 50;
    public static final float STORAGE_INCREASE_PER_LEVEL = 0.15f;
    public static final float COOLDOWN_REDUCTION_PER_LEVEL = 0.15f;
    public static final int NON_ELEMENTAL_UPGRADE_BONUS = 3;
    public static final int UPGRADE_STACK_LIMIT = 3;

    private final java.util.function.Supplier<SpellTier> tierSupplier;
    private final java.util.function.Supplier<Element> elementSupplier;
    private final int maxCharge;

    public WandItem(java.util.function.Supplier<SpellTier> tierSupplier, java.util.function.Supplier<Element> elementSupplier, int maxCharge) {
        super(new Properties().stacksTo(1).durability(maxCharge));
        this.tierSupplier = tierSupplier;
        this.elementSupplier = elementSupplier;
        this.maxCharge = maxCharge;
    }

    public WandItem(SpellTier tier, Element element) {
        this(() -> tier, () -> element, tier != null ? tier.getMaxCharge() : 700);
    }

    public SpellTier getTier() {
        return tierSupplier != null ? tierSupplier.get() : SpellTiers.NOVICE.get();
    }

    public Element getElement() {
        if (elementSupplier != null) {
            Element elem = elementSupplier.get();
            if (elem != null) return elem;
        }
        return Elements.MAGIC.get();
    }

    // ==========================================
    // Component Helpers: WandData & UpgradeData
    // ==========================================

    public WandData getWandData(ItemStack stack) {
        return stack.getOrDefault(EBDataComponents.WAND_DATA.get(), WandData.EMPTY);
    }

    public void setWandData(ItemStack stack, WandData data) {
        stack.set(EBDataComponents.WAND_DATA.get(), data);
    }

    public UpgradeData getUpgradeData(ItemStack stack) {
        return stack.getOrDefault(EBDataComponents.UPGRADE_DATA.get(), UpgradeData.EMPTY);
    }

    public void setUpgradeData(ItemStack stack, UpgradeData data) {
        stack.set(EBDataComponents.UPGRADE_DATA.get(), data);
    }

    public int getUpgradeLevel(ItemStack stack, String upgradeId) {
        return getUpgradeData(stack).getUpgradeLevel(upgradeId);
    }

    public int getTotalUpgrades(ItemStack stack) {
        return getUpgradeData(stack).getTotalUpgrades();
    }

    // ==========================================
    // IManaItem Implementation
    // ==========================================

    @Override
    public int getManaCapacity(ItemStack stack) {
        int storageLevel = getUpgradeLevel(stack, WandUpgrades.STORAGE);
        return (int) (getTier().getMaxCharge() * (1.0f + STORAGE_INCREASE_PER_LEVEL * storageLevel) + 0.5f);
    }

    @Override
    public int getMana(ItemStack stack) {
        if (!stack.has(EBDataComponents.MANA.get())) {
            return getManaCapacity(stack);
        }
        return Math.min(stack.getOrDefault(EBDataComponents.MANA.get(), getManaCapacity(stack)), getManaCapacity(stack));
    }

    @Override
    public void setMana(ItemStack stack, int mana) {
        int clamped = Math.max(0, Math.min(mana, getManaCapacity(stack)));
        stack.set(EBDataComponents.MANA.get(), clamped);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return getMana(stack) < getManaCapacity(stack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        int capacity = getManaCapacity(stack);
        return capacity > 0 ? Math.round(13.0f * getMana(stack) / capacity) : 13;
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return 0xa811ce;
    }

    // ==========================================
    // ICastItem Implementation
    // ==========================================

    @Override
    public int getSpellSlotCount(ItemStack stack) {
        return BASE_SPELL_SLOTS + getUpgradeLevel(stack, WandUpgrades.ATTUNEMENT);
    }

    @Override
    public Spell[] getSpells(ItemStack stack) {
        WandData data = getWandData(stack);
        int slotCount = getSpellSlotCount(stack);
        List<ResourceLocation> spellIds = data.spells();
        Spell[] spells = new Spell[slotCount];

        for (int i = 0; i < slotCount; i++) {
            if (i < spellIds.size() && spellIds.get(i) != null) {
                Spell spell = EBRegistries.SPELL_REGISTRY.get(spellIds.get(i));
                spells[i] = spell != null ? spell : Spells.NONE.get();
            } else {
                spells[i] = Spells.NONE.get();
            }
        }
        return spells;
    }

    @Override
    public Spell getCurrentSpell(ItemStack stack) {
        WandData data = getWandData(stack);
        ResourceLocation id = data.getSelectedSpellId();
        Spell spell = EBRegistries.SPELL_REGISTRY.get(id);
        return spell != null ? spell : Spells.NONE.get();
    }

    @Override
    public boolean selectSpell(ItemStack stack, int index) {
        if (index >= 0 && index < getSpellSlotCount(stack)) {
            setWandData(stack, getWandData(stack).withSelectedSpell(index));
            return true;
        }
        return false;
    }

    @Override
    public void selectNextSpell(ItemStack stack) {
        WandData data = getWandData(stack);
        int total = getSpellSlotCount(stack);
        if (total <= 0) return;
        int next = (data.selectedSpell() + 1) % total;
        setWandData(stack, data.withSelectedSpell(next));
    }

    @Override
    public void selectPreviousSpell(ItemStack stack) {
        WandData data = getWandData(stack);
        int total = getSpellSlotCount(stack);
        if (total <= 0) return;
        int prev = (data.selectedSpell() - 1 + total) % total;
        setWandData(stack, data.withSelectedSpell(prev));
    }

    @Override
    public int getCurrentCooldown(ItemStack stack, Level level) {
        WandData data = getWandData(stack);
        int selected = data.selectedSpell();
        if (selected >= 0 && selected < data.cooldownEndTimes().size()) {
            long endTime = data.cooldownEndTimes().get(selected);
            if (endTime > level.getGameTime()) {
                return (int) (endTime - level.getGameTime());
            }
        }
        return 0;
    }

    @Override
    public int getCurrentMaxCooldown(ItemStack stack) {
        WandData data = getWandData(stack);
        int selected = data.selectedSpell();
        if (selected >= 0 && selected < data.maxCooldowns().size()) {
            return data.maxCooldowns().get(selected);
        }
        return 0;
    }

    public void setCurrentCooldown(ItemStack stack, int cooldown, long gameTime) {
        WandData data = getWandData(stack);
        int selected = data.selectedSpell();
        int slotCount = getSpellSlotCount(stack);

        List<Long> endTimes = new ArrayList<>(data.cooldownEndTimes());
        while (endTimes.size() < slotCount) endTimes.add(0L);
        if (selected < endTimes.size()) {
            endTimes.set(selected, gameTime + Math.max(1, cooldown));
        }

        List<Integer> maxCds = new ArrayList<>(data.maxCooldowns());
        while (maxCds.size() < slotCount) maxCds.add(0);
        if (selected < maxCds.size()) {
            maxCds.set(selected, Math.max(1, cooldown));
        }

        setWandData(stack, data.withCooldownEndTimes(endTimes).withMaxCooldowns(maxCds));
    }

    // ==========================================
    // Progression & Foil Glint
    // ==========================================

    public int getProgression(ItemStack stack) {
        return getWandData(stack).progression();
    }

    public void addProgression(ItemStack stack, int amount) {
        WandData data = getWandData(stack);
        setWandData(stack, data.withProgression(data.progression() + amount));
    }

    public void checkLevelUp(Player player, ItemStack stack, int progression) {
        if (getTier() == SpellTiers.MASTER.get()) return;
        SpellTier nextTier = getTier().next();
        int excess = getProgression(stack) - nextTier.getProgression();

        if (excess >= 0 && excess < progression) {
            player.playSound(EBSounds.ITEM_WAND_LEVELUP.get(), 1.25f, 1.0f);
            if (!player.level().isClientSide) {
                player.sendSystemMessage(Component.translatable(
                        "item.ebwizardry.wand.levelup",
                        getName(stack),
                        nextTier.getDescriptionFormatted()
                ));
            }
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        if (getTier() == SpellTiers.MASTER.get()) return false;
        return getProgression(stack) >= getTier().next().getProgression();
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        Element elem = getElement();
        if (elem != null) {
            return Component.literal(super.getName(stack).getString()).withStyle(elem.getColor());
        }
        return super.getName(stack);
    }

    // ==========================================
    // Spell Casting Logic
    // ==========================================

    public boolean canCastRequirements(ItemStack stack, Spell spell, Player player, Level level) {
        if (spell == null || spell == Spells.NONE.get()) return false;
        if (spell.getTier().getLevel() > getTier().getLevel()) return false;
        if (!player.isCreative() && spell.getCost() > getMana(stack)) return false;
        return player.isCreative() || getCurrentCooldown(stack, level) == 0;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Spell spell = getCurrentSpell(stack);
        if (spell == Spells.NONE.get()) return InteractionResultHolder.pass(stack);

        if (!canCastRequirements(stack, spell, player, level)) {
            return InteractionResultHolder.fail(stack);
        }

        if (spell.getChargeup() > 0) {
            player.startUsingItem(hand);
            if (level.isClientSide) {
                player.playSound(EBSounds.ITEM_WAND_CHARGEUP.get(), 1.0f, 1.0f);
            }
            return InteractionResultHolder.success(stack);
        }

        castInstant(level, player, hand, stack, spell);
        return InteractionResultHolder.success(stack);
    }

    public void castCurrentSpell(Level level, Player player, InteractionHand hand, ItemStack stack) {
        Spell spell = getCurrentSpell(stack);
        if (!canCastRequirements(stack, spell, player, level)) return;

        if (spell.getChargeup() > 0) {
            player.startUsingItem(hand);
        } else {
            castInstant(level, player, hand, stack, spell);
        }
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity livingEntity, @NotNull ItemStack stack, int timeLeft) {
        if (!(livingEntity instanceof Player player)) return;

        Spell spell = getCurrentSpell(stack);
        if (spell == Spells.NONE.get()) {
            player.stopUsingItem();
            return;
        }

        int useTick = getUseDuration(stack, livingEntity) - timeLeft;
        if (useTick >= spell.getChargeup()) {
            if (canCastRequirements(stack, spell, player, level)) {
                castInstant(level, player, player.getUsedItemHand(), stack, spell);
            }
            player.stopUsingItem();
        }
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return MAX_USE_DURATION;
    }

    protected void castInstant(Level level, Player player, InteractionHand hand, ItemStack stack, Spell spell) {
        consumeMana(stack, spell.getCost(), player);

        int cd = spell.getCooldown();
        int cdLevel = getUpgradeLevel(stack, WandUpgrades.COOLDOWN);
        cd = (int) (cd * Math.max(0.25f, 1.0f - COOLDOWN_REDUCTION_PER_LEVEL * cdLevel));
        if (!player.isCreative()) {
            setCurrentCooldown(stack, cd, level.getGameTime());
        }

        if (getTier().getLevel() < SpellTiers.MASTER.get().getLevel()) {
            int prog = Math.max(1, spell.getCost() > 0 ? spell.getCost() / 10 : 5);
            addProgression(stack, prog);
            checkLevelUp(player, stack, prog);
        }

        spell.cast(level, player);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean isHeldInMainHand) {
        if (!world.isClientSide && !isManaFull(stack) && world.getGameTime() % CONDENSER_TICK_INTERVAL == 0) {
            int condenserLevel = getUpgradeLevel(stack, WandUpgrades.CONDENSER);
            if (condenserLevel > 0) {
                rechargeMana(stack, condenserLevel);
            }
        }
    }

    // ==========================================
    // IWorkbenchItem Implementation
    // ==========================================

    @Override
    public boolean isClearable() {
        return true;
    }

    @Override
    public boolean showTooltip(ItemStack stack) {
        return true;
    }

    @Override
    public boolean onApplyButtonPressed(Player player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
        boolean changed = false;
        if (upgrade.hasItem()) {
            changed = applyUpgradeSlot(player, centre, upgrade);
        }

        changed |= WorkbenchUtils.applySpellBooks(centre, spellBooks, getTier());
        changed |= WorkbenchUtils.rechargeManaFromCrystals(centre, crystals);
        return changed;
    }

    @Override
    public void onClearButtonPressed(Player player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
        ItemStack wand = centre.getItem();
        int slotCount = getSpellSlotCount(wand);
        List<ResourceLocation> emptySpells = new ArrayList<>(Collections.nCopies(slotCount, Spells.NONE.get().getLocation()));
        setWandData(wand, getWandData(wand).withSpells(emptySpells).withSelectedSpell(0));
        centre.setChanged();
    }

    protected boolean applyUpgradeSlot(Player player, Slot centre, Slot upgrade) {
        ItemStack original = centre.getItem().copy();
        ItemStack upgraded = applyUpgrade(player, centre.getItem(), upgrade.getItem());
        if (!ItemStack.isSameItemSameComponents(centre.getItem(), upgraded)) {
            centre.set(upgraded);
            centre.setChanged();
            return true;
        }
        return false;
    }

    @Override
    public ItemStack applyUpgrade(@Nullable Player player, ItemStack wand, ItemStack upgrade) {
        if (WandUpgrades.isTome(upgrade)) {
            return applyTierUpgrade(player, wand, upgrade);
        } else if (WandUpgrades.isWandUpgrade(upgrade)) {
            applySpecialUpgrade(player, wand, upgrade);
        }
        return wand;
    }

    protected ItemStack applyTierUpgrade(@Nullable Player player, ItemStack wand, ItemStack tomeStack) {
        if (getTier() == SpellTiers.MASTER.get()) return wand;

        SpellTier nextTier = WandUpgrades.getTomeTier(tomeStack);
        if (nextTier == null || nextTier == getTier() || nextTier.getLevel() <= getTier().getLevel()) {
            return wand;
        }

        if (player == null || getProgression(wand) >= nextTier.getProgression()) {
            int newProgression = Math.max(0, getProgression(wand) - nextTier.getProgression());
            Item newWandItem = EBWandItems.getWand(nextTier, getElement());
            if (newWandItem != null) {
                ItemStack newWand = new ItemStack(newWandItem);
                newWand.applyComponents(wand.getComponents());
                ((WandItem) newWand.getItem()).setWandData(newWand, getWandData(wand).withProgression(newProgression));
                ((WandItem) newWand.getItem()).setMana(newWand, getMana(wand));
                tomeStack.shrink(1);
                return newWand;
            }
        }
        return wand;
    }

    protected void applySpecialUpgrade(@Nullable Player player, ItemStack wand, ItemStack upgradeStack) {
        String upgradeId = WandUpgrades.getUpgradeId(upgradeStack);
        if (upgradeId == null) return;

        int maxUpgrades = getTier().getUpgradeLimit() + (getElement() == Elements.MAGIC.get() ? NON_ELEMENTAL_UPGRADE_BONUS : 0);
        int currentTotal = getTotalUpgrades(wand);
        int currentLevel = getUpgradeLevel(wand, upgradeId);

        if (currentTotal >= maxUpgrades || currentLevel >= UPGRADE_STACK_LIMIT) {
            return;
        }

        int prevMana = getMana(wand);
        setUpgradeData(wand, getUpgradeData(wand).withUpgrade(upgradeId, currentLevel + 1));

        if (WandUpgrades.STORAGE.equals(upgradeId)) {
            setMana(wand, prevMana);
        } else if (WandUpgrades.ATTUNEMENT.equals(upgradeId)) {
            expandSpellSlots(wand);
        }

        upgradeStack.shrink(1);
    }

    protected void expandSpellSlots(ItemStack wand) {
        int newSlotCount = BASE_SPELL_SLOTS + getUpgradeLevel(wand, WandUpgrades.ATTUNEMENT);
        WandData data = getWandData(wand);
        List<ResourceLocation> spells = new ArrayList<>(data.spells());

        while (spells.size() < newSlotCount) {
            spells.add(Spells.NONE.get().getLocation());
        }
        setWandData(wand, data.withSpells(spells));
    }
}
