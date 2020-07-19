package io.github.cepr0.demo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.locks.Lock;

@Slf4j
@Configuration
public class HazelcastConfig {

    public static final int RATE_IN_MS = 50;

    private final HazelcastInstance hz;
    private final Lock lock;
    private final IAtomicLong counter;
    private final ModelRepo modelRepo;

    public HazelcastConfig(@Qualifier("hazelcastInstance") HazelcastInstance hz, ModelRepo modelRepo) {
        this.hz = hz;
        this.lock = hz.getCPSubsystem().getLock("modelLock");
        this.counter = hz.getCPSubsystem().getAtomicLong("counter");
        this.modelRepo = modelRepo;
    }

    @Bean
    public QueueTemplate<Integer> modelNotFoundQueue() {
        return QueueTemplate.<Integer>builder()
                .hazelcastInstance(hz)
                .queueName("ModelNotFound")
                .eventHandler(id -> {
                    log.info("[i] Creating the model #{}", id);
                    Model model = modelRepo.save(new Model(id));
                    log.info("[i] Model has been created: {}", model);
                })
                .build();
    }

    // @SneakyThrows
    @Scheduled(fixedRate = RATE_IN_MS)
    public void createOrUpdate() {
        if (lock.tryLock(/*RATE_IN_MS, TimeUnit.MILLISECONDS*/)) {
            try {
                long i = counter.incrementAndGet();
                if (i < 1000) {
                    modelNotFoundQueue().publish((int) i);
                    log.info("[i] Event has been published {}", i);
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
