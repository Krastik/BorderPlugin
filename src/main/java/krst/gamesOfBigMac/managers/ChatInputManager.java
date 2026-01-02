package krst.gamesOfBigMac.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatInputManager {
    private static final Map<UUID, String> awaitingInput = new HashMap<>();

    public static void startInput(UUID playerId, String command) {
        awaitingInput.put(playerId, command);
    }

    public static String getCommand(UUID playerId) {
        return awaitingInput.get(playerId);
    }

    public static void remove(UUID playerId) {
        awaitingInput.remove(playerId);
    }

    public static boolean isAwaiting(UUID playerId) {
        return awaitingInput.containsKey(playerId);
    }
}
