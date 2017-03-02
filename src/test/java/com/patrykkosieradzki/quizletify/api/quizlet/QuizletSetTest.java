package com.patrykkosieradzki.quizletify.api.quizlet;

import com.patrykkosieradzki.quizletify.api.google.Language;
import com.patrykkosieradzki.quizletify.api.quizlet.QuizletException;
import com.patrykkosieradzki.quizletify.api.quizlet.QuizletSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class QuizletSetTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private String title = "title";
    private Language termsLang = Language.fromString("en");
    private Language definitionsLang = Language.fromString("pl");
    private QuizletSet set;

    @Before
    public void before() throws QuizletException {
        set = new QuizletSet(title, termsLang, definitionsLang);
    }

    @Test
    public void whenSetIsInstantiatedThenTitleIsStored() {
        assertEquals(title, set.getTitle());
    }

    @Test
    public void whenSetIsInstantiatedThenTermsLangIsStored() {
        assertEquals(termsLang, set.getTermsLang());
    }

    @Test
    public void whenSetIsInstantiatedThenDefinitionsLangIsStored() {
        assertEquals(definitionsLang, set.getDefinitionsLang());
    }

    @Test
    public void whenSetIsInstantiatedThenNoTermsAreStored() {
        assertEquals(0, set.getSize());
    }

    @Test
    public void whenSetIsInstantiatedThenNoDefinitionsAreStored() {
        assertEquals(0, set.getSize());
    }

    @Test
    public void givenInvalidTermsLangThenQuizletException() throws QuizletException {
        Language invalidTermsLang = Language.fromString("asd");
        exception.expect(QuizletException.class);
        exception.expectMessage("Unacceptable Terms Lang " + invalidTermsLang);
        new QuizletSet(title, invalidTermsLang, definitionsLang);
    }

    @Test
    public void givenInvalidDefinitionsLangThenQuizletException() throws QuizletException {
        Language invalidDefinitionsLang = Language.fromString("asd");
        exception.expect(QuizletException.class);
        exception.expectMessage("Unacceptable Definitions Lang " + invalidDefinitionsLang);
        new QuizletSet(title, termsLang, invalidDefinitionsLang);
    }

    @Test
    public void whenNewTermIsAddedThenTermIsStored() {
        set.addTerm("term", "definition");
        int finalSize = set.getSize() - 1;
        assertEquals("term", set.getTerm(finalSize));
    }

    @Test
    public void whenNewTermIsAddedThenDefinitionIsStored() {
        set.addTerm("term", "definition");
        int finalSize = set.getSize() - 1;
        assertEquals("definition", set.getDefinition(finalSize));
    }

    @Test
    public void whenNewTermIsAddedThenSizeIsBigger() {
        set.addTerm("term", "definition");
        set.addTerm("term2", "definition2");
        set.addTerm("term3", "definition3");
        assertEquals(3, set.getSize());
    }

    @Test
    public void whenTermIsRemovedThenSizeIsSmaller() {
        set.addTerm("term", "definition");
        set.addTerm("term2", "definition2");
        set.addTerm("term3", "definition3");
        set.removeTerm("term");
        set.removeDefinition("definition2");
        assertEquals(1, set.getSize());
    }



}
