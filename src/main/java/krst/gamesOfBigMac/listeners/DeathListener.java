
package krst.gamesOfBigMac.listeners;

import krst.gamesOfBigMac.managers.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private final GameManager gameManager;

    public DeathListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        gameManager.handlePlayerDeath(event.getEntity());
    }
}
