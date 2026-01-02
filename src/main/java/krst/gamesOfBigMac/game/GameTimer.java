package krst.gamesOfBigMac.game;

import krst.gamesOfBigMac.BorderShrinkPlugin;
import krst.gamesOfBigMac.managers.BorderManager;
import krst.gamesOfBigMac.managers.ConfigManager;
import krst.gamesOfBigMac.managers.GameManager;
import krst.gamesOfBigMac.models.BorderSettings;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class GameTimer {

    private final BorderShrinkPlugin plugin;
    private final GameManager gameManager;
    private final BorderManager borderManager;
    private final BorderSettings settings;

    private BukkitTask timerTask;
    private BossBar bossBar;

    private long totalTicks;
    private long remainingTicks;

    private boolean deathsTriggered;
    private boolean borderStarted;
    private long ticksToShrink;

    private int actionBarCounter = 0;
    private static final int ACTIONBAR_UPDATE_INTERVAL = 20; // 1 секунда

    public GameTimer(BorderShrinkPlugin plugin, GameManager gameManager,
                     BorderManager borderManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        this.borderManager = borderManager;
        this.settings = configManager.getBorderSettings();
        this.deathsTriggered = false;
        this.borderStarted = false;
    }

    public void start() {
        this.totalTicks = settings.getGameDurationSeconds() * 20;
        this.remainingTicks = totalTicks;

        createBossBar();

        // Запускаем таймер (каждый тик)
        timerTask = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 0L, 1L);
    }

    private void tick() {
        if (remainingTicks <= 0) {
            stop();
            return;
        }

        remainingTicks--;
        long totalSeconds = remainingTicks / 20;

        // Обновляем BossBar
        updateBossBar(totalSeconds);

        // Проверяем триггер смертей
        checkDeathTrigger(totalSeconds);

        // Двигаем границу
        updateBorder(totalSeconds);

        // Обновляем ActionBar раз в секунду
        actionBarCounter++;
        if (actionBarCounter >= ACTIONBAR_UPDATE_INTERVAL) {
            actionBarCounter = 0;
            updateActionBars();
        }
    }

    private void updateBossBar(long totalSeconds) {
        if (bossBar == null) return;

        double progress = (double) remainingTicks / totalTicks;
        bossBar.setProgress(Math.max(0, Math.min(1, progress)));
        bossBar.setTitle("§aОсталось: §e" + formatTime(totalSeconds));
    }

    private void checkDeathTrigger(long totalSeconds) {
        int deathsAt = settings.getDeathsAtSeconds();

        if (deathsAt > 0 && !deathsTriggered && totalSeconds <= deathsAt) {
            gameManager.enableDeaths();
            deathsTriggered = true;
        }
    }

    private void updateBorder(long totalSeconds) {
        int borderDelay = settings.getBorderStartDelaySeconds();

        if (borderDelay == 0 || totalSeconds <= borderDelay) {
            if (!borderStarted) {
                borderStarted = true;
                ticksToShrink = remainingTicks;

                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                    p.sendTitle("§b⚠ Зона начала сужаться!", null, 10, 60, 10);
                }
            }

            // Постепенное сужение
            double shrinkPerTick = (settings.getMaxSize() - settings.getMinSize()) / (double) ticksToShrink;
            double newSize = borderManager.getCurrentSize() - shrinkPerTick;
            borderManager.shrinkBorders(newSize);
        }
    }

    private void updateActionBars() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            double distance = borderManager.getClosestDistance(player);
            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent("§eДо границы: " + (int) distance + " блоков")
            );
        }
    }

    private void createBossBar() {
        bossBar = Bukkit.createBossBar(
                "§aВремя до конца",
                BarColor.GREEN,
                BarStyle.SOLID
        );
        bossBar.setVisible(true);

        for (Player p : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(p);
        }
    }

    private String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (hours > 0) {
            return hours + "ч " + minutes + "м";
        } else if (minutes > 0) {
            return minutes + "м " + secs + "с";
        } else {
            return secs + "с";
        }
    }

    public void stop() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        if (bossBar != null) {
            bossBar.removeAll();
            bossBar.setVisible(false);
        }
    }

    public void addPlayer(Player player) {
        if (bossBar != null) {
            bossBar.addPlayer(player);
        }
    }
}
