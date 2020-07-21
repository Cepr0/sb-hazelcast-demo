package io.github.cepr0.demo.template;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.function.Consumer;

@Slf4j
public class TopicTemplate<T extends Serializable> implements MessageListener<T> {

    private final String topicName;
    private final Consumer<T> messageHandler;
    private final ITopic<T> topic;

    public TopicTemplate(
            @NonNull HazelcastInstance hz,
            @NonNull String topicName,
            @NonNull Consumer<T> messageHandler
    ) {
        this.topicName = topicName;
        this.messageHandler = messageHandler;
        this.topic = hz.getTopic(topicName);
        this.topic.addMessageListener(this);
        log.debug("[d] A TopicTemplate for Topic '{}' has been created", topicName);
    }

    @Override
    public void onMessage(Message<T> message) {
        log.debug("[d] A message has been received from Topic '{}'", topicName);
        messageHandler.accept(message.getMessageObject());
        log.debug("[d] A message from Topic '{}' has been processed", topicName);
    }

    public void publish(@NonNull T message) {
        topic.publish(message);
    }
}
