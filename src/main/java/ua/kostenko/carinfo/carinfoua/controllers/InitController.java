package ua.kostenko.carinfo.carinfoua.controllers;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import ua.kostenko.carinfo.carinfoua.data.InfoData;
import ua.kostenko.carinfo.carinfoua.persistent.CarInfoDataApi;
import ua.kostenko.carinfo.carinfoua.utils.Constants;
import ua.kostenko.carinfo.carinfoua.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class InitController {
  private Utils utils;
  private CarInfoDataApi carInfoDataApi;

  @Autowired
  public InitController(Utils utils, CarInfoDataApi carInfoDataApi) {
    Preconditions.checkNotNull(utils);
    Preconditions.checkNotNull(carInfoDataApi);
    this.utils = utils;
    this.carInfoDataApi = carInfoDataApi;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void initDatabase() {
    initDB();
  }

  private void initDB() {
    String downloadJson = null;
    try {
      downloadJson = getOpenDataJson();
    } catch (IOException e) {
      e.printStackTrace();
    }
    List<String> downloadLinks = getDownloadLinks(downloadJson);
    List<InfoData> results = new ArrayList<>();
    downloadLinks.forEach(s -> System.out.println("Download link: " + s));
    downloadLinks.stream().forEach(linkUrl -> {
      String dateLabel = linkUrl.substring(linkUrl.lastIndexOf('/'), linkUrl.lastIndexOf('.'));
      List<InfoData> infoData = getInfoDataListFromArchive(linkUrl, dateLabel);
      results.addAll(infoData);
      System.out.println("Added data from link: " + linkUrl);
      System.out.println("Size of results: " + results.size());
    });
    carInfoDataApi.saveAll(results);
  }

  private String getOpenDataJson() throws IOException {
    return utils.downloadJson(Constants.OPEN_DATA_URL_JSON);
  }

  private List<String> getDownloadLinks(String downloadJson) {
    Gson gson = new Gson();
    Type type = new TypeToken<Map<String, Object>>() {}.getType();
    Map<String, Object> jsonMap = gson.fromJson(downloadJson, type);
    return ((List<Map<String, String>>)jsonMap.get("resources")).stream().map(stringStringMap -> stringStringMap.get("path")).collect(Collectors.toList());
  }

  private List<InfoData> getInfoDataListFromArchive(String linkUrl, String dateLabel) {
    try {
      utils.downloadZip(linkUrl);
    } catch (IOException e) {
      e.printStackTrace();
    }
    List<InfoData> infoDataList = Collections.emptyList();
    try {
      infoDataList = utils.readCsv(dateLabel);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return infoDataList;
  }

}
