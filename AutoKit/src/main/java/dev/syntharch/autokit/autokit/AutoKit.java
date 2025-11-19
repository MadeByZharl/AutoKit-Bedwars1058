package dev.syntharch.autokit.autokit;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.andrei1058.bedwars.api.events.player.PlayerReSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class    AutoKit extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("BedWars1058") == null) return;
        saveDefaultConfig();
        Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("AutoKit (Simple Config) enabled!");
    }

    // Старт игры
    @EventHandler
    public void onGameStart(GameStateChangeEvent event) {
        if (!event.getNewState().toString().toLowerCase().contains("playing")) return;
        IArena arena = event.getArena();

        Bukkit.getScheduler().runTaskLater(this, () -> {
            for (ITeam team : arena.getTeams()) {
                for (Player p : team.getMembers()) {
                    giveSimpleKit(p, team);
                }
            }
        }, 5L);
    }

    // Респавн
    @EventHandler
    public void onBedwarsRespawn(PlayerReSpawnEvent event) {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            giveSimpleKit(event.getPlayer(), event.getTeam());
        }, 5L);
    }

    private void giveSimpleKit(Player p, ITeam team) {
        PlayerInventory inv = p.getInventory();

        // Очищаем только предметы (0-35), броню не трогаем (как ты просил не выдавать, но и не снимаем, если она есть)
        inv.clear();

        List<String> items = getConfig().getStringList("items");

        for (String line : items) {
            try {
                // Разбиваем строку по пробелам
                // Пример: "WOODEN_PICKAXE 1 DIG_SPEED:1" -> ["WOODEN_PICKAXE", "1", "DIG_SPEED:1"]
                String[] parts = line.split(" ");
                if (parts.length == 0) continue;

                String matName = parts[0];
                int amount = 1;
                if (parts.length >= 2) {
                    try {
                        amount = Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        getLogger().warning("Неверное количество в: " + line);
                    }
                }

                // Определяем материал
                Material material;
                if (matName.equalsIgnoreCase("TEAM_WOOL")) {
                    material = getTeamWool(team.getColor());
                } else {
                    material = Material.getMaterial(matName.toUpperCase());
                }

                if (material == null) {
                    getLogger().warning("Неверный материал: " + matName);
                    continue;
                }

                ItemStack item = new ItemStack(material, amount);

                // Обработка зачарований (если есть 3-й аргумент и дальше)
                // Пример: DIG_SPEED:1
                if (parts.length >= 3) {
                    for (int i = 2; i < parts.length; i++) {
                        String[] enchData = parts[i].split(":");
                        if (enchData.length == 2) {
                            Enchantment ench = Enchantment.getByName(enchData[0].toUpperCase());
                            int level = Integer.parseInt(enchData[1]);
                            if (ench != null) {
                                item.addEnchantment(ench, level);
                            }
                        }
                    }
                }

                // Делаем неразрушаемым
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setUnbreakable(true);
                    item.setItemMeta(meta);
                }

                // Выдаем в первый свободный слот
                inv.addItem(item);

            } catch (Exception e) {
                getLogger().warning("Ошибка в строке конфига: " + line);
            }
        }

        p.updateInventory();
    }

    private Material getTeamWool(TeamColor tc) {
        try {
            return Material.valueOf(tc.name() + "_WOOL");
        } catch (Exception e) {
            return Material.WHITE_WOOL;
        }
    }
}