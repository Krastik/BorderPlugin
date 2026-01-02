package krst.gamesOfBigMac.listeners;

import krst.gamesOfBigMac.managers.*;
import krst.gamesOfBigMac.menus.ClassesMenu;
import krst.gamesOfBigMac.menus.SettingsMenu;
import krst.gamesOfBigMac.menus.TeamCreationMenu;
import krst.gamesOfBigMac.menus.TeamsMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scoreboard.Team;

public class MenuListener implements Listener {

    private final GameManager gameManager;
    private final TeamManager teamManager;
    private final ClassManager classManager;
    private final TeamCreationManager teamCreationManager;

    public MenuListener(GameManager gameManager, TeamManager teamManager,
                        ClassManager classManager, TeamCreationManager teamCreationManager) {
        this.gameManager = gameManager;
        this.teamManager = teamManager;
        this.classManager = classManager;
        this.teamCreationManager = teamCreationManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (e.getCurrentItem() == null) return;

        InventoryView view = e.getView();
        String title = view.getTitle();

        // ==================== SETTINGS MENU ====================

        if (title.equals(SettingsMenu.MAIN_TITLE)) {
            e.setCancelled(true);
            handleSettingsMainMenu(player, e.getSlot());
            return;
        }

        if (title.equals(SettingsMenu.SIZE_TITLE)) {
            e.setCancelled(true);
            handleSettingsSizeMenu(player, e.getSlot());
            return;
        }

        if (title.equals(SettingsMenu.TIME_TITLE)) {
            e.setCancelled(true);
            handleSettingsTimeMenu(player, e.getSlot());
            return;
        }

        if (title.equals(SettingsMenu.START_TITLE)) {
            e.setCancelled(true);
            handleSettingsStartMenu(player, e.getSlot());
            return;
        }

        if (title.startsWith("Количество команд:")) {
            e.setCancelled(true);
            handleSettingsTeamsMenu(player, e.getSlot());
            return;
        }

        // ==================== TEAM CREATION MENU ====================

        if (title.equals(TeamCreationMenu.TITLE)) {
            e.setCancelled(true);
            handleTeamCreationMenu(player, e.getSlot());
            return;
        }

        if (title.equals(TeamCreationMenu.COLOR_TITLE)) {
            e.setCancelled(true);
            handleColorSelectionMenu(player, e);
            return;
        }

        // ==================== TEAMS MENU ====================

        if (title.equals(TeamsMenu.MAIN_TITLE)) {
            e.setCancelled(true);
            handleTeamsMainMenu(player, e);
            return;
        }

        // Проверка меню команды (по паттерну заголовка)
        if (title.contains("ГОТОВ") || title.contains("НЕ ГОТОВ")) {
            e.setCancelled(true);
            handleTeamMenu(player, e.getSlot(), title);
            return;
        }

        // ==================== CLASSES MENU ====================

        if (title.equals(ClassesMenu.TITLE)) {
            e.setCancelled(true);
            handleClassesMenu(player, e.getSlot());
            return;
        }
    }

    // ==================== HANDLERS ====================

    private void handleSettingsMainMenu(Player player, int slot) {
        switch (slot) {
            case 10 -> player.openInventory(SettingsMenu.createSize());
            case 12 -> player.openInventory(SettingsMenu.createTime());
            case 14 -> player.openInventory(SettingsMenu.createTeams(teamManager.getMaxTeams()));
            case 16 -> player.openInventory(SettingsMenu.createStart());
        }
    }

    private void handleSettingsSizeMenu(Player player, int slot) {
        switch (slot) {
            case 12 -> {
                player.closeInventory();
                player.sendMessage("§bВведите финальный размер:");
                ChatInputManager.startInput(player.getUniqueId(), "bordermin");
            }
            case 15 -> {
                player.closeInventory();
                player.sendMessage("§bВведите изначальный размер:");
                ChatInputManager.startInput(player.getUniqueId(), "bordermax");
            }
            case 26 -> player.openInventory(SettingsMenu.createMain());
        }
    }

    private void handleSettingsTimeMenu(Player player, int slot) {
        switch (slot) {
            case 10 -> {
                player.closeInventory();
                player.sendMessage("§bВведите время игры (в секундах):");
                ChatInputManager.startInput(player.getUniqueId(), "bordertime");
            }
            case 12 -> {
                player.closeInventory();
                player.sendMessage("§bКогда начнётся сужение (секунд до конца):");
                ChatInputManager.startInput(player.getUniqueId(), "borderat");
            }
            case 14 -> {
                player.closeInventory();
                player.sendMessage("§bКогда включатся перманентные смерти (секунд до конца):");
                ChatInputManager.startInput(player.getUniqueId(), "deathsat");
            }
            case 16 -> {
                player.closeInventory();
                player.sendMessage("§bКогда включатся убийства (секунд от начала):");
                ChatInputManager.startInput(player.getUniqueId(), "nodamage");
            }
            case 26 -> player.openInventory(SettingsMenu.createMain());
        }
    }

    private void handleSettingsStartMenu(Player player, int slot) {
        switch (slot) {
            case 11 -> {
                player.closeInventory();
                try {
                    gameManager.startPreparation(); // Изменено!
                } catch (Exception ex) {
                    player.sendMessage("§c❌ Ошибка: " + ex.getMessage());
                }
            }
            case 15 -> player.openInventory(SettingsMenu.createMain());
        }
    }

    private void handleSettingsTeamsMenu(Player player, int slot) {
        int currentMax = teamManager.getMaxTeams();

        switch (slot) {
            case 11 -> {
                if (currentMax < 27) {
                    teamManager.setMaxTeams(currentMax + 1);
                }
                player.openInventory(SettingsMenu.createTeams(teamManager.getMaxTeams()));
            }
            case 15 -> {
                if (currentMax > 1) {
                    teamManager.setMaxTeams(currentMax - 1);
                }
                player.openInventory(SettingsMenu.createTeams(teamManager.getMaxTeams()));
            }
            case 26 -> player.openInventory(SettingsMenu.createMain());
        }
    }

    // ==================== TEAM CREATION HANDLERS ====================

    private void handleTeamCreationMenu(Player player, int slot) {
        if (slot == 13) {
            // Кнопка "Ввести имя команды"
            player.closeInventory();
            teamCreationManager.startTeamCreation(player);
        } else if (slot == 26) {
            // Отмена
            player.closeInventory();
            teamCreationManager.cancelCreation(player.getUniqueId());
            player.openInventory(TeamsMenu.createMain(teamManager));
        }
    }

    private void handleColorSelectionMenu(Player player, InventoryClickEvent e) {
        if (e.getSlot() == 26) {
            // Отмена
            teamCreationManager.cancelCreation(player.getUniqueId());
            player.closeInventory();
            return;
        }

        // Получаем цвет из предмета
        Material material = e.getCurrentItem().getType();
        ChatColor color = getColorFromWool(material);

        if (color != null) {
            teamCreationManager.createTeam(player, color);

            // Обновляем меню команд для всех
            for (Player p : org.bukkit.Bukkit.getOnlinePlayers()) {
                if (p.getOpenInventory().getTitle().equals(TeamsMenu.MAIN_TITLE)) {
                    p.openInventory(TeamsMenu.createMain(teamManager));
                }
            }
        }
    }

    private ChatColor getColorFromWool(Material wool) {
        return switch (wool) {
            case BLACK_WOOL -> ChatColor.BLACK;
            case BLUE_WOOL -> ChatColor.BLUE;
            case GREEN_WOOL -> ChatColor.GREEN;
            case CYAN_WOOL -> ChatColor.AQUA;
            case RED_WOOL -> ChatColor.RED;
            case PURPLE_WOOL -> ChatColor.DARK_PURPLE;
            case YELLOW_WOOL -> ChatColor.YELLOW;
            case GRAY_WOOL -> ChatColor.GRAY;
            case WHITE_WOOL -> ChatColor.WHITE;
            default -> null;
        };
    }

    // ==================== TEAMS MENU HANDLERS ====================

    private void handleTeamsMainMenu(Player player, InventoryClickEvent e) {
        if (!e.getCurrentItem().hasItemMeta()) return;

        Material type = e.getCurrentItem().getType();

        // Проверка на кнопку "Создать команду"
        if (type == Material.LIME_STAINED_GLASS_PANE) {
            if (!teamManager.canCreateTeam()) {
                player.sendMessage("§c❌ Достигнут лимит команд!");
                return;
            }
            player.openInventory(TeamCreationMenu.createMain());
            return;
        }

        // Выбор существующей команды
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        String teamName = ChatColor.stripColor(
                e.getCurrentItem().getItemMeta().getDisplayName()
        );

        Team team = teamManager.getScoreboard().getTeam(teamName);
        if (team == null) return;

        // Добавляем игрока в команду
        teamManager.addPlayerToTeam(player, teamName);

        // Открываем меню команды
        player.openInventory(TeamsMenu.createTeamMenu(teamName, classManager, player));
    }

    private void handleTeamMenu(Player player, int slot, String title) {
        // Извлекаем имя команды из заголовка
        String[] parts = title.split("\\|");
        if (parts.length < 3) return;

        String teamName = ChatColor.stripColor(parts[2].trim());
        Team team = teamManager.getScoreboard().getTeam(teamName);
        if (team == null) return;

        switch (slot) {
            case 18 -> { // Ready toggle
                boolean ready = classManager.isReady(player);
                classManager.setReady(player, !ready);
                player.openInventory(TeamsMenu.createTeamMenu(teamName, classManager, player));

                // Проверяем готовность после изменения
                gameManager.checkReadiness();
            }

            case 22 -> { // Class selection
                player.openInventory(ClassesMenu.create());
            }

            case 26 -> { // Leave team
                teamManager.removePlayerFromTeam(player);
                classManager.setReady(player, false);
                player.openInventory(TeamsMenu.createMain(teamManager));

                // Проверяем готовность после выхода
                gameManager.checkReadiness();
            }
        }
    }

    private void handleClassesMenu(Player player, int slot) {
        Team team = teamManager.getPlayerTeam(player);
        if (team == null) {
            player.sendMessage("§c❌ Сначала выберите команду!");
            player.closeInventory();
            return;
        }

        String className = switch (slot) {
            case 11 -> "Мечник";
            case 12 -> "Добытчик";
            case 13 -> "Танк";
            case 14 -> "Лучник";
            case 15 -> "Алхимик";
            case 26 -> {
                player.openInventory(TeamsMenu.createTeamMenu(team.getName(), classManager, player));
                yield null;
            }
            default -> null;
        };

        if (className != null) {
            classManager.setPlayerClass(player, className);
            player.openInventory(TeamsMenu.createTeamMenu(team.getName(), classManager, player));

            // Проверяем готовность после выбора класса
            gameManager.checkReadiness();
        }
    }
}
