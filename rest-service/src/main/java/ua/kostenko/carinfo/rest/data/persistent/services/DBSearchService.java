package ua.kostenko.carinfo.rest.data.persistent.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.rest.data.persistent.repositories.RegistrationInformationCrudRepository;
import ua.kostenko.carinfo.rest.data.presentation.CombinedInformation;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class DBSearchService implements SearchService {
    private final RegistrationInformationCrudRepository registrationInformationCRUDRepository;

    @Autowired
    public DBSearchService(RegistrationInformationCrudRepository registrationInformationCRUDRepository) {
        Preconditions.checkNotNull(registrationInformationCRUDRepository);
        this.registrationInformationCRUDRepository = registrationInformationCRUDRepository;
    }

    @Override
    public List<RegistrationInformationEntity> search(String value) {
        log.info("Searching RegistrationInformationEntity for field: {}, value: {}", "CarNewRegistrationNumber", value);
        List<RegistrationInformationEntity> results = registrationInformationCRUDRepository.findAllByCarNewRegistrationNumberLike(value);
        log.warn("Field {} is not supported yet, null will be returned", "CarNewRegistrationNumber");
        return results;
    }

    @Override
    public List<CombinedInformation> searchAllByRegistrationNumber(String number) {
        return null;
    }

    @Override
    public List<String> getAllBrands() {
        return null;
    }

    @Override
    public List<String> getAllModelsForBrand(String brand) {
        return null;
    }

    @Override
    public List<String> getAllColors() {
        return null;
    }

    @Override
    public List<String> getAllFuelTypes() {
        return null;
    }

    @Override
    public List<String> getAllCarKinds() {
        return null;
    }

    @Override
    public List<String> getAllRegions() {
        return null;
    }

    @Override
    public int countAllRegistrations() {
        return 0;
    }

    @Override
    public int countAllByBrand(String brand) {
        return 0;
    }

    @Override
    public int countAllByCarBrandAndModel(String brand, String model) {
        return 0;
    }

    @Override
    public int countAllByCarColor(String color) {
        return 0;
    }

    @Override
    public int countAllByFuelType(String fuelType) {
        return 0;
    }

    @Override
    public int countAllByCarKind(String carKind) {
        return 0;
    }

    @Override
    public int countAllCarsByYear(int year) {
        return 0;
    }

    @Override
    public int countAllCarsInRegion(String region) {
        return 0;
    }

}
