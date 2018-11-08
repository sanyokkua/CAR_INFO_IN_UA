package ua.kostenko.carinfo.producing.queue.senders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.configuration.ApplicationProperties;
import ua.kostenko.carinfo.producing.queue.QueueSender;

@Slf4j
@Component
public class RegionCodeQueueSender extends QueueSender {

    public RegionCodeQueueSender(RabbitTemplate rabbitTemplate, ApplicationProperties applicationProperties) {
        super(rabbitTemplate, applicationProperties);
    }

    @Override
    protected String getRoutingKey() {
        return properties.QUEUE_REGION_CODE_ROUTING_KEY;
    }
}
