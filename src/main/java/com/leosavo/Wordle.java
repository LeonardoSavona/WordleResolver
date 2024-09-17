package com.leosavo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static com.leosavo.Costants.*;

public class Wordle {


    private final File resultsFile = new File("C:\\Users\\leona\\Desktop\\Progetti\\Java\\WordleSolver\\results.txt");
    private String wordToGuess;

    private final boolean automaticGuess = true;
    private final List<String> guessedWords = new ArrayList<>();
    private final List<String> failedWords = new ArrayList<>();

    private int wins = 0;
    private int lose = 0;

    public void startGame() {
        for (int i = 0; i < GAMES; i++) {
            boolean win = play();
            if (win) {
                wins++;
                guessedWords.add(wordToGuess);
                System.out.println("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV Congratulation, the word was: "+wordToGuess);
            } else {
                lose++;
                failedWords.add(wordToGuess);
                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX You lose, the word was: "+wordToGuess);
            }
        }

        System.out.println(
                "Attempts:        "+MAX_ATTEMPTS+"\n"+
                "Guessed words:   "+guessedWords+"\n" +
                "Failed words:    "+failedWords+"\n" +
                "Games   Wins   Loses\n"+
                GAMES+"      "+wins+"     "+lose+"\n"+
                "-----------------------------------------------\n"
        );

        saveResults();
    }

    private void saveResults() {
        try (
            FileWriter fileWriter = new FileWriter(resultsFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            bufferedWriter.write(
                "Attempts:        "+MAX_ATTEMPTS+"\n"+
                    "Guessed words:   "+guessedWords+"\n" +
                    "Failed words:    "+failedWords+"\n" +
                    "Games   Wins   Loses\n"+
                    GAMES+"      "+wins+"     "+lose+"\n"+
                    "-----------------------------------------------\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean play() {
        wordToGuess = Helper.getRandomWord().toUpperCase();
        System.out.println("WORD TO GUESS: "+wordToGuess+"\n");

        AutomaticGuesser automaticGuesser = new AutomaticGuesser();
        Scanner scan = new Scanner(System.in);
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

                if (result.equals(String.join("", Stream.generate(() -> "V").limit(WORDS_LENGTH).toArray(String[]::new)))) {
                    return true;

                } else {
                    automaticGuesser.addAttemptResult(attempt, result, wordToGuess);
                }
            } else {
                System.out.println(String.format("'%s' is not a valid attempt!\n", attempt));
            }

        } while (attempts <= MAX_ATTEMPTS);

        return false;
    }

    private boolean isValidAttempt(String attempt) {
        if (attempt.length() != WORDS_LENGTH) return false;
        return Helper.wordBankList.contains(attempt);
    }

    private String checkWordAndGetResult(String attempt) {
        char[] result = new char[WORDS_LENGTH];

        for (int i = 0; i < WORDS_LENGTH; i++) {
            char attemptLetter = attempt.charAt(i);

            for (int c = 0; c < WORDS_LENGTH; c++) {
                char letter = wordToGuess.charAt(c);
                if (attemptLetter == letter) {
                    if (i == c) {
                        result[i] = 'V';
                        break;
                    } else if (!(attempt.charAt(c) == letter)) {
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
