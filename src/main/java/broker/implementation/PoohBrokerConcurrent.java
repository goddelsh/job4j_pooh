package broker.implementation;

import broker.PoohBroker;
import broker.TopicSubscribers;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PoohBrokerConcurrent<U, N, M> implements PoohBroker<U, N, M> {

    private final ConcurrentHashMap<N, Queue<M>> messages;

    private final ConcurrentHashMap<N, TopicSubscribers<U, M>> topics;

    public PoohBrokerConcurrent() {
        this.messages = new ConcurrentHashMap<>();
        this.topics = new ConcurrentHashMap<>();
    }


    @Override
    public void subscribeToTopic(U user, N topicName) {
        this.topics.computeIfPresent(topicName, (key, value) -> {
            value.subscribeToTopic(user);
            return value;
        });

        this.topics.computeIfAbsent(topicName, (key) -> {
            TopicSubscribers<U, M> topicSubscribers = new TopicSubscribersConcurrent<>();
            topicSubscribers.subscribeToTopic(user);
            return topicSubscribers;
        });
    }

    @Override
    public void unsubscribeToTopic(U user, N topicName) {
        this.topics.computeIfPresent(topicName, (key, value) -> {
            value.unsubscribeToTopic(user);
            return value;
        });
    }

    @Override
    public void addMessage(N queueName, M message) {
        this.messages.computeIfPresent(queueName, (key, value) -> {
            value.add(message);
            return value;
        });

        this.messages.computeIfAbsent(queueName, (key) -> {
            Queue<M> queue = new ConcurrentLinkedQueue<>();
            queue.add(message);
            return queue;
        });
    }

    @Override
    public M getMessage(N queueName) {
        return this.messages.get(queueName).poll();
    }

    @Override
    public void addTopic(N topicName, M message) {
        this.topics.computeIfPresent(topicName, (key, value) -> {
            value.addTopic(message);
            return value;
        });

        this.topics.computeIfAbsent(topicName, (key) -> {
            TopicSubscribers<U, M> topicSubscribers = new TopicSubscribersConcurrent<>();
            topicSubscribers.addTopic(message);
            return topicSubscribers;
        });
    }

    @Override
    public M getTopic(U user, N topic) {
        return this.topics.get(topic).getTopic(user);
    }
}

