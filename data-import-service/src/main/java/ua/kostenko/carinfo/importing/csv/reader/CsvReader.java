package ua.kostenko.carinfo.importing.csv.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.importing.csv.mappers.CsvMapper;
import ua.kostenko.carinfo.importing.csv.reader.options.ReaderOptions;
import ua.kostenko.carinfo.importing.importing.Persist;

@Slf4j
public class CsvReader<T> {

    public void readCsvFile(@NonNull @Nonnull ReaderOptions options, @NonNull @Nonnull CsvMapper<T> csvMapper,
            @NonNull @Nonnull Persist<T> persist) {
        log.info("readCsvFile: Going to parse csv file with options: {}", options.toString());
        File csvFile = options.getFile();
        if (Objects.nonNull(csvFile) && csvFile.exists()) {
            LocalTime before = LocalTime.now();
            log.info("readCsvFile: Starting mapping of csv records to objects, time: {}", before.toString());
            log.info("readCsvFile: Csv file path: {}", csvFile.getAbsolutePath());
            try (Reader input =
                    new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), options.getEncoding()))) {
                read(options.getDelimiter(), csvMapper, persist, input);
                log.info("readCsvFile: Finished mapping csv records");
            } catch (IOException ex) {
                log.warn("readCsvFile: IOException happened", ex);
            }
            Duration duration = Duration.between(before, LocalTime.now());
            log.info("readCsvFile: Finished mapping all csv files from all files in zip. Time spent: {} ms, {} min",
                    duration.toMillis(), duration.toMinutes());
        } else {
            log.warn("readCsvFile: File is not exists: " + csvFile.getAbsolutePath());
        }
    }

    private void read(char delimiter, @NonNull @Nonnull CsvMapper<T> csvMapper, @NonNull @Nonnull Persist<T> persist,
            @NonNull @Nonnull Reader input) throws IOException {
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(delimiter).withFirstRecordAsHeader().parse(input);
        for (CSVRecord record : records) {
            T obj = csvMapper.map(record);
            persist.persist(obj);
        }
    }

}
