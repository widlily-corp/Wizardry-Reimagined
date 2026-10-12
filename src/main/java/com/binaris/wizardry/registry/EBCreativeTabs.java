package com.binaris.wizardry.registry;

import com.binaris.wizardry.WizardryMod;
import com.binaris.wizardry.content.item.scroll.ScrollItem;
import com.binaris.wizardry.content.item.scroll.SpellBookItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class EBCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WizardryMod.MODID);

    // 1. Wizardry Items
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB =
            CREATIVE_MODE_TABS.register("ebwizardry", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.ebwizardry"))
                    .icon(() -> new ItemStack(EBItems.MAGIC_CRYSTAL.get()))
                    .displayItems((parameters, output) -> {
                        // Guide Handbook
                        output.accept(EBItems.WIZARD_HANDBOOK.get());

                        // Crystals & Shards
                        output.accept(EBItems.MAGIC_CRYSTAL_SHARD.get());
                        output.accept(EBItems.MAGIC_CRYSTAL.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_GRAND.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_EARTH.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_FIRE.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_HEALING.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_ICE.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_LIGHTNING.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_NECROMANCY.get());
                        output.accept(EBItems.MAGIC_CRYSTAL_SORCERY.get());

                        // Arcane Tomes
                        output.accept(EBCraftingItems.APPRENTICE_TOME.get());
                        output.accept(EBCraftingItems.ADVANCED_TOME.get());
                        output.accept(EBCraftingItems.MASTER_TOME.get());

                        // Wand Upgrades (8)
                        output.accept(EBCraftingItems.STORAGE_UPGRADE.get());
                        output.accept(EBCraftingItems.SIPHON_UPGRADE.get());
                        output.accept(EBCraftingItems.RANGE_UPGRADE.get());
                        output.accept(EBCraftingItems.BLAST_UPGRADE.get());
                        output.accept(EBCraftingItems.COOLDOWN_UPGRADE.get());
                        output.accept(EBCraftingItems.CONDENSER_UPGRADE.get());
                        output.accept(EBCraftingItems.ATTUNEMENT_UPGRADE.get());
                        output.accept(EBCraftingItems.DURATION_UPGRADE.get());

                        // Mana Flasks (if registered)
                        acceptIfExists(output, "mana_flask_small");
                        acceptIfExists(output, "mana_flask_medium");
                        acceptIfExists(output, "mana_flask_large");

                        // Spectral Dust (if registered)
                        acceptIfExists(output, "spectral_dust");
                        acceptIfExists(output, "spectral_dust_earth");
                        acceptIfExists(output, "spectral_dust_fire");
                        acceptIfExists(output, "spectral_dust_healing");
                        acceptIfExists(output, "spectral_dust_ice");
                        acceptIfExists(output, "spectral_dust_lightning");
                        acceptIfExists(output, "spectral_dust_necromancy");
                        acceptIfExists(output, "spectral_dust_sorcery");

                        // Magic Silk & Specialty Materials (if registered)
                        acceptIfExists(output, "magic_silk");
                        acceptIfExists(output, "ruined_spell_book");
                        acceptIfExists(output, "astral_diamond");
                        acceptIfExists(output, "purifying_elixir");
                        acceptIfExists(output, "identification_scroll");

                        // Elemental Dust (if registered)
                        acceptIfExists(output, "fire_elemental_dust");
                        acceptIfExists(output, "ice_elemental_dust");
                        acceptIfExists(output, "lightning_elemental_dust");
                        acceptIfExists(output, "necromancy_elemental_dust");
                        acceptIfExists(output, "earth_elemental_dust");
                        acceptIfExists(output, "sorcery_elemental_dust");
                        acceptIfExists(output, "healing_elemental_dust");

                        // Banner Patterns (if registered)
                        acceptIfExists(output, "fire_banner_pattern");
                        acceptIfExists(output, "ice_banner_pattern");
                        acceptIfExists(output, "lightning_banner_pattern");
                        acceptIfExists(output, "necromancy_banner_pattern");
                        acceptIfExists(output, "earth_banner_pattern");
                        acceptIfExists(output, "sorcery_banner_pattern");
                        acceptIfExists(output, "healing_banner_pattern");

                        // Smithing Template
                        output.accept(EBCraftingItems.ARCANE_UPGRADE_SMITHING_TEMPLATE.get());
                    })
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEMS = TAB;

    // 2. Wizardry Wands
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WANDS =
            CREATIVE_MODE_TABS.register("ebwizardry_wands", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.ebwizardry_wands"))
                    .icon(() -> new ItemStack(EBWandItems.NOVICE_WAND.get()))
                    .displayItems((parameters, output) ->
                            EBWandItems.ITEMS.getEntries().forEach(holder -> output.accept(holder.get())))
                    .build());

    // 3. Wizardry Armors
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARMORS =
            CREATIVE_MODE_TABS.register("ebwizardry_armors", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.ebwizardry_armors"))
                    .icon(() -> new ItemStack(EBArmorItems.WIZARD_HAT.get()))
                    .displayItems((parameters, output) ->
                            EBArmorItems.ITEMS.getEntries().forEach(holder -> output.accept(holder.get())))
                    .build());

    // 4. Wizardry Artifacts
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARTIFACTS =
            CREATIVE_MODE_TABS.register("ebwizardry_artifacts", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.ebwizardry_artifacts"))
                    .icon(() -> new ItemStack(EBBaubleItems.RING_CONDENSING.get()))
                    .displayItems((parameters, output) ->
                            EBBaubleItems.ITEMS.getEntries().forEach(holder -> output.accept(holder.get())))
                    .build());

    // 5. Wizardry Spells
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPELLS =
            CREATIVE_MODE_TABS.register("ebwizardry_spells", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.ebwizardry_spells"))
                    .icon(() -> new ItemStack(EBCraftingItems.SPELL_BOOK.get()))
                    .withSearchBar()
                    .displayItems((parameters, output) -> {
                        output.accept(EBCraftingItems.BLANK_SCROLL.get());
                        output.accept(EBCraftingItems.BLANK_SPELL_BOOK.get());
                        output.accept(EBCraftingItems.SCROLL.get());
                        output.accept(EBCraftingItems.SPELL_BOOK.get());

                        List<Spell> spells = new ArrayList<>();
                        for (Spell spell : EBRegistries.SPELL_REGISTRY) {
                            if (spell == null || spell == Spells.NONE.get() || spell instanceof NoneSpell) {
                                continue;
                            }
                            spells.add(spell);
                        }

                        spells.sort(Comparator.<Spell>comparingInt(s -> s.getTier() != null ? s.getTier().getLevel() : 0)
                                .thenComparing(s -> s.getElement() != null ? s.getElement().getName() : ""));

                        for (Spell spell : spells) {
                            output.accept(ScrollItem.createWithSpell(EBCraftingItems.SCROLL.get(), spell));
                            output.accept(SpellBookItem.createWithSpell(EBCraftingItems.SPELL_BOOK.get(), spell));
                        }
                    })
                    .build());

    // 6. Wizardry Blocks
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS =
            CREATIVE_MODE_TABS.register("ebwizardry_blocks", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.ebwizardry_blocks"))
                    .icon(() -> new ItemStack(EBBlocks.CRYSTAL_BLOCK_MAGIC.get()))
                    .displayItems((parameters, output) -> {
                        // Crystal Blocks
                        output.accept(EBBlocks.CRYSTAL_BLOCK_MAGIC.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_EARTH.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_FIRE.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_HEALING.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_ICE.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_LIGHTNING.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_NECROMANCY.get());
                        output.accept(EBBlocks.CRYSTAL_BLOCK_SORCERY.get());

                        // Ores & Flora
                        output.accept(EBBlocks.CRYSTAL_ORE.get());
                        output.accept(EBBlocks.DEEPSLATE_CRYSTAL_ORE.get());
                        output.accept(EBBlocks.CRYSTAL_FLOWER.get());

                        // Runestones
                        output.accept(EBBlocks.FIRE_RUNESTONE.get());
                        output.accept(EBBlocks.EARTH_RUNESTONE.get());
                        output.accept(EBBlocks.ICE_RUNESTONE.get());
                        output.accept(EBBlocks.LIGHTNING_RUNESTONE.get());
                        output.accept(EBBlocks.NECROMANCY_RUNESTONE.get());
                        output.accept(EBBlocks.SORCERY_RUNESTONE.get());
                        output.accept(EBBlocks.HEALING_RUNESTONE.get());

                        // Runestone Pedestals
                        output.accept(EBBlocks.FIRE_RUNESTONE_PEDESTAL.get());
                        output.accept(EBBlocks.EARTH_RUNESTONE_PEDESTAL.get());
                        output.accept(EBBlocks.ICE_RUNESTONE_PEDESTAL.get());
                        output.accept(EBBlocks.LIGHTNING_RUNESTONE_PEDESTAL.get());
                        output.accept(EBBlocks.NECROMANCY_RUNESTONE_PEDESTAL.get());
                        output.accept(EBBlocks.SORCERY_RUNESTONE_PEDESTAL.get());
                        output.accept(EBBlocks.HEALING_RUNESTONE_PEDESTAL.get());

                        // Bookshelves
                        output.accept(EBBlocks.BOOKSHELF.get());
                        output.accept(EBBlocks.OAK_BOOKSHELF.get());
                        output.accept(EBBlocks.SPRUCE_BOOKSHELF.get());
                        output.accept(EBBlocks.BIRCH_BOOKSHELF.get());
                        output.accept(EBBlocks.JUNGLE_BOOKSHELF.get());
                        output.accept(EBBlocks.ACACIA_BOOKSHELF.get());
                        output.accept(EBBlocks.DARK_OAK_BOOKSHELF.get());

                        // Gilded Wood
                        output.accept(EBBlocks.GILDED_WOOD.get());
                        output.accept(EBBlocks.GILDED_OAK_WOOD.get());
                        output.accept(EBBlocks.GILDED_SPRUCE_WOOD.get());
                        output.accept(EBBlocks.GILDED_BIRCH_WOOD.get());
                        output.accept(EBBlocks.GILDED_JUNGLE_WOOD.get());
                        output.accept(EBBlocks.GILDED_ACACIA_WOOD.get());
                        output.accept(EBBlocks.GILDED_DARK_OAK_WOOD.get());

                        // Workbenches, Altars & Receptacles
                        output.accept(EBWorkbenchBlocks.ARCANE_WORKBENCH.get());
                        output.accept(EBBlocks.IMBUEMENT_ALTAR.get());
                        output.accept(EBBlocks.RECEPTACLE.get());
                        output.accept(EBBlocks.WALL_RECEPTACLE.get());

                        // Lecterns (if registered)
                        acceptIfExists(output, "oak_lectern");
                        acceptIfExists(output, "spruce_lectern");
                        acceptIfExists(output, "birch_lectern");
                        acceptIfExists(output, "jungle_lectern");
                        acceptIfExists(output, "acacia_lectern");
                        acceptIfExists(output, "dark_oak_lectern");

                        // Special Blocks
                        acceptIfExists(output, "obsidian_crust");
                        acceptIfExists(output, "permafrost");
                        acceptIfExists(output, "spectral_block");
                        acceptIfExists(output, "vanishing_cobweb");
                    })
                    .build());

    private static void acceptIfExists(CreativeModeTab.Output output, String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, path);
        if (BuiltInRegistries.ITEM.containsKey(id)) {
            Item item = BuiltInRegistries.ITEM.get(id);
            if (item != null && item != Items.AIR) {
                output.accept(item);
            }
        }
    }

    public static void register(IEventBus modBus) {
        CREATIVE_MODE_TABS.register(modBus);
    }

    private EBCreativeTabs() {}
}
