package ua.kostenko.carinfo.carinfoua.utils;

import com.google.common.base.Preconditions;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CsvRegionCodeImportTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvRegionCodeImportTool.class);
    private final ApplicationProperties applicationProperties;

    public CsvRegionCodeImportTool(ApplicationProperties applicationProperties) {
        Preconditions.checkNotNull(applicationProperties);
        this.applicationProperties = applicationProperties;
    }

    public String getRegion(String carRegistrationNumber) {
        Preconditions.checkArgument(StringUtils.isNotBlank(carRegistrationNumber));
        String code = carRegistrationNumber.substring(0, 1);
        LOGGER.info("Looking for region by registration number: {}, and code: {}", carRegistrationNumber, code);
        String resultRegion = mapCsvFile().getOrDefault(code, applicationProperties.APP_REGION_CODES_ABSENT);
        LOGGER.info("Found region: {}", resultRegion);
        return resultRegion;
    }

    private Map<String, String> mapCsvFile() {
        Path destination = null;
        try {
            destination = Paths.get(new ClassPathResource(applicationProperties.APP_REGION_CODES_FILE_PATH).getFile().getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("Error with opening file", e);
        }
        Map<String, String> resultMap = new HashMap<>();
        if (Objects.nonNull(destination)) {
            LOGGER.info("Starting mapping of csv with region codes records to Map");
            try (Reader in = new FileReader(destination.toFile())) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
                for (CSVRecord record : records) {
                    resultMap.put(record.get("Code"), record.get("Region"));
                }
            } catch (IOException ex) {
                LOGGER.warn("IOException happened", ex);
            }
            LOGGER.info("Finished mapping of csv with region codes records to Map");
        }
        return resultMap;
    }
}
