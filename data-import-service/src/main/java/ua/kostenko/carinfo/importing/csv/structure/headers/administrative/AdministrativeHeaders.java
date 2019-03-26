package ua.kostenko.carinfo.importing.csv.structure.headers.administrative;

import ua.kostenko.carinfo.importing.csv.structure.enums.AdminObjCsvRecord;
import ua.kostenko.carinfo.importing.csv.structure.headers.CsvHeaders;

public class AdministrativeHeaders implements CsvHeaders {

    @Override
    public String getHeaderField() {
        return AdminObjCsvRecord.TE.getFieldName();
    }

    public String getId() {
        return AdminObjCsvRecord.TE.getFieldName();
    }

    public String getType() {
        return AdminObjCsvRecord.NP.getFieldName();
    }

    public String getName() {
        return AdminObjCsvRecord.NU.getFieldName();
    }
}
