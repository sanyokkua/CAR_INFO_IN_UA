package ua.kostenko.carinfo.importing.csv.mappers.administrative;

import lombok.NonNull;
import org.apache.commons.csv.CSVRecord;
import ua.kostenko.carinfo.importing.csv.mappers.CsvMapper;
import ua.kostenko.carinfo.importing.csv.pojo.AdministrativeObjectCsvRecord;
import ua.kostenko.carinfo.importing.csv.structure.headers.administrative.AdministrativeHeaders;

import javax.annotation.Nonnull;

public class AdministrativeObjectCsvMapper implements CsvMapper<AdministrativeObjectCsvRecord> {
    private final AdministrativeHeaders headers;

    public AdministrativeObjectCsvMapper(@NonNull @Nonnull AdministrativeHeaders headers) {
        this.headers = headers;
    }

    @Override
    public AdministrativeObjectCsvRecord map(CSVRecord csvRecord) {
        return AdministrativeObjectCsvRecord.builder()
                                            .id(getLong(csvRecord, headers.getId()))
                                            .type(getStringValueInUpperCase(csvRecord, headers.getType()))
                                            .name(getStringValueInUpperCase(csvRecord, headers.getName()))
                                            .build();
    }
}
