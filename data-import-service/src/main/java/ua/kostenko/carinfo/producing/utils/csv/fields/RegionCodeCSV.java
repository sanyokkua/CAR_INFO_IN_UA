package ua.kostenko.carinfo.producing.utils.csv.fields;

import org.apache.commons.csv.CSVRecord;
import ua.kostenko.carinfo.common.data.entities.RegionCodeEntity;

public enum RegionCodeCSV {
    CODE("Code"),
    REGION("Region");

    private final String fieldName;

    RegionCodeCSV(String fieldName) {
        this.fieldName = fieldName;
    }

    public static RegionCodeEntity mapRecord(CSVRecord record) {
        String code = record.get(CODE.fieldName);
        String region = record.get(REGION.fieldName);
        return new RegionCodeEntity(code, region);
    }
}
