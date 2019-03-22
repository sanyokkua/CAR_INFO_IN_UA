package ua.kostenko.carinfo.importing.csv.mappers;

import org.apache.commons.csv.CSVRecord;

public interface Mapper<T> {
    T map(CSVRecord csvRecord);
}
