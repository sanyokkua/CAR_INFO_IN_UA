package ua.kostenko.carinfo.rest.services;

import org.springframework.data.domain.Page;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.*;

import javax.annotation.Nonnull;

public interface QueryService {
    Page<AdministrativeObject> getAllAdminObjects();
    Page<BodyType> getAllBodyTypes();
    Page<Brand> getAllBrands();
    Page<Color> getAllColors();
    Page<Department> getAllDepartments();
    Page<FuelType> getAllFuelTypes();
    Page<Kind> getAllKinds();
    Page<Model> getAllModels();
    Page<Operation> getAllOperations();
    Page<Purpose> getAllPurposes();
    Page<Registration> getAllRegistrations();
    Page<Vehicle> getAllVehicles();

    Page<Vehicle> findVehicleWithBrand(@Nonnull String brandName);
    Page<Vehicle> findVehicleWithModel(@Nonnull String modelName);

    Page<Registration> findRegistrationsByNumber(@Nonnull String registrationNumber);
    Page<Registration> findRegistrationByParams(@Nonnull ParamsHolderBuilder params);

    int countAllAdminObjects();
    int countAllBodyTypes();
    int countAllBrands();
    int countAllColors();
    int countAllDepartments();
    int countAllFuelTypes();
    int countAllKinds();
    int countAllOperations();
    int countAllModels();
    int countAllPurposes();
    int countAllRegistrations();
    int countAllVehicles();

    int countVehiclesForBrand(@Nonnull String brandName);
    int countRegistrationsForModel(@Nonnull String modelName);
    int countRegistrationsForBrand(@Nonnull String brandName);
    int countRegistrationsForAdminObject(@Nonnull String adminObjectName);
    int countRegistrationsForBodyType(@Nonnull String bodyTypeName);
    int countRegistrationsForColor(@Nonnull String colorName);
    int countRegistrationsForDepartment(@Nonnull Long departmentCode);
    int countRegistrationsForOperation(@Nonnull Long departmentCode);
    int countRegistrationsForFuelType(@Nonnull String fuelTypeName);
    int countRegistrationsForKind(@Nonnull String kindName);
    int countRegistrationsForVehicles(@Nonnull Long vehicleId);

    int countRegistrationsByParams(@Nonnull ParamsHolderBuilder params);
}
