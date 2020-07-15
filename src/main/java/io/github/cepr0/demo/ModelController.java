package io.github.cepr0.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("models")
public class ModelController {

    private final ModelRepo modelRepo;

    public ModelController(ModelRepo modelRepo) {
        this.modelRepo = modelRepo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Model> getById(@PathVariable int id) {
        log.info("[i] Retrieving a model #{}...", id);
        return ResponseEntity.of(modelRepo.findById(id));
    }

    @GetMapping
    public Iterable<Model> getAll() {
        log.info("[i] Retrieving all models...");
        return modelRepo.findAll();
    }
}
