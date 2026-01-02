package krst.gamesOfBigMac.commands;

import krst.gamesOfBigMac.managers.ClassManager;
import krst.gamesOfBigMac.menus.ClassesMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassPickCommand implements CommandExecutor {

    private final ClassManager classManager;

    public ClassPickCommand(ClassManager classManager) {
        this.classManager = classManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Только для игроков!");
            return true;
        }

        player.openInventory(ClassesMenu.create());
        return true;
    }
}
