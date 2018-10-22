package ua.kostenko.carinfo.carinfoua.data.persistent.services;

import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.carinfoua.utils.csv.fields.RegistrationInformationCSV;

import java.util.Collection;
import java.util.List;

public interface RegistrationInformationService {

    void saveAll(Collection<RegistrationInformationEntity> registrationInformationEntityList);

    void removeAllByDateForDataSet(String date);

    List<RegistrationInformationEntity> search(RegistrationInformationCSV field, String value);

    boolean checkDatasetYearInDb(String date);
}
