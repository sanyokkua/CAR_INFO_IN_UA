package ua.kostenko.carinfo.producer.producing.utils.csv.tools;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.common.ObjectMapperTool;
import ua.kostenko.carinfo.common.entities.AdministrativeObjectEntity;
import ua.kostenko.carinfo.producer.configuration.ApplicationProperties;
import ua.kostenko.carinfo.producer.producing.queue.QueueSender;
import ua.kostenko.carinfo.producer.producing.utils.Initializer;
import ua.kostenko.carinfo.producer.producing.utils.csv.fields.AdministrativeObjectCSV;
import ua.kostenko.carinfo.producer.producing.utils.io.CSVReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Component
@Slf4j
public class CsvAdministrativeObjectsImportTool implements Initializer {
    private final ApplicationProperties applicationProperties;
    private final QueueSender queueSender;

    @Autowired
    public CsvAdministrativeObjectsImportTool(ApplicationProperties applicationProperties, @Qualifier("administrativeObjectQueueSender") QueueSender queueSender) {
        Preconditions.checkNotNull(applicationProperties);
        Preconditions.checkNotNull(queueSender);
        this.applicationProperties = applicationProperties;
        this.queueSender = queueSender;
    }

    @Override
    public void init() {
        LocalTime before = LocalTime.now();
        log.info("Started initializing AdministrativeObjects, time: {}", before.toString());
        Path administrativeObjectsFilePath = null;
        try {
            administrativeObjectsFilePath = Paths.get(new ClassPathResource(applicationProperties.APP_ADMINISTRATIVE_OBJECTS_FILE_PATH).getFile().getAbsolutePath());
        } catch (IOException e) {
            log.error("Error with opening file", e);
        }
        if (nonNull(administrativeObjectsFilePath)) {
            log.info("AdministrativeObjects file path: {}", administrativeObjectsFilePath.toAbsolutePath().toString());
            log.info("Starting mapping of csv AdministrativeObjectEntity records to object");
            final List<AdministrativeObjectEntity> administrativeObjectEntityList = new ArrayList<>();
            CSVReader.mapCsvFile(administrativeObjectsFilePath, ';', record -> administrativeObjectEntityList.add(AdministrativeObjectCSV.mapRecord(record)));
            administrativeObjectEntityList.stream().parallel()
                                          .filter(administrativeObjectEntity -> nonNull(administrativeObjectEntity) && nonNull(administrativeObjectEntity.getId()))
                                          .map(ObjectMapperTool::writeValueAsString)
                                          .forEach(queueSender::send);
            LocalTime after = LocalTime.now();
            log.info("Finished initializing AdministrativeObjects. Sent all AdministrativeObjectEntity to queue Time: {}, duration: {} ms", after.toString(), Duration
                    .between(before, after).toMillis());
        } else {
            log.warn("Problem with getting path to file: {}", applicationProperties.APP_ADMINISTRATIVE_OBJECTS_FILE_PATH);
        }
    }
}
