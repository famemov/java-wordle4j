package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    private String fileName;
    private final PrintWriter log;

    public WordleDictionaryLoader(String fileName, PrintWriter log) {
        this.log = log;
        this.fileName = fileName;
    }

    public WordleDictionary load() throws IOException {
        log.println("Загрузка словаря: " + fileName);
        log.flush();
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(fileName, StandardCharsets.UTF_8))) { // кодировка UTF-8
            String word;

            while ((word = reader.readLine()) != null) {
                words.add(word);
            }
        }

        return new WordleDictionary(words, log);
    }
}
