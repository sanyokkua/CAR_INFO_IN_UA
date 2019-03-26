package ua.kostenko.carinfo.importing.csv.reader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import ua.kostenko.carinfo.importing.csv.reader.options.ReaderOptions;
import ua.kostenko.carinfo.importing.csv.mappers.CsvMapper;
import ua.kostenko.carinfo.importing.importing.Persist;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

@Slf4j
public class CsvReader {

    public static <T> void readCsvFile(@NonNull ReaderOptions options, @NonNull CsvMapper<T> csvMapper, @NonNull Persist<T> persist) {
        log.info("readCsvFile: Going to parse csv file with options: {}", options.toString());
        File csvFile = options.getFile();
        if (Objects.nonNull(csvFile) && csvFile.exists()) {
            LocalTime before = LocalTime.now();
            log.info("readCsvFile: Starting mapping of csv records to objects, time: {}", before.toString());
            log.info("readCsvFile: Csv file path: {}", csvFile.getAbsolutePath());
            try (Reader input = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), options.getEncoding()))) {
                read(options.getDelimiter(), csvMapper, persist, input);
                log.info("readCsvFile: Finished mapping csv records");
            } catch (IOException ex) {
                log.warn("readCsvFile: IOException happened", ex);
            }
            Duration duration = Duration.between(before, LocalTime.now());
            log.info("readCsvFile: Finished mapping all csv files from all files in zip. Time spent: {} ms, {} min", duration.toMillis(), duration.toMinutes());
        } else {
            log.warn("readCsvFile: File is not exists: " + csvFile.getAbsolutePath());
        }
    }

    private static <T> void read(char delimiter, @NonNull CsvMapper<T> csvMapper, @NonNull Persist<T> persist, @NonNull Reader input) throws IOException {
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(delimiter).withFirstRecordAsHeader().parse(input);
        for (CSVRecord record : records) {
            T obj = csvMapper.map(record);
            persist.persist(obj);
        }
    }

}
