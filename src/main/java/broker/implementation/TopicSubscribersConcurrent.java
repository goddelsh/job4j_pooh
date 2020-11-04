package broker.implementation;

import broker.TopicSubscribers;

public class TopicSubscribersConcurrent<U, M> implements TopicSubscribers<U, M> {
    @Override
    public void addTopic(M message) {

    }

    @Override
    public M getTopic(U user) {
        return null;
    }
}
