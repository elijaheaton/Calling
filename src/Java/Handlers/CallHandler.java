package Java.Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class CallHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Headers head = httpExchange.getResponseHeaders();
        head.add("Location", "/home?caller=hello"); // TODO: change to input from post request
        httpExchange.sendResponseHeaders(303, head.size());
    }
}