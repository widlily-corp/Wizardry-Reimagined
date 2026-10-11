# Electroblob's Wizardry: Reimagined (Remind)

[![NeoForge](https://img.shields.io/badge/NeoForge-1.21.1-orange.svg)](https://neoforged.net/)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A modern, high-performance reimagining of the legendary RPG magic mod **Electroblob's Wizardry** for **Minecraft 1.21.1 (NeoForge)**.

---

## 🌟 Acknowledgements & Credits

This project is a modern reimagining inspired by and built upon the incredible legacy of **Electroblob's Wizardry**:

* **[Electroblob's Wizardry (Original 1.7.10 / 1.12.2)](https://github.com/Electroblob77/Wizardry)** by **Electroblob77** — The visionary creator of the original *Electroblob's Wizardry*. Thank you for creating one of the most atmospheric, beloved, and iconic magic mods in Minecraft history.
* **[Electroblob's Wizardry Redux (1.20.1)](https://github.com/Binaris00/ElectroblobsWizardryRedux)** by **Binaris00** and the Redux Team — For their tremendous work on the modern multi-loader port, texture reworks, and keeping the spirit of wizardry alive.
* **WinDanesz** — Dedicated maintainer and contributor of the 1.12.2 codebase.
* **NinjaFrito** — Creator of the refined modern spell and item textures.
* **Min01 & 19** — Early port contributors and pioneers.

---

## 🔮 Core Vision & Reimagining

While staying true to the nostalgic 1-to-1 visual particle aesthetic and authentic sound design of the original, **Wizardry: Reimagined** fundamentally overhauls the progression philosophy:

### 1. 🛠️ Total Crafting Agency (No More Pure Loot Dependency)
* In the original mod, players were forced to rely strictly on dungeon chest RNG, rare wizard towers, and trading to obtain spells and wand upgrades.
* In **Reimagined**, **everything is craftable directly at your arcane base**:
  * **Blank Scrolls & Spell Books** crafted and inscribed using elemental crystals, arcane dust, and mob reagents.
  * **Smithing Table Progression**: Wands upgrade across tiers (Novice ➔ Apprentice ➔ Advanced ➔ Master) via Arcane Smithing Templates and experience.
  * **Wand Upgrades** (Storage, Siphon, Range, Blast, Cooldown, Condenser, Attunement, Duration) craftable through vanilla crafting recipes.

### 2. ⚡ The Classic Arcane Workbench
* The iconic **Arcane Workbench** is preserved 1-to-1 as your specialized wand tuning station:
  * Mana recharging with magic crystals.
  * Spell slot rotation and dynamic binding.
  * Modifier inspection and upgrade tracking.

### 3. 🚀 Modern NeoForge 1.21.1 Architecture
* **Data Components**: Replaces legacy NBT CompoundTags with modern `DataComponentType<WandData>`, `DataComponentType<SpellData>`, and `DataComponentType<UpgradeData>`.
* **Payload Networking**: High-speed, packet-safe network messaging using NeoForge `CustomPacketPayload` and `PayloadRegistrar`.
* **Jigsaw Worldgen**: Wizard Towers, Shrines, and Obelisks generated through modern 1.21.1 Structure Template Pools (providing speed-up bonuses for adventurous explorers).
* **Curios & Accessories Integration**: Seamless bauble support with an automatic graceful fallback to player inventory if accessory mods are absent.

---

## 🧙 Magic Elements & Tiers

### 8 Elemental Schools
* 🔥 **Fire** (Pyromancy)
* ❄️ **Ice** (Cryomancy)
* ⚡ **Lightning** (Electromancy)
* 💀 **Necromancy** (Dark Arts)
* 🌿 **Earth** (Geomancy & Nature)
* 🌀 **Sorcery** (Arcane Manipulation & Telekinesis)
* ✨ **Healing** (Restoration & Holy Magic)
* 🔮 **Magic** (Universal & Force)

### 4 Mastery Tiers
1. **Novice** — Humble beginnings and foundational cantrips.
2. **Apprentice** — Versatile spells and elemental specialization.
3. **Advanced** — Powerful combat magic and tactical wards.
4. **Master** — Calamitous incantations (Meteors, Blizzards, Chain Lightnings).

---

## 📦 Building from Source

### Prerequisites
* Java 21 JDK (Adoptium Temurin recommended)
* Git

### Build Command
```bash
# Clone the repository
git clone https://github.com/widlily-corp/Wizardry-Reimagined.git

# Enter project directory
cd Wizardry-Reimagined

# Build with Gradle
./gradlew build
```

The compiled mod jar will be in `build/libs/`.

---

## 📜 License

Distributed under the **MIT License**. See `LICENSE` for details.
All original concepts and lore belong to Electroblob and respective contributors.
