package ua.kostenko.carinfo.producing.queue.senders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.configuration.ApplicationProperties;
import ua.kostenko.carinfo.producing.queue.QueueSender;

@Slf4j
@Component
public class ServiceCenterDataSender extends QueueSender {

    public ServiceCenterDataSender(RabbitTemplate rabbitTemplate, ApplicationProperties properties) {
        super(rabbitTemplate, properties);
    }

    @Override
    protected String getRoutingKey() {
        return properties.QUEUE_SERVICE_CENTER_ROUTING_KEY;
    }
}
