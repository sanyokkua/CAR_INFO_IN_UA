package ua.kostenko.carinfo.importing.importing.administrative;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.importing.configuration.ApplicationProperties;
import ua.kostenko.carinfo.importing.csv.mappers.administrative.AdministrativeObjectCsvMapper;
import ua.kostenko.carinfo.importing.csv.pojo.AdministrativeObject;
import ua.kostenko.carinfo.importing.csv.reader.CsvReader;
import ua.kostenko.carinfo.importing.csv.reader.options.Options;
import ua.kostenko.carinfo.importing.csv.structure.headers.administrative.AdministrativeHeaders;
import ua.kostenko.carinfo.importing.csv.utils.CsvUtils;
import ua.kostenko.carinfo.importing.csv.utils.administrative.AdminObjCsvUtils;
import ua.kostenko.carinfo.importing.importing.Initializer;
import ua.kostenko.carinfo.importing.importing.Persist;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Component
@Slf4j
public class AdminObjImportInitializer implements Initializer {
    private final ApplicationProperties properties;

    @Autowired
    public AdminObjImportInitializer(@NonNull ApplicationProperties properties) {
        this.properties = properties;
    }

    @Override
    public void init() {
        LocalTime before = LocalTime.now();
        log.info("init: Started initializing AdministrativeObjects, time: {}", before.toString());
        Path administrativeObjectsFilePath = null;
        try {
            administrativeObjectsFilePath = Paths.get(new ClassPathResource(properties.APP_ADMINISTRATIVE_OBJECTS_FILE_PATH).getFile().getAbsolutePath());
        } catch (IOException e) {
            log.error("init:  Error  with opening file", e);
        }
        if (nonNull(administrativeObjectsFilePath)) {
            log.info("init: AdministrativeObjects file path: {}", administrativeObjectsFilePath.toAbsolutePath().toString());
            log.info("init: Starting mapping of csv AdministrativeObjectEntity records to object");

            CsvUtils<AdministrativeHeaders> csvUtils = new AdminObjCsvUtils(administrativeObjectsFilePath.toFile());
            Options<AdministrativeHeaders> options = csvUtils.getOptions();

            if (Objects.nonNull(options)) {
                AdministrativeObjectCsvMapper mapper = new AdministrativeObjectCsvMapper(options.getHeaders());
                Persist<AdministrativeObject> persist = new AdminObjPersist();
                CsvReader.readCsvFile(options.getReaderOptions(), mapper, persist);
            } else {
                log.error("processExtractedFiles: Options is null");
            }
            LocalTime after = LocalTime.now();
            log.info("init: Finished initializing AdministrativeObjects. Sent all AdministrativeObjectEntity to queue Time: {}, duration: {} ms", after.toString(), Duration
                    .between(before, after).toMillis());
        } else {
            log.warn("init: Problem with getting path to file: {}", properties.APP_ADMINISTRATIVE_OBJECTS_FILE_PATH);
        }
    }
}
