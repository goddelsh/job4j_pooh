package broker;

public interface PoohBroker<U, N, M> {
    void addMessage(N queueName, M message);

    M getMessage(N queueName);

    void addTopic(N topicName, M message);

    M getTopic(U user, N topic);

}
