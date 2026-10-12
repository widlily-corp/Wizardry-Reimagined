package com.binaris.wizardry.client.gui;

import com.binaris.wizardry.WizardryMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * The Wizard's Handbook interactive dual-page guide screen.
 * Clean, crisp rendering without blur or HUD overlapping.
 */
public class WizardHandbookScreen extends Screen {

    public static final ResourceLocation BOOK_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/handbook.png");

    public static final int BOOK_WIDTH = 288;
    public static final int BOOK_HEIGHT = 180;
    public static final int TEX_WIDTH = 512;
    public static final int TEX_HEIGHT = 256;

    private static final int COLOR_TITLE = 0x3F2010;
    private static final int COLOR_SUBTITLE = 0x6E4226;
    private static final int COLOR_TEXT = 0x241C15;
    private static final int COLOR_MUTED = 0x7A6A58;

    private int currentChapter = 0;
    private final List<Chapter> chapters = new ArrayList<>();

    public WizardHandbookScreen() {
        super(Component.translatable("item.ebwizardry.wizard_handbook"));
        initChapters();
    }

    private void initChapters() {
        chapters.clear();

        // 0. Table of Contents
        chapters.add(new Chapter(
                "Пособие Мага", "The Wizard's Handbook",
                "§lДревний Трактат§r\n\nРуководство по изучению маны, чар и семи великих стихий.\n\nНажмите на нужную главу справа или перелистывайте страницы стрелками внизу.",
                "§lAncient Tome§r\n\nA comprehensive guide to mana manipulation, spellcraft, and the seven elements.\n\nClick a chapter on the right or turn pages using arrows below.",
                "", "",
                null, 0, 0
        ));

        // 1. Path of the Wizard
        chapters.add(new Chapter(
                "1. Путь Мага", "1. Path of the Wizard",
                "§lИстоки Магии§r\n\nМана пронизывает весь мир. Чтобы управлять ей, магу нужна волшебная палочка.\n\nЗапас маны палочки расходуется при чтении заклинаний.",
                "§lOrigins of Magic§r\n\nMana permeates the world. To channel it, a mage needs a magic wand.\n\nCasting incantations consumes mana stored within the wand.",
                "§lПервые Шаги§r\n\n1. Добудьте кристальную руду под землёй.\n2. Скрафтите первую палочку.\n3. Соберите верстак мага.\n4. Найдите свитки чар в руинах.\n\nСледите за маной: пустая палочка бесполезна!",
                "§lFirst Steps§r\n\n1. Mine crystal ore underground.\n2. Craft your first wand.\n3. Build an arcane workbench.\n4. Find scrolls in ancient ruins.\n\nWatch your mana: an empty wand cannot cast!",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/crystal_ore.png"), 36, 36
        ));

        // 2. Crystals & Flowers
        chapters.add(new Chapter(
                "2. Кристаллы и Цветы", "2. Crystals & Flowers",
                "§lКристаллы Маны§r\n\nКристаллы — основной источник маны и катализатор крафтов.\n\n• Осколок: 10 маны.\n• Кристалл: 100 маны.\n• Великий кристалл: 400 маны.",
                "§lMana Crystals§r\n\nCrystals fuel your spells and serve as key crafting components.\n\n• Shard: 10 mana.\n• Crystal: 100 mana.\n• Grand Crystal: 400 mana.",
                "§lСтихии и Цветы§r\n\nСуществует 7 видов кристаллов по стихиям.\n\nКристальные цветы цветут по всему миру. Срывайте их, чтобы получать бесплатные осколки!",
                "§lElements & Flora§r\n\nCrystals resonate with the 7 distinct schools.\n\nCrystal Flowers bloom naturally. Harvest them for renewable crystal shards!",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/crystal_flower.png"), 36, 36
        ));

        // 3. Magic Wands
        chapters.add(new Chapter(
                "3. Волшебные Палочки", "3. Magic Wands",
                "§lРанги Палочек§r\n\n• Новичок: 100 маны.\n• Ученик: 700 маны.\n• Мастер: 1400 маны.\n• Архимаг: 3000 маны.\n\nПовышайте ранг на верстаке с помощью древних томов!",
                "§lWand Tiers§r\n\n• Novice: 100 mana.\n• Apprentice: 700 mana.\n• Master: 1400 mana.\n• Archmage: 3000 mana.\n\nUpgrade your wand tier at the arcane workbench!",
                "§lУправление§r\n\n• ПКМ — творить заклинание.\n• Shift + ПКМ — открыть круговое меню заклинаний.\n\nЗарядка: объедините палочку с кристаллами в обычном верстаке.",
                "§lControls§r\n\n• Right Click — cast spell.\n• Shift + Right Click — open radial spell selector.\n\nRecharging: combine your wand with crystals in a crafting grid.",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/tiers.png"), 60, 15
        ));

        // 4. Arcane Workbench
        chapters.add(new Chapter(
                "4. Магический Верстак", "4. Arcane Workbench",
                "§lНастройка Палочек§r\n\nПоложите палочку в центр, а свитки — в круговые слоты.\n\nНажмите «Применить», чтобы привязать заклинания к слотам палочки.",
                "§lWand Customization§r\n\nPlace your wand in the center and scrolls in the surrounding slots.\n\nClick 'Apply' to bind spells to your wand.",
                "§lАпгрейды Палочек§r\n\nУгловые слоты верстака служат для рун улучшений:\n\n• Хранилище (больше маны)\n• Сифон (мана за убийства)\n• Дальность лучей\n• Снижение перезарядки",
                "§lWand Upgrades§r\n\nSocket upgrade tomes in the corner slots:\n\n• Storage (more mana capacity)\n• Siphon (mana from kills)\n• Range (longer reach)\n• Cooldown (faster recast)",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/arcane_workbench.png"), 36, 36
        ));

        // 5. The Seven Elements
        chapters.add(new Chapter(
                "5. Семь Стихий", "5. The Seven Elements",
                "§lБоевые Стихии§r\n\n🔥 Огонь: взрывы и поджог.\n❄ Лёд: заморозка и щиты.\n⚡ Молния: шок и цепи.\n☠ Некромантия: нежить и кража здоровья.",
                "§lCombat Disciplines§r\n\n🔥 Fire: burns and explosions.\n❄ Ice: slowness and frost.\n⚡ Lightning: chaining arcs.\n☠ Necromancy: drain and minions.",
                "§lСтихии Разума§r\n\n⛰ Земля: защита и камни.\n🔮 Колдовство: телепортация.\n✨ Целительство: регенерация и очищение ран.\n\nСтихийные одеяния дают до 60% скидки к мане!",
                "§lNature Disciplines§r\n\n⛰ Earth: walls and seismic blows.\n🔮 Sorcery: teleports and force.\n✨ Healing: regeneration.\n\nElemental wizard robes grant up to 60% mana discounts!",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/elements.png"), 72, 14
        ));

        // 6. Spells & Tomes
        chapters.add(new Chapter(
                "6. Свитки и Тома", "6. Spells & Tomes",
                "§lСвитки Заклинаний§r\n\nВ мире скрыто более 140 заклинаний! Свитки можно найти в сундуках или купить у магов в башнях.\n\nСвитки одноразовые — учите их бережно.",
                "§lSpell Scrolls§r\n\nOver 140 unique spells exist! Scrolls can be looted in dungeons or traded with tower wizards.\n\nScrolls are consumed when learned into tomes.",
                "§lКниги Заклинаний§r\n\nОбъединяя свитки с книгами, вы создаёте Гримуары, которые не исчезают при чтении.\n\nДревние Тома открывают новые ранги мастерства мага.",
                "§lSpellbooks & Tomes§r\n\nBinding scrolls into books crafts durable Grimoires that are never consumed.\n\nArcane Tomes unlock advanced wizarding ranks.",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/library_ruins.png"), 50, 26
        ));

        // 7. Wizard Armour
        chapters.add(new Chapter(
                "7. Одеяния Мага", "7. Wizard Armour",
                "§lМагический Шёлк§r\n\nПрядётся из паутины и кристальной пыли.\n\nИз шёлка шьются шляпы, мантии, поножи и сапоги настоящего волшебника.",
                "§lMagic Silk§r\n\nSpun from spider silk and crystal dust.\n\nUsed to tailor wizard hats, robes, leggings, and boots with arcane resonance.",
                "§lСтихийная Защита§r\n\nКаждый сет можно посвятить одной из 7 стихий.\n\nПолный комплект даёт огромную скидку к расходу маны и защиту от стихийных эффектов!",
                "§lElemental Sets§r\n\nEach armor set can be attuned to an elemental school.\n\nFull sets grant massive mana reductions and inherent elemental immunity!",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/wizard_armour.png"), 36, 36
        ));

        // 8. Towers & Exploration
        chapters.add(new Chapter(
                "8. Башни и Руины", "8. Towers & Ruins",
                "§lБашни Волшебников§r\n\nВ биомах мира возвышаются каменные шпили. Дружелюбные маги в них охотно торгуют редкими свитками и артефактами.",
                "§lWizard Spires§r\n\nTall towers stand across various biomes. Friendly mages dwell inside, trading rare scrolls and relics for crystals.",
                "§lТёмные Святилища§r\n\nОпасайтесь чернокнижников и проклятых руин!\n\nЗлые колдуны атакуют сразу, но в их сундуках лежат ценнейшие кольца и древние амулеты.",
                "§lDark Shrines§r\n\nBeware corrupt sorcerers and cursed shrines!\n\nHostile mages attack on sight, but their vaults hide precious rings and amulets.",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/wizard_tower.png"), 36, 36
        ));

        // 9. Grand Spells
        chapters.add(new Chapter(
                "9. Величайшие Чары", "9. Grand Spells",
                "§lПик Могущества§r\n\n🌟 Небесный Суд: Звёздный Обелиск с небес.\n🔥 Гнев Гелиоса: сверхновая звезда.\n❄ Ледниковый Обливион: абсолютный ноль.",
                "§lApex Spells§r\n\n🌟 Celestial Monolith: beacon crashing down.\n🔥 Wrath of Helios: thermonuclear solar flare.\n❄ Glacial Oblivion: absolute zero flash-freeze.",
                "§lСейсмическая Мощь§r\n\n⚡ Молот Титанов: сокрушительный громовой удар.\n☠ Затмение Бездны: гравитационная сингулярность.\n⛰ Тектонический Разлом: вздымающиеся скалы.\n⚡ Рельсотрон: луч молнии.",
                "§lSeismic Cataclysms§r\n\n⚡ Titan's Cataclysm: cosmic storm maul.\n☠ Eclipse of the Abyss: black hole singularity.\n⛰ Tectonic Shatter: volcanic megaliths.\n⚡ Lightning Railgun: focused beam.",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/obelisk.png"), 44, 26
        ));

        // 10. Crafting Recipes
        chapters.add(new Chapter(
                "10. Рецепты Ремесла", "10. Crafting Recipes",
                "§lКлючевые Рецепты§r\n\n• Палочка: Золото + Палка + Кристалл.\n• Верстак: 3 Камня + 2 Кристалла + Верстак + Золото.\n• Пособие: Книга + Кристалл.\n• Полка: 6 Досок + 2 Книги + Кристалл.",
                "§lCore Recipes§r\n\n• Wand: Gold + Stick + Crystal.\n• Arcane Workbench: Stone + Crystals + Crafting Table + Gold.\n• Handbook: Book + Crystal.\n• Bookshelf: Planks + Books + Crystal.",
                "§lАлхимия Кристаллов§r\n\n• 9 Осколков = 1 Кристалл (3x3).\n• 9 Кристаллов = 1 Великий Кристалл (3x3).\n• 1 Великий Кристалл = 9 Кристаллов.\n\nПокоряйте тайные искусства!",
                "§lCrystal Alchemy§r\n\n• 9 Shards = 1 Crystal (3x3 grid).\n• 9 Crystals = 1 Grand Crystal (3x3).\n• 1 Grand Crystal = 9 Crystals.\n\nMaster the arcane arts!",
                null, 0, 0
        ));
    }

    @Override
    protected void init() {
        super.init();
        int left = (this.width - BOOK_WIDTH) / 2;
        int top = Math.max(6, (this.height - BOOK_HEIGHT - 28) / 2);

        boolean isRu = isRussianLanguage();

        // Native PageButton arrows placed cleanly at bottom corners of book spread
        this.addRenderableWidget(new PageButton(left + 16, top + BOOK_HEIGHT - 18, false, b -> {
            if (currentChapter > 0) {
                currentChapter--;
                rebuildWidgets();
            }
        }, true) {
            @Override
            public void renderWidget(@NotNull GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                this.visible = currentChapter > 0;
                super.renderWidget(g, mouseX, mouseY, partialTick);
            }
        });

        this.addRenderableWidget(new PageButton(left + BOOK_WIDTH - 39, top + BOOK_HEIGHT - 18, true, b -> {
            if (currentChapter < chapters.size() - 1) {
                currentChapter++;
                rebuildWidgets();
            }
        }, true) {
            @Override
            public void renderWidget(@NotNull GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                this.visible = currentChapter < chapters.size() - 1;
                super.renderWidget(g, mouseX, mouseY, partialTick);
            }
        });

        // Bottom control buttons OUTSIDE the book (below it) so they NEVER overlap text or HUD
        int bottomButtonY = top + BOOK_HEIGHT + 6;

        if (currentChapter > 0) {
            this.addRenderableWidget(Button.builder(Component.literal(isRu ? "Оглавление" : "Index"), b -> {
                currentChapter = 0;
                rebuildWidgets();
            }).bounds(this.width / 2 - 95, bottomButtonY, 90, 20).build());

            this.addRenderableWidget(Button.builder(Component.literal(isRu ? "Закрыть" : "Close"), b -> {
                this.onClose();
            }).bounds(this.width / 2 + 5, bottomButtonY, 90, 20).build());
        } else {
            this.addRenderableWidget(Button.builder(Component.literal(isRu ? "Закрыть" : "Close"), b -> {
                this.onClose();
            }).bounds(this.width / 2 - 50, bottomButtonY, 100, 20).build());
        }

        // Quick navigation buttons on Table of Contents page (chapter 0)
        if (currentChapter == 0) {
            int rightX = left + 152;
            int btnY = top + 22;
            int btnHeight = 13;
            for (int i = 1; i <= 10; i++) {
                final int chapIndex = i;
                Chapter ch = chapters.get(i);
                String btnText = isRu ? ch.titleRu : ch.titleEn;
                this.addRenderableWidget(Button.builder(Component.literal(btnText), b -> {
                    currentChapter = chapIndex;
                    rebuildWidgets();
                }).bounds(rightX, btnY + (i - 1) * 14, 116, btnHeight).build());
            }
        }
    }

    private boolean isRussianLanguage() {
        if (this.minecraft != null && this.minecraft.getLanguageManager() != null) {
            return this.minecraft.getLanguageManager().getSelected().toLowerCase().startsWith("ru");
        }
        return true;
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Transparent dark overlay, prevents blur shader artifacts and hides distracting world HUD
        this.renderTransparentBackground(guiGraphics);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Render dim background
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        int left = (this.width - BOOK_WIDTH) / 2;
        int top = Math.max(6, (this.height - BOOK_HEIGHT - 28) / 2);

        // Render book open spread
        guiGraphics.blit(BOOK_TEXTURE, left, top, 0, 0, BOOK_WIDTH, BOOK_HEIGHT, TEX_WIDTH, TEX_HEIGHT);

        if (currentChapter < 0 || currentChapter >= chapters.size()) {
            super.render(guiGraphics, mouseX, mouseY, partialTick);
            return;
        }

        Chapter chapter = chapters.get(currentChapter);
        boolean isRu = isRussianLanguage();

        int leftX = left + 18;
        int rightX = left + 152;
        int contentY = top + 14;

        // Chapter title on Left page
        String title = isRu ? chapter.titleRu : chapter.titleEn;
        guiGraphics.drawString(this.font, Component.literal(title), leftX, contentY, COLOR_TITLE, false);

        // Optional illustration on left page
        int textOffsetY = contentY + 12;
        if (chapter.picture != null) {
            int picX = leftX + (114 - chapter.picW) / 2;
            int picY = textOffsetY;
            guiGraphics.blit(chapter.picture, picX, picY, 0, 0, chapter.picW, chapter.picH, chapter.picW, chapter.picH);
            textOffsetY += chapter.picH + 6;
        }

        // Left page body text (clipped within bounds so it never leaks down)
        String leftText = isRu ? chapter.leftPageRu : chapter.leftPageEn;
        guiGraphics.drawWordWrap(this.font, Component.literal(leftText), leftX, textOffsetY, 114, COLOR_TEXT);

        // Right page body text (when not on TOC page)
        if (currentChapter > 0) {
            String rightText = isRu ? chapter.rightPageRu : chapter.rightPageEn;
            guiGraphics.drawWordWrap(this.font, Component.literal(rightText), rightX, contentY + 4, 114, COLOR_TEXT);

            // Page Number indicator centered at bottom
            String pageNum = currentChapter + " / " + (chapters.size() - 1);
            int numWidth = this.font.width(pageNum);
            guiGraphics.drawString(this.font, Component.literal(pageNum), left + (BOOK_WIDTH - numWidth) / 2, top + BOOK_HEIGHT - 16, COLOR_MUTED, false);
        } else {
            // TOC Right page header
            String tocHeader = isRu ? "§lСодержание:§r" : "§lContents:§r";
            guiGraphics.drawString(this.font, Component.literal(tocHeader), rightX, contentY - 4, COLOR_SUBTITLE, false);
        }

        // Render widgets (page arrows and bottom buttons)
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT && currentChapter > 0) {
            currentChapter--;
            rebuildWidgets();
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT && currentChapter < chapters.size() - 1) {
            currentChapter++;
            rebuildWidgets();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static class Chapter {
        final String titleRu;
        final String titleEn;
        final String leftPageRu;
        final String leftPageEn;
        final String rightPageRu;
        final String rightPageEn;
        final ResourceLocation picture;
        final int picW;
        final int picH;

        Chapter(String titleRu, String titleEn, String leftPageRu, String leftPageEn,
                String rightPageRu, String rightPageEn, ResourceLocation picture, int picW, int picH) {
            this.titleRu = titleRu;
            this.titleEn = titleEn;
            this.leftPageRu = leftPageRu;
            this.leftPageEn = leftPageEn;
            this.rightPageRu = rightPageRu;
            this.rightPageEn = rightPageEn;
            this.picture = picture;
            this.picW = picW;
            this.picH = picH;
        }
    }
}
