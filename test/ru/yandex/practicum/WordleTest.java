package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    private WordleGame game;
    private static PrintWriter log;

    @BeforeAll
    static void setUpLog() {
        log = new PrintWriter(System.out, true);
    }

    @BeforeEach
    void setUp() {
        WordleDictionary dictionary =
                new WordleDictionary(List.of("кошка"), log);

        game = new WordleGame(dictionary, log);
    } // тут вся проверка идет на слове кошка чтобы не зависеть от рандома

    @Test
    void newGameHasSixAttempts() {
        assertEquals(6, game.getSteps());
        assertFalse(game.isWon());
        assertFalse(game.isFinished());
    }

    @Test
    void correctGuessWinsGame() throws GameException {
        String result = game.makeMove("кошка");

        assertEquals("+++++", result);
        assertTrue(game.isWon());
        assertTrue(game.isFinished());
        assertEquals(5, game.getSteps());
    } // сразу проверка на победу и что шаги уменьшаются

    @Test
    void invalidWordDoesNotSpendAttempt() {
        assertThrows(
                InvalidWordException.class,
                () -> game.makeMove("кот")
        );

        assertEquals(6, game.getSteps());
    } // проверка на то что слово не из словаря не тратит попытку(при ошибке ввода)

    @Test
    void wordOutsideDictionaryIsRejected() {
        assertThrows(
                WordNotFoundInDictionaryException.class,
                () -> game.makeMove("лампа")
        );

        assertEquals(6, game.getSteps());
    } // проверка на то что слово не из словаря не тратит попытку(если слово не в словаре)

    @Test
    void cannotMoveAfterVictory() throws GameException {
        game.makeMove("кошка");

        assertThrows(
                IllegalStateException.class,
                () -> game.makeMove("кошка")
        );
    } // проверка на то что после победы нельзя делать ход

    @Test
    void sixthWrongMoveFinishesGame() throws GameException {
        game = createGame("кошка", "кошка", "билет");

        for (int i = 1; i <= 6; i++) {
            assertEquals("-----", game.makeMove("билет"));
            assertEquals(6 - i, game.getSteps());
            assertEquals(i == 6, game.isFinished());
        }

        assertFalse(game.isWon());
        assertThrows(IllegalStateException.class, () -> game.makeMove("кошка"));
        assertEquals(0, game.getSteps());
    } // проверка на то что после 6 неудачных попыток игра заканчивается и нельзя делать ход

    @Test
    void canWinOnLastAttempt() throws GameException {
        game = createGame("кошка", "кошка", "билет");
        for (int i = 0; i < 5; i++) {
            game.makeMove("билет");
        }

        assertEquals("+++++", game.makeMove("кошка"));
        assertTrue(game.isWon());
        assertTrue(game.isFinished());
        assertEquals(0, game.getSteps());
    } // проверка на то что можно выиграть на последней попытке

    @Test
    void playingHintsDoesNotRepeatWordsAndLeadsToVictory() throws GameException {
        game = createGame("кошка", "кошка", "кашка", "шапка", "билет", "касса");
        Set<String> usedHints = new HashSet<>();

        while (!game.isFinished()) {
            int stepsBeforeHint = game.getSteps();
            String hint = game.getHint();

            assertTrue(game.findCandidates().contains(hint));
            assertTrue(usedHints.add(hint), "Подсказка повторила уже сыгранное слово");
            assertEquals(stepsBeforeHint, game.getSteps());

            game.makeMove(hint);
            assertTrue(game.findCandidates().contains("кошка"));
        }

        assertTrue(game.isWon());
    } // проверка на то что подсказки не повторяются и ведут к победе

    @Test
    void dictionaryNormalizesFiltersAndRemovesDuplicates() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("АКТЁР", "актер", "кошка", "кот", "abcde", "12345", ""), log);

        assertEquals(List.of("актер", "кошка"), dictionary.getWords());
        assertTrue(dictionary.contains("АКТЁР"));
        assertFalse(dictionary.contains("лампа"));

        dictionary.getWords().clear();
        assertEquals(List.of("актер", "кошка"), dictionary.getWords());
    } // проверка на то что словарь нормализует слова, фильтрует по длине и удаляет дубликаты

    @Test
    void dictionaryWithoutValidWordsIsRejected() {
        assertThrows(IllegalStateException.class,
                () -> new WordleDictionary(List.of(), log));
        assertThrows(IllegalStateException.class,
                () -> new WordleDictionary(List.of("кот", "abcde", ""), log));
    }

    private WordleGame createGame(String answer, String... words) {
        // Фиксируем ответ, сохраняя настоящую валидацию и поиск по словарю (вспомогательный метод)
        WordleDictionary dictionary = new WordleDictionary(List.of(words), log) {
            @Override
            public String getRandomWord() {
                return answer;
            }
        };
        return new WordleGame(dictionary, log);
    }
}
