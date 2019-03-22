package ua.kostenko.carinfo.importing.csv.file;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import ua.kostenko.carinfo.importing.csv.mappers.Mapper;
import ua.kostenko.carinfo.importing.csv.Persist;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

@Slf4j
public class CsvReader {

    public static <T> void readCsvFile(@NonNull ReaderOptions options, @NonNull Mapper<T> mapper, @NonNull Persist<T> persist) {
        log.info("Going to parse csv file with options: {}", options.toString());
        File csvFile = options.getFile();
        if (Objects.nonNull(csvFile) && csvFile.exists()) {
            LocalTime before = LocalTime.now();
            log.info("Starting mapping of csv records to objects, time: {}", before.toString());
            log.info("Csv file path: {}", csvFile.getAbsolutePath());
            try (Reader input = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), options.getEncoding()))) {
                read(options.getDelimiter(), mapper, persist, input);
                log.info("Finished mapping csv records");
            } catch (IOException ex) {
                log.warn("IOException happened", ex);
            }
            Duration duration = Duration.between(before, LocalTime.now());
            log.info("Finished mapping all csv files from all files in zip. Time spent: {} ms, {} min", duration.toMillis(), duration.toMinutes());
        } else {
            log.warn("File is not exists: " + csvFile.getAbsolutePath());
        }
    }

    private static <T> void read(char delimiter, @NonNull Mapper<T> mapper, @NonNull Persist<T> persist, @NonNull Reader input) throws IOException {
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(delimiter).withFirstRecordAsHeader().parse(input);
        for (CSVRecord record : records) {
            T obj = mapper.map(record);
            persist.persist(obj);
        }
    }

}
