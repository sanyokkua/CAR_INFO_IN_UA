package ua.kostenko.carinfo.carinfoua.utils;

import com.google.common.base.Preconditions;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.configuration.ApplicationProperties;
import ua.kostenko.carinfo.carinfoua.data.KOATUU;
import ua.kostenko.carinfo.carinfoua.persistent.repositories.KOATUUCrudRepository;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ua.kostenko.carinfo.carinfoua.data.KOATUU.KOATUUFields.*;

@Component
public class CSVKOATUUImportTool {
  private final static Logger LOGGER = LoggerFactory.getLogger(CSVKOATUUImportTool.class);
  private KOATUUCrudRepository koatuuCrudRepository;
  private ApplicationProperties applicationProperties;

  @Autowired
  public CSVKOATUUImportTool(KOATUUCrudRepository koatuuCrudRepository, ApplicationProperties applicationProperties) {
    Preconditions.checkNotNull(koatuuCrudRepository);
    Preconditions.checkNotNull(applicationProperties);
    this.koatuuCrudRepository = koatuuCrudRepository;
    this.applicationProperties = applicationProperties;
  }

  public void initDB() {
    Path koatuuFilePath = null;
    try {
      koatuuFilePath = Paths.get(new ClassPathResource(applicationProperties.APP_KOATUU_FILE_PATH).getFile().getAbsolutePath());
    } catch (IOException e) {
      LOGGER.error("Error with opening file", e);
    }
    LOGGER.info("Koatuu file path: {}", koatuuFilePath.toAbsolutePath().toString());
    List<KOATUU> koatuuList = Collections.emptyList();
    try {
      koatuuList = mapCsvFile(koatuuFilePath);
    } catch (IOException e) {
      LOGGER.error("Error with reading file with koatuu information", e);
    }
    koatuuCrudRepository.saveAll(koatuuList);
  }

  private List<KOATUU> mapCsvFile(Path destination) throws IOException {
    LOGGER.info("Starting mapping of csv KOATUU records to object");
    Reader in = new FileReader(destination.toFile());
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
    List<KOATUU> resultList = new ArrayList<>();
    for (CSVRecord record : records) {
      KOATUU data = mapCsvToKOATUU(record);
      resultList.add(data);
    }
    LOGGER.info("Finished mapping csv KOATUU records");
    return resultList;
  }

  private KOATUU mapCsvToKOATUU(CSVRecord record) {
    long id = Long.valueOf(record.get(ID_NUMBER.getFieldName()));
    String type = record.get(TYPE_NAME.getFieldName());
    String name = record.get(NAME.getFieldName());
    return new KOATUU(id, type, name);
  }
}
