package ua.kostenko.carinfo.rest.data.persistent.services;

import ua.kostenko.carinfo.common.entities.RegistrationInformationEntity;

import java.util.List;

public interface RegistrationInformationService {

    List<RegistrationInformationEntity> search(String value);

}
