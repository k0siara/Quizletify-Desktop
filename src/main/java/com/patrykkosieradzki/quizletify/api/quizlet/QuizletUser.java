package com.patrykkosieradzki.quizletify.api.quizlet;

import com.patrykkosieradzki.quizletify.api.google.Language;
import com.patrykkosieradzki.quizletify.api.quizlet.authorization.QuizletAuth;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class QuizletUser {

    private final String username;
    private final String clientId;
    private final String secretKey;
    private QuizletAuth authorization;

    public QuizletUser(String username, String clientId, String secretKey) {
        this.username = username;
        this.clientId = clientId;
        this.secretKey = secretKey;
        authorization = new QuizletAuth(this);
    }

    private JSONArray getSets() throws QuizletException {
        BufferedReader reader = null;
        StringBuilder builder = null;
        String line;

        try {
            String accessToken = authorization.getAccessToken();
            URL url = new URL("https://api.quizlet.com/2.0/"
                    + "users/" + username
                    + "/sets/"
                    + "?access_token=" + accessToken);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            if (connection.getResponseCode() != 200) {
                throw new QuizletException("Error retrieving sets");
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            builder = new StringBuilder();
            while ((line = reader.readLine()) != null)
                builder.append(line).append("\n");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (builder != null)
            return new JSONArray(builder.toString());
        else
            return null;
    }

    public void updateSet(QuizletSet set) throws QuizletException {
        deleteSet(set.getTitle());
        addSet(set);
    }

    public QuizletSet getSet(String title) throws QuizletException {
        JSONArray sets = getSets();
        for (int i = 0; i < sets.length(); i++) {
            JSONObject set = sets.getJSONObject(i);
            if (set.getString("title").equals(title)) {
                String description = set.getString("description");
                Language termsLang = Language.fromString(set.getString("lang_terms"));
                Language definitionsLang = Language.fromString(set.getString("lang_definitions"));
                ArrayList<String> terms = new ArrayList<String>();
                ArrayList<String> definitions = new ArrayList<String>();

                JSONArray termsJson = set.getJSONArray("terms");
                for (int j = 0; j < termsJson.length(); j++) {
                    JSONObject term = termsJson.getJSONObject(j);
                    terms.add(term.getString("term"));
                    definitions.add(term.getString("definition"));
                }

                return new QuizletSet(title, description, termsLang, definitionsLang, terms, definitions);
            }
        }

        throw new QuizletException("Set Not Found " + title);
    }

    public QuizletSet getSet(int id) throws QuizletException {
        JSONArray sets = getSets();
        for (int i = 0; i < sets.length(); i++) {
            JSONObject set = sets.getJSONObject(i);
            if (set.getInt("id") == id) {
                String title = set.getString("title");
                String description = set.getString("description");
                Language termsLang = Language.fromString(set.getString("lang_terms"));
                Language definitionsLang = Language.fromString(set.getString("lang_definitions"));
                ArrayList<String> terms = new ArrayList<String>();
                ArrayList<String> definitions = new ArrayList<String>();

                JSONArray termsJson = set.getJSONArray("terms");
                for (int j = 0; j < termsJson.length(); j++) {
                    JSONObject term = termsJson.getJSONObject(j);
                    terms.add(term.getString("term"));
                    definitions.add(term.getString("definition"));
                }

                return new QuizletSet(title, description, termsLang, definitionsLang, terms, definitions);
            }
        }

        throw new QuizletException("Set Not Found " + id);
    }

    public void addSet(QuizletSet set) throws QuizletException {
        if (set.getSize() < 2)
            throw new QuizletException("Set Must Contain At Least 2 Terms");

        try {
            String accessToken = authorization.getAccessToken();
            HttpPost post = new HttpPost("https://api.quizlet.com/2.0/sets");

            post.setHeader("Authorization", "Bearer " + accessToken);

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("title", set.getTitle()));
            params.add(new BasicNameValuePair("description", set.getDescription()));
            params.add(new BasicNameValuePair("lang_terms", set.getTermsLang().toString()));
            params.add(new BasicNameValuePair("lang_definitions", set.getDefinitionsLang().toString()));
            for (String term : set.getTerms()) {
                params.add(new BasicNameValuePair("terms[]", term));
            }
            for (String definition : set.getDefinitions()) {
                params.add(new BasicNameValuePair("definitions[]", definition));
            }
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            HttpResponse response = new DefaultHttpClient().execute(post);
            if (response.getStatusLine().getStatusCode() != 201)
                throw new QuizletException("Bad Request");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteSet(String title) throws QuizletException {
        JSONArray sets = getSets();
        int fails = 0;
        for (int i = 0; i < sets.length(); i++) {
            JSONObject set = sets.getJSONObject(i);
            if (set.getString("title").equals(title)) {
                int id = set.getInt("id");
                deleteSet(id);
            } else
                fails++;
        }

        if (fails == sets.length())
            throw new QuizletException("Set Not Found " + title);
    }

    public void deleteSet(int id) throws QuizletException {
        try {
            String accessToken = authorization.getAccessToken();
            URL url = new URL("https://api.quizlet.com/2.0"
                    + "/sets/" + id
                    + "?access_token=" + accessToken);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded" );
            connection.setRequestMethod("DELETE");
            connection.connect();

            if (connection.getResponseCode() != 204)
                throw new QuizletException("Set Not Found " + id);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void authorize() throws Exception {
        try {
            String accessToken = authorization.getAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSecretKey() {
        return secretKey;
    }

}
