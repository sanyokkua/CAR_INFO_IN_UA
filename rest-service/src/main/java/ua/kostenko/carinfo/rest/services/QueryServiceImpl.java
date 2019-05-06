package ua.kostenko.carinfo.rest.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.*;
import ua.kostenko.carinfo.common.api.services.DBService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Slf4j
@Service
public class QueryServiceImpl implements QueryService {
    private final DBService<Registration> registrationDBService;
    private final DBService<AdministrativeObject> administrativeObjectDBService;
    private final DBService<BodyType> bodyTypeDBService;
    private final DBService<Brand> brandDBService;
    private final DBService<Color> colorDBService;
    private final DBService<Department> departmentDBService;
    private final DBService<FuelType> fuelTypeDBService;
    private final DBService<Kind> kindDBService;
    private final DBService<Model> modelDBService;
    private final DBService<Operation> operationDBService;
    private final DBService<Purpose> purposeDBService;
    private final DBService<Vehicle> vehicleDBService;

    @Autowired
    public QueryServiceImpl(DBService<Registration> registrationDBService, DBService<AdministrativeObject> administrativeObjectDBService, DBService<BodyType> bodyTypeDBService, DBService<Brand> brandDBService, DBService<Color> colorDBService, DBService<Department> departmentDBService, DBService<FuelType> fuelTypeDBService, DBService<Kind> kindDBService, DBService<Model> modelDBService, DBService<Operation> operationDBService, DBService<Purpose> purposeDBService, DBService<Vehicle> vehicleDBService) {
        this.registrationDBService = registrationDBService;
        this.administrativeObjectDBService = administrativeObjectDBService;
        this.bodyTypeDBService = bodyTypeDBService;
        this.brandDBService = brandDBService;
        this.colorDBService = colorDBService;
        this.departmentDBService = departmentDBService;
        this.fuelTypeDBService = fuelTypeDBService;
        this.kindDBService = kindDBService;
        this.modelDBService = modelDBService;
        this.operationDBService = operationDBService;
        this.purposeDBService = purposeDBService;
        this.vehicleDBService = vehicleDBService;
    }

    @Override
    public Page<AdministrativeObject> getAllAdminObjects(@Nullable Integer page) {
        ParamsHolderBuilder builder = new ParamsHolderBuilder();
        builder.page(page);
        return administrativeObjectDBService.getAll(builder);
    }

    @Override
    public Page<BodyType> getAllBodyTypes(@Nullable Integer page) {
        return null;
    }

    @Override
    public Page<Brand> getAllBrands(@Nullable Integer page) {
        return null;
    }

    @Override
    public Page<Color> getAllColors(@Nullable Integer page) {
        return null;
    }

    @Override
    public Page<Department> getAllDepartments(@Nullable Integer page) {
        return null;
    }

    @Override
    public Page<FuelType> getAllFuelTypes(@Nullable Integer page) {
        return null;
    }

    @Override
    public Page<Kind> getAllKinds(@Nullable Integer page) {
        return null;
    }

    @Override
    public Page<Model> getAllModels(@Nullable Integer page) {
        return null;
    }

    @Override
    public Page<Operation> getAllOperations(@Nullable Integer page) {
        return null;
    }

    @Override
    public Page<Purpose> getAllPurposes(@Nullable Integer page) {
        return null;
    }

    @Override
    public Page<Registration> getAllRegistrations(@Nullable Integer page) {
        return null;
    }

    @Override
    public Page<Vehicle> getAllVehicles(@Nullable Integer page) {
        return null;
    }

    @Override
    public Page<Vehicle> findVehicleWithBrand(@Nonnull String brandName) {
        return null;
    }

    @Override
    public Page<Vehicle> findVehicleWithModel(@Nonnull String modelName) {
        return null;
    }

    @Override
    public Page<Registration> findRegistrationsByNumber(@Nonnull String registrationNumber) {
        return null;
    }

    @Override
    public Page<Registration> findRegistrationByParams(@Nonnull ParamsHolderBuilder params) {
        return null;
    }

    @Override
    public int countAllAdminObjects() {
        return administrativeObjectDBService.getAll().size();
    }

    @Override
    public int countAllBodyTypes() {
        return 0;
    }

    @Override
    public int countAllBrands() {
        return 0;
    }

    @Override
    public int countAllColors() {
        return 0;
    }

    @Override
    public int countAllDepartments() {
        return 0;
    }

    @Override
    public int countAllFuelTypes() {
        return 0;
    }

    @Override
    public int countAllKinds() {
        return 0;
    }

    @Override
    public int countAllOperations() {
        return 0;
    }

    @Override
    public int countAllModels() {
        return 0;
    }

    @Override
    public int countAllPurposes() {
        return 0;
    }

    @Override
    public int countAllRegistrations() {
        return 0;
    }

    @Override
    public int countAllVehicles() {
        return 0;
    }

    @Override
    public int countVehiclesForBrand(@Nonnull String brandName) {
        return 0;
    }

    @Override
    public int countRegistrationsForModel(@Nonnull String modelName) {
        return 0;
    }

    @Override
    public int countRegistrationsForBrand(@Nonnull String brandName) {
        return 0;
    }

    @Override
    public int countRegistrationsForAdminObject(@Nonnull String adminObjectName) {
        return 0;
    }

    @Override
    public int countRegistrationsForBodyType(@Nonnull String bodyTypeName) {
        return 0;
    }

    @Override
    public int countRegistrationsForColor(@Nonnull String colorName) {
        return 0;
    }

    @Override
    public int countRegistrationsForDepartment(@Nonnull Long departmentCode) {
        return 0;
    }

    @Override
    public int countRegistrationsForOperation(@Nonnull Long departmentCode) {
        return 0;
    }

    @Override
    public int countRegistrationsForFuelType(@Nonnull String fuelTypeName) {
        return 0;
    }

    @Override
    public int countRegistrationsForKind(@Nonnull String kindName) {
        return 0;
    }

    @Override
    public int countRegistrationsForVehicles(@Nonnull Long vehicleId) {
        return 0;
    }

    @Override
    public int countRegistrationsByParams(@Nonnull ParamsHolderBuilder params) {
        return 0;
    }
}
