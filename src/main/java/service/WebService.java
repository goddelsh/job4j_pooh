package service;

import broker.PoohBroker;
import broker.structs.User;

public class WebService {

    final private PoohBroker<User, String, String> poohBroker;

    final private int port;

    public WebService(PoohBroker<User, String, String> poohBroker, int port) {
        this.poohBroker = poohBroker;
        this.port = port;
    }
}
