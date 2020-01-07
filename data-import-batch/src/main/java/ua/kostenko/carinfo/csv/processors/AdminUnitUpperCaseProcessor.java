package ua.kostenko.carinfo.csv.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import ua.kostenko.carinfo.csv.records.AdministrativeUnitRecord;
import ua.kostenko.carinfo.utils.Util;

public class AdminUnitUpperCaseProcessor implements ItemProcessor<AdministrativeUnitRecord, AdministrativeUnitRecord> {

    @Override
    public AdministrativeUnitRecord process(@NonNull AdministrativeUnitRecord administrativeUnitRecord) {
        Long code = administrativeUnitRecord.getCode();
        String type = administrativeUnitRecord.getType();
        String name = administrativeUnitRecord.getName();
        return AdministrativeUnitRecord.builder()
                .code(code)
                .type(Util.toUpperCase(type))
                .name(Util.toUpperCase(name))
                .build();
    }
}
