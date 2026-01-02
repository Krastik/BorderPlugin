package krst.gamesOfBigMac.managers;

import krst.gamesOfBigMac.BorderShrinkPlugin;
import krst.gamesOfBigMac.models.BorderSettings;
import krst.gamesOfBigMac.models.ClassPassives;
import org.bukkit.configuration.file.FileConfiguration;


public class ConfigManager {

    private final BorderShrinkPlugin plugin;
    private final BorderSettings borderSettings;
    private final ClassPassives classPassives;

    public ConfigManager(BorderShrinkPlugin plugin) {
        this.plugin = plugin;
        this.borderSettings = new BorderSettings();
        this.classPassives = new ClassPassives();

        loadSettings();
    }

    private void loadSettings() {
        FileConfiguration config = plugin.getConfig();

        // Загрузка настроек границы (значения по умолчанию)
        borderSettings.setMinSize(config.getDouble("border.min_size", 50.0));
        borderSettings.setMaxSize(config.getDouble("border.max_size", 1000.0));
        borderSettings.setGameDurationSeconds(config.getLong("border.game_duration_seconds", 3600));
        borderSettings.setBorderStartDelaySeconds(config.getInt("border.start_delay_seconds", 300));
        borderSettings.setDeathsAtSeconds(config.getInt("border.deaths_at_seconds", -1));
        borderSettings.setNoDamageSeconds(config.getInt("border.no_damage_seconds", 0));

        // Загрузка пассивок классов
        loadClassPassives(config);
    }

    private void loadClassPassives(FileConfiguration config) {
        // Лучник
        classPassives.archerBowIncrease = config.getDouble("passives.archer.bow_increase", 1.15);
        classPassives.archerMeleeDecrease = config.getDouble("passives.archer.near_pvp_decrease", 0.90);

        // Мечник
        classPassives.swordsmanSwordIncrease = config.getDouble("passives.swordsman.sword_increase", 1.10);
        classPassives.swordsmanDamageDecrease = config.getDouble("passives.swordsman.melee_damage_decrease", 0.90);

        // Танк
        classPassives.tankHpIncrease = config.getDouble("passives.tank.hp_increase", 10.0);
        classPassives.tankSlowness = config.getInt("passives.tank.slowness", 3);

        // Добытчик
        classPassives.farmerToolStrongness = config.getDouble("passives.farmer.tools_strongness", 1.50);
        classPassives.farmerFortuneOres = config.getDouble("passives.farmer.fortune_ores", 1.15);
        classPassives.farmerPvpDecrease = config.getDouble("passives.farmer.pvp_decrease", 0.90);

        // Алхимик
        classPassives.alchemistPotionTimePlus = config.getDouble("passives.alchemist.potion_timeplus", 1.30);
        classPassives.alchemistPvpDecrease = config.getDouble("passives.alchemist.pvp_decrease", 0.80);
    }

    public void reload() {
        plugin.reloadConfig();
        loadSettings();
    }

    public BorderSettings getBorderSettings() {
        return borderSettings;
    }

    public ClassPassives getClassPassives() {
        return classPassives;
    }
}