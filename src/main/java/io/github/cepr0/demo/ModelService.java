package io.github.cepr0.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ModelService {

    private final ModelRepo modelRepo;

    public ModelService(ModelRepo modelRepo) {
        this.modelRepo = modelRepo;
    }

    public Model getById(int id) {
        return modelRepo.findById(id).orElseGet(() -> modelRepo.save(new Model(id)));
    }

    public Iterable<Model> getAll() {
        return modelRepo.findAll(Sort.by("updatedAt").descending());
    }

    public Model create(int id) {
        return modelRepo.save(new Model(id));
    }
}
