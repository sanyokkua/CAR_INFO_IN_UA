package ua.kostenko.carinfo.carinfoua.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.configuration.ApplicationProperties;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.services.RegistrationInformationService;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity.RegistrationInformationEntityFields.*;

@Component
@Slf4j
public class CsvRegistrationInformationImportTool {
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

    public void initDB() {
        log.info("Started initializing DB");
        String downloadJson = null;
        try {
            downloadJson = getOpenDataJson();
        } catch (IOException e) {
            log.error("Exception occurred when trying to get Info JSON", e);
        }
        List<String> downloadLinks = getDownloadLinks(downloadJson);
        log.info("Links for download csv archives:");
        downloadLinks.forEach(s -> log.info("Download link: {}", s));
        log.info("Starting downloading...");
        downloadLinks.forEach(linkUrl -> {
            log.info("Processing link: {}", linkUrl);
            String dateLabel = parseDateLabel(linkUrl);
            log.info("Created dateLabel for archive: {}", dateLabel);
            if (isNeedToUpdate(dateLabel)) {
                registrationInformationService.removeAllByDateForDataSet(dateLabel);//TODO: will be changed to updating entities instead their deletion
                List<RegistrationInformationEntity> infoData = getInfoDataListFromArchive(linkUrl, dateLabel);
                registrationInformationService.saveAll(infoData);
                infoData.clear();
                log.info("Added data from link: {}", linkUrl);
            } else {
                log.info("Skipping processing for archive {} by label {} because it already exists in DB", linkUrl, dateLabel);
            }
        });
        log.info("Finished initializing DB");
    }

    private boolean isNeedToUpdate(String dateLabel) {
        boolean isCurrentYear = LocalDate.now().getYear() == Integer.parseInt(dateLabel);
        boolean isExistsInDb = registrationInformationService.checkDatasetYearInDb(dateLabel);
        return !isExistsInDb || isCurrentYear;
    }

    private String getOpenDataJson() throws IOException {
        return downloadJson(applicationProperties.APP_DATA_SET_JSON_URL);
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
        if (matcher.find()) {
            String dateString = matcher.group();
            log.info("Found date of current data set {}", dateString);
            log.info("Trying to parse date by pattern {}", DATE_PATTERN);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            dateLabel = String.valueOf(LocalDate.parse(dateString, dateTimeFormatter).getYear());
        }
        assert dateLabel != null;
        return dateLabel;
    }

    private List<RegistrationInformationEntity> getInfoDataListFromArchive(String linkUrl, String dateLabel) {
        Path downloadZip = null;
        try {
            log.info("Trying to download archive from: {}", linkUrl);
            downloadZip = downloadZip(linkUrl, dateLabel);
            log.info("Archive downloaded from: {}, path to archive: {}", linkUrl, downloadZip.toAbsolutePath().toString());
        } catch (IOException e) {
            log.error(String.format("Error occurred with downloading archive from: %s", linkUrl), e);
        }
        assert downloadZip != null;
        List<RegistrationInformationEntity> registrationInformationEntityList = Collections.emptyList();
        try {
            log.info("Trying to parse csv file: {}", downloadZip.toAbsolutePath().toString());
            registrationInformationEntityList = readCsvFromFile(downloadZip, dateLabel);
            log.info("Csv file {} parsed", downloadZip.toAbsolutePath().toString());
        } catch (IOException e) {
            log.error(String.format("Error occurred with parsing csv file: %s", downloadZip.toAbsolutePath().toString()), e);
        }
        return registrationInformationEntityList;
    }

    private String downloadJson(String url) throws IOException {
        log.info("Downloading Info JSON from: {}", url);
        return IOUtils.toString(new URL(url), Charset.forName("UTF-8"));
    }

    private Path downloadZip(String fileUrl, String dateLabel) throws IOException {
        log.info("Downloading archive from: {}", fileUrl);
        URL url = new URL(fileUrl);
        String fileName = applicationProperties.APP_ARCHIVE_DIR + File.separator + applicationProperties.APP_ARCHIVE_NAME + "_" + dateLabel + ".zip";
        log.info("Created archive name: {}", fileName);
        Path targetPath = new File(fileName).toPath();
        log.info("Saving archive to folder: {}", targetPath.toAbsolutePath().toString());
        Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        log.info("Finished downloading archive from: " + fileUrl);
        return targetPath;
    }

    private List<RegistrationInformationEntity> readCsvFromFile(Path archiveFilePath, String dateLabel) throws IOException {
        log.info("Started reading csv from zip archive");
        log.info("Archive file is: {}", archiveFilePath.toAbsolutePath().toString());

        String fileName = applicationProperties.APP_ARCHIVE_DIR + File.separator + applicationProperties.APP_ARCHIVE_NAME + "_" + dateLabel;
        Path destination = Paths.get(fileName);

        extractZipArchive(archiveFilePath, destination);
        List<RegistrationInformationEntity> resultList = mapCsvFile(dateLabel, destination);
        deleteTempFiles(archiveFilePath, destination);

        log.info("Finished reading csv from zip archive. Number of csv records in result list: {}", resultList.size());
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

    private List<RegistrationInformationEntity> mapCsvFile(String dateLabel, Path destination) {
        log.info("Starting mapping of csv records to object");
        List<RegistrationInformationEntity> resultList = new LinkedList<>();
        try (Reader in = new FileReader(String.valueOf(Files.list(destination).findFirst().orElseGet(null)))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                RegistrationInformationEntity data = mapCsvToInfoData(dateLabel, record);
                data.setId(RegistrationInformationEntity.createId(data));
                resultList.add(data);
            }
            log.info("Finished mapping csv records");
        } catch (IOException ex) {
            log.warn("IOException happened", ex);
        }
        return resultList;
    }

    private RegistrationInformationEntity mapCsvToInfoData(String dateLabel, CSVRecord record) {
        RegistrationInformationEntity.RegistrationInformationEntityBuilder builder = new RegistrationInformationEntity.RegistrationInformationEntityBuilder();
        Map<String, String> recordDataMap = record.toMap();
        builder.setPerson(recordDataMap.getOrDefault(PERSON.getFieldName(), ""))
                .setAdministrativeObjectCode(parseOrGetNull(recordDataMap.getOrDefault(ADMINISTRATIVE_OBJECT.getFieldName(), "")))
                .setOperationCode(parseOrGetNull(recordDataMap.getOrDefault(OPERATION_CODE.getFieldName(), "")))
                .setOperationName(recordDataMap.getOrDefault(OPERATION_NAME.getFieldName(), ""))
                .setRegistrationDate(recordDataMap.getOrDefault(REGISTRATION_DATE.getFieldName(), ""))
                .setDepartmentCode(parseOrGetNull(recordDataMap.getOrDefault(DEPARTMENT_CODE.getFieldName(), "")))
                .setDepartmentName(recordDataMap.getOrDefault(DEPARTMENT_NAME.getFieldName(), ""))
                .setCarBrand(recordDataMap.getOrDefault(CAR_BRAND.getFieldName(), ""))
                .setCarModel(recordDataMap.getOrDefault(CAR_MODEL.getFieldName(), ""))
                .setCarMakeYear(parseOrGetNull(recordDataMap.getOrDefault(CAR_MAKE_YEAR.getFieldName(), "")))
                .setCarColor(recordDataMap.getOrDefault(CAR_COLOR.getFieldName(), ""))
                .setCarKind(recordDataMap.getOrDefault(CAR_KIND.getFieldName(), ""))
                .setCarBody(recordDataMap.getOrDefault(CAR_BODY.getFieldName(), ""))
                .setCarPurpose(recordDataMap.getOrDefault(CAR_PURPOSE.getFieldName(), ""))
                .setCarFuel(recordDataMap.getOrDefault(CAR_FUEL.getFieldName(), ""))
                .setCarEngineCapacity(parseOrGetNull(recordDataMap.getOrDefault(CAR_ENGINE_CAPACITY.getFieldName(), "")))
                .setCarOwnWeight(parseOrGetNull(recordDataMap.getOrDefault(CAR_OWN_WEIGHT.getFieldName(), "")))
                .setCarTotalWeight(parseOrGetNull(recordDataMap.getOrDefault(CAR_TOTAL_WEIGHT.getFieldName(), "")))
                .setCarNewRegistrationNumber(recordDataMap.getOrDefault(CAR_NEW_REGISTRATION_NUMBER.getFieldName(), ""))
                .setDataSetYear(dateLabel);
        return builder.build();
    }

    private Long parseOrGetNull(String value) {
        Long result = null;
        try {
            result = Long.valueOf(value);
        } catch (Exception ex) {
//      log.warn("Error occurred due parsing string to Long value. Value: {}", value);
        }
        return result;
    }
}
