package broker;

import java.util.Map;

public interface PoohBroker<U, N, M> {
    void addMessage(N queueName, M message);

    String getMessage(N queueName);

    void addTopic(N topicName, M message);

    String getTopic(U user, N topic);

}
