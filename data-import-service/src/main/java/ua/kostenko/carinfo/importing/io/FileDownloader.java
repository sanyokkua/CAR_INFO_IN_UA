package ua.kostenko.carinfo.importing.io;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
public class FileDownloader {

    public synchronized static File downloadFile(@NonNull String downloadUrl, @NonNull String pathToFileForSave) {
        Path targetPath = null;
        try {
            log.info("downloadFile: Downloading archive from URL: {}", downloadUrl);
            URL url = new URL(downloadUrl);

            targetPath = new File(pathToFileForSave).toPath();
            log.info("downloadFile: Saving archive to directory: {}", targetPath.toAbsolutePath().toString());

            Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("downloadFile: Finished downloading archive from: " + downloadUrl);
        } catch (IOException e) {
            log.error(String.format("Error occurred with downloading archive from: %s", downloadUrl), e);
        }
        Preconditions.checkNotNull(targetPath, String.format("Error occurred in downloading archive from: %s, path to archive is null", downloadUrl));
        log.info("downloadFile: Archive downloaded from: {}, path to archive: {}", downloadUrl, targetPath.toAbsolutePath().toString());
        return targetPath.toFile();
    }
}
