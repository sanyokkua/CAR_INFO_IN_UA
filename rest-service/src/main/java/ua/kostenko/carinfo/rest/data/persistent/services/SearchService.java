package ua.kostenko.carinfo.rest.data.persistent.services;

import ua.kostenko.carinfo.rest.data.presentation.CombinedInformation;

import java.util.List;

public interface SearchService {

    List<CombinedInformation> searchAllByRegistrationNumber(String number);

    List<String> getAllBrands();

    List<String> getAllModelsForBrand(String brand);

    List<String> getAllColors();

    List<String> getAllFuelTypes();

    List<String> getAllCarKinds();

    List<String> getAllRegions();

    long countAllRegistrations();

    long countAllByBrand(String brand);

    long countAllByCarBrandAndModel(String brand, String model);

    long countAllByCarColor(String color);

    long countAllByFuelType(String fuelType);

    long countAllByCarKind(String carKind);

    long countAllCarsByYear(int year);

    long countAllCarsInRegion(String region);

}
