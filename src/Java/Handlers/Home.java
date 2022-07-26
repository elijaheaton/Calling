package Java.Handlers;

import Java.Help;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class Home implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if ("GET".equals(httpExchange.getRequestMethod())) {
            Help.respond(httpExchange, "index", null);
        }

        // TODO: what if they do other requests?
    }

}