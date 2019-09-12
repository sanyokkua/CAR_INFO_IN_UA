package ua.kostenko.carinfo.importing.csv.structure.enums;

import javax.annotation.Nonnull;
import lombok.NonNull;

public enum AdminObjCsvRecord {
    TE("TE"), NP("NP"), NU("NU");

    private final String fieldName;

    AdminObjCsvRecord(@NonNull @Nonnull String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return this.fieldName;
    }
}
