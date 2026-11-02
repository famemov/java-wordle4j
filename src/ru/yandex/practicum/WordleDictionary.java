package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class WordleDictionary {

    private final List<String> words;
    private final Set<String> wordSet;
    private final PrintWriter log;

    public WordleDictionary(List<String> words, PrintWriter log) {
        this.log = log;
        List<String> preparedWords = checkWordsInList(words);

        if (preparedWords.isEmpty()) {
            throw new IllegalStateException(
                    "В словаре нет подходящих пятибуквенных слов"
            );
        }

        this.words = new ArrayList<>(new LinkedHashSet<>(preparedWords));
        this.wordSet = new HashSet<>(this.words);
        log.println("Словарь подготовлен. Количество слов: " + this.words.size());
        log.flush();
    }

    public List<String> checkWordsInList(List<String> wordsToCheck) {
        List<String> result = new ArrayList<>();

        for (String word : wordsToCheck) {
            String normalizedWord = word.toLowerCase(Locale.ROOT)
                    .replace('ё', 'е'); // нормализуем слово, чтобы оно соответствовало формату словаря

            if (normalizedWord.matches("[а-я]{5}")) {
                result.add(normalizedWord);
            }
        }

        return result;
    }

    public boolean contains(String word) {
        String normalizedWord = word.toLowerCase(Locale.ROOT)
                .replace('ё', 'е'); // проверка на наличие слова в словаре с учетом нормализации
        return wordSet.contains(normalizedWord);
    }

    public String getRandomWord() {
        int randomIndex = (int) (Math.random() * words.size());
        return words.get(randomIndex);
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }


}
