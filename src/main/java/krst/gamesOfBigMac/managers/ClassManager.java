package krst.gamesOfBigMac.managers;

import krst.gamesOfBigMac.models.PlayerData;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Менеджер классов игроков
 * Управляет выбором класса, готовностью и применением пассивных эффектов
 */
public class ClassManager {

    private final Map<UUID, PlayerData> playerData;
    private final Set<String> availableClasses;

    public ClassManager() {
        this.playerData = new ConcurrentHashMap<>();
        this.availableClasses = Set.of(
                "Мечник", "Добытчик", "Танк", "Лучник", "Алхимик"
        );
    }

    /**
     * Устанавливает класс для игрока
     */
    public void setPlayerClass(Player player, String className) {
        if (!availableClasses.contains(className)) {
            throw new IllegalArgumentException("Неизвестный класс: " + className);
        }

        PlayerData data = getOrCreateData(player);
        data.setSelectedClass(className);
    }

    /**
     * Получает класс игрока
     */
    public String getPlayerClass(Player player) {
        return getOrCreateData(player).getSelectedClass();
    }

    /**
     * Устанавливает статус готовности игрока
     */
    public void setReady(Player player, boolean ready) {
        getOrCreateData(player).setReady(ready);
    }

    /**
     * Проверяет готовность игрока
     */
    public boolean isReady(Player player) {
        return getOrCreateData(player).isReady();
    }

    /**
     * Получает всех готовых игроков
     */
    public Set<UUID> getReadyPlayers() {
        Set<UUID> ready = new HashSet<>();
        for (Map.Entry<UUID, PlayerData> entry : playerData.entrySet()) {
            if (entry.getValue().isReady()) {
                ready.add(entry.getKey());
            }
        }
        return ready;
    }

    /**
     * Применяет ПАССИВНЫЕ эффекты класса (бесконечные)
     * Вызывается при старте игры и после каждого респавна
     *
     * ВАЖНО: Эффекты применяются с duration = Integer.MAX_VALUE
     * Это означает, что они НЕ ПРОПАДУТ до конца игры
     */
    public void applyPassiveEffects(Player player) {
        String className = getPlayerClass(player).toLowerCase();

        // Очищаем старые эффекты перед применением новых
        player.removePotionEffect(PotionEffectType.SLOWNESS);
        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);

        switch (className) {
            case "танк":
                // Медлительность III (бесконечная)
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.SLOWNESS,
                        Integer.MAX_VALUE,  // ← БЕСКОНЕЧНО!
                        2,                   // Уровень 3 (Slowness III)
                        false,               // ambient
                        false,               // particles
                        true                 // icon
                ));

                // Дополнительное HP (бесконечное)
                // +10 HP = +5 сердечек = уровень 4 (каждый уровень = +2 HP)
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.HEALTH_BOOST,
                        Integer.MAX_VALUE,  // ← БЕСКОНЕЧНО!
                        4,                   // +10 HP
                        false,
                        false,
                        true
                ));

                // Восстанавливаем здоровье до максимума
                double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
                player.setHealth(maxHealth);
                break;

            // Остальные классы не имеют постоянных эффектов
            // Их пассивки работают через модификаторы урона в DamageListener
            case "мечник":
                // +10% урон мечом, -10% входящий урон (в DamageListener)
                break;

            case "лучник":
                // +15% урон луком, -10% входящий урон (в DamageListener)
                break;

            case "добытчик":
                // +50% прочность инструментов, -10% PvP урон (в DamageListener)
                break;

            case "алхимик":
                // +30% длительность зелий, -20% PvP урон (в DamageListener)
                break;

            default:
                // Класс не выбран или неизвестен
                break;
        }
    }

    /**
     * Получает или создаёт данные игрока
     */
    private PlayerData getOrCreateData(Player player) {
        return playerData.computeIfAbsent(
                player.getUniqueId(),
                uuid -> new PlayerData(uuid)
        );
    }

    /**
     * Очищает данные игрока
     */
    public void clearData(Player player) {
        playerData.remove(player.getUniqueId());
    }

    /**
     * Очищает все данные
     */
    public void clearAllData() {
        playerData.clear();
    }

    /**
     * Получает список доступных классов
     */
    public Set<String> getAvailableClasses() {
        return availableClasses;
    }

    /**
     * Проверяет, есть ли у игрока данные
     */
    public boolean hasData(Player player) {
        return playerData.containsKey(player.getUniqueId());
    }

    /**
     * Получает количество игроков с данными
     */
    public int getPlayerCount() {
        return playerData.size();
    }
}