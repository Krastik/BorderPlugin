package krst.gamesOfBigMac.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCommand implements CommandExecutor {

    private final krst.gamesOfBigMac.managers.GameManager gameManager;

    public SettingsCommand(krst.gamesOfBigMac.managers.GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Только для игроков!");
            return true;
        }

        if (!sender.hasPermission("bordershrink.admin")) {
            sender.sendMessage(ChatColor.RED + "❌ Нет прав");
            return true;
        }

        player.openInventory(krst.gamesOfBigMac.menus.SettingsMenu.createMain());
        return true;
    }
}
