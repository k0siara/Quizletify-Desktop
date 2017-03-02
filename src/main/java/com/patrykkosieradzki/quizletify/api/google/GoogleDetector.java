package com.patrykkosieradzki.quizletify.api.google;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class GoogleDetector {

    private final static String API_KEY = "";

    public static Language languageFromText(String text) {
        try {
            text = URLEncoder.encode(text, "UTF-8");

            URL url = new URL("https://www.googleapis.com/language/translate/v2/detect?key="
                    + API_KEY
                    + "&q=" + text);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            if (connection.getResponseCode() != 200)
                throw new GoogleException("Wrong API Key");

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null)
                builder.append(line).append("\n");

            JSONObject json = new JSONObject(builder.toString());
            String langAbbreviation = json.getJSONObject("data")
                    .getJSONArray("detections")
                    .getJSONArray(0)
                    .getJSONObject(0)
                    .getString("language");
            return Language.fromString(langAbbreviation);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
