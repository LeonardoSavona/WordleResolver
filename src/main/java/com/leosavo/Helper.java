package com.leosavo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Helper {

    private static final String WORD_BANK_FILE = "words.txt";
    protected static List<String> wordBankList = new ArrayList<>();

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
    public static String getRandomWord() {
        String wordBank = readFromFile();
        wordBankList = Arrays.asList(wordBank.split(" "));
        return getRandomWordFromList(wordBankList);
    }

    public static String getRandomWordFromList(List<String> list) {
        int randomInt = (int) (Math.random() * (list.size() - 1));
        return list.get(randomInt);
    }
}
