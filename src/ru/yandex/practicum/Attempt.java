package ru.yandex.practicum;

public class Attempt { //этот класс хранит в себе слово которое ввёл пользователь и результат проверки этого слова
    private final String word;
    private final String result;

    public Attempt(String word, String result) {
        this.word = word;
        this.result = result;
    }

    public String getWord() {
        return word;
    }

    public String getResult() {
        return result;
    }
}
