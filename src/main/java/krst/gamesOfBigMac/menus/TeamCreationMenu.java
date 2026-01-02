package krst.gamesOfBigMac.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class TeamCreationMenu {

    public static final String TITLE = "Создать команду";
    public static final String COLOR_TITLE = "Выбор цвета команды";

    /**
     * Главное меню создания команды
     */
    public static Inventory createMain() {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        inv.setItem(13, ItemBuilder.create(Material.OAK_SIGN)
                .name("&b&lВвести имя команды")
                .lore("&7Нажмите, чтобы ввести название")
                .build());

        inv.setItem(26, ItemBuilder.create(Material.RED_STAINED_GLASS_PANE)
                .name("&cОтмена")
                .build());

        return inv;
    }

    /**
     * Меню выбора цвета команды
     */
    public static Inventory createColorSelection() {
        Inventory inv = Bukkit.createInventory(null, 27, COLOR_TITLE);

        // Все доступные цвета команд
        ChatColor[] colors = {
                ChatColor.RED, ChatColor.DARK_RED,
                ChatColor.BLUE, ChatColor.DARK_BLUE,
                ChatColor.GREEN, ChatColor.DARK_GREEN,
                ChatColor.AQUA, ChatColor.DARK_AQUA,
                ChatColor.YELLOW, ChatColor.GOLD,
                ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE,
                ChatColor.WHITE, ChatColor.GRAY, ChatColor.DARK_GRAY,
                ChatColor.BLACK
        };

        int slot = 0;
        for (ChatColor color : colors) {
            if (slot >= 26) break;

            Material wool = getWoolFromColor(color);
            inv.setItem(slot++, ItemBuilder.create(wool)
                    .name(color + "Команда " + color.name())
                    .lore("&7Нажмите, чтобы выбрать этот цвет")
                    .build());
        }

        inv.setItem(26, ItemBuilder.create(Material.RED_STAINED_GLASS_PANE)
                .name("&cОтмена")
                .build());

        return inv;
    }

    private static Material getWoolFromColor(ChatColor color) {
        return switch (color) {
            case BLACK -> Material.BLACK_WOOL;
            case DARK_BLUE, BLUE -> Material.BLUE_WOOL;
            case DARK_GREEN, GREEN -> Material.GREEN_WOOL;
            case DARK_AQUA, AQUA -> Material.CYAN_WOOL;
            case DARK_RED, RED -> Material.RED_WOOL;
            case DARK_PURPLE, LIGHT_PURPLE -> Material.PURPLE_WOOL;
            case GOLD, YELLOW -> Material.YELLOW_WOOL;
            case GRAY -> Material.GRAY_WOOL;
            case DARK_GRAY -> Material.GRAY_WOOL;
            case WHITE -> Material.WHITE_WOOL;
            default -> Material.WHITE_WOOL;
        };
    }
}
