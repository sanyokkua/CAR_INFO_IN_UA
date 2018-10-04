package ua.kostenko.carinfo.carinfoua.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.configuration.ApplicationProperties;
import ua.kostenko.carinfo.carinfoua.data.InfoData;
import ua.kostenko.carinfo.carinfoua.persistent.services.CarInfoDataService;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CSVCarInfoImportTool {
  private static final String DATE_PATTERN = "ddMMyyyy";
  private static final Logger LOGGER = LoggerFactory.getLogger(CSVCarInfoImportTool.class);
  private Utils utils;
  private CarInfoDataService carInfoDataService;
  private ApplicationProperties applicationProperties;

  @Autowired
  public CSVCarInfoImportTool(Utils utils, CarInfoDataService carInfoDataService, ApplicationProperties applicationProperties) {
    Preconditions.checkNotNull(utils);
    Preconditions.checkNotNull(carInfoDataService);
    Preconditions.checkNotNull(applicationProperties);
    this.utils = utils;
    this.carInfoDataService = carInfoDataService;
    this.applicationProperties = applicationProperties;
  }

  public void initDB() {
    LOGGER.info("Started initializing DB");
    String downloadJson = null;
    try {
      downloadJson = getOpenDataJson();
    } catch (IOException e) {
      LOGGER.error("Exception occurred when trying to get Info JSON", e);
    }
    List<String> downloadLinks = getDownloadLinks(downloadJson);
    LOGGER.info("Links for download csv archives:");
    downloadLinks.forEach(s -> LOGGER.info("Download link: {}", s));
    LOGGER.info("Starting downloading...");
    downloadLinks.forEach(linkUrl -> {
      LOGGER.info("Processing link: {}", linkUrl);
      String dateLabel = parseDateLabel(linkUrl);
      LOGGER.info("Created dateLabel for archive: {}", dateLabel);
      if (isNeedToUpdate(dateLabel)) {
        carInfoDataService.removeAllByDateForDataSet(dateLabel);//TODO: will be changed to updating entities instead their deletion
        List<InfoData> infoData = getInfoDataListFromArchive(linkUrl, dateLabel);
        carInfoDataService.saveAll(infoData);
        LOGGER.info("Added data from link: {}", linkUrl);
      } else {
        LOGGER.info("Skipping processing for archive {} by label {} because it already exists in DB", linkUrl, dateLabel);
      }
    });
    LOGGER.info("Finished initializing DB");
  }

  private boolean isNeedToUpdate(String dateLabel) {
    boolean isCurrentYear = LocalDate.now().getYear() == Integer.parseInt(dateLabel);
    boolean isExistsInDb = carInfoDataService.checkYearInDb(dateLabel);
    return !isExistsInDb || isCurrentYear;
  }

  private String getOpenDataJson() throws IOException {
    return utils.downloadJson(applicationProperties.APP_DATA_SET_JSON_URL);
  }

  private List<String> getDownloadLinks(String downloadJson) {
    Gson gson = new Gson();
    Type type = new TypeToken<Map<String, Object>>() {}.getType();
    Map<String, Object> jsonMap = gson.fromJson(downloadJson, type);
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
      LOGGER.info("InfoJson content:\n{}", json);
    } catch (JsonProcessingException e) {
      LOGGER.warn("Problem with writing map as Json for logging", e);
    }
    return ((List<Map<String, String>>)jsonMap.get("resources")).stream().map(stringStringMap -> stringStringMap.get("path")).collect(Collectors.toList());
  }

  private String parseDateLabel(String linkUrl) {
    String lastPart = linkUrl.substring(linkUrl.lastIndexOf('/') + 1, linkUrl.lastIndexOf('.'));
    Pattern pattern = Pattern.compile("(\\d\\d\\d\\d\\d\\d\\d\\d)");
    Matcher matcher = pattern.matcher(lastPart);
    String dateLabel = null;
    if (matcher.find()) {
      String dateString = matcher.group();
      LOGGER.info("Found date of current data set {}", dateString);
      LOGGER.info("Trying to parse date by pattern {}", DATE_PATTERN);
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
      dateLabel = String.valueOf(LocalDate.parse(dateString, dateTimeFormatter).getYear());
    }
    assert dateLabel != null;
    return dateLabel;
  }

  private List<InfoData> getInfoDataListFromArchive(String linkUrl, String dateLabel) {
    Path downloadZip = null;
    try {
      LOGGER.info("Trying to download archive from: {}", linkUrl);
      downloadZip = utils.downloadZip(linkUrl, dateLabel);
      LOGGER.info("Archive downloaded from: {}, path to archive: {}", linkUrl, downloadZip.toAbsolutePath().toString());
    } catch (IOException e) {
      LOGGER.error(String.format("Error occurred with downloading archive from: %s", linkUrl), e);
    }
    assert downloadZip != null;
    List<InfoData> infoDataList = Collections.emptyList();
    try {
      LOGGER.info("Trying to parse csv file: {}", downloadZip.toAbsolutePath().toString());
      infoDataList = utils.readCsvFromFile(downloadZip, dateLabel);
      LOGGER.info("Csv file {} parsed", downloadZip.toAbsolutePath().toString());
    } catch (IOException e) {
      LOGGER.error(String.format("Error occurred with parsing csv file: %s", downloadZip.toAbsolutePath().toString()), e);
    }
    return infoDataList;
  }
}
