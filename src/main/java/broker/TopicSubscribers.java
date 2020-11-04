package broker;

public interface TopicSubscribers<U, M> {
    void addTopic(M message);

    M getTopic(U user);

    void subscribeToTopic(U user);
}
