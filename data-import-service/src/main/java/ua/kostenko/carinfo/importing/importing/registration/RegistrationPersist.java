package ua.kostenko.carinfo.importing.importing.registration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ua.kostenko.carinfo.common.api.records.*;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.importing.csv.pojo.RegistrationCsvRecord;
import ua.kostenko.carinfo.importing.importing.Persist;

import javax.annotation.Nonnull;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class RegistrationPersist implements Persist<RegistrationCsvRecord> {
    private static final SimpleDateFormat FORMAT_FIRST = new SimpleDateFormat("yyyy-MM-dd");//2013-02-02
    private static final SimpleDateFormat FORMAT_SECOND = new SimpleDateFormat("dd-MM-yyyy");//19.02.2019
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

    public RegistrationPersist(@NonNull @Nonnull DBService<Registration> registrationDBService,
                               @NonNull @Nonnull DBService<AdministrativeObject> administrativeObjectDBService,
                               @NonNull @Nonnull DBService<BodyType> bodyTypeDBService,
                               @NonNull @Nonnull DBService<Brand> brandDBService,
                               @NonNull @Nonnull DBService<Color> colorDBService,
                               @NonNull @Nonnull DBService<Department> departmentDBService,
                               @NonNull @Nonnull DBService<FuelType> fuelTypeDBService,
                               @NonNull @Nonnull DBService<Kind> kindDBService,
                               @NonNull @Nonnull DBService<Model> modelDBService,
                               @NonNull @Nonnull DBService<Operation> operationDBService,
                               @NonNull @Nonnull DBService<Purpose> purposeDBService,
                               @NonNull @Nonnull DBService<Vehicle> vehicleDBService) {
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

    private Optional<Operation> getOperation(@NonNull @Nonnull RegistrationCsvRecord record) {
        Long operationCode = record.getOperationCode();
        Optional<Operation> foundOperation = operationDBService.get(operationCode);
        if (foundOperation.isPresent()) {
            return foundOperation;
        }
        String operationName = record.getOperationName();
        Operation operation = Operation.builder().operationCode(operationCode).operationName(operationName).build();
        return operationDBService.create(operation);
    }

    private Optional<Model> getModel(@NonNull @Nonnull RegistrationCsvRecord record) {
        String vehicleModel = StringUtils.trim(record.getVehicleModel());
        Model model = Model.builder().modelName(vehicleModel).build();
        Optional<Model> foundModel = modelDBService.get(model);
        return Objects.nonNull(foundModel) ? foundModel : modelDBService.create(model);
    }

    private Optional<Brand> getBrand(@NonNull @Nonnull RegistrationCsvRecord record) {
        String vehicleModel = StringUtils.trim(record.getVehicleModel());
        String vehicleBrand = StringUtils.trim(record.getVehicleBrand());
        if (vehicleBrand.contains(vehicleModel)) {
            vehicleBrand = StringUtils.remove(vehicleBrand, vehicleModel);
            vehicleBrand = StringUtils.trim(vehicleBrand);
        }
        Brand brand = Brand.builder().brandName(vehicleBrand).build();
        Optional<Brand> foundBrand = brandDBService.get(brand);
        return Objects.nonNull(foundBrand) ? foundBrand : brandDBService.create(brand);
    }

    private Optional<Color> getColor(@NonNull @Nonnull RegistrationCsvRecord record) {
        String vehicleColor = record.getVehicleColor();
        Color color = Color.builder().colorName(vehicleColor).build();
        Optional<Color> foundColor = colorDBService.get(color);
        return Objects.nonNull(foundColor) ? foundColor : colorDBService.create(color);
    }

    private Optional<Kind> getKind(@NonNull @Nonnull RegistrationCsvRecord record) {
        String vehicleKind = record.getVehicleKind();
        Kind kind = Kind.builder().kindName(vehicleKind).build();
        Optional<Kind> foundKind = kindDBService.get(kind);
        return Objects.nonNull(foundKind) ? foundKind : kindDBService.create(kind);
    }

    private Optional<BodyType> getBodyType(@NonNull @Nonnull RegistrationCsvRecord record) {
        String vehicleBodyType = record.getVehicleBodyType();
        BodyType bodyType = BodyType.builder().bodyTypeName(vehicleBodyType).build();
        Optional<BodyType> foundBodyType = bodyTypeDBService.get(bodyType);
        return Objects.nonNull(foundBodyType) ? foundBodyType : bodyTypeDBService.create(bodyType);
    }

    private Optional<Purpose> getPurpose(@NonNull @Nonnull RegistrationCsvRecord record) {
        String vehiclePurpose = record.getVehiclePurpose();
        Purpose purpose = Purpose.builder().purposeName(vehiclePurpose).build();
        Optional<Purpose> foundPurpose = purposeDBService.get(purpose);
        return Objects.nonNull(foundPurpose) ? foundPurpose : purposeDBService.create(purpose);
    }

    private Optional<FuelType> getFuelType(@NonNull @Nonnull RegistrationCsvRecord record) {
        String vehicleFuelType = record.getVehicleFuelType();
        FuelType fuelType = FuelType.builder().fuelTypeName(vehicleFuelType).build();
        Optional<FuelType> foundFuelType = fuelTypeDBService.get(fuelType);
        return Objects.nonNull(foundFuelType) ? foundFuelType : fuelTypeDBService.create(fuelType);
    }

    private Optional<AdministrativeObject> getAdministrativeObject(@NonNull @Nonnull RegistrationCsvRecord record) {
        Long administrativeObjectId = record.getAdministrativeObject();
        return administrativeObjectDBService.get(administrativeObjectId);
    }

    private Optional<Department> getDepartment(@NonNull @Nonnull RegistrationCsvRecord record) {
        Long departmentCode = record.getDepartmentCode();
        return departmentDBService.get(departmentCode);
    }

    private Optional<Vehicle> getVehicle(Model createdModel, Brand createdBrand) {
        if (Objects.nonNull(createdBrand) && Objects.nonNull(createdModel)) {
            Vehicle vehicle = Vehicle.builder()
                                     .brandName(createdBrand.getBrandName())
                                     .modelName(createdModel.getModelName())
                                     .build();
            Optional<Vehicle> foundVehicle = vehicleDBService.get(vehicle);
            return Objects.nonNull(foundVehicle) ? foundVehicle : vehicleDBService.create(vehicle);
        }
        return Optional.empty();
    }

    private Date getDate(@Nonnull @NonNull RegistrationCsvRecord record) {
        String registrationDate = record.getRegistrationDate();
        Date resultDate = null;
        try {
            java.util.Date date = FORMAT_FIRST.parse(registrationDate);
            resultDate = new Date(date.getTime());
        } catch (Exception ex) {
            log.warn("Problem with parsing date with first formatter: {}", registrationDate);
        }
        if (Objects.isNull(resultDate)) {
            try {
                java.util.Date date = FORMAT_SECOND.parse(registrationDate);
                resultDate = new Date(date.getTime());
            } catch (Exception ex) {
                log.warn("Problem with parsing date with second formatter: {}", registrationDate);
            }
        }
        return resultDate;
    }

    @Override
    public void persist(@NonNull @Nonnull RegistrationCsvRecord record) {
        log.info("persist: Thread N: {}", Thread.currentThread().getId());
        log.info("persist: Record: {}", record.toString());
        try {
            Optional<Operation> operation = getOperation(record);
            Optional<Model> model = getModel(record);
            Optional<Brand> brand = getBrand(record);
            Optional<Color> color = getColor(record);
            Optional<Kind> kind = getKind(record);
            Optional<BodyType> bodyType = getBodyType(record);
            Optional<Purpose> purpose = getPurpose(record);
            Optional<FuelType> fuelType = getFuelType(record);
            Optional<AdministrativeObject> administrativeObject = getAdministrativeObject(record);
            Optional<Department> department = getDepartment(record);
            Optional<Vehicle> createdVehicle = getVehicle(model.orElseGet(null), brand.orElseGet(null));
            java.sql.Date registrationDate = getDate(record);
            String personType = record.getPersonType();
            Long vehicleMakeYear = record.getVehicleMakeYear();
            Long vehicleEngineCapacity = record.getVehicleEngineCapacity();
            Long vehicleOwnWeight = record.getVehicleOwnWeight();
            Long vehicleTotalWeight = record.getVehicleTotalWeight();
            String vehicleRegistrationNumber = record.getVehicleRegistrationNumber();

            if (operation.isPresent() && department.isPresent() && kind.isPresent() && color.isPresent() &&
                    purpose.isPresent() && brand.isPresent() && model.isPresent() &&
                    createdVehicle.isPresent() && StringUtils.isNotBlank(personType) &&
                    Objects.nonNull(vehicleMakeYear) && Objects.nonNull(registrationDate)) {
                AdministrativeObject nullableAdminObj = administrativeObject.orElseGet(null);
                BodyType nullableBodyType = bodyType.orElseGet(null);
                FuelType nullableFuelType = fuelType.orElseGet(null);

                Registration registration = Registration.builder()
                                                        .adminObjName(Objects.nonNull(nullableAdminObj) ? nullableAdminObj.getAdminObjName() : null)//NULLABLE
                                                        .adminObjType(Objects.nonNull(nullableAdminObj) ? nullableAdminObj.getAdminObjType() : null)//NULLABLE
                                                        .operationCode(operation.get().getOperationCode())//non NULLABLE opName
                                                        .operationName(operation.get().getOperationName())//non NULLABLE opName
                                                        .departmentCode(department.get().getDepartmentCode())//non NULLABLE
                                                        .departmentAddress(department.get().getDepartmentAddress())//non NULLABLE
                                                        .departmentEmail(department.get().getDepartmentEmail())//non NULLABLE
                                                        .kind(kind.get().getKindName())//non NULLABLE
                                                        .color(color.get().getColorName())//non NULLABLE
                                                        .bodyType(Objects.nonNull(nullableAdminObj) ? nullableBodyType.getBodyTypeName() : null)//NULLABLE
                                                        .purpose(purpose.get().getPurposeName())//non NULLABLE
                                                        .brand(brand.get().getBrandName())//non NULLABLE
                                                        .model(model.get().getModelName())//non NULLABLE
                                                        .fuelType(Objects.nonNull(nullableFuelType) ? nullableFuelType.getFuelTypeName() : null)//NULLABLE
                                                        .engineCapacity(vehicleEngineCapacity)//NULLABLE
                                                        .makeYear(vehicleMakeYear)//non NULLABLE
                                                        .ownWeight(vehicleOwnWeight)//NULLABLE
                                                        .totalWeight(vehicleTotalWeight)//NULLABLE
                                                        .personType(personType)//non NULLABLE
                                                        .registrationNumber(vehicleRegistrationNumber)//NULLABLE
                                                        .registrationDate(registrationDate)//non NULLABLE
                                                        .build();
                Optional<Registration> result = registrationDBService.create(registration);
                if (!result.isPresent()) {
                    log.warn("Record {} is not created!", record);
                }
            } else {
                log.warn("Registration record is not valid");
            }
        } catch (Exception ex) {
            log.warn("Problem with saving record: {}", record.toString());
            log.error("ERROR OCCURRED IN PERSISTING CURRENT RECORD", ex);
        }
    }
}
