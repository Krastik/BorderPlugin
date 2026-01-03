package krst.gamesOfBigMac.managers;

import krst.gamesOfBigMac.BorderShrinkPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final BorderShrinkPlugin plugin;
    private boolean classesEnabled;

    public ConfigManager(BorderShrinkPlugin plugin) {
        this.plugin = plugin;
        loadSettings();
    }

    private void loadSettings() {
        FileConfiguration config = plugin.getConfig();

        // Чтение настройки classes.enabled
        this.classesEnabled = config.getBoolean("classes.enabled", true);
    }

    public boolean areClassesEnabled() {
        return classesEnabled;
    }

    public void reload() {
        plugin.reloadConfig();
        loadSettings();
    }
}