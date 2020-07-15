package io.github.cepr0.demo;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class ModelFeeder {

    private final ModelRepo modelRepo;

    public ModelFeeder(ModelRepo modelRepo) {
        this.modelRepo = modelRepo;
    }

    @SneakyThrows
    @Scheduled(fixedRate = 10_000)
    @SchedulerLock(name = "feedLock")
    public void feed() {
        LockAssert.assertLocked();
        log.info("[i] Feeding a model...");
        Thread.sleep(1500);
        int id = ThreadLocalRandom.current().nextInt(1, 100);
        Model model = new Model(id, "model #" + id);
        modelRepo.save(model);
        log.info("[i] Model has been saved: {}", model);
    }
}
