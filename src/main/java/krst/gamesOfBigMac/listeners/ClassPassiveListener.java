package krst.gamesOfBigMac.listeners;

import krst.gamesOfBigMac.managers.ClassManager;
import krst.gamesOfBigMac.managers.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ClassPassiveListener implements Listener {

    private final ClassManager classManager;

    public ClassPassiveListener(ClassManager classManager, ConfigManager configManager) {
        this.classManager = classManager;
    }

    /**
     * При респавне заново применяем пассивные эффекты
     * (они бесконечные и не должны пропадать)
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        // Применяем пассивки через небольшую задержку
        org.bukkit.Bukkit.getScheduler().runTaskLater(
                org.bukkit.Bukkit.getPluginManager().getPlugin("BorderShrink"),
                () -> classManager.applyPassiveEffects(event.getPlayer()),
                5L // 5 тиков = 0.25 секунды
        );
    }
}