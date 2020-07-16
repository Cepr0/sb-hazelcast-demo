package io.github.cepr0.demo;

import org.springframework.data.hazelcast.repository.HazelcastRepository;

public interface ModelRepo extends HazelcastRepository<Model, Integer> {
}
