package com.patrykkosieradzki.quizletify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;

public class TextDocumentTest {

    String filePath;
    TextDocument document;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test(expected = FileNotFoundException.class)
    public void givenNotExistingFileThenFileNotFoundException() throws FileNotFoundException {
        filePath = "testFiles/asd";
        document = new TextDocument(filePath);
    }

    @Test
    public void whenWordHasTheSameTranslationThenItIsRemoved() {

    }

}
