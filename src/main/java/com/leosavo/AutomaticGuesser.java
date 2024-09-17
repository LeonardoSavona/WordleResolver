package com.leosavo;

import java.util.*;

import static com.leosavo.Costants.WORDS_LENGTH;

public class AutomaticGuesser {

    private final List<String> attemptsList = new ArrayList<>();
    private List<String> possibleWords = new ArrayList<>();

    private final Map<Integer, Character> guessedLetters = new HashMap<>();
    private final Map<Character, Integer> wrongPositionLetters = new HashMap<>();
    private final Map<Character, Integer> wrongLetters = new HashMap<>();

    public AutomaticGuesser() {
        possibleWords.addAll(Helper.wordBankList);
    }

    public String getAutomaticGuess() {
        String attempt = Helper.getBestWordFromList(possibleWords);
        if (attempt == null) {
            attempt = Helper.getRandomWordFromList(possibleWords);
        }
        return attempt;
    }

    public void addAttemptResult(String attempt, String result, String wordToGuess) {
        attemptsList.add(attempt);

        for (int i = 0; i < WORDS_LENGTH; i++) {
            if (result.charAt(i) == 'V') {
                guessedLetters.put(i, attempt.charAt(i));
            } else if (result.charAt(i) == '/') {
                wrongPositionLetters.put(attempt.charAt(i), i);
            } else {
                if (wordToGuess.contains(String.valueOf(attempt.charAt(i)))){
                    wrongLetters.put(attempt.charAt(i), i);
                } else {
                    wrongLetters.put(attempt.charAt(i), -1);
                }
            }
        }
        possibleWords = Helper.clearList(possibleWords, guessedLetters, wrongPositionLetters, wrongLetters, attemptsList);
    }

    public List<String> getRemainingPossibleWords() {
        return this.possibleWords;
    }
}
