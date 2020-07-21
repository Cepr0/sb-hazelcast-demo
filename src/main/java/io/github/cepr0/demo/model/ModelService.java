package io.github.cepr0.demo.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ModelService {

    private final Map<Long, Model> modelMap;

    public ModelService(Map<Long, Model> modelMap) {
        this.modelMap = modelMap;
    }

    public Model getById(long id) {
        return modelMap.computeIfAbsent(id, Model::new);
    }

    public Iterable<Model> getAll() {
        return modelMap.values();
    }

    public Model create(long id) {
        Model model = new Model(id);
        modelMap.put(id, model);
        return model;
    }
}
