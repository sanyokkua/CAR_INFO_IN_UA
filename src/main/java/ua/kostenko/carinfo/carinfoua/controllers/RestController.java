package ua.kostenko.carinfo.carinfoua.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.kostenko.carinfo.carinfoua.data.InfoData;
import ua.kostenko.carinfo.carinfoua.utils.Constants;
import ua.kostenko.carinfo.carinfoua.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class RestController {

  @RequestMapping(value = "/csv", method = RequestMethod.GET)
  public ResponseEntity<String> getAllRecords() throws IOException {
    Utils utils = new Utils();
    String downloadJson = utils.downloadJson(Constants.OPEN_DATA_URL_JSON);
    Gson gson = new Gson();
    Type type = new TypeToken<Map<String, Object>>(){}.getType();
    Map<String, Object> jsonMap = gson.fromJson(downloadJson, type);
    List<Map<String, String>> resources = (List<Map<String, String>>)jsonMap.get("resources");
    List<InfoData> results = new ArrayList<>();
    resources.stream().limit(1).forEach(obj -> {
      String path = obj.get("path");
      try {
        utils.downloadZip(path);
      } catch (IOException e) {
        e.printStackTrace();
      }
      try {
        List<InfoData> infoData = utils.readCsv();
        results.addAll(infoData);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    ObjectMapper mapper = new ObjectMapper();
    return new ResponseEntity<>(mapper.writeValueAsString(results), HttpStatus.OK);
  }

}
