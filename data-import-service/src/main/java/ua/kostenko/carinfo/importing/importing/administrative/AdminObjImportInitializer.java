package ua.kostenko.carinfo.importing.importing.administrative;

import static java.util.Objects.nonNull;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.importing.configuration.ApplicationProperties;
import ua.kostenko.carinfo.importing.csv.mappers.administrative.AdministrativeObjectCsvMapper;
import ua.kostenko.carinfo.importing.csv.pojo.AdministrativeObjectCsvRecord;
import ua.kostenko.carinfo.importing.csv.reader.CsvReader;
import ua.kostenko.carinfo.importing.csv.reader.options.Options;
import ua.kostenko.carinfo.importing.csv.structure.headers.administrative.AdministrativeHeaders;
import ua.kostenko.carinfo.importing.csv.utils.CsvUtils;
import ua.kostenko.carinfo.importing.csv.utils.administrative.AdminObjCsvUtils;
import ua.kostenko.carinfo.importing.importing.Initializer;

@Component
@Slf4j
public class AdminObjImportInitializer implements Initializer {

    private final ApplicationProperties properties;
    private final AdminObjPersist persist;

    @Autowired
    public AdminObjImportInitializer(@NonNull @Nonnull ApplicationProperties properties,
            @NonNull @Nonnull AdminObjPersist persist) {
        this.properties = properties;
        this.persist = persist;
    }

    @Override
    public void init() {
        LocalTime before = LocalTime.now();
        log.info("init: Started initializing AdministrativeObjects, time: {}", before.toString());
        Path administrativeObjectsFilePath = null;
        try {
            administrativeObjectsFilePath = Paths.get(
                    new ClassPathResource(properties.APP_ADMINISTRATIVE_OBJECTS_FILE_PATH).getFile().getAbsolutePath());
        } catch (IOException e) {
            log.error("init:  Error  with opening file", e);
        }
        if (nonNull(administrativeObjectsFilePath)) {
            log.info("init: AdministrativeObjects file path: {}",
                    administrativeObjectsFilePath.toAbsolutePath().toString());
            log.info("init: Starting mapping of csv AdministrativeObjectEntity records to object");

            CsvUtils<AdministrativeHeaders> csvUtils = new AdminObjCsvUtils(administrativeObjectsFilePath.toFile());
            Options<AdministrativeHeaders> options = csvUtils.getOptions();

            if (Objects.nonNull(options)) {
                AdministrativeObjectCsvMapper mapper = new AdministrativeObjectCsvMapper(options.getHeaders());
                CsvReader<AdministrativeObjectCsvRecord> csvReader = new CsvReader<>();
                csvReader.readCsvFile(options.getReaderOptions(), mapper, persist);
            } else {
                log.error("processExtractedFiles: Options is null");
            }
            LocalTime after = LocalTime.now();
            log.info(
                    "init: Finished initializing AdministrativeObjects. Sent all AdministrativeObjectEntity to queue Time: {}, duration: {} ms",
                    after.toString(), Duration
                            .between(before, after).toMillis());
        } else {
            log.warn("init: Problem with getting path to file: {}", properties.APP_ADMINISTRATIVE_OBJECTS_FILE_PATH);
        }
    }
}
