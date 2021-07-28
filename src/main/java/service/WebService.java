package service;

import broker.PoohBroker;
import broker.structs.User;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class WebService {

    final private PoohBroker<User, String, String> poohBroker;

    final private int port;

    public WebService(PoohBroker<User, String, String> poohBroker, int port) {
        this.poohBroker = poohBroker;
        this.port = port;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(port), 0);
        server.createContext("/", new WebHandler(poohBroker));
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println("Pooh started on " + port + " port");
    }

}
