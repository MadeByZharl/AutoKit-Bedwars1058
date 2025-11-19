# AutoKit-Bedwars1058

A lightweight, high-performance add-on for [BedWars1058](https://www.spigotmc.org/resources/bedwars1058-minigame.39904/) that automatically equips players with configurable kits immediately upon game start and respawn. 

Designed specifically for fast-paced modes like **Fireball Fight** or **Rush**, where players need instant access to blocks and tools without purchasing them from the shop.

## Features

* ⚡ **Instant Equip:** Items are given 1 tick after the game starts or a player respawns, overriding default items.
* 🛡️ **Team Armor & Wool:** Automatically detects team color and dyes leather armor/wool accordingly.
* ⚙️ **Per-Arena Configuration:** Define specific kits for different maps or use a global default.
* ✨ **Enchantment Support:** Easily add enchantments to tools and weapons via config.
* 🚫 **No Bloat:** Extremely lightweight code (~100 lines), no unnecessary checks or heavy loops.

## Installation

1.  Ensure you have **BedWars1058** installed.
2.  Download `AutoKit-1.0.0.jar` and place it in your `/plugins` folder.
3.  Restart the server.
4.  Edit `plugins/AutoKit/config.yml` to customize your loadout.

## Configuration

The configuration allows you to define a default kit and specific kits for arena names.

**Format:** `MATERIAL AMOUNT ENCHANTMENT:LEVEL`

```yaml
# Enable/Disable Leather Armor (Helmet, Chest, Leggings, Boots)
default:
  armor: true
  items:
    - STONE_SWORD 1
    - TEAM_WOOL 64
    - FIRE_CHARGE 2
    - DIAMOND_PICKAXE 1 DIG_SPEED:1

# Specific Arena Overrides
arenas:
  FireballFight:
    armor: false
    items:
      - WOODEN_SWORD 1
      - TEAM_WOOL 128
