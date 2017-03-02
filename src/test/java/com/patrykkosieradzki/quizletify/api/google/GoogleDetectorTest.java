package com.patrykkosieradzki.quizletify.api.google;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class GoogleDetectorTest {

    private GoogleDetector detector;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void before() {
        detector = new GoogleDetector();
    }

    @Test
    public void whenPolishThenPolishDetected() throws IOException, GoogleException {
        String text = "Lubię placki";
        assertThat(detector.languageFromText(text), is(Language.POLISH));
    }

    @Test
    public void whenEnglishThenEnglishDetected() throws IOException, GoogleException {
        String text = "I like trains";
        assertThat(detector.languageFromText(text), is(Language.ENGLISH));
    }

    @Test
    public void whenUnsupportedLanguageThenNull() throws IOException, GoogleException {
        String text = "blahblah";
        assertThat(detector.languageFromText(text), is(nullValue()));
    }


}
