package ua.kostenko.carinfo.importing.importing.registration;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.common.api.records.BodyType;
import ua.kostenko.carinfo.common.api.records.Brand;
import ua.kostenko.carinfo.common.api.records.Color;
import ua.kostenko.carinfo.common.api.records.Department;
import ua.kostenko.carinfo.common.api.records.FuelType;
import ua.kostenko.carinfo.common.api.records.Kind;
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.common.api.records.Operation;
import ua.kostenko.carinfo.common.api.records.Purpose;
import ua.kostenko.carinfo.common.api.records.Registration;
import ua.kostenko.carinfo.common.api.records.Vehicle;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.importing.csv.mappers.registration.RegistrationCsvMapper;
import ua.kostenko.carinfo.importing.csv.pojo.RegistrationCsvRecord;
import ua.kostenko.carinfo.importing.csv.reader.CsvReader;
import ua.kostenko.carinfo.importing.csv.reader.options.Options;
import ua.kostenko.carinfo.importing.csv.structure.headers.registration.RegistrationHeaders;
import ua.kostenko.carinfo.importing.csv.utils.CsvUtils;
import ua.kostenko.carinfo.importing.csv.utils.registration.RegistrationCsvUtils;
import ua.kostenko.carinfo.importing.importing.Persist;
import ua.kostenko.carinfo.importing.io.ArchiveUtils;
import ua.kostenko.carinfo.importing.io.FileDownloader;
import ua.kostenko.carinfo.importing.io.FileUtil;

@Slf4j
class FileProcessingTask implements Runnable {

    private final String link;
    private final File tempDirectory;
    private final DBService<Registration> registrationDBService;
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

    FileProcessingTask(@NonNull @Nonnull String link, @NonNull @Nonnull File tempDirectory,
            @NonNull @Nonnull DBService<Registration> registrationDBService,
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
        this.link = link;
        this.tempDirectory = tempDirectory;
        this.registrationDBService = registrationDBService;
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
            File destinationDirectory = ArchiveUtils.extractZipArchive(file,
                    Paths.get(tempDirectory.getAbsolutePath() + File.separator + name).toFile());
            if (Objects.isNull(destinationDirectory)) {
                throw new NullPointerException("destinationDirectory is null. Problem with extracting zip archive");
            }
            processExtractedFiles(destinationDirectory);
        } else {
            throw new RuntimeException("extractArchive: Temp directory is null");
        }
    }

    private void processExtractedFiles(@NonNull @Nonnull File destinationDirectory) {
        File[] listFiles = destinationDirectory.listFiles();
        if (Objects.isNull(listFiles)) {
            throw new IllegalArgumentException("List of files in destination directory is null");
        }
        Stream.of(listFiles).forEach(fileInDirectory -> {
            CsvUtils<RegistrationHeaders> csvUtils = new RegistrationCsvUtils(fileInDirectory);
            Options<RegistrationHeaders> options = csvUtils.getOptions();
            if (Objects.nonNull(options)) {
                RegistrationCsvMapper mapper = new RegistrationCsvMapper(options.getHeaders());
                Persist<RegistrationCsvRecord> persist = new RegistrationPersist(registrationDBService,
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
                        vehicleDBService,
                        fileInDirectory.getName());
                CsvReader<RegistrationCsvRecord> csvReader = new CsvReader<>();
                csvReader.readCsvFile(options.getReaderOptions(), mapper, persist);
            } else {
                log.error("processExtractedFiles: Options is null");
            }
            FileUtil.deleteFiles(fileInDirectory);
        });
    }
}
