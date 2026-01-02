package krst.gamesOfBigMac.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class KitManager {

    private final ClassManager classManager;
    private final Set<UUID> kitsGiven = new HashSet<>();

    public KitManager(ClassManager classManager) {
        this.classManager = classManager;
    }

    /**
     * Выдаёт кит игроку ТОЛЬКО ОДИН РАЗ за игру
     */
    public void giveKitOnce(Player player) {
        if (kitsGiven.contains(player.getUniqueId())) {
            return; // Кит уже выдан
        }

        String className = classManager.getPlayerClass(player).toLowerCase();

        // Очищаем инвентарь
        player.getInventory().clear();

        // Выдаём броню с цветом команды
        ItemStack[] armor = createColoredArmor(player);
        player.getInventory().setArmorContents(armor);

        // Выдаём предметы по классу
        switch (className) {
            case "лучник":
                player.getInventory().addItem(
                        new ItemStack(Material.BOW),
                        new ItemStack(Material.ARROW, 24),
                        new ItemStack(Material.STONE_SWORD)
                );
                break;

            case "мечник":
                player.getInventory().addItem(
                        new ItemStack(Material.STONE_SWORD),
                        new ItemStack(Material.SHIELD)
                );
                break;

            case "танк":
                player.getInventory().addItem(
                        new ItemStack(Material.STONE_SWORD),
                        new ItemStack(Material.SHIELD)
                );
                break;

            case "добытчик":
                player.getInventory().addItem(
                        new ItemStack(Material.STONE_PICKAXE),
                        new ItemStack(Material.STONE_AXE)
                );
                break;

            case "алхимик":
                player.getInventory().addItem(
                        new ItemStack(Material.BREWING_STAND),
                        new ItemStack(Material.BLAZE_POWDER, 5)
                );
                break;
        }

        kitsGiven.add(player.getUniqueId());
        player.sendMessage("§a✓ Вы получили кит класса §e" + className.toUpperCase());
    }

    /**
     * Создаёт кожаную броню с цветом команды игрока
     */
    private ItemStack[] createColoredArmor(Player player) {
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = sb.getPlayerTeam(player);

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

        Color color = team != null ? fromChatColor(team.getColor()) : Color.WHITE;

        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        LeatherArmorMeta chestMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        LeatherArmorMeta legsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();

        helmetMeta.setColor(color);
        chestMeta.setColor(color);
        legsMeta.setColor(color);
        bootsMeta.setColor(color);

        helmet.setItemMeta(helmetMeta);
        chestplate.setItemMeta(chestMeta);
        leggings.setItemMeta(legsMeta);
        boots.setItemMeta(bootsMeta);

        return new ItemStack[]{boots, leggings, chestplate, helmet};
    }

    /**
     * Конвертирует ChatColor в RGB Color для брони
     */
    private Color fromChatColor(ChatColor color) {
        if (color == null) return Color.WHITE;

        return switch (color) {
            case BLACK -> Color.BLACK;
            case DARK_BLUE -> Color.fromRGB(0, 0, 170);
            case DARK_GREEN -> Color.fromRGB(0, 170, 0);
            case DARK_AQUA -> Color.fromRGB(0, 170, 170);
            case DARK_RED -> Color.fromRGB(170, 0, 0);
            case DARK_PURPLE -> Color.fromRGB(170, 0, 170);
            case GOLD -> Color.fromRGB(255, 170, 0);
            case GRAY -> Color.fromRGB(170, 170, 170);
            case DARK_GRAY -> Color.fromRGB(85, 85, 85);
            case BLUE -> Color.fromRGB(85, 85, 255);
            case GREEN -> Color.fromRGB(85, 255, 85);
            case AQUA -> Color.fromRGB(85, 255, 255);
            case RED -> Color.fromRGB(255, 85, 85);
            case LIGHT_PURPLE -> Color.fromRGB(255, 85, 255);
            case YELLOW -> Color.fromRGB(255, 255, 85);
            case WHITE -> Color.WHITE;
            default -> Color.WHITE;
        };
    }

    /**
     * Сбрасывает список выданных китов (при новой игре)
     */
    public void reset() {
        kitsGiven.clear();
    }

    /**
     * Проверяет, был ли выдан кит игроку
     */
    public boolean hasKit(Player player) {
        return kitsGiven.contains(player.getUniqueId());
    }
}