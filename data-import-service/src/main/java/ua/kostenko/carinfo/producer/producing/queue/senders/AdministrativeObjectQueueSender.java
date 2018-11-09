package ua.kostenko.carinfo.producer.producing.queue.senders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.producer.configuration.ApplicationProperties;
import ua.kostenko.carinfo.producer.producing.queue.QueueSender;

@Slf4j
@Component
public class AdministrativeObjectQueueSender extends QueueSender {

    public AdministrativeObjectQueueSender(RabbitTemplate rabbitTemplate, ApplicationProperties applicationProperties) {
        super(rabbitTemplate, applicationProperties);
    }

    @Override
    protected String getRoutingKey() {
        return properties.QUEUE_ADMIN_OBJECT_ROUTING_KEY;
    }
}
