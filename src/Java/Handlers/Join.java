package Java.Handlers;

import Java.Help;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Join implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // two possiblities, they come here from /home or from the /call link
        Map<String, String> map = new HashMap<>();
        if ("GET".equals(httpExchange.getRequestMethod())) {
            String uri = String.valueOf(httpExchange.getRequestURI());
            String no = "";
            if (uri.contains("?no=")) {
                // then we see if that call is actively occurring
                // TODO: is this call even happening?
                String[] array = uri.split("\\?");
                for (String s : array) {
                    String[] sides = s.split("=");
                    if (sides.length == 2 && "no".equals(sides[0])) {
                        no = sides[1];
                        break;
                    }
                }
            }
            else {
                // they want to submit their own call number
            }
            map.put("no", no);

            Help.respond(httpExchange, "join", map);
        }

        // TODO: what if they do other requests?
    }

}