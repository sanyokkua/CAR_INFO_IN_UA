package ua.kostenko.carinfo.carinfoua.utils.csv.tools;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.configuration.ApplicationProperties;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegionCodeEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.RegionCodeCrudRepository;
import ua.kostenko.carinfo.carinfoua.utils.Initializer;
import ua.kostenko.carinfo.carinfoua.utils.csv.CSVReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static ua.kostenko.carinfo.carinfoua.utils.csv.fields.RegionCodeCSV.CODE;
import static ua.kostenko.carinfo.carinfoua.utils.csv.fields.RegionCodeCSV.REGION;

@Component
@Slf4j
public class CsvRegionCodeImportTool implements Initializer {
    private final ApplicationProperties applicationProperties;
    private final RegionCodeCrudRepository regionCodeCrudRepository;

    public CsvRegionCodeImportTool(ApplicationProperties applicationProperties, RegionCodeCrudRepository regionCodeCrudRepository) {
        Preconditions.checkNotNull(applicationProperties);
        Preconditions.checkNotNull(regionCodeCrudRepository);
        this.applicationProperties = applicationProperties;
        this.regionCodeCrudRepository = regionCodeCrudRepository;
    }

    @Override
    public void init() {
        final List<RegionCodeEntity> resultList = new ArrayList<>();
        Path destination = null;
        try {
            destination = Paths.get(new ClassPathResource(applicationProperties.APP_REGION_CODES_FILE_PATH).getFile().getAbsolutePath());
        } catch (IOException e) {
            log.error("Error with opening file", e);
        }
        CSVReader.mapCsvFile(destination, ';', record -> resultList.add(new RegionCodeEntity(record.get(CODE.getFieldName()), record.get(REGION.getFieldName()))));
        regionCodeCrudRepository.saveAll(resultList);
    }
}
