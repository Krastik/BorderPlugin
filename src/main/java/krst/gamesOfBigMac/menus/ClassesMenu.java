package krst.gamesOfBigMac.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class ClassesMenu {

    public static final String TITLE = "Выбор класса";

    public static Inventory create() {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        inv.setItem(11, ItemBuilder.create(Material.IRON_SWORD)
                .name("&b&lМечник")
                .lore(
                        "&fМечник, для тех кто любит ближний бой",
                        "&f======================================",
                        "&fНабор:",
                        "&fКожаная броня, Каменный меч, Щит",
                        "",
                        "&fПассивки:",
                        "&f+10% урона мечом",
                        "&f−10% входящего урона в ближнем бою"
                )
                .build());

        inv.setItem(12, ItemBuilder.create(Material.IRON_PICKAXE)
                .name("&b&lДобытчик")
                .lore(
                        "&fДобытчик, для тех кто любит копаться в шахте",
                        "&f======================================",
                        "&fНабор:",
                        "&fКожаная броня, Каменная кирка, Каменный топор",
                        "",
                        "&fПассивки:",
                        "&fИнструменты ломаются на 50% медленнее",
                        "&fУдача на добычу руды",
                        "&f−10% PvP-урона"
                )
                .build());

        inv.setItem(13, ItemBuilder.create(Material.SHIELD)
                .name("&b&lТанк")
                .lore(
                        "&fТанк, для тех кто любит много HP",
                        "&f======================================",
                        "&fНабор:",
                        "&fКожаная броня, Каменный меч, Щит",
                        "",
                        "&fПассивки:",
                        "&f+5 Сердечек",
                        "&fМедленное передвижение"
                )
                .build());

        inv.setItem(14, ItemBuilder.create(Material.BOW)
                .name("&b&lЛучник")
                .lore(
                        "&fЛучник, для тех кто предпочитает лук",
                        "&f======================================",
                        "&fНабор:",
                        "&fЛук, Кожаная броня, 24 стрелы, Каменный меч",
                        "",
                        "&fПассивки:",
                        "&f+15% урона из лука",
                        "&f−10% урона в ближнем бою"
                )
                .build());

        inv.setItem(15, ItemBuilder.create(Material.BREWING_STAND)
                .name("&b&lАлхимик")
                .lore(
                        "&fАлхимик, для тех кто знает как варить зелья",
                        "&f======================================",
                        "&fНабор:",
                        "&fКожаная броня, Зельеварка, Бесконечное топливо",
                        "",
                        "&fПассивки:",
                        "&f+30% длительности зелий",
                        "&f−20% урона оружием"
                )
                .build());

        inv.setItem(26, ItemBuilder.create(Material.RED_STAINED_GLASS_PANE)
                .name("&cВыйти")
                .build());

        return inv;
    }
}
