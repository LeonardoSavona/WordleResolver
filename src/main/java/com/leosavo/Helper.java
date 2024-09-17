package com.leosavo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Helper {

    private static final String WORD_BANK_FILE = "words.txt";
    protected static List<String> wordBankList = new ArrayList<>();
    private static String wordBank;

    private static String readFromFile() {
        ClassLoader classLoader = Helper.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(WORD_BANK_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException("Resource not found");
            }

            return new Scanner(inputStream, StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadWordBank() {
        wordBank = readFromFile();
        wordBankList = Arrays.asList(wordBank.split(" "));
    }

    public static List<String> getWords() {
        if (wordBank == null) loadWordBank();
        return wordBankList;
    }

    public static String getRandomWord() {
        if (wordBank == null) loadWordBank();
        return getRandomWordFromList(wordBankList);
    }

    public static String getRandomWordFromList(List<String> list) {
        int randomInt = (int) (Math.random() * (list.size() - 1));
        return list.get(randomInt);
    }

    public static String getBestWordFromList(List<String> possibleWords) {
        if (possibleWords.size() < 20) {
            return sortWordsByMostUsedLetters(possibleWords).get(0).getKey();
        }
        return getWordWithMoreDifferentLetters(sortWordsByMostUsedLetters(possibleWords));
    }

    private static String getWordWithMoreDifferentLetters(List<Map.Entry<String, Integer>> possibleWords) {
        String result = null;
        for (Map.Entry<String, Integer> possibleWord : possibleWords) {
            String word = possibleWord.getKey();
            if (countDifferentLetters(word) > countDifferentLetters(result)){
                result = word;
            }
        }

        return result;
    }

    private static int countDifferentLetters(String s){
        if (s == null) return 0;
        Set<String> set = new HashSet<>(Arrays.asList(s.split("")));
        return set.size();
    }

    public static List<Map.Entry<String, Integer>> sortWordsByMostUsedLetters(List<String> possibleWords) {
        Map<String, Integer> lettersOccurrence = getMostUsedLetters(possibleWords);

        Map<String, Integer> mostUsedWords = new HashMap<>();
        for (String word : possibleWords) {
            String[] letters = word.split("");
            int sum = 0;
            for (String letter : letters) {
                sum += lettersOccurrence.get(letter);
            }
            mostUsedWords.put(word, sum);
        }
        List<Map.Entry<String, Integer>> mostUsedWordsList = new ArrayList<>(mostUsedWords.entrySet());
        mostUsedWordsList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return mostUsedWordsList;
    }

    private static Map<String, Integer> getMostUsedLetters(List<String> possibleWords) {
        Map<String, Integer> lettersOccurrence = new HashMap<>();
        for(String word : possibleWords) {
            for (String letter : word.split("")) {
                Integer letterOccurrences = lettersOccurrence.putIfAbsent(letter, 1);
                if (letterOccurrences != null) {
                    letterOccurrences += 1;
                    lettersOccurrence.put(letter, letterOccurrences);
                }
            }
        }
        return lettersOccurrence;
    }

    private static List<Map.Entry<String, Integer>> getMostUsedLettersList(List<String> possibleWords) {
        // Converti la mappa in una lista di entry
        List<Map.Entry<String, Integer>> mostUsedLetters = new ArrayList<>(getMostUsedLetters(possibleWords).entrySet());

        // Ordina la lista in base ai valori
        mostUsedLetters.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return mostUsedLetters;
    }

    public static List<String> clearList(List<String> possibleWords, Map<Integer, Character> guessedLetters, Map<Character, Integer> wrongPositionLetters, Map<Character, Integer> wrongLetters, List<String> previousAttempt) {
        return possibleWords.stream()
                .filter(s ->
                        !isPreviousAttempt(s, previousAttempt)
                                && !hasWrongLetters(s, wrongLetters)
                                && hasGuessedLetters(s, guessedLetters)
                                && hasWrongPositionLetters(s, wrongPositionLetters)
                )
                .collect(Collectors.toList());
    }

    private static boolean isPreviousAttempt(String s, List<String> previousAttempt) {
        return previousAttempt.contains(s);
    }

    private static boolean hasWrongPositionLetters(String s, Map<Character, Integer> wrongPositionLetters) {
        for (Map.Entry<Character, Integer> entry : wrongPositionLetters.entrySet()) {
            Character letter = entry.getKey();
            int position = entry.getValue();

            if (!s.contains(letter.toString())) return false;
            if (s.charAt(position) == letter) return false;
        }
        return true;
    }

    private static boolean hasGuessedLetters(String s, Map<Integer, Character> guessedLetters) {
        for (Map.Entry<Integer, Character> entry : guessedLetters.entrySet()) {
            Integer position = entry.getKey();
            Character letter = entry.getValue();
            if (!(s.charAt(position) == letter)) return false;
        }
        return true;
    }

    private static boolean hasWrongLetters(String s, Map<Character, Integer> wrongLetters) {
        for (Map.Entry<Character, Integer> c : wrongLetters.entrySet()) {
            if (s.contains(c.getKey().toString())) {
                if (c.getValue() == -1) return true;
                if (s.charAt(c.getValue()) == c.getKey()) return true;
            }
        }
        return false;
    }
}
