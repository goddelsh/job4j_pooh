package broker.implementation;

import broker.TopicSubscribers;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicSubscribersConcurrent<U, M> implements TopicSubscribers<U, M> {

    private final Map<String, Queue<M>> topicMessages = new ConcurrentHashMap<>();


    @Override
    public void addTopic(M message) {
        for(Map.Entry<String, Queue<M>> entry : topicMessages.entrySet()) {
            entry.getValue().add(message);
        }
    }

    @Override
    public M getTopic(U user) {
        return topicMessages.get(user.toString()).poll();
    }

    @Override
    public void subscribeToTopic(U user) {
        topicMessages.computeIfAbsent(user.toString(), (key) -> new ConcurrentLinkedQueue<>());
    }

    @Override
    public void unsubscribeToTopic(U user) {
        topicMessages.remove(user.toString());
    }
}
