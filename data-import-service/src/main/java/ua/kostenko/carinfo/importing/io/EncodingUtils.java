package ua.kostenko.carinfo.importing.io;

import java.io.File;
import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.mozilla.universalchardet.UniversalDetector;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EncodingUtils {

    @Nullable
    public static synchronized String getEncoding(@NonNull @Nonnull File textFile) {
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
