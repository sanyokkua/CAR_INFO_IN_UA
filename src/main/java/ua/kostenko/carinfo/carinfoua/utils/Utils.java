package ua.kostenko.carinfo.carinfoua.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
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

import static ua.kostenko.carinfo.carinfoua.data.InfoDataFields.*;

@Component
public class Utils {

  public String downloadJson(String url) throws IOException {
    return IOUtils.toString(new URL(url), Charset.forName("UTF-8"));
  }

  public Path downloadZip(String fileUrl) throws IOException {
    System.out.println("Downloading archive from: " + fileUrl);
    URL url = new URL(fileUrl);
    String fileName = "archive.zip";
    Path targetPath = new File("." + File.separator + fileName).toPath();
    Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
    System.out.println("Finished downloading archive from: " + fileUrl);
    return targetPath;
  }

  public List<InfoData> readCsv(String dateLabel) throws IOException {
    System.out.println("Started reading csv from zip archive");
    String source = "./archive.zip";
    String destination = "./archive";
    try {
      ZipFile zipFile = new ZipFile(source);
      zipFile.extractAll(destination);
    } catch (ZipException e) {
      e.printStackTrace();
    }
    Reader in = new FileReader(String.valueOf(Files.list(Paths.get(destination)).findFirst().orElseGet(null)));
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
    List<InfoData> resultList = new ArrayList<>();
    for (CSVRecord record : records) {
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
      resultList.add(data);
    }
    FileUtils.forceDelete(Paths.get(source).toFile());
    FileUtils.forceDelete(Paths.get(destination).toFile());
    System.out.println("Finished reading csv from zip archive");
    return resultList;
  }
}
