package krst.gamesOfBigMac.game;

public enum GameState {
    WAITING,      // Ожидание игроков
    PREPARING,    // Подготовка (выбор команд/классов)
    STARTING,     // Отсчёт до старта
    ACTIVE,       // Игра идёт
    ENDING,       // Конец игры
    STOPPING,     // Остановка
    STOPPED       // Остановлена
}
