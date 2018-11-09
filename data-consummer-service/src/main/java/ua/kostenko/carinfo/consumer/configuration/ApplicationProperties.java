package ua.kostenko.carinfo.consumer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:additional.properties")
public class ApplicationProperties {
    @Value("${app.log.mapper.batch.size}")
    public String APP_LOG_MAPPER_BATCH_SIZE;

    @Value("${app.queue.registration.info.name}")
    public String QUEUE_REGISTRATION_INFO_NAME;
    @Value("${app.queue.exchange.name}")
    public String QUEUE_EXCHANGE;
    @Value("${app.queue.registration.info.routing.key}")
    public String QUEUE_REGISTRATION_INFO_ROUTING_KEY;
    @Value("${app.queue.region.code.name}")
    public String QUEUE_REGION_CODE_NAME;
    @Value("${app.queue.region.code.routing.key}")
    public String QUEUE_REGION_CODE_ROUTING_KEY;
    @Value("${app.queue.admin.object.name}")
    public String QUEUE_ADMIN_OBJECT_NAME;
    @Value("${app.queue.admin.object.routing.key}")
    public String QUEUE_ADMIN_OBJECT_ROUTING_KEY;
    @Value("${app.queue.service.center.name}")
    public String QUEUE_SERVICE_CENTER_NAME;
    @Value("${app.queue.service.center.routing.key}")
    public String QUEUE_SERVICE_CENTER_ROUTING_KEY;


}
