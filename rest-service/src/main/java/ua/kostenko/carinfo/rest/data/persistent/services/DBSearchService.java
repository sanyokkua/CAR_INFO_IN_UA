package ua.kostenko.carinfo.rest.data.persistent.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.common.api.records.BodyType;
import ua.kostenko.carinfo.common.api.records.Brand;
import ua.kostenko.carinfo.common.api.records.Color;
import ua.kostenko.carinfo.common.api.records.Department;
import ua.kostenko.carinfo.common.api.records.FuelType;
import ua.kostenko.carinfo.common.api.records.Kind;
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.common.api.records.Operation;
import ua.kostenko.carinfo.common.api.records.Purpose;
import ua.kostenko.carinfo.common.api.records.Registration;
import ua.kostenko.carinfo.common.api.records.Vehicle;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.rest.data.presentation.Auto;
import ua.kostenko.carinfo.rest.data.presentation.CombinedInformation;
import ua.kostenko.carinfo.rest.data.presentation.ServiceCenter;
import ua.kostenko.carinfo.rest.data.presentation.VRegistration;

@Service
@Transactional
@Deprecated
public class DBSearchService implements SearchService {

    private final DBService<Registration> registrationDBService;
    private final DBService<Brand> brandDBService;
    private final DBService<Color> colorDBService;
    private final DBService<FuelType> fuelTypeDBService;
    private final DBService<Kind> kindDBService;
    private final DBService<Vehicle> vehicleDBService;

    @Autowired
    public DBSearchService(DBService<Registration> registrationDBService,
            DBService<AdministrativeObject> administrativeObjectDBService,
            DBService<BodyType> bodyTypeDBService,
            DBService<Brand> brandDBService,
            DBService<Color> colorDBService,
            DBService<Department> departmentDBService,
            DBService<FuelType> fuelTypeDBService,
            DBService<Kind> kindDBService,
            DBService<Model> modelDBService,
            DBService<Operation> operationDBService,
            DBService<Purpose> purposeDBService,
            DBService<Vehicle> vehicleDBService) {
        this.registrationDBService = registrationDBService;
        this.brandDBService = brandDBService;
        this.colorDBService = colorDBService;
        this.fuelTypeDBService = fuelTypeDBService;
        this.kindDBService = kindDBService;
        this.vehicleDBService = vehicleDBService;
    }

    @Override
    public List<CombinedInformation> searchAllByRegistrationNumber(String number) {
        ParamsHolderBuilder params = new ParamsHolderBuilder().param(Registration.REGISTRATION_NUMBER, number);
        Page<Registration> registration = registrationDBService.getAll(params);

        if (registration.getContent().size() > 0) {
            return registration.getContent().stream().map(record -> {
                Auto auto = Auto.map(record);
                VRegistration vRegistration = VRegistration.map(record);
                ServiceCenter serviceCenter = ServiceCenter.map(record);
                return CombinedInformation.builder().auto(auto).registration(vRegistration).serviceCenter(serviceCenter)
                        .build();
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAllBrands() {
        return brandDBService.getAll().stream().map(Brand::getBrandName).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllModelsForBrand(String brand) {
        Page<Vehicle> all = vehicleDBService.getAll(new ParamsHolderBuilder().param(Vehicle.BRAND_NAME, brand));
        if (all.getContent().size() > 0) {
            return all.getContent().stream().map(Vehicle::getModelName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAllColors() {
        return colorDBService.getAll().stream().map(Color::getColorName).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllFuelTypes() {
        return fuelTypeDBService.getAll().stream().map(FuelType::getFuelTypeName).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllCarKinds() {
        return kindDBService.getAll().stream().map(Kind::getKindName).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllRegions() {
        return Collections.emptyList();
    }

    @Override
    public long countAllRegistrations() {
        return 0;
    }

    @Override
    public long countAllByBrand(String brand) {
        return 0;
    }

    @Override
    public long countAllByCarBrandAndModel(String brand, String model) {
        return 0;
    }

    @Override
    public long countAllByCarColor(String color) {
        return 0;
    }

    @Override
    public long countAllByFuelType(String fuelType) {
        return 0;
    }

    @Override
    public long countAllByCarKind(String carKind) {
        return 0;
    }

    @Override
    public long countAllCarsByYear(int year) {
        return 0;
    }

    @Override
    public long countAllCarsInRegion(String region) {
        return 0;
    }

}
