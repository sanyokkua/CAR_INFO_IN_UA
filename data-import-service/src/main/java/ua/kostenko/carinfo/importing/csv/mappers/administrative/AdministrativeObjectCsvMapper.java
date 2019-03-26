package ua.kostenko.carinfo.importing.csv.mappers.administrative;

import lombok.NonNull;
import org.apache.commons.csv.CSVRecord;
import ua.kostenko.carinfo.importing.csv.mappers.CsvMapper;
import ua.kostenko.carinfo.importing.csv.pojo.AdministrativeObject;
import ua.kostenko.carinfo.importing.csv.structure.headers.administrative.AdministrativeHeaders;

public class AdministrativeObjectCsvMapper implements CsvMapper<AdministrativeObject> {
    private final AdministrativeHeaders headers;

    public AdministrativeObjectCsvMapper(@NonNull AdministrativeHeaders headers) {
        this.headers = headers;
    }

    @Override
    public AdministrativeObject map(CSVRecord csvRecord) {
        return AdministrativeObject.builder()
                                   .id(getLong(csvRecord, headers.getId()))
                                   .type(getString(csvRecord, headers.getType()))
                                   .name(getString(csvRecord, headers.getName()))
                                   .build();
    }
}
