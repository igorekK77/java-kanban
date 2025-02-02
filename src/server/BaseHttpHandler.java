package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected static final Charset standardCharsets = StandardCharsets.UTF_8;

    protected void sendText(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(standardCharsets);
        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        String httpMethod = httpExchange.getRequestMethod();
        switch (httpMethod) {
            case "POST":
                httpExchange.sendResponseHeaders(201,0);
                break;
            default:
                httpExchange.sendResponseHeaders(200,0);
                break;
        }
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(resp);
        }
    }

    protected void sendNotFound(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(standardCharsets);
        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(404,0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(resp);
        }
    }

    protected void sendHasInteractions(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(standardCharsets);
        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(406,0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(resp);
        }
    }
}
