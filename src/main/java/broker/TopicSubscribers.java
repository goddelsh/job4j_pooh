package broker;

public interface TopicSubscribers<U, N, M> {
    void addTopic(N topicName, M message);

    M getTopic(U user, N topic);
}
