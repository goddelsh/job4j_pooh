package broker.implementation;

import broker.PoohBroker;

public class PoohBrokerConcurrent<U, N, M> implements PoohBroker<U, N, M> {
    @Override
    public void addMessage(N queueName, M message) {

    }

    @Override
    public String getMessage(N queueName) {
        return null;
    }

    @Override
    public void addTopic(N topicName, M message) {

    }

    @Override
    public String getTopic(U user, N topic) {
        return null;
    }
}

