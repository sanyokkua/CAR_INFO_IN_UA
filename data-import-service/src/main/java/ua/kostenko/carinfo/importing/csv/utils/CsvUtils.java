package ua.kostenko.carinfo.importing.csv.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.importing.csv.reader.options.Options;
import ua.kostenko.carinfo.importing.csv.reader.options.ReaderOptions;
import ua.kostenko.carinfo.importing.csv.structure.headers.CsvHeaders;
import ua.kostenko.carinfo.importing.io.EncodingUtils;

@Slf4j
public abstract class CsvUtils<T extends CsvHeaders> {

    private final File file;

    protected CsvUtils(@NonNull @Nonnull File file) {
        this.file = file;
    }

    @Nullable
    public Options<T> getOptions() {
        log.info("getOptions: checking encoding");
        String encoding = EncodingUtils.getEncoding(file);
        if (Objects.isNull(encoding)) {
            log.warn("getOptions: Encoding is null");
            return null;
        }
        String headerString = null;
        try {
            log.info("getOptions: opening file and reading all the lines");
            List<String> strings = FileUtils.readLines(file, encoding);
            headerString = strings.get(0);
            log.info("getOptions: header line is: {}", headerString);
        } catch (IOException e) {
            log.warn("getOptions: Problem with reading csv file");
        }
        if (Objects.isNull(headerString)) {
            log.warn("getOptions: Header line is null");
            return null;
        }

        T correctHeaders = getCorrectHeaders(headerString);
        if (Objects.isNull(correctHeaders)) {
            log.warn("getOptions: Problem with getting correct headers for this csv file");
            return null;
        }
        char delimiter = getDelimiter(correctHeaders.getHeaderField(), headerString);
        return new Options<>(new ReaderOptions(delimiter, encoding, file), correctHeaders);
    }

    private char getDelimiter(@NonNull @Nonnull String firstColumn, @NonNull @Nonnull String headerString) {
        log.info("getDelimiter: trying to figure out delimiter in this csv file");
        String resultString = headerString.replaceAll("\"", "").replaceFirst(firstColumn, "");
        char delimiter = resultString.charAt(0);
        log.info("getDelimiter: found next delimiter: {}", delimiter);
        return delimiter;
    }

    protected abstract T getCorrectHeaders(@Nonnull String headerString);

}
