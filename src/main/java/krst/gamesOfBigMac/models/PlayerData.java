package krst.gamesOfBigMac.models;

import java.util.UUID;

public class PlayerData {
    private final UUID playerId;
    private String selectedClass = "Нет класса";
    private boolean ready = false;

    public PlayerData(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() { return playerId; }
    public String getSelectedClass() { return selectedClass; }
    public void setSelectedClass(String selectedClass) { this.selectedClass = selectedClass; }
    public boolean isReady() { return ready; }
    public void setReady(boolean ready) { this.ready = ready; }
}
