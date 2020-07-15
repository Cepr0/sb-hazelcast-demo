package io.github.cepr0.demo;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ModelService {

    @SneakyThrows
    @Cacheable("models")
    public Model getById(int id) {
        Thread.sleep(1500);
        Model model = new Model(id, "Model #" + id);
        log.info("[i] Retrieved model: {}", model);
        return model;
    }
}