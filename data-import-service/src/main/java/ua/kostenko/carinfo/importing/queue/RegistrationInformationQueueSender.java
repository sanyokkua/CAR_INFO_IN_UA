package ua.kostenko.carinfo.importing.queue;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.importing.configuration.ApplicationProperties;

@Component
@Slf4j
public class RegistrationInformationQueueSender {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public RegistrationInformationQueueSender(RabbitTemplate rabbitTemplate, ApplicationProperties applicationProperties) {
        Preconditions.checkNotNull(rabbitTemplate);
        Preconditions.checkNotNull(applicationProperties);
        this.rabbitTemplate = rabbitTemplate;
        this.applicationProperties = applicationProperties;
    }

    public void send(String message) {
        log.info("Sending message...");
        if (StringUtils.isNotBlank(message)) {
            rabbitTemplate.convertAndSend(applicationProperties.QUEUE_EXCHANGE_NAME, applicationProperties.QUEUE_ROUTING_KEY, message);
        } else {
            log.warn("Message is empty");
        }
    }
}
