package ua.kostenko.carinfo.carinfoua.utils;

import com.google.common.base.Preconditions;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.configuration.ApplicationProperties;
import ua.kostenko.carinfo.carinfoua.data.InfoData;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static ua.kostenko.carinfo.carinfoua.data.InfoDataFields.*;

@Component
public class Utils {
  private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
  private ApplicationProperties applicationProperties;

  @Autowired
  public Utils(ApplicationProperties applicationProperties) {
    Preconditions.checkNotNull(applicationProperties);
    this.applicationProperties = applicationProperties;
  }

  public String downloadJson(String url) throws IOException {
    LOGGER.info("Downloading Info JSON from: {}", url);
    return IOUtils.toString(new URL(url), Charset.forName("UTF-8"));
  }

  public Path downloadZip(String fileUrl, String dateLabel) throws IOException {
    LOGGER.info("Downloading archive from: {}", fileUrl);
    URL url = new URL(fileUrl);
    String fileName = applicationProperties.APP_ARCHIVE_DIR + File.separator + applicationProperties.APP_ARCHIVE_NAME + "_" + dateLabel + ".zip";
    LOGGER.info("Created archive name: {}", fileName);
    Path targetPath = new File(fileName).toPath();
    LOGGER.info("Saving archive to folder: {}", targetPath.toAbsolutePath().toString());
    Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
    LOGGER.info("Finished downloading archive from: " + fileUrl);
    return targetPath;
  }

  public List<InfoData> readCsvFromFile(Path archiveFilePath, String dateLabel) throws IOException {
    LOGGER.info("Started reading csv from zip archive");
    LOGGER.info("Archive file is: {}", archiveFilePath.toAbsolutePath().toString());

    String fileName = applicationProperties.APP_ARCHIVE_DIR + File.separator + applicationProperties.APP_ARCHIVE_NAME + "_" + dateLabel;
    Path destination = Paths.get(fileName);

    extractZipArchive(archiveFilePath, destination);
    List<InfoData> resultList = mapCsvFile(dateLabel, destination);
    deleteTempFiles(archiveFilePath, destination);

    LOGGER.info("Finished reading csv from zip archive. Number of csv records in result list: {}", resultList.size());
    return resultList;
  }

  private void deleteTempFiles(Path archiveFilePath, Path destination) throws IOException {
    FileUtils.forceDelete(archiveFilePath.toFile());
    FileUtils.forceDelete(destination.toFile());
  }

  private void extractZipArchive(Path archiveFilePath, Path destination) {
    try {
      LOGGER.info("Extracting zip archive...");
      ZipFile zipFile = new ZipFile(archiveFilePath.toFile());
      zipFile.extractAll(destination.toAbsolutePath().toString());
      LOGGER.info("All files was extracted to: {}", destination.toAbsolutePath().toString());
      LOGGER.info("Extracted files in folder {}", destination.toAbsolutePath().toString());
      Stream.of(Objects.requireNonNull(destination.toFile().list())).forEach(LOGGER::info);
    } catch (ZipException e) {
      LOGGER.error("Error occurred with extracting files from zip archive", e);
    }
  }

  private List<InfoData> mapCsvFile(String dateLabel, Path destination) throws IOException {
    LOGGER.info("Starting mapping of csv records to object");
    Reader in = new FileReader(String.valueOf(Files.list(destination).findFirst().orElseGet(null)));
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
    List<InfoData> resultList = new ArrayList<>();
    for (CSVRecord record : records) {
      InfoData data = mapCsvToInfoData(dateLabel, record);
      resultList.add(data);
    }
    LOGGER.info("Finished mapping csv records");
    return resultList;
  }

  private InfoData mapCsvToInfoData(String dateLabel, CSVRecord record) {
    InfoData data = new InfoData();
    data.setPerson(record.get(PERSON.getFieldName()));
    data.setReg_addr_koatuu(record.get(REG_ADDR_KOATUU.getFieldName()));
    data.setOperationCode(record.get(OPERATION_CODE.getFieldName()));
    data.setOperationName(record.get(OPERATION_NAME.getFieldName()));
    data.setRegistrationDate(record.get(REGISTRATION_DATE.getFieldName()));
    data.setDepartmentCode(record.get(DEPARTMENT_CODE.getFieldName()));
    data.setDepartmentName(record.get(DEPARTMENT_NAME.getFieldName()));
    data.setCarBrand(record.get(CAR_BRAND.getFieldName()));
    data.setCarModel(record.get(CAR_MODEL.getFieldName()));
    data.setCarMakeYear(record.get(CAR_MAKE_YEAR.getFieldName()));
    data.setCarColor(record.get(CAR_COLOR.getFieldName()));
    data.setCarKind(record.get(CAR_KIND.getFieldName()));
    data.setCarBody(record.get(CAR_BODY.getFieldName()));
    data.setCarPurpose(record.get(CAR_PURPOSE.getFieldName()));
    data.setCarFuel(record.get(CAR_FUEL.getFieldName()));
    data.setCarEngineCapacity(record.get(CAR_ENGINE_CAPACITY.getFieldName()));
    data.setCarOwnWeight(record.get(CAR_OWN_WEIGHT.getFieldName()));
    data.setCarTotalWeight(record.get(CAR_TOTAL_WEIGHT.getFieldName()));
    data.setCarNewRegistrationNumber(record.get(CAR_NEW_REGISTRATION_NUMBER.getFieldName()));
    data.setDataSetYear(dateLabel);
    return data;
  }
}
