package ua.kostenko.carinfo.carinfoua.utils;

import com.google.common.base.Preconditions;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.configuration.ApplicationProperties;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.AdministrativeObjectEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.AdministrativeObjectsCrudRepository;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static ua.kostenko.carinfo.carinfoua.data.persistent.entities.AdministrativeObjectEntity.AdministrativeObjectsFields.*;

@Component
public class CsvAdministrativeObjectsImportTool {
    private final static Logger LOGGER = LoggerFactory.getLogger(CsvAdministrativeObjectsImportTool.class);
    private final AdministrativeObjectsCrudRepository administrativeObjectsCrudRepository;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public CsvAdministrativeObjectsImportTool(AdministrativeObjectsCrudRepository administrativeObjectsCrudRepository, ApplicationProperties applicationProperties) {
        Preconditions.checkNotNull(administrativeObjectsCrudRepository);
        Preconditions.checkNotNull(applicationProperties);
        this.administrativeObjectsCrudRepository = administrativeObjectsCrudRepository;
        this.applicationProperties = applicationProperties;
    }

    public void initDB() {
        LOGGER.info("Started initializing AdministrativeObjects");
        Path administrativeObjectsFilePath = null;
        try {
            administrativeObjectsFilePath = Paths.get(new ClassPathResource(applicationProperties.APP_ADMINISTRATIVE_OBJECTS_FILE_PATH).getFile().getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("Error with opening file", e);
        }
        if (Objects.nonNull(administrativeObjectsFilePath)) {
            LOGGER.info("AdministrativeObjects file path: {}", administrativeObjectsFilePath.toAbsolutePath().toString());
            List<AdministrativeObjectEntity> administrativeObjectEntityList = mapCsvFile(administrativeObjectsFilePath);
            administrativeObjectsCrudRepository.saveAll(administrativeObjectEntityList.stream()
                    .filter(administrativeObjectEntity -> nonNull(administrativeObjectEntity) && nonNull(
                            administrativeObjectEntity.getId()))
                    .collect(Collectors.toList()));
            LOGGER.info("Finished initializing AdministrativeObjects. Saved all AdministrativeObjectEntity to DB");
        } else {
            LOGGER.warn("Problem with getting path to file: {}", applicationProperties.APP_ADMINISTRATIVE_OBJECTS_FILE_PATH);
        }
    }

    private List<AdministrativeObjectEntity> mapCsvFile(Path destination) {
        LOGGER.info("Starting mapping of csv AdministrativeObjectEntity records to object");
        List<AdministrativeObjectEntity> resultList = new ArrayList<>();
        try (Reader in = new FileReader(destination.toFile())) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                AdministrativeObjectEntity data = mapCsvToAdministrativeObject(record);
                resultList.add(data);
            }
            LOGGER.info("Finished mapping csv AdministrativeObjectEntity records");
        } catch (IOException ex) {
            LOGGER.warn("IOException happened", ex);
        }
        return resultList;
    }

    private AdministrativeObjectEntity mapCsvToAdministrativeObject(CSVRecord record) {
        long id = Long.valueOf(record.get(ID_NUMBER.getFieldName()));
        String type = record.get(TYPE_NAME.getFieldName());
        String name = record.get(NAME.getFieldName());
        return new AdministrativeObjectEntity(id, type, name);
    }
}
