package ua.kostenko.carinfo.producing.utils.io;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import ua.kostenko.carinfo.producing.utils.csv.CSVMapper;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;

@Slf4j
public final class CSVReader {

    public static void mapCsvFile(Path file, char delimiter, CSVMapper mapper) {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(mapper);
        if (file.toFile().exists()) {
            LocalTime before = LocalTime.now();
            log.info("Starting mapping of csv records to objects, time: {}", before.toString());
            log.info("Csv file path: {}", file.toAbsolutePath().toString());
            try (Reader in = new FileReader(file.toFile())) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(delimiter).withFirstRecordAsHeader().parse(in);
                for (CSVRecord record : records) {
                    mapper.map(record);
                }
                log.info("Finished mapping csv records");
            } catch (IOException ex) {
                log.warn("IOException happened", ex);
            }
            Duration duration = Duration.between(before, LocalTime.now());
            log.info("Finished mapping all csv files from all files in zip. Time spent: {} ms, {} min", duration.toMillis(), duration.toMinutes());
        } else {
            log.warn("File is not exists: " + file.toAbsolutePath().toString());
        }
    }
}
