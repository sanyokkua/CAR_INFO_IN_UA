package ua.kostenko.carinfo.importing.csv.file;

import ua.kostenko.carinfo.importing.csv.structure.CsvHeaders;

public class RegistrationOptions<T extends CsvHeaders> {
    ReaderOptions readerOptions;
    T headers;
}
