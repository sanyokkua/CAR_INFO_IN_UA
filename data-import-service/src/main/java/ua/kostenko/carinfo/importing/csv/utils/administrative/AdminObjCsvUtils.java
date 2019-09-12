package ua.kostenko.carinfo.importing.csv.utils.administrative;

import java.io.File;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.importing.csv.structure.enums.AdminObjCsvRecord;
import ua.kostenko.carinfo.importing.csv.structure.headers.administrative.AdministrativeHeaders;
import ua.kostenko.carinfo.importing.csv.utils.CsvUtils;

@Slf4j
public class AdminObjCsvUtils extends CsvUtils<AdministrativeHeaders> {

    public AdminObjCsvUtils(File file) {
        super(file);
    }

    @Override
    protected AdministrativeHeaders getCorrectHeaders(@Nonnull String headerString) {
        if (headerString.contains(AdminObjCsvRecord.NP.getFieldName())) {
            log.info("Csv contains headers");
            return new AdministrativeHeaders();
        }
        log.warn("Csv is not contains headers");
        return null;
    }
}
