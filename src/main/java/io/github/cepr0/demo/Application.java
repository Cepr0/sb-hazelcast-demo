package io.github.cepr0.demo;

import com.hazelcast.core.HazelcastInstance;
import net.javacrumbs.shedlock.provider.hazelcast.HazelcastLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.hazelcast.repository.config.EnableHazelcastRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

// @EnableCaching
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "10s")
@EnableHazelcastRepositories
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public HazelcastLockProvider lockProvider(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
        return new HazelcastLockProvider(hazelcastInstance);
    }

}
