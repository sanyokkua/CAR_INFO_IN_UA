package ua.kostenko.carinfo.carinfoua.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.kostenko.carinfo.carinfoua.data.InfoData;
import ua.kostenko.carinfo.carinfoua.data.InfoDataFields;
import ua.kostenko.carinfo.carinfoua.persistent.CarInfoDataApi;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
public class RestController {
  private CarInfoDataApi carInfoDataApi;

  @Autowired
  public RestController(CarInfoDataApi carInfoDataApi) {
    Preconditions.checkNotNull(carInfoDataApi);
    this.carInfoDataApi = carInfoDataApi;
  }

  @RequestMapping(value = "/csv", method = RequestMethod.GET)
  public ResponseEntity<String> getAllRecords() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<InfoData> results = carInfoDataApi.search(InfoDataFields.CAR_MODEL, "Lancer");
    return new ResponseEntity<>(mapper.writeValueAsString(results.stream().limit(500).collect(Collectors.toList())), HttpStatus.OK);
  }

  @RequestMapping(value = "/search/{number}", method = RequestMethod.GET)
  public ResponseEntity<String> searchByNumber(@PathVariable String number) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    List<InfoData> results = carInfoDataApi.search(InfoDataFields.CAR_NEW_REGISTRATION_NUMBER, number);
    return new ResponseEntity<>(mapper.writeValueAsString(results), HttpStatus.OK);
  }

}
