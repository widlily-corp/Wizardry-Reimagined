package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.scroll.BlankScrollItem;
import com.binaris.wizardry.content.item.scroll.BlankSpellBookItem;
import com.binaris.wizardry.content.item.scroll.ScrollItem;
import com.binaris.wizardry.content.item.scroll.SpellBookItem;
import com.binaris.wizardry.content.item.template.ArcaneUpgradeSmithingTemplateItem;
import com.binaris.wizardry.content.item.upgrade.ArcaneTomeItem;
import com.binaris.wizardry.content.item.upgrade.WandUpgradeItem;
import com.binaris.wizardry.content.item.wand.WandUpgrades;
import com.binaris.wizardry.content.recipe.EBRecipeSerializers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EBCraftingItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WizardryMod.MODID);

    // Spells & Scrolls
    public static final DeferredItem<SpellBookItem> SPELL_BOOK =
            ITEMS.register("spell_book", () -> new SpellBookItem(new Item.Properties()));

    public static final DeferredItem<ScrollItem> SCROLL =
            ITEMS.register("scroll", () -> new ScrollItem(new Item.Properties()));

    // Blank items
    public static final DeferredItem<BlankScrollItem> BLANK_SCROLL =
            ITEMS.register("blank_scroll", () -> new BlankScrollItem(new Item.Properties()));

    public static final DeferredItem<BlankSpellBookItem> BLANK_SPELL_BOOK =
            ITEMS.register("blank_spell_book", () -> new BlankSpellBookItem(new Item.Properties()));

    // Wand Upgrades (8 items)
    public static final DeferredItem<WandUpgradeItem> STORAGE_UPGRADE =
            ITEMS.register("storage_upgrade", () -> new WandUpgradeItem(WandUpgrades.STORAGE));

    public static final DeferredItem<WandUpgradeItem> SIPHON_UPGRADE =
            ITEMS.register("siphon_upgrade", () -> new WandUpgradeItem(WandUpgrades.SIPHON));

    public static final DeferredItem<WandUpgradeItem> RANGE_UPGRADE =
            ITEMS.register("range_upgrade", () -> new WandUpgradeItem(WandUpgrades.RANGE));

    public static final DeferredItem<WandUpgradeItem> BLAST_UPGRADE =
            ITEMS.register("blast_upgrade", () -> new WandUpgradeItem(WandUpgrades.BLAST));

    public static final DeferredItem<WandUpgradeItem> COOLDOWN_UPGRADE =
            ITEMS.register("cooldown_upgrade", () -> new WandUpgradeItem(WandUpgrades.COOLDOWN));

    public static final DeferredItem<WandUpgradeItem> CONDENSER_UPGRADE =
            ITEMS.register("condenser_upgrade", () -> new WandUpgradeItem(WandUpgrades.CONDENSER));

    public static final DeferredItem<WandUpgradeItem> ATTUNEMENT_UPGRADE =
            ITEMS.register("attunement_upgrade", () -> new WandUpgradeItem(WandUpgrades.ATTUNEMENT));

    public static final DeferredItem<WandUpgradeItem> DURATION_UPGRADE =
            ITEMS.register("duration_upgrade", () -> new WandUpgradeItem(WandUpgrades.DURATION));

    // Tomes
    public static final DeferredItem<ArcaneTomeItem> APPRENTICE_TOME =
            ITEMS.register("apprentice_tome", () -> new ArcaneTomeItem(SpellTiers.APPRENTICE, net.minecraft.world.item.Rarity.UNCOMMON));

    public static final DeferredItem<ArcaneTomeItem> ADVANCED_TOME =
            ITEMS.register("advanced_tome", () -> new ArcaneTomeItem(SpellTiers.ADVANCED, net.minecraft.world.item.Rarity.RARE));

    public static final DeferredItem<ArcaneTomeItem> MASTER_TOME =
            ITEMS.register("master_tome", () -> new ArcaneTomeItem(SpellTiers.MASTER, net.minecraft.world.item.Rarity.EPIC));

    // Compatibility aliases for tomes
    public static final DeferredItem<ArcaneTomeItem> ARCANE_TOME =
            ITEMS.register("arcane_tome", () -> new ArcaneTomeItem(SpellTiers.APPRENTICE, net.minecraft.world.item.Rarity.UNCOMMON));

    public static final DeferredItem<ArcaneTomeItem> APPRENTICE_ARCANE_TOME =
            ITEMS.register("apprentice_arcane_tome", () -> new ArcaneTomeItem(SpellTiers.APPRENTICE, net.minecraft.world.item.Rarity.UNCOMMON));

    public static final DeferredItem<ArcaneTomeItem> ADVANCED_ARCANE_TOME =
            ITEMS.register("advanced_arcane_tome", () -> new ArcaneTomeItem(SpellTiers.ADVANCED, net.minecraft.world.item.Rarity.RARE));

    public static final DeferredItem<ArcaneTomeItem> MASTER_ARCANE_TOME =
            ITEMS.register("master_arcane_tome", () -> new ArcaneTomeItem(SpellTiers.MASTER, net.minecraft.world.item.Rarity.EPIC));

    // Smithing Template
    public static final DeferredItem<SmithingTemplateItem> ARCANE_UPGRADE_SMITHING_TEMPLATE =
            ITEMS.register("arcane_upgrade_smithing_template", ArcaneUpgradeSmithingTemplateItem::new);

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
        EBRecipeSerializers.register(modBus);
        modBus.addListener(EBCraftingItems::commonSetup);
        modBus.addListener(com.binaris.wizardry.datagen.WizardryRecipeProvider::gatherData);
    }

    private static void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            WandUpgrades.register(STORAGE_UPGRADE.get(), WandUpgrades.STORAGE);
            WandUpgrades.register(SIPHON_UPGRADE.get(), WandUpgrades.SIPHON);
            WandUpgrades.register(RANGE_UPGRADE.get(), WandUpgrades.RANGE);
            WandUpgrades.register(BLAST_UPGRADE.get(), WandUpgrades.BLAST);
            WandUpgrades.register(COOLDOWN_UPGRADE.get(), WandUpgrades.COOLDOWN);
            WandUpgrades.register(CONDENSER_UPGRADE.get(), WandUpgrades.CONDENSER);
            WandUpgrades.register(ATTUNEMENT_UPGRADE.get(), WandUpgrades.ATTUNEMENT);
            WandUpgrades.register(DURATION_UPGRADE.get(), WandUpgrades.DURATION);
        });
    }

    private EBCraftingItems() {}
}
