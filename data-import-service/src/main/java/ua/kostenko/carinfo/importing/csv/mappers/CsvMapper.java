package ua.kostenko.carinfo.importing.csv.mappers;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

public interface CsvMapper<T> {
    T map(CSVRecord csvRecord);

    @Nullable
    default Long getLong(String number) {
        Long result = null;
        try {
            result = Long.valueOf(StringUtils.trim(number));
        } catch (Exception ex) {
            //ignore
        }
        return result;
    }

    @Nullable
    default String getString(String value) {
        if (StringUtils.isNotBlank(value)) {
            return value.trim();
        }
        return value;
    }

    @Nullable
    default String getString(CSVRecord csvRecord, String key) {
        return getString(csvRecord.get(key));
    }

    @Nullable
    default Long getLong(CSVRecord csvRecord, String key) {
        return getLong(csvRecord.get(key));
    }

    @Nullable
    default String getStringValueInUpperCase(CSVRecord csvRecord, String header){
        String value = getString(csvRecord, header);
        return StringUtils.isNotBlank(value)? value.toUpperCase() : null;
    }
}
