package io.github.cepr0.demo;

import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MessengerConfig {

    @Bean
    public MessengerTemplate<Integer> modelNotFoundMessenger(
            @Qualifier("hazelcastInstance") HazelcastInstance hz,
            ModelRepo modelRepo
    ) {
        return new MessengerTemplate<>(hz, "ModelNotFound", id -> {
            log.info("[i] Received message ModelNotFound: #{}", id);
            log.info("[i] Start creating the model #{}", id);
            Model model = modelRepo.save(new Model(id));
            log.info("[i] Model has been created: {}", model);
        });
    }
}
