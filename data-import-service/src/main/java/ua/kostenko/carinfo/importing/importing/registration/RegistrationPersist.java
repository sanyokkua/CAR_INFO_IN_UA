package ua.kostenko.carinfo.importing.importing.registration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ua.kostenko.carinfo.common.api.records.*;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.importing.csv.pojo.RegistrationPojo;
import ua.kostenko.carinfo.importing.importing.Persist;

import javax.annotation.Nonnull;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Slf4j
public class RegistrationPersist implements Persist<RegistrationPojo> {
    private static final SimpleDateFormat FORMAT_FIRST = new SimpleDateFormat("yyyy-MM-dd");//2013-02-02
    private static final SimpleDateFormat FORMAT_SECOND = new SimpleDateFormat("dd-MM-yyyy");//19.02.2019
    private final DBService<Registration> registrationCrudService;
    private final DBService<AdministrativeObject> adminService;
    private final DBService<BodyType> bodyTypeCrudService;
    private final DBService<Brand> brandCrudService;
    private final DBService<Color> colorCrudService;
    private final DBService<Department> departmentCrudService;
    private final DBService<FuelType> fuelTypeCrudService;
    private final DBService<Kind> kindCrudService;
    private final DBService<Model> modelCrudService;
    private final DBService<Operation> operationCrudService;
    private final DBService<Purpose> purposeCrudService;
    private final DBService<Vehicle> vehicleCrudService;

    public RegistrationPersist(@NonNull @Nonnull DBService<Registration> registrationCrudService,
                               @NonNull @Nonnull DBService<AdministrativeObject> adminService,
                               @NonNull @Nonnull DBService<BodyType> bodyTypeCrudService,
                               @NonNull @Nonnull DBService<Brand> brandCrudService,
                               @NonNull @Nonnull DBService<Color> colorCrudService,
                               @NonNull @Nonnull DBService<Department> departmentCrudService,
                               @NonNull @Nonnull DBService<FuelType> fuelTypeCrudService,
                               @NonNull @Nonnull DBService<Kind> kindCrudService,
                               @NonNull @Nonnull DBService<Model> modelCrudService,
                               @NonNull @Nonnull DBService<Operation> operationCrudService,
                               @NonNull @Nonnull DBService<Purpose> purposeCrudService,
                               @NonNull @Nonnull DBService<Vehicle> vehicleCrudService) {
        this.registrationCrudService = registrationCrudService;
        this.adminService = adminService;
        this.bodyTypeCrudService = bodyTypeCrudService;
        this.brandCrudService = brandCrudService;
        this.colorCrudService = colorCrudService;
        this.departmentCrudService = departmentCrudService;
        this.fuelTypeCrudService = fuelTypeCrudService;
        this.kindCrudService = kindCrudService;
        this.modelCrudService = modelCrudService;
        this.operationCrudService = operationCrudService;
        this.purposeCrudService = purposeCrudService;
        this.vehicleCrudService = vehicleCrudService;
    }

    private Operation getOperation(@NonNull @Nonnull RegistrationPojo record) {
        Long operationCode = record.getOperationCode();
        Operation foundOperation = operationCrudService.get(operationCode).orElse(null);
        if (Objects.nonNull(foundOperation)){
            return foundOperation;
        }
        String operationName = record.getOperationName().toUpperCase();
        Operation operation = Operation.builder().operationCode(operationCode).operationName(operationName).build();
        return operationCrudService.create(operation).orElse(null);
    }

    private Model getModel(@NonNull @Nonnull RegistrationPojo record) {
        String vehicleModel = StringUtils.trim(record.getVehicleModel()).toUpperCase();
        Model model = Model.builder().modelName(vehicleModel).build();
        Model foundModel = modelCrudService.get(model).orElse(null);
        return Objects.nonNull(foundModel) ? foundModel : modelCrudService.create(model).orElse(null);
    }

    private Brand getBrand(@NonNull @Nonnull RegistrationPojo record) {
        String vehicleModel = StringUtils.trim(record.getVehicleModel()).toUpperCase();
        String vehicleBrand = StringUtils.trim(record.getVehicleBrand()).toUpperCase();
        if (vehicleBrand.contains(vehicleModel)) {
            vehicleBrand = StringUtils.remove(vehicleBrand, vehicleModel);
            vehicleBrand = StringUtils.trim(vehicleBrand);
        }
        Brand brand = Brand.builder().brandName(vehicleBrand).build();
        Brand foundBrand = brandCrudService.get(brand).orElse(null);
        return Objects.nonNull(foundBrand) ? foundBrand : brandCrudService.create(brand).orElse(null);
    }

    private Color getColor(@NonNull @Nonnull RegistrationPojo record) {
        String vehicleColor = record.getVehicleColor().toUpperCase();
        Color color = Color.builder().colorName(vehicleColor).build();
        Color foundColor = colorCrudService.get(color).orElse(null);
        return Objects.nonNull(foundColor) ? foundColor : colorCrudService.create(color).orElse(null);
    }

    private Kind getKind(@NonNull @Nonnull RegistrationPojo record) {
        String vehicleKind = record.getVehicleKind().toUpperCase();
        Kind kind = Kind.builder().kindName(vehicleKind).build();
        Kind foundKind = kindCrudService.get(kind).orElse(null);
        return Objects.nonNull(foundKind) ? foundKind : kindCrudService.create(kind).orElse(null);
    }

    private BodyType getBodyType(@NonNull @Nonnull RegistrationPojo record) {
        String vehicleBodyType = record.getVehicleBodyType().toUpperCase();
        BodyType bodyType = BodyType.builder().bodyTypeName(vehicleBodyType).build();
        BodyType foundBodyType = bodyTypeCrudService.get(bodyType).orElse(null);
        return Objects.nonNull(foundBodyType) ? foundBodyType : bodyTypeCrudService.create(bodyType).orElse(null);
    }

    private Purpose getPurpose(@NonNull @Nonnull RegistrationPojo record) {
        String vehiclePurpose = record.getVehiclePurpose().toUpperCase();
        Purpose purpose = Purpose.builder().purposeName(vehiclePurpose).build();
        Purpose foundPurpose = purposeCrudService.get(purpose).orElse(null);
        return Objects.nonNull(foundPurpose) ? foundPurpose : purposeCrudService.create(purpose).orElse(null);
    }

    private FuelType getFuelType(@NonNull @Nonnull RegistrationPojo record) {
        String vehicleFuelType = record.getVehicleFuelType().toUpperCase();
        FuelType fuelType = FuelType.builder().fuelTypeName(vehicleFuelType).build();
        FuelType foundFuelType = fuelTypeCrudService.get(fuelType).orElse(null);
        return Objects.nonNull(foundFuelType) ? foundFuelType : fuelTypeCrudService.create(fuelType).orElse(null);
    }

    private AdministrativeObject getAdministrativeObject(@NonNull @Nonnull RegistrationPojo record) {
        Long administrativeObjectId = record.getAdministrativeObject();
        return adminService.get(administrativeObjectId).orElse(null);
    }

    private Department getDepartment(@NonNull @Nonnull RegistrationPojo record) {
        Long departmentCode = record.getDepartmentCode();
        return departmentCrudService.get(departmentCode).orElse(null);
    }

    private Vehicle getVehicle(Model createdModel, Brand createdBrand ){
        Vehicle vehicle = Vehicle.builder()
                                 .brandName(createdBrand.getBrandName())
                                 .modelName(createdModel.getModelName())
                                 .build();
        Vehicle foundVehicle = vehicleCrudService.get(vehicle).orElse(null);
        return Objects.nonNull(foundVehicle) ? foundVehicle : vehicleCrudService.create(vehicle).orElse(null);
    }

    private Date getDate(@Nonnull @NonNull RegistrationPojo record) {
        String registrationDate = record.getRegistrationDate().toUpperCase();
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
    public void persist(@NonNull @Nonnull RegistrationPojo record) {
        log.info("persist: Thread N: {}", Thread.currentThread().getId());
        log.info("persist: Record: {}", record.toString());
        try {
            Operation operation = getOperation(record);
            Model model = getModel(record);
            Brand brand = getBrand(record);
            Color color = getColor(record);
            Kind kind = getKind(record);
            BodyType bodyType = getBodyType(record);
            Purpose purpose = getPurpose(record);
            FuelType fuelType = getFuelType(record);
            AdministrativeObject administrativeObject = getAdministrativeObject(record);
            Department department = getDepartment(record);

            if (Objects.isNull(administrativeObject)) {
                log.warn("AdminObject is null");
                return;
            }
            if (Objects.isNull(department)) {
                log.warn("Department is null");
                return;
            }
            if (Objects.isNull(brand)) {
                log.warn("Brand is null");
                return;
            }
            if (Objects.isNull(model)) {
                log.warn("Model is null");
                return;
            }
            if (Objects.isNull(operation)) {
                log.warn("Operation is null");
                return;
            }

            Vehicle createdVehicle = getVehicle(model, brand);

            if (Objects.isNull(createdVehicle)) {
                log.warn("createdVehicle is null");
                return;
            }

            java.sql.Date resultDate = getDate(record);
            String personType = record.getPersonType().toUpperCase();
            Long vehicleMakeYear = record.getVehicleMakeYear();
            Long vehicleEngineCapacity = record.getVehicleEngineCapacity();
            Long vehicleOwnWeight = record.getVehicleOwnWeight();
            Long vehicleTotalWeight = record.getVehicleTotalWeight();
            String vehicleRegistrationNumber = record.getVehicleRegistrationNumber().toUpperCase();
            Registration registration = Registration.builder()
//                                                    .adminObjectId(administrativeObject.getAdminObjId())
//                                                    .opCode(operation.getOperationCode())
//                                                    .depCode(department.getDepartmentCode())
//                                                    .kindId(kind.getKindId())
//                                                    .colorId(color.getColorId())
//                                                    .bodyTypeId(bodyType.getBodyTypeId())
//                                                    .purposeId(Objects.nonNull(purpose) ? purpose.getPurposeId() : null)
//                                                    .fuelTypeId(Objects.nonNull(fuelType) ? fuelType.getFuelTypeId() : null)
//                                                    .vehicleId(createdVehicle.getVehicleId())
//                                                    .adminObjName(administrativeObject.getAdminObjName())
//                                                    .adminObjType(administrativeObject.getAdminObjType())
//                                                    .operation(operation.getOperationName())
//                                                    .departmentName(department.getDepartmentName())
//                                                    .departmentAddress(department.getDepartmentAddress())
//                                                    .departmentEmail(department.getDepartmentEmail())
//                                                    .kind(kind.getKindName())
//                                                    .brand(brand.getBrandName())
//                                                    .model(model.getModelName())
//                                                    .color(color.getColorName())
//                                                    .bodyType(bodyType.getBodyTypeName())
//                                                    .purpose(Objects.nonNull(purpose) ? purpose.getPurposeName() : null)
//                                                    .fuelType(Objects.nonNull(fuelType) ? fuelType.getFuelTypeName() : null)
//                                                    .engineCapacity(vehicleEngineCapacity)
//                                                    .ownWeight(vehicleOwnWeight)
//                                                    .totalWeight(vehicleTotalWeight)
//                                                    .makeYear(vehicleMakeYear)
//                                                    .personType(personType)
//                                                    .registrationNumber(vehicleRegistrationNumber)
//                                                    .registrationDate(resultDate)
                                                    .build();
            registrationCrudService.create(registration);
        } catch (Exception ex) {
            log.warn("Problem with saving record: {}", record.toString());
            log.error("ERROR OCCURRED IN PERSISTING CURRENT RECORD", ex);
        }
    }
}
