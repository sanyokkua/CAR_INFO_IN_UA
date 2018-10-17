package ua.kostenko.carinfo.carinfoua.utils.csv.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.configuration.ApplicationProperties;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.services.RegistrationInformationService;
import ua.kostenko.carinfo.carinfoua.utils.Initializer;
import ua.kostenko.carinfo.carinfoua.utils.csv.fields.RegistrationInformationCSV;
import ua.kostenko.carinfo.carinfoua.utils.io.CSVReader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class CsvRegistrationInformationImportTool implements Initializer {
    private static final String DATE_PATTERN = "ddMMyyyy";
    private final RegistrationInformationService registrationInformationService;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public CsvRegistrationInformationImportTool(RegistrationInformationService registrationInformationService, ApplicationProperties applicationProperties) {
        Preconditions.checkNotNull(registrationInformationService);
        Preconditions.checkNotNull(applicationProperties);
        this.registrationInformationService = registrationInformationService;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void init() {
        LocalTime before = LocalTime.now();
        log.info("Started initializing DB: {}", before.toString());
        String downloadJson = getOpenDataJson();
        log.info("Downloaded json with next content: \n{}", downloadJson);

        log.info("Links for download csv archives:");
        List<String> downloadLinks = getDownloadLinks(downloadJson);
        downloadLinks.forEach(s -> log.info("Download link: {}", s));

        log.info("Starting downloading...");
        downloadLinks.forEach(linkUrl -> {
            log.info("Processing link: {}", linkUrl);
            String dateLabel = parseDateLabel(linkUrl);
            log.info("Created dateLabel for archive: {}", dateLabel);
            if (isNeedToUpdate(dateLabel)) {
                log.info("Data for datelabel {} is need to update", dateLabel);
//                registrationInformationService.removeAllByDateForDataSet(dateLabel);//TODO: will be changed to updating entities instead their deletion
                Collection<RegistrationInformationEntity> registrationInformationFromArchive = getRegistrationInformationFromArchive(linkUrl, dateLabel);
                registrationInformationService.saveAll(registrationInformationFromArchive);
                registrationInformationFromArchive.clear();
                log.info("Added data from link: {}", linkUrl);
            } else {
                log.info("Skipping processing for archive {} by label {} because it already exists in DB", linkUrl, dateLabel);
            }
        });
        LocalTime after = LocalTime.now();
        log.info("Finished initializing DB: finish time: {}, duration: {} minutes", after.toString(), Duration.between(before, after).toMinutes());
    }

    private String getOpenDataJson() {
        String downloadJson = null;
        try {
            log.info("Downloading Info JSON from: {}", applicationProperties.APP_DATA_SET_JSON_URL);
            downloadJson = IOUtils.toString(new URL(applicationProperties.APP_DATA_SET_JSON_URL), Charset.forName("UTF-8"));
        } catch (IOException e) {
            log.error("Exception occurred when trying to get Info JSON", e);
        }
        return downloadJson;
    }

    private List<String> getDownloadLinks(String downloadJson) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> jsonMap = gson.fromJson(downloadJson, type);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
            log.info("InfoJson content:\n{}", json);
        } catch (JsonProcessingException e) {
            log.warn("Problem with writing map as Json for logging", e);
        }
        return ((List<Map<String, String>>) jsonMap.get("resources")).stream().map(stringStringMap -> stringStringMap.get("path")).collect(Collectors.toList());
    }

    private String parseDateLabel(String linkUrl) {
        String lastPart = linkUrl.substring(linkUrl.lastIndexOf('/') + 1, linkUrl.lastIndexOf('.'));
        Pattern pattern = Pattern.compile("(\\d\\d\\d\\d\\d\\d\\d\\d)");
        Matcher matcher = pattern.matcher(lastPart);
        String dateLabel = null;
        if (matcher.find()) {//TODO:
            String dateString = matcher.group();
            log.info("Found date of current data set {}", dateString);
            log.info("Trying to parse date by pattern {}", DATE_PATTERN);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            dateLabel = String.valueOf(LocalDate.parse(dateString, dateTimeFormatter).getYear());
        }
        assert dateLabel != null;
        return dateLabel;
    }

    private boolean isNeedToUpdate(String dateLabel) {
        boolean isCurrentYear = LocalDate.now().getYear() == Integer.parseInt(dateLabel);
        boolean isExistsInDb = registrationInformationService.checkDatasetYearInDb(dateLabel);
        return !isExistsInDb || isCurrentYear;
    }

    private Collection<RegistrationInformationEntity> getRegistrationInformationFromArchive(String linkUrl, String dateLabel) {
        Path downloadedZip = downloadZip(linkUrl, dateLabel);
        return readCsvFromArchiveFile(downloadedZip, dateLabel);
    }

    private Path downloadZip(String fileUrl, String dateLabel) {
        Path targetPath = null;
        try {
            log.info("Downloading archive from: {}", fileUrl);
            URL url = new URL(fileUrl);
            String fileName = applicationProperties.APP_ARCHIVE_DIR + File.separator + applicationProperties.APP_ARCHIVE_NAME + "_" + dateLabel + ".zip";
            log.info("Created archive name: {}", fileName);
            targetPath = new File(fileName).toPath();
            log.info("Saving archive to folder: {}", targetPath.toAbsolutePath().toString());
            Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Finished downloading archive from: " + fileUrl);
        } catch (IOException e) {
            log.error(String.format("Error occurred with downloading archive from: %s", fileUrl), e);
        }
        Preconditions.checkNotNull(targetPath, String.format("Error occurred in downloading archive from: %s, path to archive is null", fileUrl));
        log.info("Archive downloaded from: {}, path to archive: {}", fileUrl, targetPath.toAbsolutePath().toString());
        return targetPath;
    }

    private Collection<RegistrationInformationEntity> readCsvFromArchiveFile(Path archiveFilePath, String dateLabel) {
        Collection<RegistrationInformationEntity> resultList = null;
        try {
            log.info("Trying to parse csv file: {}", archiveFilePath.toAbsolutePath().toString());
            log.info("Started reading csv from zip archive");
            log.info("Archive file is: {}", archiveFilePath.toAbsolutePath().toString());
            String fileName = applicationProperties.APP_ARCHIVE_DIR + File.separator + applicationProperties.APP_ARCHIVE_NAME + "_" + dateLabel;
            Path destination = Paths.get(fileName);
            extractZipArchive(archiveFilePath, destination);
            List<Path> filesInArchive = Files.list(destination).collect(Collectors.toList());
            resultList = mapCsvFiles(dateLabel, filesInArchive);
            deleteTempFiles(archiveFilePath, destination);
            log.info("Finished reading csv from zip archive. Number of csv records in result list: {}", resultList.size());
            log.info("Csv file {} parsed", archiveFilePath.toAbsolutePath().toString());
        } catch (IOException e) {
            log.error(String.format("Error occurred with parsing csv file: %s", archiveFilePath.toAbsolutePath().toString()), e);
        }
        return resultList;
    }

    private void deleteTempFiles(Path... archiveFilePaths) throws IOException {
        for (Path path : archiveFilePaths) {
            String pathForDeletion = path.toAbsolutePath().toString();
            log.info("Deleting: {}", pathForDeletion);
            FileUtils.forceDelete(path.toFile());
            log.info("{} deleted successfully", pathForDeletion);
        }
    }

    private void extractZipArchive(Path archiveFilePath, Path destination) {
        try {
            log.info("Extracting zip archive...");
            ZipFile zipFile = new ZipFile(archiveFilePath.toFile());
            zipFile.extractAll(destination.toAbsolutePath().toString());
            log.info("All files was extracted to: {}", destination.toAbsolutePath().toString());
            log.info("Extracted files in folder {}", destination.toAbsolutePath().toString());
            Stream.of(Objects.requireNonNull(destination.toFile().list())).forEach(log::info);
        } catch (ZipException e) {
            log.error("Error occurred with extracting files from zip archive", e);
        }
    }

    private Collection<RegistrationInformationEntity> mapCsvFiles(String dateLabel, List<Path> filesInArchive) {
        LocalTime before = LocalTime.now();
        log.info("Starting mapping of csv records to objects, time: {}", before.toString());
        Map<String, RegistrationInformationEntity> resultMap = new HashMap<>();
        long count = Long.valueOf(applicationProperties.APP_LOG_MAPPER_BATCH_SIZE);
        filesInArchive.forEach(destination -> {
            AtomicLong counter = new AtomicLong(0);
            CSVReader.mapCsvFile(destination, getDelimiter(destination), record -> {
                RegistrationInformationEntity data = RegistrationInformationCSV.mapRecord(record);
                if (data != null) {
                    data.setDataSetYear(dateLabel);
                    if (resultMap.containsKey(data.getId())) {
                        log.debug("Result already has object with this id: {}", data.getId());
                        log.debug("First object in collection: {}", resultMap.get(data.getId()).toString());
                        log.debug("Object which will be added: {}", data.toString());
                    }
                    resultMap.put(data.getId(), data);
                } else {
                    log.info("Returned null from mapping function");
                }
                if (counter.incrementAndGet() % count == 0) {
                    log.info("Mapped: {}", resultMap.size());
                }
            });
        });
        Duration duration = Duration.between(before, LocalTime.now());
        log.info("Finished mapping all csv files from all files in zip. Time spent: {} ms, {} min", duration.toMillis(), duration.toMinutes());
        return resultMap.values();
    }

    private char getDelimiter(Path destination) {
        char result = 0;
        try {
            List<String> strings = FileUtils.readLines(destination.toFile(), StandardCharsets.UTF_8);
            if (strings.size() > 0) {
                String firstString = strings.get(0);
                result = firstString.replaceFirst("person", "").charAt(0);
            }
        } catch (Exception e) {
            log.error("Error with reading file", e);
            result = ',';
        }
        return result;
    }
}
