package ua.kostenko.carinfo.carinfoua.utils.csv;

import org.apache.commons.csv.CSVRecord;

public interface CSVMapper {
    void map(CSVRecord record);
}
