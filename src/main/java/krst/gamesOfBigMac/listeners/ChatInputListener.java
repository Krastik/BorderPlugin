package krst.gamesOfBigMac.listeners;

import krst.gamesOfBigMac.BorderShrinkPlugin;
import krst.gamesOfBigMac.managers.ChatInputManager;
import krst.gamesOfBigMac.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatInputListener implements Listener {

    private final BorderShrinkPlugin plugin;
    private final ConfigManager configManager;

    public ChatInputListener(BorderShrinkPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (!ChatInputManager.isAwaiting(player.getUniqueId())) return;

        e.setCancelled(true); // Скрываем сообщение

        String command = ChatInputManager.getCommand(player.getUniqueId());
        String input = e.getMessage();

        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                int value = Integer.parseInt(input);
                processCommand(player, command, value);
            } catch (NumberFormatException ex) {
                player.sendMessage("§c❌ Ошибка! Введите целое число.");
            }
        });

        ChatInputManager.remove(player.getUniqueId());
    }

    private void processCommand(Player player, String command, int value) {
        switch (command) {
            case "bordermin":
                configManager.getBorderSettings().setMinSize(value);
                player.sendMessage("§b✓ Финальный размер: " + value);
                break;

            case "bordermax":
                configManager.getBorderSettings().setMaxSize(value);
                player.sendMessage("§b✓ Изначальный размер: " + value);
                break;

            case "bordertime":
                configManager.getBorderSettings().setGameDurationSeconds(value);
                player.sendMessage("§b✓ Время игры: " + value + " секунд");
                break;

            case "borderat":
                configManager.getBorderSettings().setBorderStartDelaySeconds(value);
                player.sendMessage("§b✓ Сужение начнётся за " + value + " секунд до конца");
                break;

            case "deathsat":
                configManager.getBorderSettings().setDeathsAtSeconds(value);
                player.sendMessage("§b✓ Смерти включатся за " + value + " секунд до конца");
                break;

            case "nodamage":
                configManager.getBorderSettings().setNoDamageSeconds(value);
                player.sendMessage("§b✓ PvP будет выключен первые " + value + " секунд");
                break;

            default:
                player.sendMessage("§c❌ Неизвестная команда");
        }
    }
}