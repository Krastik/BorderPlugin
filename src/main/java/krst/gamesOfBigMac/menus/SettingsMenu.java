package krst.gamesOfBigMac.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class SettingsMenu {

    public static final String MAIN_TITLE = "Настройки Игры";
    public static final String SIZE_TITLE = "Настройки размера зоны";
    public static final String TIME_TITLE = "Настройки времени";
    public static final String START_TITLE = "ВЫ УВЕРЕНЫ ЧТО ХОТИТЕ НАЧАТЬ ИГРУ?";

    public static String getTeamsTitle(int maxTeams) {
        return "Количество команд: " + maxTeams;
    }

    public static Inventory createMain() {
        Inventory inv = Bukkit.createInventory(null, 27, MAIN_TITLE);

        inv.setItem(10, ItemBuilder.create(Material.BARRIER)
                .name("&b&lНастройки размера зоны")
                .build());

        inv.setItem(12, ItemBuilder.create(Material.CLOCK)
                .name("&b&lНастройки времени")
                .build());

        inv.setItem(14, ItemBuilder.create(Material.LEATHER_CHESTPLATE)
                .name("&b&lКоманды")
                .build());

        inv.setItem(16, ItemBuilder.create(Material.OMINOUS_TRIAL_KEY)
                .name("&b&lНачать игру")
                .build());

        return inv;
    }

    public static Inventory createSize() {
        Inventory inv = Bukkit.createInventory(null, 27, SIZE_TITLE);

        inv.setItem(12, ItemBuilder.create(Material.MOSS_CARPET)
                .name("&b&lФинальный размер")
                .build());

        inv.setItem(15, ItemBuilder.create(Material.MOSS_BLOCK)
                .name("&b&lИзначальный размер")
                .build());

        inv.setItem(26, ItemBuilder.create(Material.RED_STAINED_GLASS_PANE)
                .name("&c&lНазад")
                .build());

        return inv;
    }

    public static Inventory createTime() {
        Inventory inv = Bukkit.createInventory(null, 27, TIME_TITLE);

        inv.setItem(10, ItemBuilder.create(Material.CLOCK)
                .name("&b&lВремя игры")
                .build());

        inv.setItem(12, ItemBuilder.create(Material.BARRIER)
                .name("&b&lНачало сужения зоны")
                .build());

        inv.setItem(14, ItemBuilder.create(Material.SKELETON_SKULL)
                .name("&b&lПерманентные смерти")
                .build());

        inv.setItem(16, ItemBuilder.create(Material.DIAMOND_SWORD)
                .name("&b&lУбийства в начале")
                .build());

        inv.setItem(26, ItemBuilder.create(Material.RED_STAINED_GLASS_PANE)
                .name("&c&lНазад")
                .build());

        return inv;
    }

    public static Inventory createStart() {
        Inventory inv = Bukkit.createInventory(null, 27, START_TITLE);

        inv.setItem(11, ItemBuilder.create(Material.GREEN_STAINED_GLASS_PANE)
                .name("&a&lНАЧАТЬ")
                .build());

        inv.setItem(15, ItemBuilder.create(Material.RED_STAINED_GLASS_PANE)
                .name("&c&lОтмена")
                .build());

        return inv;
    }

    public static Inventory createTeams(int maxTeams) {
        Inventory inv = Bukkit.createInventory(null, 27, getTeamsTitle(maxTeams));

        inv.setItem(11, ItemBuilder.create(Material.GREEN_WOOL)
                .name("&a&lПрибавить")
                .build());

        inv.setItem(15, ItemBuilder.create(Material.RED_WOOL)
                .name("&c&lОтбавить")
                .build());

        inv.setItem(26, ItemBuilder.create(Material.RED_STAINED_GLASS_PANE)
                .name("&c&lНазад")
                .build());

        return inv;
    }
}
