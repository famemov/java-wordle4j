package ru.yandex.practicum;

public class WordNotFoundInDictionaryException extends GameException {
    public WordNotFoundInDictionaryException(String message) {
        super(message);
    }
    // Этот класс представляет собой исключение, которое выбрасывается, когда слово не найдено в словаре.
}
