package io.github.cepr0.demo;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.keyvalue.core.KeyValueAdapter;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.map.MapKeyValueAdapter;

import java.util.Map;

@Configuration
public class MapRepoConfig {

    private final HazelcastInstance hz;

    public MapRepoConfig(@Qualifier("hazelcastInstance") HazelcastInstance hz) {
        this.hz = hz;
    }

    @Bean
    public KeyValueOperations keyValueTemplate() {
        return new KeyValueTemplate(keyValueAdapter());
    }

    @Bean
    public KeyValueAdapter keyValueAdapter() {
        return new MapKeyValueAdapter(Map.of(
                "models", hz.getMap("models")
        ));
    }
}
