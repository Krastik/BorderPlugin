package krst.gamesOfBigMac.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;

public class TeamManager {

    private final Scoreboard scoreboard;
    private int maxTeams;

    public TeamManager() {
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        this.maxTeams = 4;
    }

    public void setMaxTeams(int max) {
        this.maxTeams = Math.max(1, Math.min(27, max));
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public void addPlayerToTeam(Player player, String teamName) {
        // Удаляем из старой команды
        removePlayerFromTeam(player);

        Team team = scoreboard.getTeam(teamName);
        if (team != null) {
            team.addEntry(player.getName());
        }
    }

    public void removePlayerFromTeam(Player player) {
        Team team = scoreboard.getPlayerTeam(player);
        if (team != null) {
            team.removeEntry(player.getName());

            // Проверяем, осталась ли команда пустой
            checkAndRemoveEmptyTeam(team);
        }
    }

    /**
     * Удаляет команду, если в ней меньше 1 игрока
     */
    private void checkAndRemoveEmptyTeam(Team team) {
        if (team.getEntries().isEmpty()) {
            String teamName = team.getName();
            team.unregister();
            Bukkit.broadcastMessage("§c✗ Команда §r" + teamName + " §cбыла расформирована (нет игроков)");
        }
    }

    /**
     * Проверяет все команды и удаляет пустые
     */
    public void cleanupEmptyTeams() {
        for (Team team : scoreboard.getTeams()) {
            if (team.getEntries().isEmpty()) {
                team.unregister();
            }
        }
    }

    /**
     * Получает количество созданных команд
     */
    public int getTeamCount() {
        return (int) scoreboard.getTeams().stream()
                .filter(team -> !team.getEntries().isEmpty())
                .count();
    }

    /**
     * Проверяет, можно ли создать еще одну команду
     */
    public boolean canCreateTeam() {
        return getTeamCount() < maxTeams;
    }

    public Team getPlayerTeam(Player player) {
        return scoreboard.getPlayerTeam(player);
    }

    public Collection<Team> getAllTeams() {
        return scoreboard.getTeams();
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
