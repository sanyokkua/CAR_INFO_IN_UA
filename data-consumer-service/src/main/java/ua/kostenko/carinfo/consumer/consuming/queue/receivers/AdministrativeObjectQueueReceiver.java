package ua.kostenko.carinfo.consumer.consuming.queue.receivers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.common.ObjectMapperTool;
import ua.kostenko.carinfo.common.entities.AdministrativeObjectEntity;
import ua.kostenko.carinfo.consumer.consuming.persistent.services.AdministrativeObjectsService;
import ua.kostenko.carinfo.consumer.consuming.queue.QueueReceiver;
import ua.kostenko.carinfo.consumer.consuming.queue.SaveScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class AdministrativeObjectQueueReceiver implements QueueReceiver {
    private final List<AdministrativeObjectEntity> temp;

    @Autowired
    public AdministrativeObjectQueueReceiver(AdministrativeObjectsService registrationInformationService) {
        temp = Collections.synchronizedList(new ArrayList<>());
        SaveScheduler<AdministrativeObjectEntity> saver = new SaveScheduler<>(registrationInformationService);
        saver.schedule(temp, 5);
    }

    @Override
    public void receiveMessage(String message) {
        AdministrativeObjectEntity administrativeObjectEntity = ObjectMapperTool.readValue(message, AdministrativeObjectEntity.class);
        if (Objects.nonNull(administrativeObjectEntity)) {
            temp.add(administrativeObjectEntity);
        }
    }
}
