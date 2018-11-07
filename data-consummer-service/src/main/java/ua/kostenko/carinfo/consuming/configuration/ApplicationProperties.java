package ua.kostenko.carinfo.consuming.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:additional.properties")
public class ApplicationProperties {
    @Value("${app.log.mapper.batch.size}")
    public String APP_LOG_MAPPER_BATCH_SIZE;
    @Value("${app.queue.name}")
    public String QUEUE_NAME;
    @Value("${app.queue.exchange.name}")
    public String QUEUE_EXCHANGE_NAME;
    @Value("${app.queue.routing.key}")
    public String QUEUE_ROUTING_KEY;
}
