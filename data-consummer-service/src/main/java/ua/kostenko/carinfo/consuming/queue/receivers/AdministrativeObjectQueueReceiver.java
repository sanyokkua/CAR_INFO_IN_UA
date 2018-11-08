package ua.kostenko.carinfo.consuming.queue.receivers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.common.ObjectMapperTool;
import ua.kostenko.carinfo.common.data.entities.AdministrativeObjectEntity;
import ua.kostenko.carinfo.consuming.persistent.services.AdministrativeObjectsService;
import ua.kostenko.carinfo.consuming.queue.QueueReceiver;
import ua.kostenko.carinfo.consuming.queue.SaveScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class AdministrativeObjectQueueReceiver implements QueueReceiver {
    private final List<AdministrativeObjectEntity> temp;
    private final SaveScheduler<AdministrativeObjectEntity> saver;

    @Autowired
    public AdministrativeObjectQueueReceiver(AdministrativeObjectsService registrationInformationService) {
        temp = Collections.synchronizedList(new ArrayList<>());
        saver = new SaveScheduler<>(registrationInformationService);
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
