package ua.kostenko.carinfo.importing.csv.reader.options;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.kostenko.carinfo.importing.csv.structure.headers.CsvHeaders;

@Data
@AllArgsConstructor
public class Options<T extends CsvHeaders> {

    ReaderOptions readerOptions;
    T headers;
}
