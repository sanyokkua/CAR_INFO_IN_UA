package ua.kostenko.carinfo.carinfoua.utils.csv.fields;

import org.apache.commons.csv.CSVRecord;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.AdministrativeObjectEntity;

public enum AdministrativeObjectCSV {
    ID_NUMBER("TE"),
    TYPE_NAME("NP"),
    NAME("NU");

    private final String fieldName;

    AdministrativeObjectCSV(String fieldName) {
        this.fieldName = fieldName;
    }

    public static AdministrativeObjectEntity mapRecord(CSVRecord record) {
        long id = Long.valueOf(record.get(ID_NUMBER.fieldName));
        String type = record.get(TYPE_NAME.fieldName);
        String name = record.get(NAME.fieldName);
        return new AdministrativeObjectEntity(id, type, name);
    }
}
