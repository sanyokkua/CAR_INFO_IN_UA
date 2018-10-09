package ua.kostenko.carinfo.carinfoua.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.services.RegistrationInformationService;
import ua.kostenko.carinfo.carinfoua.data.presentation.CombinedInformation;
import ua.kostenko.carinfo.carinfoua.utils.ResponseCreatorHelper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@Slf4j
public class RestController {
    private final RegistrationInformationService registrationInformationService;
    private final ResponseCreatorHelper responseCreatorHelper;

    @Autowired
    public RestController(RegistrationInformationService registrationInformationService, ResponseCreatorHelper responseCreatorHelper) {
        Preconditions.checkNotNull(registrationInformationService);
        Preconditions.checkNotNull(responseCreatorHelper);
        this.registrationInformationService = registrationInformationService;
        this.responseCreatorHelper = responseCreatorHelper;
    }

    @RequestMapping(value = "/search/{number}", method = RequestMethod.GET)
    public ResponseEntity<String> searchByNumber(@PathVariable String number) throws JsonProcessingException {
        number = number.toUpperCase();
        log.info("Processing request by url /search/{}", number);
        LocalDateTime before = LocalDateTime.now();
        ObjectMapper mapper = new ObjectMapper();
        List<RegistrationInformationEntity> results = registrationInformationService.search(RegistrationInformationEntity.RegistrationInformationEntityFields.CAR_NEW_REGISTRATION_NUMBER, number.toUpperCase());
        List<CombinedInformation> combinedInformation = responseCreatorHelper.getCombinedInformation(results);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(mapper.writeValueAsString(combinedInformation), HttpStatus.OK);
        log.info("Processing of request by url /search/{} finished, time: {} ms", number, Duration.between(before, LocalDateTime.now()).toMillis());
        return responseEntity;
    }

    @RequestMapping(value = "/search/raw/{field}/{value}", method = RequestMethod.GET)
    public ResponseEntity<String> searchRaw(@PathVariable String field, @PathVariable String value) throws JsonProcessingException {
        field = field.toUpperCase();
        value = value.toUpperCase();
        log.info("Processing request by url /search/raw/{}/{}", field, value);
        LocalDateTime before = LocalDateTime.now();
        ObjectMapper mapper = new ObjectMapper();
        List<RegistrationInformationEntity> results = registrationInformationService.search(RegistrationInformationEntity.RegistrationInformationEntityFields.getInfoDataFieldByName(field), value);
        List<CombinedInformation> combinedInformation = responseCreatorHelper.getCombinedInformation(results);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(mapper.writeValueAsString(combinedInformation), HttpStatus.OK);
        log.info("Processing of request by url /search/raw/{}/{} finished, time: {} ms", field, value, Duration.between(before, LocalDateTime.now()).toMillis());
        return responseEntity;
    }

}
