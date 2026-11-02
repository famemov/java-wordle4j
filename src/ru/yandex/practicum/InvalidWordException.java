package ru.yandex.practicum;

public class InvalidWordException extends GameException {
    public InvalidWordException(String message) {
        super(message);
    }
    // Этот класс представляет собой исключение, которое выбрасывается, когда введенное слово является недопустимым.
}
