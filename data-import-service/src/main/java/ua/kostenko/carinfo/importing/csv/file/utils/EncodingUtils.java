package ua.kostenko.carinfo.importing.csv.file.utils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mozilla.universalchardet.UniversalDetector;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

@Slf4j
public class EncodingUtils {

    @Nullable
    public static String getEncoding(@NonNull File textFile) {
        String encoding = null;
        try {
            encoding = UniversalDetector.detectCharset(textFile);
        } catch (IOException e) {
            log.error("Exception occurred due encoding detecting", e);
        }
        if (encoding != null) {
            log.info("Detected encoding = {}", encoding);
            return encoding;
        } else {
            log.warn("No encoding detected for file: {}", textFile.getAbsolutePath());
            return null;
        }
    }
}
