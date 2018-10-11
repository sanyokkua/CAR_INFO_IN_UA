package ua.kostenko.carinfo.carinfoua.utils.csv.tools;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.configuration.ApplicationProperties;
import ua.kostenko.carinfo.carinfoua.utils.csv.CSVReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static ua.kostenko.carinfo.carinfoua.utils.csv.fields.RegionCodeCSV.CODE;
import static ua.kostenko.carinfo.carinfoua.utils.csv.fields.RegionCodeCSV.REGION;

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
        String code = carRegistrationNumber.substring(0, 2);
        log.info("Looking for region by registration number: {}, and code: {}", carRegistrationNumber, code);
        final Map<String, String> resultMap = new HashMap<>();
        Path destination = null;
        try {
            destination = Paths.get(new ClassPathResource(applicationProperties.APP_REGION_CODES_FILE_PATH).getFile().getAbsolutePath());
        } catch (IOException e) {
            log.error("Error with opening file", e);
        }
        CSVReader.mapCsvFile(destination, ';', record -> resultMap.put(record.get(CODE.getFieldName()), record.get(REGION.getFieldName())));
        String resultRegion = resultMap.getOrDefault(code, applicationProperties.APP_REGION_CODES_ABSENT);
        log.info("Found region: {}", resultRegion);
        return resultRegion;
    }

}
