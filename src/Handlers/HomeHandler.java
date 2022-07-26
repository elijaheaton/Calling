package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class HomeHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if("GET".equals(httpExchange.getRequestMethod())) {
            handleResponse(httpExchange);
        }
        else if("POST".equals(httpExchange.getRequestMethod())) {
            // for now, we'll get the name and use it in the video call
            System.out.println("post time");
        }
        // TODO: what if they do other requests?
    }

    private void handleResponse(HttpExchange httpExchange)  throws  IOException {
        // TODO: file not found exception
        // Get file
        File file = new File("src/index.html");
        Scanner scan = new Scanner(file);

        // Set up response builder
        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();

        // Push file to String
        while (scan.hasNextLine()) {
            htmlBuilder.append(scan.nextLine());
        }
        String htmlResponse = htmlBuilder.toString();

        // Send response and close out
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
        scan.close();
    }
}