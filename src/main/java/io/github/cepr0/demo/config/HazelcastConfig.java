package io.github.cepr0.demo.config;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import io.github.cepr0.demo.model.Model;
import io.github.cepr0.demo.template.QueueTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Slf4j
@Configuration
public class HazelcastConfig {

    private final HazelcastInstance hz;

    public HazelcastConfig(@Qualifier("hazelcastInstance") HazelcastInstance hz) {
        this.hz = hz;
    }

    @Bean
    public Map<Long, Model> modelMap() {
        return hz.getMap("models");
    }

    @Bean
    public FlakeIdGenerator idGenerator() {
        return hz.getFlakeIdGenerator("id");
    }

    @Bean
    public QueueTemplate<Long> modelNotFoundQueue() {
        return QueueTemplate.<Long>builder()
                .hazelcastInstance(hz)
                .queueName("ModelNotFound")
                .eventHandler(id -> {
                    log.info("[i] Creating the model #{}", id);
                    Model model = new Model(id);
                    modelMap().put(id, model);
                    log.info("[i] Model has been created: {}", model);
                })
                .build();
    }
}
