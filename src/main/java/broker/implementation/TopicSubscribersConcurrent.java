package broker.implementation;

import broker.TopicSubscribers;

public class TopicSubscribersConcurrent<U, N, M> implements TopicSubscribers<U, N, M> {
    @Override
    public void addTopic(N topicName, M message) {

    }

    @Override
    public M getTopic(U user, N topic) {
        return null;
    }
}
