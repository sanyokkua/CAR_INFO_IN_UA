package ua.kostenko.carinfo.importing.io;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mozilla.universalchardet.UniversalDetector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

@Slf4j
public class EncodingUtils {

    @Nullable
    public synchronized static String getEncoding(@NonNull @Nonnull File textFile) {
        String encoding = null;
        try {
            log.debug("getEncoding: Trying to distinguish encoding of file: {}", textFile.getAbsolutePath());
            encoding = UniversalDetector.detectCharset(textFile);
        } catch (IOException e) {
            log.error("getEncoding: Exception occurred due encoding detecting", e);
        }
        if (encoding != null) {
            log.info("getEncoding: Detected encoding = {} for file: {}", encoding, textFile.getAbsoluteFile());
            return encoding;
        } else {
            log.warn("getEncoding: No encoding detected for file: {}", textFile.getAbsolutePath());
            return null;
        }
    }
}
