package ua.kostenko.carinfo.importing.importing.registration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.importing.configuration.ApplicationProperties;
import ua.kostenko.carinfo.importing.importing.Initializer;
import ua.kostenko.carinfo.importing.io.FileDownloader;
import ua.kostenko.carinfo.importing.io.FileUtil;
import ua.kostenko.carinfo.importing.json.registration.RegistrationDataPackage;
import ua.kostenko.carinfo.importing.json.registration.ResourceDataPackage;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RegistrationImportInitializer implements Initializer {
    private static final String METADATA_JSON_FILE_NAME = "metadata.json";
    private final ExecutorService executorService;
    private final ApplicationProperties properties;

    @Autowired
    public RegistrationImportInitializer(@NonNull ApplicationProperties properties) {
        this.properties = properties;
        int coresNumber = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(coresNumber);
    }

    @Override
    public void init() {
        File metadataJson = downloadMetadataJson(properties.APP_STRUCTURE_DATA_PACKAGE_JSON_URL);
        String metadataJsonText = FileUtil.getTextFromFile(metadataJson);
        List<String> downloadLinks = getDownloadLinks(metadataJsonText);
        File tempDirectory = FileUtil.getTempDirectory();
        if (Objects.nonNull(tempDirectory)) {
            downloadLinks.forEach(link -> executorService.execute(new FileProcessingTask(link, tempDirectory)));
        } else {
            throw new RuntimeException("importRegistrations: Temp directory is null. Download can't be performed");
        }
        executorService.shutdown();
    }

    @Nullable
    private File downloadMetadataJson(@NonNull String metadataJsonUrl) {
        File tempDirectory = FileUtil.getTempDirectory();
        if (Objects.nonNull(tempDirectory)) {
            File metadataJsonFile = new File(tempDirectory.getAbsoluteFile() + File.pathSeparator + METADATA_JSON_FILE_NAME);
            return FileDownloader.downloadFile(metadataJsonUrl, metadataJsonFile.getAbsolutePath());
        }
        return null;
    }

    private List<String> getDownloadLinks(@NonNull String metadataJson) {
        Gson gson = new Gson();
        RegistrationDataPackage pojo = gson.fromJson(metadataJson, RegistrationDataPackage.class);
        try {
            log.info("getDownloadLinks: StructureDataPackage JSON content:\n{}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pojo));
        } catch (JsonProcessingException e) {
            log.warn("getDownloadLinks: Problem with writing StructureDataPackage JSON for logging", e);
        }
        return Objects.nonNull(pojo) ? pojo.getResources().stream().map(ResourceDataPackage::getPath).collect(Collectors.toList()) : Collections.emptyList();
    }
}
