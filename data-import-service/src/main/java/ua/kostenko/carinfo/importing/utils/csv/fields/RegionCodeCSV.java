package ua.kostenko.carinfo.importing.utils.csv.fields;

public enum RegionCodeCSV {
    CODE("Code"),
    REGION("Region");

    private final String fieldName;

    RegionCodeCSV(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
