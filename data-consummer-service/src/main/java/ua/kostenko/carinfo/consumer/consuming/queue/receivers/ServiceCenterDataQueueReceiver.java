package ua.kostenko.carinfo.consumer.consuming.queue.receivers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.common.ObjectMapperTool;
import ua.kostenko.carinfo.common.entities.ServiceCenterEntity;
import ua.kostenko.carinfo.consumer.consuming.persistent.services.ServiceCenterService;
import ua.kostenko.carinfo.consumer.consuming.queue.QueueReceiver;
import ua.kostenko.carinfo.consumer.consuming.queue.SaveScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ServiceCenterDataQueueReceiver implements QueueReceiver {
    private final List<ServiceCenterEntity> temp;
    private final SaveScheduler<ServiceCenterEntity> saver;

    public ServiceCenterDataQueueReceiver(ServiceCenterService serviceCenterService) {
        temp = Collections.synchronizedList(new ArrayList<>());
        saver = new SaveScheduler<>(serviceCenterService);
        saver.schedule(temp, 5);
    }

    @Override
    public void receiveMessage(String message) {
        ServiceCenterEntity serviceCenterEntity = ObjectMapperTool.readValue(message, ServiceCenterEntity.class);
        if (Objects.nonNull(serviceCenterEntity)) {
            temp.add(serviceCenterEntity);
        }
    }
}
