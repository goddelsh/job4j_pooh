package broker.implementation;

import broker.TopicSubscribers;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicSubscribersConcurrent<U, M> implements TopicSubscribers<U, M> {


    private final Map<U, Queue<M>> topicMessages = new HashMap<>();


    @Override
    public void addTopic(M message) {
        for(Map.Entry<U, Queue<M>> entry : topicMessages.entrySet()) {
            entry.getValue().add(message);
        }
    }

    @Override
    public M getTopic(U user) {
        return topicMessages.get(user).poll();
    }

    @Override
    public void subscribeToTopic(U user) {
        topicMessages.put(user, new ConcurrentLinkedQueue<>());
    }
}
