package ua.kostenko.carinfo.producing.data.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.data.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.producing.data.repositories.RegistrationInformationCrudRepository;

@Service
@Slf4j
public class RegistrationInformationService {
    private final RegistrationInformationCrudRepository registrationInformationCRUDRepository;

    @Autowired
    public RegistrationInformationService(RegistrationInformationCrudRepository registrationInformationCRUDRepository) {
        Preconditions.checkNotNull(registrationInformationCRUDRepository);
        this.registrationInformationCRUDRepository = registrationInformationCRUDRepository;
    }

    public boolean isDataWithLabelAndDateExists(String dataSetLabel, String date) {
        log.info("Checking existing records for date: {} and label {}", date, dataSetLabel);
        Preconditions.checkNotNull(date);
        Preconditions.checkNotNull(dataSetLabel);
        RegistrationInformationEntity firstByDataSetYear = registrationInformationCRUDRepository.findFirstByDataSetYear(date);
        return firstByDataSetYear != null && date.equalsIgnoreCase(firstByDataSetYear.getDataSetYear()) && dataSetLabel.equalsIgnoreCase(firstByDataSetYear.getDataSetLabel());
    }

}
