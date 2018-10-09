package ua.kostenko.carinfo.carinfoua.data.persistent.services;

import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity.RegistrationInformationEntityFields;

import java.util.List;

public interface RegistrationInformationService {

    void saveAll(List<RegistrationInformationEntity> registrationInformationEntityList);

    void removeAllByDateForDataSet(String date);

    List<RegistrationInformationEntity> search(RegistrationInformationEntityFields field, String value);

    boolean checkDatasetYearInDb(String date);
}
