package krst.gamesOfBigMac.commands;

import krst.gamesOfBigMac.managers.ClassManager;
import krst.gamesOfBigMac.managers.TeamManager;
import krst.gamesOfBigMac.menus.TeamsMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamsPickCommand implements CommandExecutor {

    private final TeamManager teamManager;
    private final ClassManager classManager;

    public TeamsPickCommand(TeamManager teamManager, ClassManager classManager) {
        this.teamManager = teamManager;
        this.classManager = classManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Только для игроков!");
            return true;
        }

        player.openInventory(TeamsMenu.createMain(teamManager));
        return true;
    }
}

// ============================================================
