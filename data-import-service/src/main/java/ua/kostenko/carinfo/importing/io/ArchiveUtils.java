package ua.kostenko.carinfo.importing.io;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
public class ArchiveUtils {

    public synchronized static File extractZipArchive(@NonNull File archiveFile, @NonNull File destinationDirectory) {
        try {
            log.info("extractZipArchive: Extracting zip archive...");
            log.info("extractZipArchive: Archive file: {}", archiveFile.getAbsolutePath());
            log.info("extractZipArchive: Destination directory: {}", destinationDirectory.getAbsolutePath());
            ZipFile zipFile = new ZipFile(archiveFile);
            zipFile.extractAll(destinationDirectory.getAbsolutePath());
            log.info("extractZipArchive: All files was extracted to: {}", destinationDirectory.getAbsolutePath());
            log.info("extractZipArchive: All extracted files in folder {} [", destinationDirectory.getAbsolutePath());
            Stream.of(Objects.requireNonNull(destinationDirectory.list())).forEach(log::info);
            log.info("]");
            return destinationDirectory;
        } catch (ZipException e) {
            log.error("extractZipArchive: Error occurred with extracting files from zip archive", e);
            return null;
        }
    }
}
