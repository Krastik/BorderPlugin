package krst.gamesOfBigMac.listeners;

import krst.gamesOfBigMac.managers.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final GameManager gameManager;

    public PlayerJoinListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Добавляем игрока в BossBar, если игра активна
        if (gameManager.isActive() && gameManager.getTimer() != null) {
            gameManager.getTimer().addPlayer(event.getPlayer());
        }
    }
}