package io.github.cepr0.demo.config;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import io.github.cepr0.demo.model.Model;
import io.github.cepr0.demo.template.QueueTemplate;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.provider.hazelcast4.HazelcastLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;

@Slf4j
@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "1m")
public class SchedulerConfig {
    public static final int RATE_IN_MS = 50;

    private final FlakeIdGenerator idGenerator;
    private final Map<Long, Model> modelMap;
    private final QueueTemplate<Long> modelNotFoundQueue;

    public SchedulerConfig(FlakeIdGenerator idGenerator, Map<Long, Model> modelMap, QueueTemplate<Long> modelNotFoundQueue) {
        this.idGenerator = idGenerator;
        this.modelMap = modelMap;
        this.modelNotFoundQueue = modelNotFoundQueue;
    }

    @Bean
    public HazelcastLockProvider lockProvider(@Qualifier("hazelcastInstance") HazelcastInstance hz) {
        return new HazelcastLockProvider(hz);
    }

    @Scheduled(fixedRate = RATE_IN_MS)
    @SchedulerLock(name = "modelLock")
    public void createOrUpdate() {
        LockAssert.assertLocked();
        long id = idGenerator.newId();
        int count = modelMap.size();
        if (count < 1000) {
            modelNotFoundQueue.publish(id);
            log.info("[i] Event has been published {}", id);
        }
    }

}
