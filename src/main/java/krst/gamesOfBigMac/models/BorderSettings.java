package krst.gamesOfBigMac.models;

public class BorderSettings {
    private double minSize = 50.0;
    private double maxSize = 1000.0;
    private long gameDurationSeconds = 3600;
    private int borderStartDelaySeconds = 300;
    private int deathsAtSeconds = -1;
    private int noDamageSeconds = 0;

    // Геттеры и сеттеры
    public double getMinSize() {
        return minSize;
    }

    public void setMinSize(double minSize) {
        this.minSize = minSize;
    }

    public double getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(double maxSize) {
        this.maxSize = maxSize;
    }

    public long getGameDurationSeconds() {
        return gameDurationSeconds;
    }

    public void setGameDurationSeconds(long gameDurationSeconds) {
        this.gameDurationSeconds = gameDurationSeconds;
    }

    public int getBorderStartDelaySeconds() {
        return borderStartDelaySeconds;
    }

    public void setBorderStartDelaySeconds(int borderStartDelaySeconds) {
        this.borderStartDelaySeconds = borderStartDelaySeconds;
    }

    public int getDeathsAtSeconds() {
        return deathsAtSeconds;
    }

    public void setDeathsAtSeconds(int deathsAtSeconds) {
        this.deathsAtSeconds = deathsAtSeconds;
    }

    public int getNoDamageSeconds() {
        return noDamageSeconds;
    }

    public void setNoDamageSeconds(int noDamageSeconds) {
        this.noDamageSeconds = noDamageSeconds;
    }
}
