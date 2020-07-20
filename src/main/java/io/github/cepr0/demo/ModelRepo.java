package io.github.cepr0.demo;

import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface ModelRepo extends KeyValueRepository<Model, Integer> {
}
