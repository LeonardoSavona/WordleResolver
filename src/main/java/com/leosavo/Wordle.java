package com.leosavo;

import java.util.Scanner;

public class Wordle {

    private static final int MAX_ATTEMPTS = 5;
    private String wordToGuess;

    private AutomaticGuesser automaticGuesser;
    private final boolean automaticGuess = true;

    private void init() {
        wordToGuess = Helper.getRandomWord().toUpperCase();
        automaticGuesser = new AutomaticGuesser();
        System.out.println("WORD TO GUESS: "+wordToGuess+"\n");
    }

    public void startGame() {
        init();
        Scanner scan = new Scanner(System.in);

        boolean win = false;
        int attempts = 1;
        do {
            System.out.println(automaticGuesser.getRemainingPossibleWords().size() + " possible words left: "+ automaticGuesser.getRemainingPossibleWords());
            System.out.println(String.format("Attempt %s: ", attempts));
            String attempt;

            if (automaticGuess) {
                attempt = automaticGuesser.getAutomaticGuess();
            } else {
                attempt = scan.next().toUpperCase();
            }

            if (isValidAttempt(attempt)) {
                attempts++;
                System.out.println(attempt);
                String result = checkWordAndGetResult(attempt);
                if (result.equals("VVVVV")) {
                    win = true;
                    break;
                } else {
                    automaticGuesser.addAttemptResult(attempt, result);
                }
            } else {
                System.out.println(String.format("'%s' is not a valid attempt!\n", attempt));
            }

        } while (attempts <= MAX_ATTEMPTS);

        if (win) {
            System.out.println("Congratulation, the word was: "+wordToGuess);
        } else {
            System.out.println("You lose, the word was: "+wordToGuess);
        }
    }

    private boolean isValidAttempt(String attempt) {
        if (attempt.length() != 5) return false;
        if (!Helper.wordBankList.contains(attempt)) return false;
        return true;
    }

    private String checkWordAndGetResult(String attempt) {
        char[] result = new char[5];

        for (int i = 0; i < 5; i++) {
            char letter = attempt.charAt(i);

            for (int c = 0; c < 5; c++) {
                char letter1 = wordToGuess.charAt(c);
                if (letter == letter1) {
                    if (i == c) {
                        result[i] = 'V';
                        break;
                    } else {
                        result[i] = '/';
                    }
                }
            }

            if (result[i] == 0) {
                result[i] = 'X';
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (char c : result) {
            stringBuilder.append(c);
        }

        System.out.println(stringBuilder+"\n");
        return stringBuilder.toString();
    }
}
