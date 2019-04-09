package ua.kostenko.carinfo.importing.csv.mappers.administrative;

import lombok.NonNull;
import org.apache.commons.csv.CSVRecord;
import ua.kostenko.carinfo.importing.csv.mappers.CsvMapper;
import ua.kostenko.carinfo.importing.csv.pojo.AdministrativeObjectPojo;
import ua.kostenko.carinfo.importing.csv.structure.headers.administrative.AdministrativeHeaders;

import javax.annotation.Nonnull;

public class AdministrativeObjectCsvMapper implements CsvMapper<AdministrativeObjectPojo> {
    private final AdministrativeHeaders headers;

    public AdministrativeObjectCsvMapper(@NonNull @Nonnull AdministrativeHeaders headers) {
        this.headers = headers;
    }

    @Override
    public AdministrativeObjectPojo map(CSVRecord csvRecord) {
        return AdministrativeObjectPojo.builder()
                                       .id(getLong(csvRecord, headers.getId()))
                                       .type(getString(csvRecord, headers.getType()))
                                       .name(getString(csvRecord, headers.getName()))
                                       .build();
    }
}
