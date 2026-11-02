package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordleGame {

    private String answer;

    private int steps;

    private WordleDictionary dictionary;
    private final PrintWriter log;

    private boolean won;

    private final List<Attempt> history = new ArrayList<>();

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.log = log;
        this.answer = dictionary.getRandomWord();
        this.steps = 6;
        this.dictionary = dictionary;
        this.won = false;
    }

    public String checkGuess(String guess) throws InvalidWordException, WordNotFoundInDictionaryException {
        if (guess == null) {
            throw new InvalidWordException("Введите слово из пяти русских букв");
        }
        guess = guess.toLowerCase(Locale.ROOT).replace('ё', 'е');

        if (guess.length() != 5) {
            throw new InvalidWordException(
                    "Слово должно быть пятибуквенным"
            );
        }

        if (!guess.matches("[а-я]{5}")) {
            throw new InvalidWordException("Используйте только русские буквы");
        } // когда я через нейронку прогонял по соответсвию сказала что надобы это тоже добавтить

        if (!dictionary.contains(guess)) {
            throw new WordNotFoundInDictionaryException(
                    "Слово не найдено в словаре"
            );
        }

        return compareWords(guess, answer);
    }

    private String compareWords(String guess, String candidateAnswer) {
        char[] result = {'-', '-', '-', '-', '-'};
        boolean[] usedAnswerLetters = new boolean[5];

        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == candidateAnswer.charAt(i)) {
                result[i] = '+';
                usedAnswerLetters[i] = true;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (result[i] == '+') {
                continue;
            }

            for (int j = 0; j < 5; j++) {
                if (!usedAnswerLetters[j]
                        && guess.charAt(i) == candidateAnswer.charAt(j)) {
                    result[i] = '^';
                    usedAnswerLetters[j] = true;
                    break;
                }
            }
        } //тут я воспользовался нейросетью тк мой алгоритм был не оптимален и не учитывал повторяющиеся буквы

        return new StringBuilder().append(result).toString();
    }

    public String makeMove(String guess) throws InvalidWordException, WordNotFoundInDictionaryException {
        if (isFinished()) {
            throw new IllegalStateException("Игра уже завершена");
        }
        String result = checkGuess(guess);
        guess = guess.toLowerCase(Locale.ROOT).replace('ё', 'е');
        steps--;
        won = result.equals("+++++");

        history.add(new Attempt(guess, result));
        log.println("Ход: " + guess
                + ", результат: " + result
                + ", осталось попыток: " + steps);
        log.flush();
        return result;
    }

    public List<String> findCandidates() { //этот метод возвращает список слов из словаря
        // которые подходят под все предыдущие попытки
        List<String> candidates = new ArrayList<>();

        for (String candidate : dictionary.getWords()) {
            boolean suitable = true;

            for (Attempt attempt : history) {
                String result = compareWords(
                        attempt.getWord(), candidate
                );

                if (!result.equals(attempt.getResult())) {
                    suitable = false;
                    break;
                }
            }

            if (suitable) {
                candidates.add(candidate);
            }
        }

        return candidates;
    }

    public String getHint() { //этот метод возвращает случайное слово из списка подходящих слов
        List<String> candidates = findCandidates();

        if (candidates.isEmpty()) {
            throw new IllegalStateException(
                    "Не осталось подходящих слов"
            );
        }

        int index = (int) (Math.random() * candidates.size());
        return candidates.get(index);
    }

    public boolean isWon() {
        return won;
    }

    public boolean isFinished() {
        return steps <= 0 || won;
    }

    public int getSteps() {
        return steps;
    }

    public String getAnswer() {
        return answer;
    }

}
