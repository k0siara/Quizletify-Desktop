package com.patrykkosieradzki.quizletify.api.mashape;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class WordsAPITest {

    private WordsAPI api;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void before() {
        api = new WordsAPI();
    }

    @Test
    public void whenGetWordFrequencyThenNoException() {
        String word = "banana";
        double frequency = api.getWordFrequency(word);
        assertEquals(3.98, frequency, 0.10);
    }

}
