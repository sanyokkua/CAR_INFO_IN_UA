package ua.kostenko.carinfo.importing.importing.registration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.common.api.records.*;
import ua.kostenko.carinfo.common.services.CrudService;
import ua.kostenko.carinfo.importing.csv.mappers.registration.RegistrationCsvMapper;
import ua.kostenko.carinfo.importing.csv.pojo.RegistrationPojo;
import ua.kostenko.carinfo.importing.csv.reader.CsvReader;
import ua.kostenko.carinfo.importing.csv.reader.options.Options;
import ua.kostenko.carinfo.importing.csv.structure.headers.registration.RegistrationHeaders;
import ua.kostenko.carinfo.importing.csv.utils.CsvUtils;
import ua.kostenko.carinfo.importing.csv.utils.registration.RegistrationCsvUtils;
import ua.kostenko.carinfo.importing.importing.Persist;
import ua.kostenko.carinfo.importing.io.ArchiveUtils;
import ua.kostenko.carinfo.importing.io.FileDownloader;
import ua.kostenko.carinfo.importing.io.FileUtil;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
public class FileProcessingTask implements Runnable {
    private final String link;
    private final File tempDirectory;
    private final CrudService<Registration> registrationCrudService;
    private final CrudService<AdministrativeObject> administrativeObjectCrudService;
    private final CrudService<BodyType> bodyTypeCrudService;
    private final CrudService<Brand> brandCrudService;
    private final CrudService<Color> colorCrudService;
    private final CrudService<Department> departmentCrudService;
    private final CrudService<FuelType> fuelTypeCrudService;
    private final CrudService<Kind> kindCrudService;
    private final CrudService<Model> modelCrudService;
    private final CrudService<Operation> operationCrudService;
    private final CrudService<Purpose> purposeCrudService;
    private final CrudService<Vehicle> vehicleCrudService;

    public FileProcessingTask(@NonNull @Nonnull String link,
                              @NonNull @Nonnull File tempDirectory,
                              @NonNull @Nonnull CrudService<Registration> registrationCrudService,
                              @NonNull @Nonnull CrudService<AdministrativeObject> administrativeObjectCrudService,
                              @NonNull @Nonnull CrudService<BodyType> bodyTypeCrudService,
                              @NonNull @Nonnull CrudService<Brand> brandCrudService,
                              @NonNull @Nonnull CrudService<Color> colorCrudService,
                              @NonNull @Nonnull CrudService<Department> departmentCrudService,
                              @NonNull @Nonnull CrudService<FuelType> fuelTypeCrudService,
                              @NonNull @Nonnull CrudService<Kind> kindCrudService,
                              @NonNull @Nonnull CrudService<Model> modelCrudService,
                              @NonNull @Nonnull CrudService<Operation> operationCrudService,
                              @NonNull @Nonnull CrudService<Purpose> purposeCrudService,
                              @NonNull @Nonnull CrudService<Vehicle> vehicleCrudService) {
        this.link = link;
        this.tempDirectory = tempDirectory;
        this.registrationCrudService = registrationCrudService;
        this.administrativeObjectCrudService = administrativeObjectCrudService;
        this.bodyTypeCrudService = bodyTypeCrudService;
        this.brandCrudService = brandCrudService;
        this.colorCrudService = colorCrudService;
        this.departmentCrudService = departmentCrudService;
        this.fuelTypeCrudService = fuelTypeCrudService;
        this.kindCrudService = kindCrudService;
        this.modelCrudService = modelCrudService;
        this.operationCrudService = operationCrudService;
        this.purposeCrudService = purposeCrudService;
        this.vehicleCrudService = vehicleCrudService;
    }

    @Override
    public void run() {
        doInBackground(tempDirectory, link);
    }

    private void doInBackground(@NonNull @Nonnull File tempDirectory, @NonNull @Nonnull String link) {
        String fileName = getFileName(link);
        String downloadFilePath = tempDirectory.getAbsoluteFile() + File.separator + fileName;
        File file = FileDownloader.downloadFile(link, downloadFilePath);
        if (Objects.nonNull(file) && file.exists()) {
            extractArchive(file, fileName);
        } else {
            log.warn("doInBackground: File is not downloaded successfully, file: {}", downloadFilePath);
        }
    }

    private String getFileName(@NonNull @Nonnull String link) {
        return link.substring(link.lastIndexOf("/"));
    }

    private void extractArchive(@NonNull @Nonnull File file, @NonNull @Nonnull String fileName) {
        File tempDirectory = FileUtil.getTempDirectory();
        if (Objects.nonNull(tempDirectory)) {
            String name = fileName.substring(0, fileName.lastIndexOf("."));
            File destinationDirectory = ArchiveUtils.extractZipArchive(file, Paths.get(tempDirectory.getAbsolutePath() + File.separator + name).toFile());
            processExtractedFiles(destinationDirectory);
        } else {
            throw new RuntimeException("extractArchive: Temp directory is null");
        }
    }

    private void processExtractedFiles(@NonNull @Nonnull File destinationDirectory) {
        Stream.of(destinationDirectory.listFiles()).forEach(fileInDirectory -> {
            CsvUtils<RegistrationHeaders> csvUtils = new RegistrationCsvUtils(fileInDirectory);
            Options<RegistrationHeaders> options = csvUtils.getOptions();
            if (Objects.nonNull(options)) {
                RegistrationCsvMapper mapper = new RegistrationCsvMapper(options.getHeaders());
                Persist<RegistrationPojo> persist = new RegistrationPersist(registrationCrudService,
                                                                            administrativeObjectCrudService,
                                                                            bodyTypeCrudService,
                                                                            brandCrudService,
                                                                            colorCrudService,
                                                                            departmentCrudService,
                                                                            fuelTypeCrudService,
                                                                            kindCrudService,
                                                                            modelCrudService,
                                                                            operationCrudService,
                                                                            purposeCrudService,
                                                                            vehicleCrudService);
                CsvReader.readCsvFile(options.getReaderOptions(), mapper, persist);
            } else {
                log.error("processExtractedFiles: Options is null");
            }
            FileUtil.deleteFiles(fileInDirectory);
        });
    }
}
