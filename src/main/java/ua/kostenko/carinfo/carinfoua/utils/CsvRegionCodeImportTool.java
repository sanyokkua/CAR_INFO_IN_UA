package ua.kostenko.carinfo.carinfoua.utils;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.configuration.ApplicationProperties;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class CsvRegionCodeImportTool {
    private final ApplicationProperties applicationProperties;

    public CsvRegionCodeImportTool(ApplicationProperties applicationProperties) {
        Preconditions.checkNotNull(applicationProperties);
        this.applicationProperties = applicationProperties;
    }

    public String getRegion(String carRegistrationNumber) {
        Preconditions.checkArgument(StringUtils.isNotBlank(carRegistrationNumber));
        String code = carRegistrationNumber.substring(0, 1);
        log.info("Looking for region by registration number: {}, and code: {}", carRegistrationNumber, code);
        String resultRegion = mapCsvFile().getOrDefault(code, applicationProperties.APP_REGION_CODES_ABSENT);
        log.info("Found region: {}", resultRegion);
        return resultRegion;
    }

    private Map<String, String> mapCsvFile() {
        Path destination = null;
        try {
            destination = Paths.get(new ClassPathResource(applicationProperties.APP_REGION_CODES_FILE_PATH).getFile().getAbsolutePath());
        } catch (IOException e) {
            log.error("Error with opening file", e);
        }
        Map<String, String> resultMap = new HashMap<>();
        if (Objects.nonNull(destination)) {
            log.info("Starting mapping of csv with region codes records to Map");
            try (Reader in = new FileReader(destination.toFile())) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
                for (CSVRecord record : records) {
                    resultMap.put(record.get("Code"), record.get("Region"));
                }
            } catch (IOException ex) {
                log.warn("IOException happened", ex);
            }
            log.info("Finished mapping of csv with region codes records to Map");
        }
        return resultMap;
    }
}
