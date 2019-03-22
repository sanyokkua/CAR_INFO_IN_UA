package ua.kostenko.carinfo.importing.csv.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.io.File;

@Data
@AllArgsConstructor
@ToString
public class ReaderOptions {
    @NonNull
    private char delimiter;
    @NonNull
    private String encoding;
    @NonNull
    private File file;
}
