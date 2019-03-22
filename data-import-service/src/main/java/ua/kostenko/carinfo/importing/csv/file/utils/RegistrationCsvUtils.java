package ua.kostenko.carinfo.importing.csv.file.utils;

import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.importing.csv.structure.LowerCaseRegistrationHeaders;
import ua.kostenko.carinfo.importing.csv.structure.RegistrationCsvRecord;
import ua.kostenko.carinfo.importing.csv.structure.RegistrationHeaders;
import ua.kostenko.carinfo.importing.csv.structure.UpperCaseRegistrationHeaders;

import java.io.File;

@Slf4j
public class RegistrationCsvUtils extends CsvUtils<RegistrationHeaders> {

    public RegistrationCsvUtils(File file) {
        super(file);
    }

    protected RegistrationHeaders getCorrectHeaders(String headerString) {
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
