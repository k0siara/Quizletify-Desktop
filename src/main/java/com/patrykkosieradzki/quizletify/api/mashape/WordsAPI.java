package com.patrykkosieradzki.quizletify.api.mashape;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class WordsAPI {

    private final String API_KEY = "";

    public double getWordFrequency(String word) {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://wordsapiv1.p.mashape.com/words/"
                    + word + "/frequency")
                    .header("X-Mashape-Key", API_KEY)
                    .header("Accept", "application/json")
                    .asJson();

            double frequency = response.getBody().getObject()
                    .getJSONObject("frequency")
                    .getDouble("zipf");
            return frequency;

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
