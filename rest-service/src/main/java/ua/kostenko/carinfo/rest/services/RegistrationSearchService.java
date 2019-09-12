package ua.kostenko.carinfo.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Registration;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.rest.services.common.CommonSearchService;

@Service
public class RegistrationSearchService extends CommonSearchService<Registration, String> {

    @Autowired
    public RegistrationSearchService(final DBService<Registration> service) {
        super(service);
    }

    @Override
    public String getFindForFieldParam() {
        return Registration.REGISTRATION_NUMBER;
    }
}
