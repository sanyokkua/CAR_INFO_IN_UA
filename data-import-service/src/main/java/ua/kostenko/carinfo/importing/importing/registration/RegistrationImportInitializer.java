package ua.kostenko.carinfo.importing.importing.registration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.common.api.records.*;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.importing.configuration.ApplicationProperties;
import ua.kostenko.carinfo.importing.importing.Initializer;
import ua.kostenko.carinfo.importing.io.FileDownloader;
import ua.kostenko.carinfo.importing.io.FileUtil;
import ua.kostenko.carinfo.importing.json.registration.RegistrationDataPackage;
import ua.kostenko.carinfo.importing.json.registration.ResourceDataPackage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RegistrationImportInitializer implements Initializer {
    private static final String METADATA_JSON_FILE_NAME = "metadata.json";
    private final ExecutorService executorService;
    private final ApplicationProperties properties;
    private final DBService<Registration> service;
    private final DBService<AdministrativeObject> administrativeObjectDBService;
    private final DBService<BodyType> bodyTypeDBService;
    private final DBService<Brand> brandDBService;
    private final DBService<Color> colorDBService;
    private final DBService<Department> departmentDBService;
    private final DBService<FuelType> fuelTypeDBService;
    private final DBService<Kind> kindDBService;
    private final DBService<Model> modelDBService;
    private final DBService<Operation> operationDBService;
    private final DBService<Purpose> purposeDBService;
    private final DBService<Vehicle> vehicleDBService;

    @Autowired
    public RegistrationImportInitializer(@NonNull @Nonnull ApplicationProperties properties,
                                         @NonNull @Nonnull DBService<Registration> service,
                                         @NonNull @Nonnull DBService<AdministrativeObject> administrativeObjectDBService,
                                         @NonNull @Nonnull DBService<BodyType> bodyTypeDBService,
                                         @NonNull @Nonnull DBService<Brand> brandDBService,
                                         @NonNull @Nonnull DBService<Color> colorDBService,
                                         @NonNull @Nonnull DBService<Department> departmentDBService,
                                         @NonNull @Nonnull DBService<FuelType> fuelTypeDBService,
                                         @NonNull @Nonnull DBService<Kind> kindDBService,
                                         @NonNull @Nonnull DBService<Model> modelDBService,
                                         @NonNull @Nonnull DBService<Operation> operationDBService,
                                         @NonNull @Nonnull DBService<Purpose> purposeDBService,
                                         @NonNull @Nonnull DBService<Vehicle> vehicleDBService) {
        this.properties = properties;
        this.service = service;
        this.administrativeObjectDBService = administrativeObjectDBService;
        this.bodyTypeDBService = bodyTypeDBService;
        this.brandDBService = brandDBService;
        this.colorDBService = colorDBService;
        this.departmentDBService = departmentDBService;
        this.fuelTypeDBService = fuelTypeDBService;
        this.kindDBService = kindDBService;
        this.modelDBService = modelDBService;
        this.operationDBService = operationDBService;
        this.purposeDBService = purposeDBService;
        this.vehicleDBService = vehicleDBService;
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
            downloadLinks.forEach(link -> executorService.execute(new FileProcessingTask(link, tempDirectory, service,
                                                                                         administrativeObjectDBService,
                                                                                         bodyTypeDBService,
                                                                                         brandDBService,
                                                                                         colorDBService,
                                                                                         departmentDBService,
                                                                                         fuelTypeDBService,
                                                                                         kindDBService,
                                                                                         modelDBService,
                                                                                         operationDBService,
                                                                                         purposeDBService,
                                                                                         vehicleDBService)));
        } else {
            throw new RuntimeException("importRegistrations: Temp directory is null. Download can't be performed");
        }
        executorService.shutdown();
    }

    @Nullable
    private File downloadMetadataJson(@NonNull @Nonnull String metadataJsonUrl) {
        File tempDirectory = FileUtil.getTempDirectory();
        if (Objects.nonNull(tempDirectory)) {
            File metadataJsonFile = new File(tempDirectory.getAbsoluteFile() + File.pathSeparator + METADATA_JSON_FILE_NAME);
            return FileDownloader.downloadFile(metadataJsonUrl, metadataJsonFile.getAbsolutePath());
        }
        return null;
    }

    private List<String> getDownloadLinks(@NonNull @Nonnull String metadataJson) {
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
