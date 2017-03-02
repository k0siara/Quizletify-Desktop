package com.patrykkosieradzki.quizletify.api.quizlet.authorization;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AuthCodeHandler implements HttpHandler {

    final String state = UUID.randomUUID().toString();

    final BlockingQueue<String> result = new ArrayBlockingQueue<String>(1);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        OutputStream responseBody = exchange.getResponseBody();

        if (requestMethod.equalsIgnoreCase("GET")) {
            String uri = exchange.getRequestURI().toString();
            int code = uri.indexOf("&code=");
            if (code > -1 && uri.contains("state=" + state)) {
                result.add(uri.substring(code + 6));
                exchange.sendResponseHeaders(200, 0);
                responseBody.write("ok.\n You can close this window now."
                        .getBytes());
            } else {
                exchange.sendResponseHeaders(400, 0);
                responseBody.write(("invalid state " + uri).getBytes());
                result.add("");
            }
        } else {
            exchange.sendResponseHeaders(500, 0);
            responseBody.write(("ignored unexpected request "
                    + exchange.getRequestMethod() + " " + exchange
                    .getRequestURI()).getBytes());
        }
        responseBody.close();
    }

}
