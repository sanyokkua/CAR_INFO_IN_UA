package ua.kostenko.carinfo.importing.utils.csv;

import org.apache.commons.csv.CSVRecord;

public interface CSVMapper {
    void map(CSVRecord record);
}
