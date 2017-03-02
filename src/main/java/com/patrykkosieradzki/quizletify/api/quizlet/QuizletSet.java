package com.patrykkosieradzki.quizletify.api.quizlet;

import com.patrykkosieradzki.quizletify.api.google.Language;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizletSet {

    private String title;
    private String description;
    private Language termsLang;
    private Language definitionsLang;

    private ArrayList<String> terms;
    private ArrayList<String> definitions;

    private JSONObject acceptableLangs;

    public QuizletSet(String title, Language termsLang, Language definitionsLang)
            throws QuizletException {
        this.title = title;
        this.termsLang = termsLang;
        this.definitionsLang = definitionsLang;

        this.terms = new ArrayList<String>();
        this.definitions = new ArrayList<String>();

        loadLangs();
        checkLangs();
    }

    public QuizletSet(String title, String description, Language termsLang, Language definitionsLang,
                      ArrayList<String> terms,
                      ArrayList<String> definitions) throws QuizletException {
        this.title = title;
        this.description = description;
        this.termsLang = termsLang;
        this.definitionsLang = definitionsLang;

        this.terms = terms;
        this.definitions = definitions;

        loadLangs();
        checkLangs();
    }

    private void loadLangs() {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("languages.json");
        acceptableLangs = new JSONObject(convertStreamToString(inputStream));
    }

    private void checkLangs() throws QuizletException {
        checkTermsLang();
        checkDefinitionsLang();
    }

    private void checkTermsLang() throws QuizletException {
        if (!acceptableLangs.has(termsLang.toString()))
            throw new QuizletException("Unacceptable Terms Lang " + termsLang);
    }

    private void checkDefinitionsLang() throws QuizletException {
        if (!acceptableLangs.has(definitionsLang.toString()))
            throw new QuizletException("Unacceptable Definitions Lang " + definitionsLang);
    }

    public String convertStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Language getTermsLang() {
        return termsLang;
    }

    public Language getDefinitionsLang() {
        return definitionsLang;
    }

    public ArrayList<String> getTerms() {
        return terms;
    }

    public String getTerm(int index) {
        return terms.get(index);
    }

    public ArrayList<String> getDefinitions() {
        return definitions;
    }

    public String getDefinition(int index) {
        return definitions.get(index);
    }

    public int getSize() {
        return terms.size();
    }

    public JSONObject getAcceptableLangs() {
        return acceptableLangs;
    }

    public void addTerm(String term, String definition) {
        terms.add(term);
        definitions.add(definition);
    }

    public void removeTerm(String term) {
        int termIndex = terms.indexOf(term);
        terms.remove(termIndex);
        definitions.remove(termIndex);
    }

    public void removeDefinition(String definition) {
        int definitionIndex = definition.indexOf(definition);
        terms.remove(definitionIndex);
        definitions.remove(definitionIndex);
    }

}
