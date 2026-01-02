package krst.gamesOfBigMac.listeners;

import krst.gamesOfBigMac.managers.ClassManager;
import krst.gamesOfBigMac.managers.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final TeamManager teamManager;
    private final ClassManager classManager;

    public PlayerQuitListener(TeamManager teamManager, ClassManager classManager) {
        this.teamManager = teamManager;
        this.classManager = classManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Удаляем из команды (автоматически удалит пустые команды)
        teamManager.removePlayerFromTeam(player);

        // Очищаем данные класса
        classManager.clearData(player);
    }
}
