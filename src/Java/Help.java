package Java;

import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class Help {

    /**
     * Connection name generator, url safe
     * @return the uuid connection name
     */
    public static String connectionID() {
        UUID uuid =UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Random number generator to connect users to video streaming
     * @return the connection port, ranging from 8002 to 8999
     */
    public static int generatePortNumber() {
        Random random = new Random();
        return random.nextInt(998) + 8002; // 8002-8999
        // TODO: what if this is already used?
    }

    /**
     * Generic response generators with possibility to replace text in the html
     * @param httpExchange the exchange to turn requests to responses
     * @param replacements the "$variable$" to replace with "value"
     */
    public static void respond(HttpExchange httpExchange, String fileName, Map<String, String> replacements) {
        try {
            // Get file
            File file = new File("src/HTML/" + fileName + ".html");
            Scanner scan = new Scanner(file);

            // Set up response builder
            OutputStream outputStream = httpExchange.getResponseBody();
            StringBuilder htmlBuilder = new StringBuilder();

            // Push file to String with necessary replacements
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                // Check for any inputs
                if (replacements != null) {
                    for (String key : replacements.keySet()) {
                        String rep = "$" + key + "$";
                        if (line.contains(rep)) {
                            line = line.replace(rep, replacements.get(key));
                        }
                    }
                }


                // Add the completed line
                htmlBuilder.append(line);
            }
            String htmlResponse = htmlBuilder.toString();

            // Send response and close out
            httpExchange.sendResponseHeaders(200, htmlResponse.length());
            outputStream.write(htmlResponse.getBytes());
            outputStream.flush();
            outputStream.close();
            scan.close();
        }
        catch (IOException ignore) {}
    }
}
