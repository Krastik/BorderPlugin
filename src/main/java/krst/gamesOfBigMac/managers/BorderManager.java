package krst.gamesOfBigMac.managers;

import krst.gamesOfBigMac.models.BorderSettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BorderManager {

    private final List<WorldBorder> borders;
    private final BorderSettings settings;
    private Location defaultCenter;

    public BorderManager(BorderSettings settings) {
        this.settings = settings;
        this.borders = new ArrayList<>();

        // Добавляем границы всех миров
        for (World world : Bukkit.getWorlds()) {
            borders.add(world.getWorldBorder());
        }
    }

    public void initialize(Location center) {
        this.defaultCenter = center;

        for (WorldBorder border : borders) {
            border.setCenter(center);
            border.setSize(settings.getMaxSize());
        }
    }

    public void shrinkBorders(double newSize) {
        double finalSize = Math.max(settings.getMinSize(), newSize);

        for (WorldBorder border : borders) {
            if (border.getSize() > finalSize) {
                border.setSize(finalSize);
            }
        }
    }

    public void reset() {
        for (WorldBorder border : borders) {
            if (defaultCenter != null) {
                border.setCenter(defaultCenter);
            }
            border.setSize(settings.getMaxSize());
        }
    }

    /**
     * Вычисляет ближайшее расстояние до границы для игрока
     */
    public double getClosestDistance(Player player) {
        return borders.stream()
                .mapToDouble(b -> calculateDistance(player, b))
                .min()
                .orElse(0);
    }

    private double calculateDistance(Player player, WorldBorder border) {
        double centerX = border.getCenter().getX();
        double centerZ = border.getCenter().getZ();
        double halfSize = border.getSize() / 2.0;

        double dx = halfSize - Math.abs(player.getLocation().getX() - centerX);
        double dz = halfSize - Math.abs(player.getLocation().getZ() - centerZ);

        return Math.max(0, Math.min(dx, dz));
    }

    public BorderSettings getSettings() {
        return settings;
    }

    public double getCurrentSize() {
        return borders.isEmpty() ? 0 : borders.get(0).getSize();
    }
}

// ============================================================
