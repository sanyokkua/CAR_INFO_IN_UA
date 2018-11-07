package ua.kostenko.carinfo.consuming.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.consuming.data.persistent.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.consuming.data.persistent.services.RegistrationInformationService;

import java.io.IOException;

@Component
@Slf4j
public class RegistrationInformationQueueReceiver {
    private final RegistrationInformationService registrationInformationService;

    @Autowired
    public RegistrationInformationQueueReceiver(RegistrationInformationService registrationInformationService) {
        this.registrationInformationService = registrationInformationService;
    }

    public void receiveMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            RegistrationInformationEntity registrationInformationEntity = mapper.readValue(message, RegistrationInformationEntity.class);
            registrationInformationService.save(registrationInformationEntity);
        } catch (IOException e) {
            log.error("Problem with mapping json string to object", e);
        }

    }
}
