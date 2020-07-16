package io.github.cepr0.demo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public ITopic<Integer> modelNotFoundTopic(
            @Qualifier("hazelcastInstance") HazelcastInstance hz,
            ModelListener modelListener
    ) {
        ITopic<Integer> modelNotFound = hz.getTopic("ModelNotFound");
        modelNotFound.addMessageListener(modelListener);
        return modelNotFound;
    }
}
