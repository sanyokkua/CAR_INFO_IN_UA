package ua.kostenko.carinfo.importing.csv.structure.enums;

import lombok.NonNull;

public enum AdminObjCsvRecord {
    TE("TE"),
    NP("NP"),
    NU("NU");

    final String fieldName;

    AdminObjCsvRecord(@NonNull String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return this.fieldName;
    }
}
