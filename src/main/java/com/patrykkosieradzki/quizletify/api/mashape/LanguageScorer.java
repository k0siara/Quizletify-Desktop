package com.patrykkosieradzki.quizletify.api.mashape;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class LanguageScorer {

    private final String API_KEY = "";

    public double getWordScore(String word) {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://twinword-language-scoring.p.mashape.com/word/"
                    + "?entry=" + word)
                    .header("X-Mashape-Key", API_KEY)
                    .header("Accept", "application/json")
                    .asJson();

            double score = response.getBody().getObject()
                    .getDouble("value");
            return score;

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
