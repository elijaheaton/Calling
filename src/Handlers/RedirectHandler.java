package Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class RedirectHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Headers head = httpExchange.getResponseHeaders();
        head.add("Location", "/home");
        httpExchange.sendResponseHeaders(303, head.size());
    }
}