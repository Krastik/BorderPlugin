package krst.gamesOfBigMac.commands;

import krst.gamesOfBigMac.managers.ConfigManager;
import krst.gamesOfBigMac.managers.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BorderCommand implements CommandExecutor {

    private final GameManager gameManager;
    private final ConfigManager configManager;

    public BorderCommand(GameManager gameManager, ConfigManager configManager) {
        this.gameManager = gameManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("bordershrink.admin")) {
            sender.sendMessage(ChatColor.RED + "❌ Нет прав");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start":
                try {
                    gameManager.startPreparation(); // Изменено!
                    sender.sendMessage("§a✓ Началась подготовка к игре!");
                } catch (Exception e) {
                    sender.sendMessage("§c❌ Ошибка: " + e.getMessage());
                }
                break;

            case "forcestart":
                try {
                    gameManager.startGame(); // Принудительный старт без проверок
                    sender.sendMessage("§a✓ Игра началась принудительно!");
                } catch (Exception e) {
                    sender.sendMessage("§c❌ Ошибка: " + e.getMessage());
                }
                break;

            case "stop":
                gameManager.stopGame();
                sender.sendMessage("§a✓ Игра остановлена!");
                break;

            case "setmin":
                if (args.length < 2) {
                    sender.sendMessage("§cИспользуй: /border setmin <размер>");
                    return true;
                }
                try {
                    double min = Double.parseDouble(args[1]);
                    configManager.getBorderSettings().setMinSize(min);
                    sender.sendMessage("§a✓ Финальный размер: " + min);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§c❌ Неверное число");
                }
                break;

            case "setmax":
                if (args.length < 2) {
                    sender.sendMessage("§cИспользуй: /border setmax <размер>");
                    return true;
                }
                try {
                    double max = Double.parseDouble(args[1]);
                    configManager.getBorderSettings().setMaxSize(max);
                    sender.sendMessage("§a✓ Изначальный размер: " + max);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§c❌ Неверное число");
                }
                break;

            case "settime":
                if (args.length < 2) {
                    sender.sendMessage("§cИспользуй: /border settime <секунды>");
                    return true;
                }
                try {
                    long time = Long.parseLong(args[1]);
                    configManager.getBorderSettings().setGameDurationSeconds(time);
                    sender.sendMessage("§a✓ Время игры: " + time + " секунд");
                } catch (NumberFormatException e) {
                    sender.sendMessage("§c❌ Неверное число");
                }
                break;

            default:
                sendHelp(sender);
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§b§l=== Border Shrink Commands ===");
        sender.sendMessage("§e/border start §7- Начать подготовку (выбор команд)");
        sender.sendMessage("§e/border forcestart §7- Начать игру принудительно");
        sender.sendMessage("§e/border stop §7- Остановить игру");
        sender.sendMessage("§e/border setmin <размер> §7- Финальный размер");
        sender.sendMessage("§e/border setmax <размер> §7- Начальный размер");
        sender.sendMessage("§e/border settime <секунды> §7- Время игры");
    }
}
