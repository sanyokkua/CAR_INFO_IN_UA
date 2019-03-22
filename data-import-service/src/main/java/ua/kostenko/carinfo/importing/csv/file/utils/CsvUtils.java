package ua.kostenko.carinfo.importing.csv.file.utils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import ua.kostenko.carinfo.importing.csv.file.ReaderOptions;
import ua.kostenko.carinfo.importing.csv.structure.*;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class CsvUtils <T extends CsvHeaders> {
    private final File file;

    public CsvUtils(@NonNull File file) {
        this.file = file;
    }

    @Nullable
    public ReaderOptions getOptions() {
        String encoding = EncodingUtils.getEncoding(file);
        if (Objects.isNull(encoding)) {
            return null;
        }
        String headerString = null;
        try {
            List<String> strings = FileUtils.readLines(file, encoding);
            headerString = strings.get(0);
        } catch (IOException e) {
            log.warn("Problem with reading csv file");
        }
        if (Objects.isNull(headerString)) {
            return null;
        }

        T correctHeaders = getCorrectHeaders(headerString);
        if (Objects.isNull(correctHeaders)) {
            return null;
        }
        char delimiter = getDelimiter(correctHeaders.getHeaderField(), headerString);
        return new ReaderOptions(delimiter, encoding, file);
    }

    private char getDelimiter(@NonNull String firstColumn, @NonNull String headerString) {
        String resultString = headerString.replaceFirst(firstColumn, "");
        return resultString.charAt(0);
    }

    protected abstract T getCorrectHeaders(String headerString);

}
