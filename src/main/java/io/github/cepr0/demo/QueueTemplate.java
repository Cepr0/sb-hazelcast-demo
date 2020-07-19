package io.github.cepr0.demo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.function.Consumer;

@Slf4j
public class QueueTemplate<T extends Serializable> implements ItemListener<T> {

    private final String queueName;
    private final Consumer<T> eventHandler;
    private final IQueue<T> queue;

    @Builder
    private QueueTemplate(
            @NonNull HazelcastInstance hazelcastInstance,
            @NonNull String queueName,
            @NonNull Consumer<T> eventHandler
    ) {
        this.queueName = queueName + "Queue";
        this.eventHandler = eventHandler;
        this.queue = hazelcastInstance.getQueue(this.queueName);
        this.queue.addItemListener(this, true);
        log.debug("[d] A QueueTemplate '{}' has been created", this.queueName);
    }

    public void publish(@NonNull T message) {
        queue.add(message);
    }

    @Override
    public void itemAdded(ItemEvent<T> event) {
        T item = event.getItem();
        log.debug("[d] Event 'Item {} added' to Queue '{}' has been received", item, queueName);
        T message = queue.poll();
        if (message != null) {
            eventHandler.accept(message);
        }
        log.debug("[d] Event 'Item {} added' to Queue '{}' has been processed", item, queueName);
    }

    @Override
    public void itemRemoved(ItemEvent<T> message) {
        log.debug("[d] Event 'Item {} removed' from Queue '{}' has been received", queueName, message.getItem());
    }
}
