import Handlers.*;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class Server {


    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        server.createContext("/", new RedirectHandler());
        server.createContext("/home", new HomeHandler());
        server.createContext("/call", new CallHandler());

        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println("Listening on http://localhost:8001");
    };

    //logger.info(" Server started on port 8001");

    public Server() throws IOException {
        System.out.println("io exception thrown.");
    }



}