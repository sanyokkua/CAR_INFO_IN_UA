package ua.kostenko.carinfo.importing.utils.csv.tools;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.importing.configuration.ApplicationProperties;
import ua.kostenko.carinfo.importing.data.entities.RegionCodeEntity;
import ua.kostenko.carinfo.importing.utils.Initializer;
import ua.kostenko.carinfo.importing.utils.csv.fields.RegionCodeCSV;
import ua.kostenko.carinfo.importing.utils.io.CSVReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CsvRegionCodeImportTool implements Initializer {
    private final ApplicationProperties applicationProperties;

    public CsvRegionCodeImportTool(ApplicationProperties applicationProperties) {
        Preconditions.checkNotNull(applicationProperties);
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void init() {
        LocalTime before = LocalTime.now();
        log.info("Initializing RegionCode data from csv. Time: {}", before.toString());
        final List<RegionCodeEntity> resultList = new ArrayList<>();
        Path destination = null;
        try {
            destination = Paths.get(new ClassPathResource(applicationProperties.APP_REGION_CODES_FILE_PATH).getFile().getAbsolutePath());
        } catch (IOException e) {
            log.error("Error with opening file", e);
        }
        CSVReader.mapCsvFile(destination, ';', record -> resultList.add(new RegionCodeEntity(record.get(RegionCodeCSV.CODE.getFieldName()), record.get(RegionCodeCSV.REGION.getFieldName()))));
        //regionCodeCrudRepository.saveAll(resultList);
        LocalTime after = LocalTime.now();
        log.info("RegionCode data parsed and saved into DB. Time: {}, duration: {} ms", after.toString(), Duration.between(before, after).toMillis());
    }
}
