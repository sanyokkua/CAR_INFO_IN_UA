package ua.kostenko.carinfo.importing.csv.structure.enums;

import javax.annotation.Nonnull;
import lombok.NonNull;

public enum RegistrationCsvRecord {
    PERSON("person", "PERSON"),
    REG_ADDR_KOATUU("reg_addr_koatuu", "REG_ADDR_KOATUU"),
    OPER_CODE("oper_code", "OPER_CODE"),
    OPER_NAME("oper_name", "OPER_NAME"),
    D_REG("d_reg", "D_REG"),
    DEP_CODE("dep_code", "DEP_CODE"),
    DEP("dep", "DEP"),
    BRAND("brand", "BRAND"),
    MODEL("model", "MODEL"),
    MAKE_YEAR("make_year", "MAKE_YEAR"),
    COLOR("color", "COLOR"),
    KIND("kind", "KIND"),
    BODY("body", "BODY"),
    PURPOSE("purpose", "PURPOSE"),
    FUEL("fuel", "FUEL"),
    CAPACITY("capacity", "CAPACITY"),
    OWN_WEIGHT("own_weight", "OWN_WEIGHT"),
    TOTAL_WEIGHT("total_weight", "TOTAL_WEIGHT"),
    N_REG_NEW("n_reg_new", "N_REG_NEW");

    private final String lowerCase;
    private final String upperCase;

    RegistrationCsvRecord(@NonNull @Nonnull String lowerCase, @NonNull @Nonnull String upperCase) {
        this.lowerCase = lowerCase;
        this.upperCase = upperCase;
    }

    public String getLowerCase() {
        return lowerCase;
    }

    public String getUpperCase() {
        return upperCase;
    }
}
