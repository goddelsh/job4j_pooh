import broker.implementation.PoohBrokerConcurrent;
import service.WebService;

import java.io.IOException;

public class ServiceWorker {

    private static final int port = 5213;

    public static void main(String[] args) {
        WebService service = new WebService(new PoohBrokerConcurrent<>(), port);
        try {
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
