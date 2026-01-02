package krst.gamesOfBigMac.managers;

import krst.gamesOfBigMac.BorderShrinkPlugin;
import krst.gamesOfBigMac.game.GameState;
import krst.gamesOfBigMac.game.GameTimer;
import krst.gamesOfBigMac.menus.TeamsMenu;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

public class GameManager {

    private final BorderShrinkPlugin plugin;
    private final BorderManager borderManager;
    private final ClassManager classManager;
    private final KitManager kitManager;
    private final ConfigManager configManager;
    private final TeamManager teamManager;

    private GameState state;
    private GameTimer timer;

    private boolean deathsEnabled;
    private boolean damageDisabled;

    private BukkitTask countdownTask;
    private int countdown = -1;

    public GameManager(BorderShrinkPlugin plugin, BorderManager borderManager,
                       ClassManager classManager, KitManager kitManager,
                       ConfigManager configManager, TeamManager teamManager) {
        this.plugin = plugin;
        this.borderManager = borderManager;
        this.classManager = classManager;
        this.kitManager = kitManager;
        this.configManager = configManager;
        this.teamManager = teamManager;
        this.state = GameState.WAITING;
        this.deathsEnabled = false;
        this.damageDisabled = false;
    }

    /**
     * Начинает подготовку к игре - открывает меню выбора команд всем
     */
    public void startPreparation() {
        if (state != GameState.WAITING) {
            throw new IllegalStateException("Игра уже началась или идет!");
        }

        state = GameState.PREPARING;

        // Очищаем старые команды и сбрасываем данные
        teamManager.cleanupEmptyTeams();
        classManager.clearAllData();

        // Открываем всем меню выбора команды
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.openInventory(TeamsMenu.createMain(teamManager));
        }

        Bukkit.broadcastMessage("§b§l✦ Выберите команду и класс!");
        Bukkit.broadcastMessage("§7Когда больше половины игроков будут готовы,");
        Bukkit.broadcastMessage("§7игра начнется через 10 секунд");
    }

    /**
     * Проверяет готовность команд и запускает отсчет, если нужно
     */
    public void checkReadiness() {
        if (state != GameState.PREPARING) return;

        int totalPlayers = Bukkit.getOnlinePlayers().size();
        if (totalPlayers == 0) return;

        int readyPlayers = 0;
        int playersInTeams = 0;

        // Подсчитываем готовых игроков
        for (Player player : Bukkit.getOnlinePlayers()) {
            Team team = teamManager.getPlayerTeam(player);

            // Игрок должен быть в команде
            if (team == null) continue;
            playersInTeams++;

            // И иметь выбранный класс
            String playerClass = classManager.getPlayerClass(player);
            if (playerClass == null || playerClass.equals("Нет класса")) continue;

            // И быть готовым
            if (!classManager.isReady(player)) continue;

            readyPlayers++;
        }

        // Проверяем: все ли в командах?
        if (playersInTeams < totalPlayers) {
            if (countdown > 0) {
                cancelCountdown();
                Bukkit.broadcastMessage("§c✗ Отсчет отменен: не все игроки выбрали команду!");
            }
            return;
        }

        // Проверяем: больше половины готовы?
        boolean majorityReady = readyPlayers > (totalPlayers / 2.0);

        if (majorityReady) {
            if (countdown == -1) {
                startCountdown();
            }
        } else {
            if (countdown > 0) {
                cancelCountdown();
                Bukkit.broadcastMessage("§c✗ Отсчет отменен: недостаточно готовых игроков!");
            }
        }
    }

    /**
     * Запускает 10-секундный отсчет
     */
    private void startCountdown() {
        countdown = 10;

        Bukkit.broadcastMessage("§a§l✓ Больше половины игроков готовы!");
        Bukkit.broadcastMessage("§e⏱ Игра начнется через 10 секунд...");

        countdownTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (countdown <= 0) {
                cancelCountdown();
                try {
                    startGame();
                } catch (Exception e) {
                    Bukkit.broadcastMessage("§c❌ Ошибка запуска: " + e.getMessage());
                    state = GameState.WAITING;
                }
                return;
            }

            if (countdown <= 5) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle("§e" + countdown, "§7До начала игры", 0, 25, 5);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                }
            }

            countdown--;
        }, 0L, 20L);
    }

    /**
     * Отменяет отсчет
     */
    private void cancelCountdown() {
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }
        countdown = -1;
    }

    /**
     * Запускает игру (вызывается после отсчета или вручную)
     */
    public void startGame() {
        if (state == GameState.ACTIVE) {
            throw new IllegalStateException("Игра уже идёт!");
        }

        // Финальная проверка
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (teamManager.getPlayerTeam(player) == null) {
                throw new IllegalStateException("Не все игроки в командах!");
            }
            String playerClass = classManager.getPlayerClass(player);
            if (playerClass == null || playerClass.equals("Нет класса")) {
                throw new IllegalStateException("Не все игроки выбрали класс!");
            }
        }

        cancelCountdown();
        state = GameState.STARTING;
        deathsEnabled = false;
        damageDisabled = false;

        // Сбрасываем выданные киты
        kitManager.reset();

        // Подготовка игроков
        Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();

        for (Player player : Bukkit.getOnlinePlayers()) {
            preparePlayer(player, spawn);
        }

        // Инициализация границ
        borderManager.initialize(spawn);

        // Запуск таймера
        timer = new GameTimer(plugin, this, borderManager, configManager);
        timer.start();

        // No-Damage период
        int noDamageSeconds = configManager.getBorderSettings().getNoDamageSeconds();
        if (noDamageSeconds > 0) {
            damageDisabled = true;

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendTitle("§aPvP выключено", "§7На " + noDamageSeconds + " секунд", 10, 60, 10);
            }

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                damageDisabled = false;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle("§cPvP включено!", "", 10, 40, 10);
                    p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 1f, 1f);
                }
            }, 20L * noDamageSeconds);
        }

        state = GameState.ACTIVE;

        Bukkit.broadcastMessage("§a§l✓ ИГРА НАЧАЛАСЬ! УДАЧИ!");
    }

    private void preparePlayer(Player player, Location spawn) {
        // Телепорт и очистка
        player.teleport(spawn);
        player.setBedSpawnLocation(spawn, true);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setHealth(20.0);

        // Выдаём кит ОДИН РАЗ
        kitManager.giveKitOnce(player);

        // Применяем пассивки (бесконечные эффекты)
        classManager.applyPassiveEffects(player);
    }

    public void stopGame() {
        if (state != GameState.ACTIVE) {
            return;
        }

        cancelCountdown();
        state = GameState.STOPPING;

        if (timer != null) {
            timer.stop();
        }

        borderManager.reset();
        kitManager.reset();
        deathsEnabled = false;
        damageDisabled = false;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                player.setGameMode(GameMode.SURVIVAL);
            }
            player.getInventory().clear();
            player.setHealth(20.0);
            player.setFoodLevel(20);
        }

        state = GameState.WAITING;

        Bukkit.broadcastMessage("§c§l✗ Игра остановлена!");
    }

    public void forceStop() {
        cancelCountdown();
        if (timer != null) {
            timer.stop();
        }
        borderManager.reset();
    }

    public void enableDeaths() {
        if (deathsEnabled) return;

        deathsEnabled = true;

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle("§4§l☠ СМЕРТИ ВКЛЮЧЕНЫ!", null, 10, 60, 10);
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1f, 1f);
        }
    }

    public void handlePlayerDeath(Player player) {
        if (!deathsEnabled) {
            // Обычная смерть - применяем пассивки заново после респавна
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                classManager.applyPassiveEffects(player);
            }, 5L);
            return;
        }

        // Перманентная смерть
        player.setGameMode(GameMode.SPECTATOR);

        Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
        double x = spawn.getBlockX() + 0.5;
        double z = spawn.getBlockZ() + 0.5;
        double y = spawn.getWorld().getHighestBlockYAt((int)x, (int)z) + 1;
        player.teleport(new Location(spawn.getWorld(), x, y, z));

        player.sendMessage("§c§lВы погибли! Теперь вы наблюдатель.");
    }

    // Геттеры
    public GameState getState() { return state; }
    public boolean isActive() { return state == GameState.ACTIVE; }
    public boolean isPreparing() { return state == GameState.PREPARING; }
    public boolean areDeathsEnabled() { return deathsEnabled; }
    public boolean isDamageDisabled() { return damageDisabled; }
    public GameTimer getTimer() { return timer; }
}
