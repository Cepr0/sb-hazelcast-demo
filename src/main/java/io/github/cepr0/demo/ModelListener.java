package io.github.cepr0.demo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

@Slf4j
@Component
public class ModelListener implements MessageListener<Integer> {

    private final ModelRepo modelRepo;
    private final Lock lock;

    public ModelListener(ModelRepo modelRepo, @Qualifier("hazelcastInstance") HazelcastInstance hz) {
        this.modelRepo = modelRepo;
        this.lock = hz.getCPSubsystem().getLock("onMessageModelNotFoundLock");
    }

    @SneakyThrows
    @Override
    public void onMessage(Message<Integer> message) {
        if (lock.tryLock()) {
            try {
                Integer id = message.getMessageObject();
                log.info("[i] Received message ModelNotFound: #{}", id);
                log.info("[i] Start creating the model #{}", id);
                Thread.sleep(2000);
                Model model = modelRepo.save(new Model(id));
                log.info("[i] Model has been created: {}", model);
            } finally {
                lock.unlock();
            }
        }
    }
}
