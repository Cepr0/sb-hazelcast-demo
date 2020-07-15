package io.github.cepr0.demo;

import org.springframework.data.hazelcast.repository.HazelcastRepository;
import org.springframework.data.keyvalue.annotation.KeySpace;

@KeySpace("models")
public interface ModelRepo extends HazelcastRepository<Model, Integer> {
}
