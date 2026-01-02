package krst.gamesOfBigMac.listeners;

import krst.gamesOfBigMac.managers.ClassManager;
import krst.gamesOfBigMac.managers.ConfigManager;
import krst.gamesOfBigMac.managers.GameManager;
import krst.gamesOfBigMac.models.ClassPassives;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    private final GameManager gameManager;
    private final ClassManager classManager;
    private final ClassPassives passives;

    public DamageListener(GameManager gameManager, ClassManager classManager, ConfigManager configManager) {
        this.gameManager = gameManager;
        this.classManager = classManager;
        this.passives = configManager.getClassPassives();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        // Проверка no-damage периода
        if (gameManager.isDamageDisabled()) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        String victimClass = classManager.getPlayerClass(victim).toLowerCase();
        String attackerClass = classManager.getPlayerClass(attacker).toLowerCase();

        double damage = event.getDamage();

        // Модификаторы защиты жертвы
        switch (victimClass) {
            case "лучник":
                // Лучник получает меньше урона в ближнем бою
                damage *= passives.archerMeleeDecrease;
                break;

            case "мечник":
                // Мечник получает меньше урона в ближнем бою
                damage *= passives.swordsmanDamageDecrease;
                break;

            case "добытчик":
                // Добытчик получает меньше PvP урона
                damage *= passives.farmerPvpDecrease;
                break;

            case "алхимик":
                // Алхимик получает меньше PvP урона
                damage *= passives.alchemistPvpDecrease;
                break;
        }

        // Модификаторы атаки нападающего
        Material weapon = attacker.getInventory().getItemInMainHand().getType();

        switch (attackerClass) {
            case "лучник":
                if (weapon == Material.BOW) {
                    // Лучник наносит больше урона луком
                    damage *= passives.archerBowIncrease;
                }
                break;

            case "мечник":
                if (weapon.toString().contains("SWORD")) {
                    // Мечник наносит больше урона мечом
                    damage *= passives.swordsmanSwordIncrease;
                }
                break;
        }

        event.setDamage(damage);
    }
}