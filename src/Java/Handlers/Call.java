package Java.Handlers;

import Java.Help;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.github.sarxos.webcam.*;

import javax.imageio.ImageIO;

public class Call implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String name = getName(httpExchange);
        Headers responseHeaders = httpExchange.getResponseHeaders();

        if (name != null) {
            // They can chill with us!
            Map<String, String> map = new HashMap<>();

            // Open Camera
            Webcam webcam = Webcam.getDefault();
            webcam.open();

            // Capture single image for now
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(webcam.getImage(), "PNG", os);
            String s = Base64.getEncoder().encodeToString(os.toByteArray());

            webcam.close();

            // Add picture and name
            map.put("image", s);
            map.put("name", name);

            // Send response
            Help.respond(httpExchange, "call", map);
        }
        else {
            // Redirect home
            responseHeaders.add("Location", "/home");
            httpExchange.sendResponseHeaders(303, responseHeaders.size());
        }

    }

    private String getName(HttpExchange httpExchange) throws IOException {
        InputStream in = httpExchange.getRequestBody();
        try {
            String text = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            if (text.contains("name")) {
                String[] array = text.split(","); // TODO: is this how multiple posts are sent?
                for (String a : array) {
                    String[] equals = a.split("=");
                    if (Objects.equals(equals[0], "name")) {
                        if (equals.length == 1) {
                            // to get here, they didn't submit a name,
                            // but we should have captured this earlier
                            return null;
                        }
                        return equals[1];
                    }
                }
            }

        }
        catch (IOException ignored) {}
        return null;
    }
}