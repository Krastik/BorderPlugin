package krst.gamesOfBigMac;

import krst.gamesOfBigMac.commands.BorderCommand;
import krst.gamesOfBigMac.commands.ClassPickCommand;
import krst.gamesOfBigMac.commands.SettingsCommand;
import krst.gamesOfBigMac.commands.TeamsPickCommand;
import krst.gamesOfBigMac.listeners.*;
import krst.gamesOfBigMac.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BorderShrinkPlugin extends JavaPlugin {

    // Менеджеры
    private GameManager gameManager;
    private BorderManager borderManager;
    private TeamManager teamManager;
    private ClassManager classManager;
    private KitManager kitManager;
    private ConfigManager configManager;
    private TeamCreationManager teamCreationManager;

    @Override
    public void onEnable() {
        // Сохраняем дефолтный конфиг
        saveDefaultConfig();

        // Инициализируем менеджеры в правильном порядке
        this.configManager = new ConfigManager(this);
        this.borderManager = new BorderManager(configManager.getBorderSettings());
        this.teamManager = new TeamManager();
        this.classManager = new ClassManager();
        this.kitManager = new KitManager(classManager);
        this.teamCreationManager = new TeamCreationManager(teamManager);
        this.gameManager = new GameManager(
                this,
                borderManager,
                classManager,
                kitManager,
                configManager,
                teamManager  // Добавлен TeamManager
        );

        // Регистрируем команды
        registerCommands();

        // Регистрируем слушатели
        registerListeners();

        getLogger().info("BorderShrink плагин загружен!");
        getLogger().info("Новая система создания команд активирована!");
    }

    @Override
    public void onDisable() {
        if (gameManager != null) {
            gameManager.forceStop();
        }

        // Очищаем все команды
        if (teamManager != null) {
            teamManager.cleanupEmptyTeams();
        }

        getLogger().info("BorderShrink плагин выгружен!");
    }

    private void registerCommands() {
        getCommand("border").setExecutor(new BorderCommand(gameManager, configManager));
        getCommand("bordersettings").setExecutor(new SettingsCommand(gameManager));
        getCommand("teamspick").setExecutor(new TeamsPickCommand(teamManager, classManager));
        getCommand("classpick").setExecutor(new ClassPickCommand(classManager));
    }

    private void registerListeners() {
        // Основные слушатели
        Bukkit.getPluginManager().registerEvents(
                new MenuListener(gameManager, teamManager, classManager, teamCreationManager),
                this
        );
        Bukkit.getPluginManager().registerEvents(
                new AnvilInputListener(teamCreationManager),
                this
        );
        Bukkit.getPluginManager().registerEvents(
                new DamageListener(gameManager, classManager, configManager),
                this
        );
        Bukkit.getPluginManager().registerEvents(
                new DeathListener(gameManager),
                this
        );
        Bukkit.getPluginManager().registerEvents(
                new PlayerJoinListener(gameManager),
                this
        );
        Bukkit.getPluginManager().registerEvents(
                new ChatInputListener(this, configManager),
                this
        );
        Bukkit.getPluginManager().registerEvents(
                new ClassPassiveListener(classManager, configManager),
                this
        );

        // Новый слушатель для очистки пустых команд
        Bukkit.getPluginManager().registerEvents(
                new PlayerQuitListener(teamManager, classManager),
                this
        );
    }

    // Геттеры для менеджеров
    public GameManager getGameManager() { return gameManager; }
    public BorderManager getBorderManager() { return borderManager; }
    public TeamManager getTeamManager() { return teamManager; }
    public ClassManager getClassManager() { return classManager; }
    public KitManager getKitManager() { return kitManager; }
    public ConfigManager getConfigManager() { return configManager; }
    public TeamCreationManager getTeamCreationManager() { return teamCreationManager; }
}
