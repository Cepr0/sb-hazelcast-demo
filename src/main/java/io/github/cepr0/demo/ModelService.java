package io.github.cepr0.demo;

import com.hazelcast.core.HazelcastInstance;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
@Component
public class ModelService {

    private final ModelRepo modelRepo;
    private final Lock lock;
    private final MessengerTemplate<Integer> modelNotFoundMessenger;

    public ModelService(
            ModelRepo modelRepo,
            @Qualifier("hazelcastInstance") HazelcastInstance hz,
            MessengerTemplate<Integer> modelNotFoundMessenger
    ) {
        this.modelRepo = modelRepo;
        this.lock = hz.getCPSubsystem().getLock("modelLock");
        this.modelNotFoundMessenger = modelNotFoundMessenger;
    }

    public Optional<Model> getById(int id) {
        return modelRepo.findById(id)
                .or(() -> {
                    modelNotFoundMessenger.publish(id);
                    log.info("[i] Published message ModelNotFound #{}", id);
                    return Optional.empty();
                });
    }

    @SneakyThrows
    @Scheduled(fixedRate = 2_000)
    public void createOrUpdate() {
        if (lock.tryLock(3, TimeUnit.SECONDS)) {
            try {
                log.info("[i] Start creating or updating the model...");

                Thread.sleep(2000);
                int id = ThreadLocalRandom.current().nextInt(1, 101);
                Model model = modelRepo.findById(id)
                        .map(m -> m.withUpdatedAt(Instant.now()))
                        .orElseGet(() -> new Model(id));

                modelRepo.save(model);
                log.info("[i] Model has been saved: {}", model);
            } finally {
                lock.unlock();
            }
        }
    }

    public Iterable<Model> getAll() {
        return modelRepo.findAll(Sort.by("updatedAt").descending());
    }
}
