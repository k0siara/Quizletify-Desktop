package com.patrykkosieradzki.quizletify;

import com.patrykkosieradzki.quizletify.api.google.GoogleDetector;
import com.patrykkosieradzki.quizletify.api.google.GoogleTranslator;
import com.patrykkosieradzki.quizletify.api.google.Language;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TextDocument {

    private String filePath;

    private ArrayList<Word> words;

    private ArrayList<String> wordsList;
    private ArrayList<String> translationsList;

    private Language sourceLanguage;
    private Language targetLanguage;

    private GoogleTranslator translator;

    public TextDocument(String filePath) {
        this.filePath = filePath;

        this.translator = new GoogleTranslator();
    }

    public void retrieveWords() throws FileNotFoundException {
        retrieveWordsFromFile();
        translate();
        deleteSameTranslations();
        createWords();
    }

    private void retrieveWordsFromFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        Pattern pattern = Pattern.compile("[^\\p{Alpha}']+");

        wordsList = new ArrayList<String>();
        while(scanner.hasNext()) {
            scanner.useDelimiter(pattern);
            String word = scanner.next();
            if (!(wordsList.contains(word) || wordsList.contains(word.toLowerCase()))) {
                wordsList.add(word.toLowerCase());
            }
        }

        this.sourceLanguage = GoogleDetector.languageFromText(wordsList.get(0));
        this.targetLanguage = Language.POLISH;
    }

    private void translate() {
        try {
            translationsList = translator.translationsFromArray(wordsList, sourceLanguage, targetLanguage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteSameTranslations() {             // needs repair
        for(int i = 0; i < wordsList.size(); i++) {
            if (wordsList.get(i).toLowerCase().equals(translationsList.get(i).toLowerCase())) {
                wordsList.remove(i);
                translationsList.remove(i);
                i = 0;
            }
        }
    }

    private void createWords() {
        words = new ArrayList<Word>();
        if (wordsList.size() == translationsList.size()) {
            int size = wordsList.size();
            for (int i = 0; i < size; i++) {
                String word = wordsList.get(i);
                String translation = translationsList.get(i);
                words.add(new Word(word, translation));
            }
        }
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setSourceLanguage(Language sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public void setTargetLanguage(Language targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

}
