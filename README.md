# Electroblob's Wizardry: Reimagined

[![Modrinth](https://img.shields.io/badge/Modrinth-Wizardry_Reimagined-00AF5C?logo=modrinth&logoColor=white)](https://modrinth.com/mod/wizardry-reimagined/)
[![NeoForge](https://img.shields.io/badge/NeoForge-1.21.1-orange.svg)](https://neoforged.net/)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A modern, high-performance reimagining of the legendary RPG magic mod **Electroblob's Wizardry** for **Minecraft 1.21.1 (NeoForge)**.

📥 **[Download on Modrinth: https://modrinth.com/mod/wizardry-reimagined/](https://modrinth.com/mod/wizardry-reimagined/)**

---

## 🌟 Acknowledgements & Credits

This project is a modern reimagining inspired by and built upon the incredible legacy of **Electroblob's Wizardry**:

* **[Electroblob's Wizardry (Original 1.7.10 / 1.12.2)](https://github.com/Electroblob77/Wizardry)** by **Electroblob77** — The visionary creator of the original *Electroblob's Wizardry*. Thank you for creating one of the most atmospheric, beloved, and iconic magic mods in Minecraft history.
* **[Electroblob's Wizardry Redux (1.20.1)](https://github.com/Binaris00/ElectroblobsWizardryRedux)** by **Binaris00** and the Redux Team — For their tremendous work on the modern multi-loader port, texture reworks, and keeping the spirit of wizardry alive.

---

## 🔮 Core Vision & Reimagining

While staying true to the nostalgic 1-to-1 visual particle aesthetic and authentic sound design of the original, **Wizardry: Reimagined** fundamentally overhauls progression and end-game magic:

### 1. 🛠️ Total Crafting Agency & Full JEI Support
* In the original mod, players were forced to rely strictly on dungeon chest RNG, rare wizard towers, and trading to obtain spells and wand upgrades.
* In **Reimagined**, **everything is craftable directly at your arcane base** with full **JEI (Just Enough Items)** recipe integration:
  * **Blank Scrolls & Spell Books** crafted and inscribed using elemental crystals, arcane dust, and mob reagents.
  * **Smithing Table Progression**: Wands upgrade across tiers (Novice ➔ Apprentice ➔ Advanced ➔ Master) via Arcane Smithing Templates.
  * **Wand Upgrades** (Storage, Siphon, Range, Blast, Cooldown, Condenser, Attunement, Duration, Melee) craftable through standard crafting recipes.
  * **Imbuement Altar**: Infuse wizard robes, wands, and receptacles with elemental crystals on surrounding pedestals.

### 2. 📖 In-Game Wizard's Handbook
* Craft the illustrated **Wizard's Handbook** (`Book + Magic Crystal`) — an interactive 11-chapter dual-page guide covering every mechanic, crafting station, elemental school, wand upgrade, and mastery tier in both **English** and **Russian**.

### 3. ⚡ The Classic Arcane Workbench
* The iconic **Arcane Workbench** is preserved 1-to-1 as your specialized wand tuning station:
  * Mana recharging with magic crystals (Shard, Regular, and Grand Crystals).
  * Spell slot binding, book/scroll inscription, and wand upgrade application.
  * Decorative **Arcane Bookshelves** (Oak, Birch, Spruce, Jungle, Acacia, Dark Oak) & **Lecterns**.

### 4. 🚀 Modern NeoForge 1.21.1 Architecture
* **Data Components**: Replaces legacy NBT CompoundTags with modern `DataComponentType` records.
* **Payload Networking**: High-speed, packet-safe network messaging using NeoForge `CustomPacketPayload` and `PayloadRegistrar`.
* **Jigsaw Worldgen**: Wizard Towers, Shrines, and Obelisks generated through modern 1.21.1 Structure Template Pools.
* **Curios Integration**: Seamless bauble support for rings, amulets, and charms with automatic inventory fallback.

---

## 🧙 Magic Elements & 5 Mastery Tiers (146 Spells)

### 8 Elemental Schools
* 🔥 **Fire** (Pyromancy)
* ❄️ **Ice** (Cryomancy)
* ⚡ **Lightning** (Electromancy)
* 💀 **Necromancy** (Dark Arts)
* 🌿 **Earth** (Geomancy & Nature)
* 🌀 **Sorcery** (Arcane Manipulation & Telekinesis)
* ✨ **Healing** (Restoration & Holy Magic)
* 🔮 **Magic** (Universal & Force)

### 5 Mastery Tiers
1. **Novice** — Humble beginnings and foundational cantrips.
2. **Apprentice** — Versatile spells and elemental specialization.
3. **Advanced** — Powerful combat magic and tactical wards.
4. **Master** — Calamitous incantations (Meteors, Blizzards, Diamondflesh, Font of Mana).
5. **Supreme (New!)** — World-shattering cataclysms with **12-second, 50-block radius cinematic aftermath zones**:
   * 🔥 *Supernova* — Thermonuclear crater with fire tornadoes & ash storms.
   * ❄️ *Absolute Zero* — Cryo-anomaly freezing terrain with shattering ice monoliths.
   * ⚡ *Thunderstorm* — Ionized geomagnetic storm with continuous chain-lightning strikes.
   * 💀 *Zombie Apocalypse* — Dark Crimson Abyssal Rift in the sky raining undead legions.
   * 🌿 *Tectonic Cataclysm* — Geothermal magma geysers, seismic tremors & collapsing monolith avalanches.
   * 🌀 *Chrono Stasis* — Temporal anomaly Dome freezing projectiles and enemies before a quantum implosion.
   * ✨ *Sanctuary of Astraea* — Consecrated golden dome of divine judgment, healing rays, and celestial pillars.
   * 🔮 *Arcane Singularity* — Gravitational black hole vortex pulling matter across 50 blocks.

---

## 📦 Installation & Building

### Download Release
Get the latest build directly from **[Modrinth](https://modrinth.com/mod/wizardry-reimagined/)**.

### Building from Source
```bash
# Clone the repository
git clone https://github.com/widlily-corp/Wizardry-Reimagined.git

# Enter project directory
cd Wizardry-Reimagined

# Build with Gradle (Java 21 required)
./gradlew build
```

The compiled mod jar will be located in `build/libs/`.

---

## 📜 License

Distributed under the **MIT License**. See `LICENSE` for details.
All original concepts and lore belong to Electroblob and respective contributors.
