package ua.kostenko.carinfo.consuming.data.persistent.services;

import ua.kostenko.carinfo.consuming.data.persistent.entities.RegistrationInformationEntity;

import java.util.Collection;

public interface RegistrationInformationService {

    void saveAll(Collection<RegistrationInformationEntity> registrationInformationEntityList);

    void save(RegistrationInformationEntity registrationInformationEntity);

    boolean isDataWithLabelAndDateExists(String dataSetLabel, String date);

}
