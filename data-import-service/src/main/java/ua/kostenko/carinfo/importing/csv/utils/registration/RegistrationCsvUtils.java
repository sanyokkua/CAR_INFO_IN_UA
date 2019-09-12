package ua.kostenko.carinfo.importing.csv.utils.registration;

import java.io.File;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.importing.csv.structure.enums.RegistrationCsvRecord;
import ua.kostenko.carinfo.importing.csv.structure.headers.registration.LowerCaseRegistrationHeaders;
import ua.kostenko.carinfo.importing.csv.structure.headers.registration.RegistrationHeaders;
import ua.kostenko.carinfo.importing.csv.structure.headers.registration.UpperCaseRegistrationHeaders;
import ua.kostenko.carinfo.importing.csv.utils.CsvUtils;

@Slf4j
public class RegistrationCsvUtils extends CsvUtils<RegistrationHeaders> {

    public RegistrationCsvUtils(File file) {
        super(file);
    }

    protected RegistrationHeaders getCorrectHeaders(@Nonnull String headerString) {
        if (headerString.contains(RegistrationCsvRecord.PERSON.getLowerCase())) {
            log.info("Csv contains lowerCaseHeaders");
            return new LowerCaseRegistrationHeaders();
        } else if (headerString.contains(RegistrationCsvRecord.PERSON.getUpperCase())) {
            log.info("Csv contains upperCaseHeaders");
            return new UpperCaseRegistrationHeaders();
        }
        log.warn("Csv is not contains headers");
        return null;
    }
}
