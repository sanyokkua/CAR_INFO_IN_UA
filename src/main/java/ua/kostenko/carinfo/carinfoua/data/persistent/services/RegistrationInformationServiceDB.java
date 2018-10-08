package ua.kostenko.carinfo.carinfoua.data.persistent.services;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.AdministrativeObjectsCrudRepository;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.RegistrationInformationCrudRepository;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.ServiceCenterCrudRepository;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
public class RegistrationInformationServiceDB implements RegistrationInformationService {
    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationInformationServiceDB.class);
    private final RegistrationInformationCrudRepository registrationInformationCRUDRepository;
    private final AdministrativeObjectsCrudRepository administrativeObjectsCrudRepository;
    private final ServiceCenterCrudRepository serviceCenterCrudRepository;

    @Autowired
    public RegistrationInformationServiceDB(RegistrationInformationCrudRepository registrationInformationCRUDRepository, AdministrativeObjectsCrudRepository administrativeObjectsCrudRepository,
                                            ServiceCenterCrudRepository serviceCenterCrudRepository) {
        Preconditions.checkNotNull(registrationInformationCRUDRepository);
        Preconditions.checkNotNull(administrativeObjectsCrudRepository);
        Preconditions.checkNotNull(serviceCenterCrudRepository);
        this.registrationInformationCRUDRepository = registrationInformationCRUDRepository;
        this.administrativeObjectsCrudRepository = administrativeObjectsCrudRepository;
        this.serviceCenterCrudRepository = serviceCenterCrudRepository;
    }

    @Override
    public void saveAll(List<RegistrationInformationEntity> registrationInformationEntityList) {
        LOGGER.info("Saving list of RegistrationInformationEntity Objects, size: {}", registrationInformationEntityList.size());
        LocalTime before = LocalTime.now();
        registrationInformationCRUDRepository.saveAll(registrationInformationEntityList);
        Duration duration = Duration.between(before, LocalTime.now());
        LOGGER.info("Saved list of RegistrationInformationEntity Objects, size: {}", registrationInformationEntityList.size());
        LOGGER.info("Time spent for saving: {} ms, {} sec, {} min", duration.toMillis(), duration.getSeconds(), duration.toMinutes());
    }

    @Override
    public void removeAllByDateForDataSet(String date) {
        LOGGER.info("Removing all data for date: {}", date);
        LocalTime before = LocalTime.now();
        registrationInformationCRUDRepository.deleteAllByDataSetYear(date);
        Duration duration = Duration.between(before, LocalTime.now());
        LOGGER.info("Removed all data for date: {}", date);
        LOGGER.info("Time spent for removing: {} ms, {} sec, {} min", duration.toMillis(), duration.getSeconds(), duration.toMinutes());
    }

    @Override
    public List<RegistrationInformationEntity> search(RegistrationInformationEntity.InfoDataFields field, String value) {
        LOGGER.info("Searching RegistrationInformationEntity for field: {}, value: {}", field.name(), value);
        List<RegistrationInformationEntity> results = Collections.EMPTY_LIST;
        switch (field) {
            case CAR_NEW_REGISTRATION_NUMBER:
                results = registrationInformationCRUDRepository.findAllByCarNewRegistrationNumberLike(value);
                break;
            case CAR_MODEL:
                results = registrationInformationCRUDRepository.findAllByCarModelLike(value);
                break;
            case CAR_BRAND:
                results = registrationInformationCRUDRepository.findAllByCarBrandLike(value);
                break;
            default:
                LOGGER.warn("Field {} is not supported yet, null will be returned", field.name());
                break;
        }
        return results;
    }

    @Override
    public boolean checkDatasetYearInDb(String date) {
        LOGGER.info("Checking existing records for date: {}", date);
        Preconditions.checkNotNull(date);
        RegistrationInformationEntity firstByDataSetYear = registrationInformationCRUDRepository.findFirstByDataSetYear(date);
        return firstByDataSetYear != null && date.equalsIgnoreCase(firstByDataSetYear.getDataSetYear());
    }
}
