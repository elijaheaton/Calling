package Java.Handlers;

import Java.DB;
import Java.Help;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.github.sarxos.webcam.*;

import javax.imageio.ImageIO;

public class Call implements HttpHandler {

    private String TASK;
    public Call(String task) {
        this.TASK = task;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // First thing's first: are they starting or joining a call?
        String connectionID; // TODO: join or create?
        DB database = new DB();

        // Set up stream
        Webcam webcam = Webcam.getDefault();
        // TODO: this is a new one unncessarily when its time to destroy?

        // if their only purpose is to end the call, end it
        if (this.TASK.equals("end")) {
            webcam.close();
            database.dbConnect();
            database.deleteByID(connectionID); // TODO: this isnt real yet
            database.dbConnect();
        }

        String name = getName(httpExchange);
        Headers responseHeaders = httpExchange.getResponseHeaders();

        if (name != null) {
            // They can chill with us!
            Map<String, String> map = new HashMap<>();
            int port = Help.generatePortNumber();

            WebcamStreamer webStream = new WebcamStreamer(port, webcam, 1000, false);
            webStream.start();

            // add to database
            // TODO: check with database before adding
            database.dbConnect();
            connectionID = Help.connectionID();
            database.insertConnection(connectionID, name, port);
            database.dbDisconnect(); // TODO: connection and disconnection should be inherent in other tasks

            // Add name and link
            map.put("name", name);
            map.put("link", "http://localhost:8001/"+connectionID);

            // Send response
            Help.respond(httpExchange, "call", map);

            // TODO: if they leave the call, end the video recording lol
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