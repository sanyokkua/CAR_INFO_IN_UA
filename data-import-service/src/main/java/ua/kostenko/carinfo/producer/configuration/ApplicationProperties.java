package ua.kostenko.carinfo.producer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:additional.properties")
public class ApplicationProperties {
    @Value("${app.data.set.json.url}")
    public String APP_STRUCTURE_DATA_PACKAGE_JSON_URL;
    @Value("${app.archive.name}")
    public String APP_ARCHIVE_NAME;
    @Value("${app.archive.dir}")
    public String APP_ARCHIVE_DIR;
    @Value("${app.administrative.objects.file.path}")
    public String APP_ADMINISTRATIVE_OBJECTS_FILE_PATH;
    @Value("${app.data.service.center.url}")
    public String APP_DATA_SERVICE_CENTER_URL;
    @Value("${app.data.service.center.css.selector}")
    public String APP_DATA_SERVICE_CENTER_CSS_SELECTOR;
    @Value("${app.region.codes.file.path}")
    public String APP_REGION_CODES_FILE_PATH;
    @Value("${app.region.codes.absent}")
    public String APP_REGION_CODES_ABSENT;
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
