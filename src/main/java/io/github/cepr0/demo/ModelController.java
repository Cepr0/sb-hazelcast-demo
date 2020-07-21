package io.github.cepr0.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("models")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping("/{id}")
    public Model getById(@PathVariable int id) {
        log.info("[i] Retrieving a model #{}...", id);
        return modelService.getById(id);
    }

    @GetMapping
    public Iterable<Model> getAll() {
        log.info("[i] Retrieving all models...");
        return modelService.getAll();
    }

    @PostMapping
    public Model create(@RequestBody Map<String, Object> body) {
        log.info("[i] Creating a new model...");
        return modelService.create((int) body.get("id"));
    }
}
