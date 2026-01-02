package krst.gamesOfBigMac.menus;

import krst.gamesOfBigMac.managers.ClassManager;
import krst.gamesOfBigMac.managers.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TeamsMenu {

    public static final String MAIN_TITLE = "Выбор команды";

    public static Inventory createMain(TeamManager teamManager) {
        Inventory inv = Bukkit.createInventory(null, 27, MAIN_TITLE);

        int slot = 0;

        // Добавляем существующие команды
        for (Team team : teamManager.getAllTeams()) {
            if (slot >= 25) break; // Оставляем место для кнопки создания

            ChatColor color = team.getColor();
            DyeColor dyeColor = fromChatColor(color);

            ItemStack banner = new ItemStack(dyeToBanner(dyeColor));
            ItemMeta meta = banner.getItemMeta();

            meta.setDisplayName(color + team.getName());

            List<String> lore = new ArrayList<>();
            lore.add("§7Игроки: " + team.getEntries().size());
            if (!team.getEntries().isEmpty()) {
                team.getEntries().forEach(p -> lore.add(color + " - " + p));
            }
            meta.setLore(lore);

            banner.setItemMeta(meta);
            banner.setAmount(Math.min(64, Math.max(1, team.getEntries().size())));

            inv.setItem(slot++, banner);
        }

        // Кнопка создания новой команды (если есть место)
        if (teamManager.canCreateTeam()) {
            inv.setItem(26, ItemBuilder.create(Material.LIME_STAINED_GLASS_PANE)
                    .name("§a§l+ Создать команду")
                    .lore("§7Нажмите, чтобы создать новую команду")
                    .build());
        }

        return inv;
    }

    public static String getTeamMenuTitle(String teamName, String className, boolean ready, ChatColor teamColor) {
        String readyText = ready ? "§2ГОТОВ" : "§4НЕ ГОТОВ";
        return readyText + " §8§l| §0" + className + " §8§l| " + teamColor + teamName;
    }

    public static Inventory createTeamMenu(String teamName, ClassManager classManager, Player player) {
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
        if (team == null) {
            return Bukkit.createInventory(null, 27, "Ошибка");
        }

        String className = classManager.getPlayerClass(player);
        boolean ready = classManager.isReady(player);
        Set<UUID> readyPlayers = classManager.getReadyPlayers();

        Inventory inv = Bukkit.createInventory(
                null, 27,
                getTeamMenuTitle(teamName, className, ready, team.getColor())
        );

        // Кнопка готовности
        if (ready) {
            inv.setItem(18, ItemBuilder.create(Material.RED_STAINED_GLASS_PANE)
                    .name("&cЯ не готов")
                    .build());
        } else {
            inv.setItem(18, ItemBuilder.create(Material.GREEN_STAINED_GLASS_PANE)
                    .name("&aЯ готов")
                    .build());
        }

        // Кнопка выбора класса
        inv.setItem(22, ItemBuilder.create(Material.DIAMOND_SWORD)
                .name("&fВыбрать класс")
                .build());

        // Кнопка выхода
        inv.setItem(26, ItemBuilder.create(Material.RED_STAINED_GLASS_PANE)
                .name("&cВыйти из команды")
                .build());

        // Головы игроков
        int slot = 0;
        for (String memberName : team.getEntries()) {
            Player member = Bukkit.getPlayer(memberName);
            if (member == null) continue;

            if (slot == 18) slot = 19;
            if (slot == 22) slot = 23;
            if (slot >= 26) break;

            boolean memberReady = readyPlayers.contains(member.getUniqueId());
            inv.setItem(slot++, createPlayerHead(member, classManager, memberReady));
        }

        return inv;
    }

    private static ItemStack createPlayerHead(Player player, ClassManager classManager, boolean ready) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        meta.setOwningPlayer(player);
        meta.setDisplayName(player.getName());

        List<String> lore = new ArrayList<>();
        lore.add(ready ? "§aГотов" : "§cНе готов");
        lore.add("§fКласс: " + classManager.getPlayerClass(player));
        meta.setLore(lore);

        skull.setItemMeta(meta);
        return skull;
    }

    private static DyeColor fromChatColor(ChatColor color) {
        if (color == null) return DyeColor.WHITE;

        return switch (color) {
            case BLACK -> DyeColor.BLACK;
            case DARK_BLUE, BLUE -> DyeColor.BLUE;
            case DARK_GREEN, GREEN -> DyeColor.GREEN;
            case DARK_AQUA, AQUA -> DyeColor.CYAN;
            case DARK_RED, RED -> DyeColor.RED;
            case DARK_PURPLE, LIGHT_PURPLE -> DyeColor.PURPLE;
            case GOLD, YELLOW -> DyeColor.YELLOW;
            case GRAY, DARK_GRAY -> DyeColor.GRAY;
            default -> DyeColor.WHITE;
        };
    }

    private static Material dyeToBanner(DyeColor dye) {
        return Material.valueOf(dye.name() + "_BANNER");
    }
}