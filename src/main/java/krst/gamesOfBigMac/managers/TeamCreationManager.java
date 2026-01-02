package krst.gamesOfBigMac.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamCreationManager {

    private final TeamManager teamManager;
    private final Map<UUID, String> awaitingTeamName = new HashMap<>();
    private final Map<UUID, String> pendingTeamNames = new HashMap<>();

    public TeamCreationManager(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    /**
     * Начинает процесс создания команды
     */
    public void startTeamCreation(Player player) {
        // Открываем GUI наковальни для ввода имени
        Inventory anvil = Bukkit.createInventory(null, 9, "Введите название команды");

        ItemStack nameItem = new ItemStack(Material.NAME_TAG);
        anvil.setItem(0, nameItem);

        awaitingTeamName.put(player.getUniqueId(), "");
        player.openInventory(anvil);
    }

    /**
     * Устанавливает имя команды после ввода
     */
    public void setTeamName(UUID playerId, String teamName) {
        pendingTeamNames.put(playerId, teamName);
        awaitingTeamName.remove(playerId);
    }

    /**
     * Создает команду с выбранным цветом
     */
    public void createTeam(Player player, ChatColor color) {
        String teamName = pendingTeamNames.get(player.getUniqueId());
        if (teamName == null) {
            player.sendMessage("§c❌ Ошибка: имя команды не найдено!");
            return;
        }

        // Проверяем, существует ли уже команда с таким именем
        Team existingTeam = teamManager.getScoreboard().getTeam(teamName);
        if (existingTeam != null) {
            player.sendMessage("§c❌ Команда с таким именем уже существует!");
            return;
        }

        // Создаем команду
        Team team = teamManager.getScoreboard().registerNewTeam(teamName);
        team.setColor(color);
        team.setAllowFriendlyFire(false);
        team.setCanSeeFriendlyInvisibles(true);

        // Добавляем игрока в команду
        teamManager.addPlayerToTeam(player, teamName);

        // Очищаем временные данные
        pendingTeamNames.remove(player.getUniqueId());

        player.sendMessage("§a✓ Команда §r" + color + teamName + " §aсоздана!");
        player.closeInventory();
    }

    public boolean isAwaitingTeamName(UUID playerId) {
        return awaitingTeamName.containsKey(playerId);
    }

    public void cancelCreation(UUID playerId) {
        awaitingTeamName.remove(playerId);
        pendingTeamNames.remove(playerId);
    }
}
