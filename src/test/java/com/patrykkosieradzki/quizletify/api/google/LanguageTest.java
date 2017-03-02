package com.patrykkosieradzki.quizletify.api.google;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class LanguageTest {

    @Test
    public void whenLanguagePolishThenAbbreviationIsGiven() {
        assertThat(Language.POLISH.toString(), is("pl"));
    }

    @Test
    public void whenLanguageEnglishThenAbbreviationIsGiven() {
        assertThat(Language.ENGLISH.toString(), is("en"));
    }

    @Test
    public void whenPolishAbbreviationIsPassedThenLanguageIsGiven() {
        assertThat(Language.fromString("pl"), is(Language.POLISH));
    }

    @Test
    public void whenEnglishAbbreviationIsPassedThenLanguageIsGiven() {
        assertThat(Language.fromString("en"), is(Language.ENGLISH));
    }

    @Test
    public void whenInvalidAbbreviationThenNull() {
        assertThat(Language.fromString("asd"), is(nullValue()));
    }

}
