package com.patrykkosieradzki.quizletify.api.quizlet.authorization;

import com.patrykkosieradzki.quizletify.api.quizlet.QuizletException;
import com.patrykkosieradzki.quizletify.api.quizlet.QuizletUser;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizletAuth {

    private final QuizletUser user;
    private String authHeader;

    private HttpServer server;
    private AuthCodeHandler handler;
    private ExecutorService executor;
    private ByteArrayOutputStream buffer;

    private String accessToken;
    private Calendar expiration;

    public QuizletAuth(QuizletUser user) {
        this.user = user;

        init();
    }

    private void init() {
        createCalendarInstance();
        createAuthHeader();
    }

    private void createCalendarInstance() {
        expiration = Calendar.getInstance();
    }

    private void createAuthHeader() {
        try {
            String clientId = user.getClientId();
            String secretKey = user.getSecretKey();
            authHeader = Base64.getEncoder()
                    .encodeToString((clientId + ":" + secretKey).getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getAccessToken()
            throws IOException, URISyntaxException, InterruptedException, QuizletException {
        Date currentDate = Calendar.getInstance().getTime();
        Date expirationDate = expiration.getTime();
        if (currentDate.equals(expirationDate) || currentDate.after(expirationDate)) {
            checkClientId();
            checkUsername();
            requestToken();

            JSONObject json = new JSONObject(
                    new String(buffer.toByteArray(), "UTF-8"));

            expiration = Calendar.getInstance();
            expiration.add(Calendar.SECOND, json.getInt("expires_in"));

            accessToken = json.getString("access_token");
        }

        return accessToken;
    }

    private void checkClientId() throws IOException, QuizletException {
        URL url = new URL("https://api.quizlet.com/2.0/sets/415"
                + "?client_id=" + user.getClientId());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        if (connection.getResponseCode() != 200)
            throw new QuizletException("Wrong ClientId");
    }

    private void checkUsername() throws IOException, QuizletException {
        URL url = new URL("https://api.quizlet.com/2.0/"
                + "users/" + user.getUsername()
                + "/?client_id=" + user.getClientId());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        if (connection.getResponseCode() != 200)
            throw new QuizletException("Wrong Username");
    }

    private void checkAuthorizationCode(String authCode) throws QuizletException {
        if (authCode == null || authCode.length() == 0)
            throw new QuizletException("The User Denied Access To Your Application");
    }

    private void requestToken ()
            throws QuizletException, IOException, URISyntaxException, InterruptedException {

        String authorizationCode = getAuthorizationCode();
        checkAuthorizationCode(authorizationCode);

        HttpPost post = new HttpPost("https://api.quizlet.com/oauth/token");
        post.setHeader("Authorization", "Basic " + authHeader);
        post.setEntity(new UrlEncodedFormEntity(Arrays.asList(
                new BasicNameValuePair("grant_type", "authorization_code"),
                new BasicNameValuePair("code", authorizationCode))));

        CloseableHttpResponse response = new DefaultHttpClient().execute(post);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new QuizletException("Wrong SecretKey");                                                 ////
        }

        buffer = new ByteArrayOutputStream(1000);
        response.getEntity().writeTo(buffer);
        response.close();
    }

    private String getAuthorizationCode()
            throws IOException, URISyntaxException, InterruptedException {

        startServer();
        Desktop.getDesktop().browse(
                new URI("https://quizlet.com/authorize/" +
                        "?scope=read%20write_set" +
                        "&client_id=" + user.getClientId() +
                        "&response_type=code" +
                        "&state=" + handler.state));

        String authorizationCode = handler.result.take();
        stopServer();

        return authorizationCode;
    }

    private void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(7777), 0);
        handler = new AuthCodeHandler();
        executor = Executors.newCachedThreadPool();
        server.createContext("/", handler);
        server.setExecutor(executor);
        server.start();
    }

    private void stopServer() {
        server.stop(1);
        executor.shutdownNow();
    }


}
