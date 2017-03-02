package com.patrykkosieradzki.quizletify.api.google;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class GoogleTranslator {

    private final static String API_KEY = "";

    private final int TRANSLATIONS_AT_ONCE_LIMIT = 50;

    public static String translationFromText(String text, Language source, Language target) {
        try {
            text = URLEncoder.encode(text, "UTF-8");

            URL url = new URL("https://www.googleapis.com/language/translate/v2?"
                    + "key=" + API_KEY
                    + "&source=" + source
                    + "&target=" + target
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
            String translation = json
                    .getJSONObject("data")
                    .getJSONArray("translations")
                    .getJSONObject(0)
                    .getString("translatedText");

            return translation;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<String> translationsFromArray(ArrayList<String> texts,
                                                   Language source, Language target)
            throws IOException, GoogleException {
        ArrayList<String> translations = new ArrayList<String>();
        ArrayList<String> textsToTranslate = new ArrayList<String>();
        for (int i = 0; i < texts.size(); i++) {
            if ((i % TRANSLATIONS_AT_ONCE_LIMIT == 0 && i != 0) || i == texts.size() - 1) {
                StringBuilder URL = new StringBuilder("https://www.googleapis.com/language/translate/v2?"
                        + "key=" + API_KEY
                        + "&source=" + source
                        + "&target=" + target);

                for (String text : textsToTranslate) {
                    URL.append("&q=" + text);
                }
                URLEncoder.encode(URL.toString(), "UTF-8");

                URL url = new URL(URL.toString());
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                if (connection.getResponseCode() != 200)
                    throw new GoogleException("Wrong API Key");

                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null)
                    builder.append(line).append("\n");

                JSONArray jsonTranslations = new JSONObject(builder.toString())
                        .getJSONObject("data")
                        .getJSONArray("translations");
                for(int j = 0; j < jsonTranslations.length(); j++) {
                    translations.add(jsonTranslations
                            .getJSONObject(j)
                            .getString("translatedText"));
                }

                textsToTranslate.clear();
                textsToTranslate.add(texts.get(i));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                if (i == texts.size() - 2) {
                    textsToTranslate.add(texts.get(i));
                    textsToTranslate.add(texts.get(i + 1));
                } else
                    textsToTranslate.add(texts.get(i));
            }
        }

        return translations;
    }

}
