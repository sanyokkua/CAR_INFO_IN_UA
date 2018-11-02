package ua.kostenko.carinfo.importing.utils.csv.tools;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.configuration.ApplicationProperties;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.AdministrativeObjectEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.AdministrativeObjectsCrudRepository;
import ua.kostenko.carinfo.importing.utils.Initializer;
import ua.kostenko.carinfo.importing.utils.csv.fields.AdministrativeObjectCSV;
import ua.kostenko.carinfo.importing.utils.io.CSVReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Component
@Slf4j
public class CsvAdministrativeObjectsImportTool implements Initializer {
    private final AdministrativeObjectsCrudRepository administrativeObjectsCrudRepository;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public CsvAdministrativeObjectsImportTool(AdministrativeObjectsCrudRepository administrativeObjectsCrudRepository, ApplicationProperties applicationProperties) {
        Preconditions.checkNotNull(administrativeObjectsCrudRepository);
        Preconditions.checkNotNull(applicationProperties);
        this.administrativeObjectsCrudRepository = administrativeObjectsCrudRepository;
        this.applicationProperties = applicationProperties;
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
        if (Objects.nonNull(administrativeObjectsFilePath)) {
            log.info("AdministrativeObjects file path: {}", administrativeObjectsFilePath.toAbsolutePath().toString());

            log.info("Starting mapping of csv AdministrativeObjectEntity records to object");
            final List<AdministrativeObjectEntity> administrativeObjectEntityList = new ArrayList<>();
            CSVReader.mapCsvFile(administrativeObjectsFilePath, ';', record -> {
                AdministrativeObjectEntity data = AdministrativeObjectCSV.mapRecord(record);
                administrativeObjectEntityList.add(data);
            });

            administrativeObjectsCrudRepository.saveAll(administrativeObjectEntityList.stream()
                    .filter(administrativeObjectEntity -> nonNull(administrativeObjectEntity)
                            && nonNull(administrativeObjectEntity.getId()))
                    .collect(Collectors.toList()));
            LocalTime after = LocalTime.now();
            log.info("Finished initializing AdministrativeObjects. Saved all AdministrativeObjectEntity to DB Time: {}, duration: {} ms", after.toString(), Duration.between(before, after).toMillis());
        } else {
            log.warn("Problem with getting path to file: {}", applicationProperties.APP_ADMINISTRATIVE_OBJECTS_FILE_PATH);
        }
    }
}
