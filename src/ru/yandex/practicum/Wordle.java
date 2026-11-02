package ru.yandex.practicum;

import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Wordle {

    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter(
                "wordle.log", StandardCharsets.UTF_8)) {

            try {
                String dictionaryFileName = "words_ru.txt";
                WordleDictionaryLoader loader = new WordleDictionaryLoader(dictionaryFileName, log);
                WordleDictionary dictionary = loader.load();
                WordleGame game = new WordleGame(dictionary, log);
                Scanner scanner = new Scanner(System.in);

                while (!game.isFinished()) {
                    System.out.println("Осталось попыток: " + game.getSteps());
                    if (!scanner.hasNextLine()) {
                        break;
                    }
                    String userGuess = scanner.nextLine();

                    try {
                        if (userGuess.isEmpty()) {
                            userGuess = game.getHint();
                            System.out.println("Подсказка: " + userGuess);
                        }

                        String result = game.makeMove(userGuess);
                        System.out.println(result);
                    } catch (GameException e) {
                        System.out.println(e.getMessage());
                    }
                }

                if (game.isWon()) {
                    System.out.println("Ты угадал слово!");
                } else if (game.isFinished()) {
                    System.out.println("Попытки закончились.");
                } else {
                    System.out.println("Ввод завершён. Игра остановлена.");
                }
                System.out.println("Правильный ответ: " + game.getAnswer());
                log.println("Игра завершена. Победа: " + game.isWon()
                        + ", осталось попыток: " + game.getSteps());
            } catch (Exception e) {
                e.printStackTrace(log);
                System.out.println(
                        "Произошла ошибка программы. Подробности в wordle.log"
                );
            }

        } catch (IOException e) {
            System.out.println("Не удалось создать лог-файл.");
        }
    }
}
