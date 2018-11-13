package ua.kostenko.carinfo.consumer.consuming.queue.receivers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.common.ObjectMapperTool;
import ua.kostenko.carinfo.common.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.consumer.consuming.persistent.services.RegistrationInformationService;
import ua.kostenko.carinfo.consumer.consuming.queue.QueueReceiver;
import ua.kostenko.carinfo.consumer.consuming.queue.SaveScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class RegistrationInformationQueueReceiver implements QueueReceiver {
    private final List<RegistrationInformationEntity> temp;

    @Autowired
    public RegistrationInformationQueueReceiver(RegistrationInformationService registrationInformationService) {
        temp = Collections.synchronizedList(new ArrayList<>());
        SaveScheduler<RegistrationInformationEntity> saver = new SaveScheduler<>(registrationInformationService);
        saver.schedule(temp, 5);
    }

    @Override
    public void receiveMessage(String message) {
        RegistrationInformationEntity registrationInformationEntity = ObjectMapperTool.readValue(message, RegistrationInformationEntity.class);
        if (Objects.nonNull(registrationInformationEntity)) {
            temp.add(registrationInformationEntity);
        }
    }
}
