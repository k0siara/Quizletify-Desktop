package com.patrykkosieradzki.quizletify.api.google;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GoogleTranslatorTest {

    GoogleTranslator translator;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void before() {
        translator = new GoogleTranslator();
    }

    @Test
    public void whenPolishTextThenEnglishTranslation() throws IOException, GoogleException {
        String text = "placki";
        String translation = "cakes";
        assertThat(translator.translationFromText(text, Language.POLISH, Language.ENGLISH),
                is(translation));
    }

    @Test
    public void whenMoreThanOneTranslationsThenNoException() throws IOException, GoogleException {
        ArrayList<String> words = new ArrayList<String>();
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        words.add("bananas");
        words.add("potatoes");
        words.add("toes");
        words.add("blacks");
        ArrayList<String> translations = translator.translationsFromArray(words, Language.ENGLISH, Language.POLISH);
        assertThat(translations.get(30), is("palce u stóp"));
    }

}
