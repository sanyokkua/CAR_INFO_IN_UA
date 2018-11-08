package ua.kostenko.carinfo.consuming.queue.receivers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.common.ObjectMapperTool;
import ua.kostenko.carinfo.common.data.entities.RegionCodeEntity;
import ua.kostenko.carinfo.consuming.persistent.services.RegionCodeService;
import ua.kostenko.carinfo.consuming.queue.QueueReceiver;
import ua.kostenko.carinfo.consuming.queue.SaveScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class RegionCodeQueueReceiver implements QueueReceiver {
    private final List<RegionCodeEntity> temp;
    private final SaveScheduler<RegionCodeEntity> saver;

    public RegionCodeQueueReceiver(RegionCodeService regionCodeService) {
        temp = Collections.synchronizedList(new ArrayList<>());
        saver = new SaveScheduler<>(regionCodeService);
        saver.schedule(temp, 5);
    }

    @Override
    public void receiveMessage(String message) {
        RegionCodeEntity regionCodeEntity = ObjectMapperTool.readValue(message, RegionCodeEntity.class);
        if (Objects.nonNull(regionCodeEntity)) {
            temp.add(regionCodeEntity);
        }
    }
}
