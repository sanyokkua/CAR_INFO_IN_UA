package ua.kostenko.carinfo.carinfoua.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.kostenko.carinfo.carinfoua.data.InfoData;
import ua.kostenko.carinfo.carinfoua.persistent.services.CarInfoDataService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {
  private final static Logger LOGGER = LoggerFactory.getLogger(RestController.class);
  private CarInfoDataService carInfoDataService;

  @Autowired
  public RestController(CarInfoDataService carInfoDataService) {
    Preconditions.checkNotNull(carInfoDataService);
    this.carInfoDataService = carInfoDataService;
  }

  @RequestMapping(value = "/search/{number}", method = RequestMethod.GET)
  public ResponseEntity<String> searchByNumber(@PathVariable String number) throws JsonProcessingException {
    LOGGER.info("Processing request by url /search/{}", number);
    LocalDateTime before = LocalDateTime.now();
    ObjectMapper mapper = new ObjectMapper();
    List<InfoData> results = carInfoDataService.search(InfoData.InfoDataFields.CAR_NEW_REGISTRATION_NUMBER, number);
    ResponseEntity<String> responseEntity = new ResponseEntity<>(mapper.writeValueAsString(results), HttpStatus.OK);
    LOGGER.info("Processing of request by url /search/{} finished, time: {} ms", number, Duration.between(before, LocalDateTime.now()).toMillis());
    return responseEntity;
  }

  @RequestMapping(value = "/search/raw/{field}/{value}", method = RequestMethod.GET)
  public ResponseEntity<String> searchRaw(@PathVariable String field, @PathVariable String value) throws JsonProcessingException {
    LOGGER.info("Processing request by url /search/raw/{}/{}", field, value);
    LocalDateTime before = LocalDateTime.now();
    ObjectMapper mapper = new ObjectMapper();
    List<InfoData> results = carInfoDataService.search(InfoData.InfoDataFields.getInfoDataFieldByName(field), value);
    ResponseEntity<String> responseEntity = new ResponseEntity<>(mapper.writeValueAsString(results), HttpStatus.OK);
    LOGGER.info("Processing of request by url /search/raw/{}/{} finished, time: {} ms", field, value, Duration.between(before, LocalDateTime.now()).toMillis());
    return responseEntity;
  }

}
