package krst.gamesOfBigMac.listeners;

import krst.gamesOfBigMac.managers.TeamCreationManager;
import krst.gamesOfBigMac.menus.TeamCreationMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class AnvilInputListener implements Listener {

    private final TeamCreationManager teamCreationManager;

    public AnvilInputListener(TeamCreationManager teamCreationManager) {
        this.teamCreationManager = teamCreationManager;
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (!(e.getInventory() instanceof AnvilInventory anvil)) return;
        if (e.getSlot() != 2) return; // Результат в слоте 2

        // Проверяем, ожидает ли игрок ввода имени команды
        if (!teamCreationManager.isAwaitingTeamName(player.getUniqueId())) return;

        e.setCancelled(true);

        ItemStack result = anvil.getItem(2);
        if (result == null || !result.hasItemMeta()) return;

        String teamName = result.getItemMeta().getDisplayName();
        if (teamName == null || teamName.trim().isEmpty()) {
            player.sendMessage("§c❌ Введите название команды!");
            return;
        }

        // Сохраняем имя и открываем меню выбора цвета
        teamCreationManager.setTeamName(player.getUniqueId(), teamName);
        player.closeInventory();
        player.openInventory(TeamCreationMenu.createColorSelection());
    }

    @EventHandler
    public void onAnvilClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)) return;
        if (e.getInventory().getType() != InventoryType.ANVIL) return;

        // Если игрок закрыл наковальню без ввода - возвращаем в главное меню
        if (teamCreationManager.isAwaitingTeamName(player.getUniqueId())) {
            org.bukkit.Bukkit.getScheduler().runTaskLater(
                    org.bukkit.Bukkit.getPluginManager().getPlugin("BorderShrink"),
                    () -> {
                        if (teamCreationManager.isAwaitingTeamName(player.getUniqueId())) {
                            player.openInventory(TeamCreationMenu.createMain());
                        }
                    },
                    1L
            );
        }
    }
}