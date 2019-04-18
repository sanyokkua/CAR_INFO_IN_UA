package ua.kostenko.carinfo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Deprecated
public final class PathUtils {

    @Deprecated
    public synchronized static Path getPath(String path) {
        Path destination = null;
        try {
            destination = Paths.get(new ClassPathResource(path).getFile().getAbsolutePath());
        } catch (IOException e) {
            log.error("Error with opening file", e);
        }
        return destination;
    }
}