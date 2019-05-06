package ua.kostenko.carinfo.rest.services;

import org.springframework.data.domain.Page;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface QueryService {
    Page<AdministrativeObject> getAllAdminObjects(@Nullable Integer page);
    Page<BodyType> getAllBodyTypes(@Nullable Integer page);
    Page<Brand> getAllBrands(@Nullable Integer page);
    Page<Color> getAllColors(@Nullable Integer page);
    Page<Department> getAllDepartments(@Nullable Integer page);
    Page<FuelType> getAllFuelTypes(@Nullable Integer page);
    Page<Kind> getAllKinds(@Nullable Integer page);
    Page<Model> getAllModels(@Nullable Integer page);
    Page<Operation> getAllOperations(@Nullable Integer page);
    Page<Purpose> getAllPurposes(@Nullable Integer page);
    Page<Registration> getAllRegistrations(@Nullable Integer page);
    Page<Vehicle> getAllVehicles(@Nullable Integer page);

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
