package ua.kostenko.carinfo.carinfoua.data.persistent.services;

import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity;

import java.util.List;

public interface RegistrationInformationService {

  List<RegistrationInformationEntity> search(String value);

}
