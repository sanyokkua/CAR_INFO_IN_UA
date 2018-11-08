package ua.kostenko.carinfo.producing.utils.csv.tools;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.common.ObjectMapperTool;
import ua.kostenko.carinfo.common.PathUtils;
import ua.kostenko.carinfo.common.data.entities.RegionCodeEntity;
import ua.kostenko.carinfo.configuration.ApplicationProperties;
import ua.kostenko.carinfo.producing.queue.QueueSender;
import ua.kostenko.carinfo.producing.utils.Initializer;
import ua.kostenko.carinfo.producing.utils.csv.fields.RegionCodeCSV;
import ua.kostenko.carinfo.producing.utils.io.CSVReader;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class CsvRegionCodeImportTool implements Initializer {
    private final ApplicationProperties applicationProperties;
    private final QueueSender queueSender;

    @Autowired
    public CsvRegionCodeImportTool(ApplicationProperties applicationProperties, @Qualifier("regionCodeQueueSender") QueueSender queueSender) {
        Preconditions.checkNotNull(applicationProperties);
        Preconditions.checkNotNull(queueSender);
        this.applicationProperties = applicationProperties;
        this.queueSender = queueSender;
    }

    @Override
    public void init() {
        LocalTime before = LocalTime.now();
        log.info("Initializing RegionCode data from csv. Time: {}", before.toString());
        final List<RegionCodeEntity> resultList = new ArrayList<>();
        Path destination = PathUtils.getPath(applicationProperties.APP_REGION_CODES_FILE_PATH);
        CSVReader.mapCsvFile(destination, ';', record -> resultList.add(RegionCodeCSV.mapRecord(record)));
        resultList.stream().parallel().filter(Objects::nonNull).map(ObjectMapperTool::writeValueAsString).forEach(queueSender::send);
        LocalTime after = LocalTime.now();
        log.info("RegionCode data parsed and sent into RabbitMQ queue. Time: {}, duration: {} ms", after.toString(), Duration.between(before, after).toMillis());
    }
}
