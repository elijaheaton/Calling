package Java;

import Java.Handlers.*;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class Server {


    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        DB database = new DB();
        database.dbConnect(); // connect
        database.initializeDB(); // make sure table exists

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        server.createContext("/", new Else());
        server.createContext("/home", new Home());
        server.createContext("/call", new Call("join"));
        server.createContext("/endCall", new Call("end"));
        server.createContext("/join", new Join());

        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println("Listening on http://localhost:8001");
    };

    //logger.info(" java.Server started on port 8001");

    public Server() throws IOException {
        System.out.println("io exception thrown.");
    }



}