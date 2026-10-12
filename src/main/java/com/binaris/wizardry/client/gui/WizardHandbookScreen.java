package com.binaris.wizardry.client.gui;

import com.binaris.wizardry.WizardryMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * The Wizard's Handbook interactive dual-page guide screen.
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
                "Оглавление", "Table of Contents",
                "§lПособие Мага§r\n\nДревний трактат о тайнах тайных искусств, мане и стихийных заклинаниях.\n\nВыберите главу справа или листайте страницы стрелками внизу.",
                "§lThe Wizard's Handbook§r\n\nAn ancient treatise detailing the arcane arts, mana manipulation, and elemental incantations.\n\nSelect a chapter on the right or turn pages using arrows below.",
                "§lРазделы фолианта:§r\n\n1. Путь Мага\n2. Кристаллы и Цветы\n3. Волшебные Палочки\n4. Магический Верстак\n5. Семь Стихий\n6. Свитки и Тома\n7. Одеяния Мага\n8. Башни и Исследования\n9. Величайшие Заклинания\n10. Рецепты Ремесла",
                "§lTome Chapters:§r\n\n1. Path of the Wizard\n2. Crystals & Flowers\n3. Magic Wands\n4. Arcane Workbench\n5. The Seven Elements\n6. Spells & Tomes\n7. Wizard Armour\n8. Towers & Exploration\n9. Grand Spells\n10. Crafting Recipes",
                null, 0, 0
        ));

        // 1. Path of the Wizard
        chapters.add(new Chapter(
                "1. Путь Мага", "1. Path of the Wizard",
                "§lИстоки Магии§r\n\nМагия в мире Minecraft существует повсюду, скрытая в кристаллах и недрах земли. Чтобы прикоснуться к ней, обычному человеку нужен проводник — первая волшебная палочка.\n\nМана — это жизненная энергия заклинателя. Каждое заклинание расходует запас маны палочки.",
                "§lOrigins of Magic§r\n\nMagic permeates the world, dormant within crystal veins and ancient earth. To harness it, a mortal requires a conduit: a magic wand.\n\nMana is the vital conduit fuel. Every incantation consumes mana from the equipped wand.",
                "§lПервые Шаги§r\n\n1. Добудьте кристальную руду под землей.\n2. Скрафтите волшебную палочку из кристалла и золота.\n3. Соберите магический верстак.\n4. Найдите свитки заклинаний в сокровищницах.\n\nБерегите запасы маны: разряженная палочка не сможет сотворить чары!",
                "§lFirst Steps§r\n\n1. Mine crystal ore deep underground.\n2. Forge a wand using a crystal and gold.\n3. Construct an arcane workbench.\n4. Discover spell scrolls in dungeons.\n\nConserve your mana: a depleted wand cannot weave spells!",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/crystal_ore.png"), 64, 64
        ));

        // 2. Crystals & Flowers
        chapters.add(new Chapter(
                "2. Кристаллы и Цветы", "2. Crystals & Flowers",
                "§lМагические Кристаллы§r\n\nКристаллы — сердце всей магии мода. Они служат источником маны и основным компонентом всех рецептов.\n\n• Осколки: малые крупицы энергии (10 маны).\n• Кристалл: стандартный камень (100 маны).\n• Великий кристалл: могущественный фокус (400 маны).",
                "§lMagic Crystals§r\n\nCrystals are the beating heart of all spellcraft, providing raw mana and acting as primary crafting catalysts.\n\n• Shards: small sparks (10 mana).\n• Crystal: pure stone (100 mana).\n• Grand Crystal: concentrated focus (400 mana).",
                "§lСтихийные Кристаллы§r\n\nВстречаются 7 элементальных кристаллов: Огонь, Лед, Молния, Некромантия, Земля, Колдовство, Целительство.\n\nКристальные цветы цветут в лесах и на равнинах, давая бесплатные осколки при сборе. Выращивайте их для восполнения запасов!",
                "§lElemental Crystals§r\n\nCrystals resonate with 7 distinct elements: Fire, Ice, Lightning, Necromancy, Earth, Sorcery, and Healing.\n\nCrystal Flowers bloom in biomes worldwide, dropping shards when harvested. Cultivate them for sustainable mana!",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/crystal_flower.png"), 64, 64
        ));

        // 3. Magic Wands
        chapters.add(new Chapter(
                "3. Волшебные Палочки", "3. Magic Wands",
                "§lРанги Палочек§r\n\nСуществует 4 тира мастерства:\n\n• Новичок (Novice): 100 маны, базовые чары.\n• Ученик (Apprentice): 700 маны, средние чары.\n• Мастер (Master): 1400 маны, сильные чары.\n• Архимаг (Archmage): до 3000 маны, высшая магия.",
                "§lWand Tiers§r\n\nFour tiers of craftsmanship exist:\n\n• Novice: 100 mana, basic spells.\n• Apprentice: 700 mana, intermediate.\n• Master: 1400 mana, advanced spells.\n• Archmage: up to 3000 mana, supreme power.",
                "§lУправление и Зарядка§r\n\n• ПКМ — применить выбранное заклинание.\n• Shift + ПКМ — открыть круговое меню выбора слотов.\n• Зарядка: положите палочку и кристаллы в сетку верстака.\n\nКаждая палочка также может быть специализирована под стихию на верстаке!",
                "§lControls & Recharging§r\n\n• Right Click — cast active spell.\n• Shift + Right Click — open radial spell selector.\n• Recharging: combine wand and crystals in a crafting grid.\n\nWands can also be aligned to specific elements at the workbench!",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/tiers.png"), 64, 16
        ));

        // 4. Arcane Workbench
        chapters.add(new Chapter(
                "4. Магический Верстак", "4. Arcane Workbench",
                "§lСердце Мастерской§r\n\nМагический верстак позволяет настраивать палочки.\n\nЦентральный слот: поместите сюда вашу палочку.\nКруг слотов: для привязки свитков и книг заклинаний.\n\nНажмите 'Применить' после выбора конфигурации.",
                "§lThe Arcane Workbench§r\n\nThe Arcane Workbench is your magical laboratory.\n\nCenter Slot: place your magic wand here.\nSurrounding Slots: assign spell scrolls and tomes.\n\nClick 'Apply' after configuring your loadout.",
                "§lУлучшения Палочек§r\n\nВ угловые слоты верстака можно устанавливать руны улучшений:\n\n• Хранилище: увеличивает запас маны.\n• Сифон: восстанавливает ману при убийствах.\n• Дальность: увеличивает дальность лучей.\n• Перезарядка: снижает кулдаун заклинаний.",
                "§lWand Upgrades§r\n\nSocket upgrade tomes into corner slots:\n\n• Storage: increases maximum mana capacity.\n• Siphon: recovers mana on entity kills.\n• Range: extends projectile and ray reach.\n• Cooldown: decreases cast recovery delay.",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/arcane_workbench.png"), 55, 55
        ));

        // 5. The Seven Elements
        chapters.add(new Chapter(
                "5. Семь Стихий", "5. The Seven Elements",
                "§lСтихии Мира§r\n\nКаждое заклинание принадлежит к одной из семи школ:\n\n🔥 Огонь: колоссальный урон и поджог.\n❄ Лед: замедление, заморозка и защита.\n⚡ Молния: цепные удары и паралич.\n☠ Некромантия: призыв нежити и вампиризм.",
                "§lElemental Schools§r\n\nEvery incantation belongs to one of 7 disciplines:\n\n🔥 Fire: devastating damage and burning.\n❄ Ice: slowness, freezing, and shields.\n⚡ Lightning: chaining arcs and paralysis.\n☠ Necromancy: undead summons and life drain.",
                "§lСтихии Природы и Разума§r\n\n⛰ Земля: защита, сейсмические удары и камни.\n🔮 Колдовство: телепортация, кинетика, щиты.\n✨ Целительство: регенерация, ауры и очищение.\n\nНошение стихийной брони дает колоссальную скидку к расходу маны!",
                "§lNature & Mind Disciplines§r\n\n⛰ Earth: defense, seismic shocks, stones.\n🔮 Sorcery: teleportation, barriers, force.\n✨ Healing: regeneration, auras, cleansing.\n\nEquipping elemental robes grants substantial mana discounts!",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/elements.png"), 80, 16
        ));

        // 6. Spells & Tomes
        chapters.add(new Chapter(
                "6. Свитки и Тома", "6. Spells & Tomes",
                "§lПоиск Знаний§r\n\nВ мире сокрыто более 140 уникальных заклинаний! Свитки можно найти в сундуках данжей, купить у магов в башнях или выбить с боссов.\n\nСвитки одноразовые, но их можно скопировать на пустой свиток на верстаке.",
                "§lSeeking Knowledge§r\n\nOver 140 unique spells are scattered across the world! Scrolls can be looted from dungeon chests, traded with tower wizards, or won from bosses.\n\nScrolls are consumable, but can be transcribed onto blank parchment at the workbench.",
                "§lКниги Заклинаний§r\n\nСвязывая свитки с книгами, вы создаете долговечные Гримуары. Они не исчезают при изучении и позволяют хранить целые библиотеки чар.\n\nДревние Тома повышают ранг мастерства волшебника, открывая доступ к высшей магии.",
                "§lSpell Tomes§r\n\nBinding scrolls into spellbooks creates durable Grimoires that are never consumed upon reading.\n\nArcane Tomes advance your wizarding tier, unlocking the ability to wield apprentice, master, and grand spells.",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/library_ruins.png"), 75, 40
        ));

        // 7. Wizard Armour
        chapters.add(new Chapter(
                "7. Одеяния Мага", "7. Wizard Armour",
                "§lОдеяния Волшебника§r\n\nОбычная железная броня глушит магические потоки. Истинному магу требуются шелковые мантии, шляпы и сапоги.\n\nМагический шелк прядется из паутины и кристальной пыли.",
                "§lArcane Regalia§r\n\nMundane plate armor dampens magical resonance. True mages wear enchanted silk robes, hats, and boots.\n\nMagic silk is spun from spiderwebs infused with crystal dust.",
                "§lСтихийные Сеты§r\n\nКаждый комплект брони может быть освящен стихией:\n\n• Полный сет дает до 60% скидки к расходу маны соответствующей школы!\n• Встроенная защита от эффектов стихии (огнестойкость, иммунитет к заморозке).",
                "§lElemental Attunement§r\n\nEvery robe set can be attuned to a discipline:\n\n• Full set grants up to 60% mana reduction for that element!\n• Inherent protection against aligned effects (fire resistance, freeze immunity).",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/wizard_armour.png"), 60, 60
        ));

        // 8. Towers & Exploration
        chapters.add(new Chapter(
                "8. Башни и Исследования", "8. Towers & Exploration",
                "§lБашни Магов§r\n\nПутешествуя по миру, вы встретите величественные каменные башни. В них живут дружелюбные волшебники, готовые обменять редкие свитки и артефакты на кристаллы.",
                "§lWizard Towers§r\n\nWandering across diverse biomes, you will encounter towering spires. Friendly wizards dwell within, eager to trade rare scrolls, wands, and relics for crystals.",
                "§lОпасности и Руины§r\n\nОстерегайтесь злых магов и темных святилищ! Чернокнижники атакуют без предупреждения, применяя смертоносные чары.\n\nВ руинах библиотек сокрыты ценнейшие реликвии и древние тома мастерства.",
                "§lRuins & Dangers§r\n\nBeware corrupted wizards and cursed shrines! Dark mages will strike without hesitation using lethal sorcery.\n\nAncient library ruins guard invaluable artifacts, rings, amulets, and grand secrets.",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/wizard_tower.png"), 60, 60
        ));

        // 9. Grand Spells
        chapters.add(new Chapter(
                "9. Величайшие Заклинания", "9. Grand Spells",
                "§lПик Магической Мощи§r\n\nЗаклинания высшего порядка требуют колоссальной маны и высшего мастерства:\n\n🌟 Небесный Суд: Звездный Обелиск, низвергающийся с орбит.\n🔥 Гнев Гелиоса: термоядерная вспышка Сверхновой.\n❄ Ледниковый Обливион: абсолютный морозный разлом.",
                "§lApex Arcana§r\n\nGrand spells demand immense mana reserves and true wizard mastery:\n\n🌟 Celestial Monolith: an orbital Astraea beacon crashing from orbit.\n🔥 Wrath of Helios: thermonuclear solar flare.\n❄ Glacial Oblivion: absolute zero glacier erupting from the earth.",
                "§lСейсмическая Сила§r\n\n⚡ Молот Титанов: сокрушительный громовой удар.\n☠ Затмение Бездны: гравитационная черная дыра.\n⛰ Тектонический Разлом: вздымающиеся монолиты скал.\n⚡ Рельсотрон Молний: непрерывный луч чистой энергии.\n\nЭти чары изменяют ландшафт и сотрясают мир!",
                "§lCataclysmic Might§r\n\n⚡ Titan's Cataclysm: cosmic storm maul.\n☠ Eclipse of the Abyss: gravitational black hole.\n⛰ Tectonic Shatter: volcanic megaliths erupting.\n⚡ Lightning Railgun: focused beam of annihilation.\n\nThese incantations leave lasting scars upon the battlefield!",
                ResourceLocation.fromNamespaceAndPath(WizardryMod.MODID, "textures/gui/handbook/pictures/obelisk.png"), 64, 40
        ));

        // 10. Crafting Recipes
        chapters.add(new Chapter(
                "10. Рецепты Ремесла", "10. Crafting Recipes",
                "§lБазовые Крафты§r\n\n• Палочка: Золотой слиток + Палка + Магический кристалл.\n• Верстак: 3 Камня + 2 Кристалла + Верстак + Золото.\n• Пособие Мага: Обычная книга + Магический кристалл.\n• Книжная полка: 6 Досок + 3 Книги.",
                "§lBasic Recipes§r\n\n• Magic Wand: Gold Ingot + Stick + Magic Crystal.\n• Arcane Workbench: Stone + Crystals + Workbench + Gold.\n• Wizard's Handbook: Book + Magic Crystal.\n• Bookshelf: 6 Planks + 3 Books.",
                "§lАлхимия Кристаллов§r\n\n• 9 Осколков = 1 Магический Кристалл.\n• 9 Кристаллов = 1 Великий Кристалл.\n• Магический шелк: 4 Нити + 1 Кристальная пыль.\n\nСнаряжайтесь, исследуйте мир и покоряйте стихии!",
                "§lCrystal Alchemy§r\n\n• 9 Shards = 1 Magic Crystal (3x3 grid).\n• 9 Crystals = 1 Grand Crystal (3x3 grid).\n• Magic Silk: 4 String + 1 Crystal Dust.\n\nGear up, venture forth, and master the arcane!",
                null, 0, 0
        ));
    }

    @Override
    protected void init() {
        super.init();
        int left = (this.width - BOOK_WIDTH) / 2;
        int top = (this.height - BOOK_HEIGHT) / 2;

        boolean isRu = isRussianLanguage();

        // Previous page button (bottom left)
        if (currentChapter > 0) {
            this.addRenderableWidget(Button.builder(Component.literal("◀"), b -> {
                currentChapter--;
                playPageTurnSound();
                rebuildWidgets();
            }).bounds(left + 16, top + BOOK_HEIGHT - 22, 24, 16).build());
        }

        // Table of contents button (bottom center-left)
        if (currentChapter > 0) {
            this.addRenderableWidget(Button.builder(Component.literal(isRu ? "Оглавление" : "Index"), b -> {
                currentChapter = 0;
                playPageTurnSound();
                rebuildWidgets();
            }).bounds(left + 44, top + BOOK_HEIGHT - 22, 60, 16).build());
        }

        // Next page button (bottom right)
        if (currentChapter < chapters.size() - 1) {
            this.addRenderableWidget(Button.builder(Component.literal("▶"), b -> {
                currentChapter++;
                playPageTurnSound();
                rebuildWidgets();
            }).bounds(left + BOOK_WIDTH - 40, top + BOOK_HEIGHT - 22, 24, 16).build());
        }

        // Close button (bottom center-right)
        this.addRenderableWidget(Button.builder(Component.literal(isRu ? "Закрыть" : "Close"), b -> {
            this.onClose();
        }).bounds(left + BOOK_WIDTH - 96, top + BOOK_HEIGHT - 22, 52, 16).build());

        // Quick navigation buttons on Table of Contents page (chapter 0)
        if (currentChapter == 0) {
            int rightX = left + 152;
            int btnY = top + 34;
            int btnHeight = 12;
            for (int i = 1; i <= 10; i++) {
                final int chapIndex = i;
                Chapter ch = chapters.get(i);
                String btnText = isRu ? ch.titleRu : ch.titleEn;
                if (btnText.length() > 20) btnText = btnText.substring(0, 18) + "..";
                this.addRenderableWidget(Button.builder(Component.literal(btnText), b -> {
                    currentChapter = chapIndex;
                    playPageTurnSound();
                    rebuildWidgets();
                }).bounds(rightX, btnY + (i - 1) * 13, 116, btnHeight).build());
            }
        }
    }

    private void playPageTurnSound() {
        if (this.minecraft != null) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }

    private boolean isRussianLanguage() {
        if (this.minecraft != null && this.minecraft.getLanguageManager() != null) {
            return this.minecraft.getLanguageManager().getSelected().toLowerCase().startsWith("ru");
        }
        return true;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        int left = (this.width - BOOK_WIDTH) / 2;
        int top = (this.height - BOOK_HEIGHT) / 2;

        // Render book open spread
        guiGraphics.blit(BOOK_TEXTURE, left, top, 0, 0, BOOK_WIDTH, BOOK_HEIGHT, TEX_WIDTH, TEX_HEIGHT);

        if (currentChapter < 0 || currentChapter >= chapters.size()) return;
        Chapter chapter = chapters.get(currentChapter);
        boolean isRu = isRussianLanguage();

        // Left Page: x: left + 16, width: 116
        int leftX = left + 18;
        int rightX = left + 152;
        int contentY = top + 14;

        // Chapter title on Left page
        String title = isRu ? chapter.titleRu : chapter.titleEn;
        guiGraphics.drawString(this.font, Component.literal(title), leftX, contentY, COLOR_TITLE, false);

        // Chapter picture if present
        int textOffsetY = contentY + 12;
        if (chapter.picture != null) {
            int picX = leftX + (114 - chapter.picW) / 2;
            int picY = textOffsetY;
            guiGraphics.blit(chapter.picture, picX, picY, 0, 0, chapter.picW, chapter.picH, chapter.picW, chapter.picH);
            textOffsetY += chapter.picH + 4;
        }

        // Left page body text
        String leftText = isRu ? chapter.leftPageRu : chapter.leftPageEn;
        guiGraphics.drawWordWrap(this.font, Component.literal(leftText), leftX, textOffsetY, 114, COLOR_TEXT);

        // Right page body text (only when not on page 0 or alongside buttons)
        if (currentChapter > 0) {
            String rightText = isRu ? chapter.rightPageRu : chapter.rightPageEn;
            guiGraphics.drawWordWrap(this.font, Component.literal(rightText), rightX, contentY + 4, 114, COLOR_TEXT);
        } else {
            // Page 0 Title for Right Page
            String tocHeader = isRu ? "Быстрый переход:" : "Jump to Chapter:";
            guiGraphics.drawString(this.font, Component.literal(tocHeader), rightX, contentY + 4, COLOR_SUBTITLE, false);
        }

        // Page Numbers in corners
        if (currentChapter > 0) {
            String pageNum = currentChapter + " / " + (chapters.size() - 1);
            guiGraphics.drawString(this.font, Component.literal(pageNum), left + BOOK_WIDTH / 2 - 14, top + BOOK_HEIGHT - 16, COLOR_MUTED, false);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT && currentChapter > 0) {
            currentChapter--;
            playPageTurnSound();
            rebuildWidgets();
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT && currentChapter < chapters.size() - 1) {
            currentChapter++;
            playPageTurnSound();
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
