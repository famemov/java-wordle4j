package ru.yandex.practicum;

public class GameException extends Exception {
    public GameException(String message) {
        super(message);
    }
    // Этот класс представляет собой базовое исключение для игры, от которого наследуются другие исключения.
}
