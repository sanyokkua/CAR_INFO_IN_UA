package ua.kostenko.carinfo.importing.csv.reader.options;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class ReaderOptions {

    private char delimiter;
    @NonNull
    private String encoding;
    @NonNull
    private File file;
}
