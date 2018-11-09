package ua.kostenko.carinfo.producer.producing.queue;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.producer.configuration.ApplicationProperties;

@Slf4j
@Component
public abstract class QueueSender {
    protected final ApplicationProperties properties;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public QueueSender(RabbitTemplate rabbitTemplate, ApplicationProperties properties) {
        Preconditions.checkNotNull(rabbitTemplate);
        Preconditions.checkNotNull(properties);
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    public void send(String message) {
        if (StringUtils.isNotBlank(message)) {
            rabbitTemplate.convertAndSend(properties.QUEUE_EXCHANGE, getRoutingKey(), message);
        } else {
            log.warn("Message is empty");
        }
    }

    protected abstract String getRoutingKey();

}
