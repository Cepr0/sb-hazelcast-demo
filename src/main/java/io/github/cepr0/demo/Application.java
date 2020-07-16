package io.github.cepr0.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.hazelcast.repository.config.EnableHazelcastRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableHazelcastRepositories
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
