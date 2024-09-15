package com.leosavo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomaticGuesser {

    private List<String> attemptsList = new ArrayList<>();
    private int attempts = 0;
    private final List<String> possibleWords = new ArrayList<>();

    public AutomaticGuesser() {
        possibleWords.addAll(Helper.wordBankList);
    }

    public String getAutomaticGuess() {
        if (attempts == 0) {
            return Helper.getRandomWord();
        }

        return Helper.getRandomWordFromList(possibleWords);
    }

    public void addAttemptResult(String attempt, String result) {
        attemptsList.add(attempt);
        attempts++;

        Map<Character, Integer> guessedLetters = new HashMap<>();
        Map<Character, Integer> wrongPositionLetters = new HashMap<>();
        List<Character> wrongLetters = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            if (result.charAt(i) == 'V') {
                guessedLetters.put(attempt.charAt(i), i);
            } else if (result.charAt(i) == '/') {
                wrongPositionLetters.put(attempt.charAt(i), i);
            } else {
             wrongLetters.add(attempt.charAt(i));
            }
        }
        possibleWords.removeIf((s) -> !isPossibleWord(s, guessedLetters, wrongPositionLetters, wrongLetters));
    }

    private boolean isPossibleWord(String s, Map<Character, Integer> guessedLetters, Map<Character, Integer> wrongPositionLetters, List<Character> wrongLetters) {
        if (attemptsList.contains(s)) return false;

        for (Character character : wrongLetters) {
            if (s.contains(character.toString())) return false;
        }

        for (Map.Entry<Character, Integer> character : wrongPositionLetters.entrySet()) {
            if (!s.contains(character.getKey().toString())) return false;
            if (s.charAt(character.getValue()) == character.getKey()) return false;
        }

        for (Map.Entry<Character, Integer> guessedLetter : guessedLetters.entrySet()) {
            if (!(s.charAt(guessedLetter.getValue()) == guessedLetter.getKey())) {
                return false;
            }
        }

        return true;
    }

    public List<String> getRemainingPossibleWords() {
        return this.possibleWords;
    }
}
