package ua.kostenko.carinfo.importing.io;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

    private static final String SYSTEM_TMP_FOLDER_PROPERTY = "java.io.tmpdir";
    private static final String SUB_DIRECTORY = "/registration";

    @Nullable
    public static synchronized File getTempDirectory() {
        String tempDirPath = System.getProperty(SYSTEM_TMP_FOLDER_PROPERTY);
        File tempDirectory = new File(tempDirPath);
        if (tempDirectory.exists()) {
            File directory = createDirectory(tempDirectory.getAbsolutePath() + SUB_DIRECTORY);
            if (Objects.nonNull(directory)) {
                log.info("getTempDirectory: Temp directory path: {}", directory.getAbsolutePath());
                return directory;
            } else {
                log.warn("getTempDirectory: Was the problem with creating temp directory");
                return null;
            }
        } else {
            log.warn("getTempDirectory: Temp directory {} doesn't exist", tempDirectory.getAbsolutePath());
            return null;
        }
    }

    @Nullable
    private synchronized static File createDirectory(@NonNull @Nonnull String filePath) {
        log.info("createDirectory: Creating directory by path: " + filePath);
        File directory = new File(filePath);
        if (directory.exists()) {
            log.info("createDirectory: Directory by path {} already exists", filePath);
            return directory;
        } else {
            log.warn("createDirectory: Directory by path {} doesn't exist", filePath);
            log.info("createDirectory: Trying to create directory: {}", filePath);
            boolean isCreated = directory.mkdir();
            log.info("createDirectory: Directory is created: {}", isCreated);
            return isCreated ? directory : null;
        }
    }

    public synchronized static void deleteFiles(File... files) {
        for (File file : files) {
            try {
                String fileForDeletion = file.getAbsolutePath();
                log.info("deleteFiles: Deleting: {}", fileForDeletion);
                FileUtils.forceDelete(file);
                log.info("deleteFiles: {} deleted successfully", fileForDeletion);
            } catch (IOException e) {
                log.warn("deleteFiles: Problem occurred with deletion file: " + file.getAbsolutePath(), e);
            }
        }
    }

    @Nullable
    public synchronized static String getTextFromFile(@NonNull @Nonnull File file) {
        if (file.exists()) {
            String encoding = EncodingUtils.getEncoding(file);
            String resultString = null;
            try {
                resultString = FileUtils.readFileToString(file, encoding);
                log.info("getTextFromFile: File: " + file.getAbsolutePath() + " contains next text: \n" + resultString);
            } catch (IOException e) {
                log.error("getTextFromFile: Problem with reading file: " + file.getAbsolutePath() + " with encoding: "
                        + encoding);
            }
            return resultString;
        } else {
            log.warn("getTextFromFile: File doesn't exist: " + file.getAbsolutePath());
            return null;
        }
    }
}
