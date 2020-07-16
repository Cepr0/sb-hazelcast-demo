package io.github.cepr0.demo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import lombok.NonNull;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

public class MessengerTemplate<T extends Serializable> implements MessageListener<T> {

    private final Consumer<T> messageHandler;
    private final Lock lock;
    private final ITopic<T> topic;

    public MessengerTemplate(
            @NonNull HazelcastInstance hz,
            @NonNull String topicName,
            @NonNull Consumer<T> messageHandler
    ) {
        this.messageHandler = messageHandler;
        this.lock = hz.getCPSubsystem().getLock(topicName + "Lock");
        this.topic = hz.getTopic(topicName);
        this.topic.addMessageListener(this);
    }

    @Override
    public void onMessage(Message<T> message) {
        if (lock.tryLock()) {
            try {
                messageHandler.accept(message.getMessageObject());
            } finally {
                lock.unlock();
            }
        }
    }

    public void publish(T message) {
        topic.publish(message);
    }
}