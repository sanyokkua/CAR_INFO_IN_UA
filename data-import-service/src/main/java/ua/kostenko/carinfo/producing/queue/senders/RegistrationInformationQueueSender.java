package ua.kostenko.carinfo.producing.queue.senders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.configuration.ApplicationProperties;
import ua.kostenko.carinfo.producing.queue.QueueSender;

@Component
@Slf4j
public class RegistrationInformationQueueSender extends QueueSender {

    @Autowired
    public RegistrationInformationQueueSender(RabbitTemplate rabbitTemplate, ApplicationProperties applicationProperties) {
        super(rabbitTemplate, applicationProperties);
    }

    @Override
    protected String getRoutingKey() {
        return properties.QUEUE_REGISTRATION_INFO_ROUTING_KEY;
    }
}
