package ua.kostenko.carinfo.rest.data.persistent.services;

import ua.kostenko.carinfo.common.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.rest.data.presentation.CombinedInformation;

import java.util.List;

public interface SearchService {

    List<RegistrationInformationEntity> search(String value);

    ///NEW API

    List<CombinedInformation> searchAllByRegistrationNumber(String number);

    List<String> getAllBrands();

    List<String> getAllModelsForBrand(String brand);

    List<String> getAllColors();

    List<String> getAllFuelTypes();

    List<String> getAllCarKinds();

    List<String> getAllRegions();

    int countAllRegistrations();

    int countAllByBrand(String brand);

    int countAllByCarBrandAndModel(String brand, String model);

    int countAllByCarColor(String color);

    int countAllByFuelType(String fuelType);

    int countAllByCarKind(String carKind);

    int countAllCarsByYear(int year);

    int countAllCarsInRegion(String region);

}
