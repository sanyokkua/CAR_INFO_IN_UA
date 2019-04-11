package ua.kostenko.carinfo.importing.importing.registration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ua.kostenko.carinfo.common.api.records.*;
import ua.kostenko.carinfo.common.records.*;
import ua.kostenko.carinfo.common.services.CrudService;
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
    private final CrudService<Registration> registrationCrudService;
    private final CrudService<AdministrativeObject> adminService;
    private final CrudService<BodyType> bodyTypeCrudService;
    private final CrudService<Brand> brandCrudService;
    private final CrudService<Color> colorCrudService;
    private final CrudService<Department> departmentCrudService;
    private final CrudService<FuelType> fuelTypeCrudService;
    private final CrudService<Kind> kindCrudService;
    private final CrudService<Model> modelCrudService;
    private final CrudService<Operation> operationCrudService;
    private final CrudService<Purpose> purposeCrudService;
    private final CrudService<Vehicle> vehicleCrudService;

    public RegistrationPersist(@NonNull @Nonnull CrudService<Registration> registrationCrudService,
                               @NonNull @Nonnull CrudService<AdministrativeObject> adminService,
                               @NonNull @Nonnull CrudService<BodyType> bodyTypeCrudService,
                               @NonNull @Nonnull CrudService<Brand> brandCrudService,
                               @NonNull @Nonnull CrudService<Color> colorCrudService,
                               @NonNull @Nonnull CrudService<Department> departmentCrudService,
                               @NonNull @Nonnull CrudService<FuelType> fuelTypeCrudService,
                               @NonNull @Nonnull CrudService<Kind> kindCrudService,
                               @NonNull @Nonnull CrudService<Model> modelCrudService,
                               @NonNull @Nonnull CrudService<Operation> operationCrudService,
                               @NonNull @Nonnull CrudService<Purpose> purposeCrudService,
                               @NonNull @Nonnull CrudService<Vehicle> vehicleCrudService) {
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
        Operation foundOperation = operationCrudService.find(operationCode);
        if (Objects.nonNull(foundOperation)){
            return foundOperation;
        }
        String operationName = record.getOperationName().toUpperCase();
        Operation operation = Operation.builder().operationCode(operationCode).operationName(operationName).build();
        return operationCrudService.create(operation);
    }

    private Model getModel(@NonNull @Nonnull RegistrationPojo record) {
        String vehicleModel = StringUtils.trim(record.getVehicleModel()).toUpperCase();
        Model model = Model.builder().modelName(vehicleModel).build();
        Model foundModel = modelCrudService.find(model);
        return Objects.nonNull(foundModel) ? foundModel : modelCrudService.create(model);
    }

    private Brand getBrand(@NonNull @Nonnull RegistrationPojo record) {
        String vehicleModel = StringUtils.trim(record.getVehicleModel()).toUpperCase();
        String vehicleBrand = StringUtils.trim(record.getVehicleBrand()).toUpperCase();
        if (vehicleBrand.contains(vehicleModel)) {
            vehicleBrand = StringUtils.remove(vehicleBrand, vehicleModel);
            vehicleBrand = StringUtils.trim(vehicleBrand);
        }
        Brand brand = Brand.builder().brandName(vehicleBrand).build();
        Brand foundBrand = brandCrudService.find(brand);
        return Objects.nonNull(foundBrand) ? foundBrand : brandCrudService.create(brand);
    }

    private Color getColor(@NonNull @Nonnull RegistrationPojo record) {
        String vehicleColor = record.getVehicleColor().toUpperCase();
        Color color = Color.builder().colorName(vehicleColor).build();
        Color foundColor = colorCrudService.find(color);
        return Objects.nonNull(foundColor) ? foundColor : colorCrudService.create(color);
    }

    private Kind getKind(@NonNull @Nonnull RegistrationPojo record) {
        String vehicleKind = record.getVehicleKind().toUpperCase();
        Kind kind = Kind.builder().kindName(vehicleKind).build();
        Kind foundKind = kindCrudService.find(kind);
        return Objects.nonNull(foundKind) ? foundKind : kindCrudService.create(kind);
    }

    private BodyType getBodyType(@NonNull @Nonnull RegistrationPojo record) {
        String vehicleBodyType = record.getVehicleBodyType().toUpperCase();
        BodyType bodyType = BodyType.builder().bodyTypeName(vehicleBodyType).build();
        BodyType foundBodyType = bodyTypeCrudService.find(bodyType);
        return Objects.nonNull(foundBodyType) ? foundBodyType : bodyTypeCrudService.create(bodyType);
    }

    private Purpose getPurpose(@NonNull @Nonnull RegistrationPojo record) {
        String vehiclePurpose = record.getVehiclePurpose().toUpperCase();
        Purpose purpose = Purpose.builder().purposeName(vehiclePurpose).build();
        Purpose foundPurpose = purposeCrudService.find(purpose);
        return Objects.nonNull(foundPurpose) ? foundPurpose : purposeCrudService.create(purpose);
    }

    private FuelType getFuelType(@NonNull @Nonnull RegistrationPojo record) {
        String vehicleFuelType = record.getVehicleFuelType().toUpperCase();
        FuelType fuelType = FuelType.builder().fuelTypeName(vehicleFuelType).build();
        FuelType foundFuelType = fuelTypeCrudService.find(fuelType);
        return Objects.nonNull(foundFuelType) ? foundFuelType : fuelTypeCrudService.create(fuelType);
    }

    private AdministrativeObject getAdministrativeObject(@NonNull @Nonnull RegistrationPojo record) {
        Long administrativeObjectId = record.getAdministrativeObject();
        return adminService.find(administrativeObjectId);
    }

    private Department getDepartment(@NonNull @Nonnull RegistrationPojo record) {
        Long departmentCode = record.getDepartmentCode();
        return departmentCrudService.find(departmentCode);
    }

    private Vehicle getVehicle(Model createdModel, Brand createdBrand ){
        Vehicle vehicle = Vehicle.builder()
                                 .brandId(createdBrand.getBrandId())
                                 .modelId(createdModel.getModelId())
                                 .brandName(createdBrand.getBrandName())
                                 .modelName(createdModel.getModelName())
                                 .build();
        Vehicle foundVehicle = vehicleCrudService.find(vehicle);
        return Objects.nonNull(foundVehicle) ? foundVehicle : vehicleCrudService.create(vehicle);
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
                                                    .adminObjectId(administrativeObject.getAdminObjId())
                                                    .opCode(operation.getOperationCode())
                                                    .depCode(department.getDepartmentCode())
                                                    .kindId(kind.getKindId())
                                                    .colorId(color.getColorId())
                                                    .bodyTypeId(bodyType.getBodyTypeId())
                                                    .purposeId(Objects.nonNull(purpose) ? purpose.getPurposeId() : null)
                                                    .fuelTypeId(Objects.nonNull(fuelType) ? fuelType.getFuelTypeId() : null)
                                                    .vehicleId(createdVehicle.getVehicleId())
                                                    .adminObjName(administrativeObject.getAdminObjName())
                                                    .adminObjType(administrativeObject.getAdminObjType())
                                                    .operation(operation.getOperationName())
                                                    .departmentName(department.getDepartmentName())
                                                    .departmentAddress(department.getDepartmentAddress())
                                                    .departmentEmail(department.getDepartmentEmail())
                                                    .kind(kind.getKindName())
                                                    .brand(brand.getBrandName())
                                                    .model(model.getModelName())
                                                    .color(color.getColorName())
                                                    .bodyType(bodyType.getBodyTypeName())
                                                    .purpose(Objects.nonNull(purpose) ? purpose.getPurposeName() : null)
                                                    .fuelType(Objects.nonNull(fuelType) ? fuelType.getFuelTypeName() : null)
                                                    .engineCapacity(vehicleEngineCapacity)
                                                    .ownWeight(vehicleOwnWeight)
                                                    .totalWeight(vehicleTotalWeight)
                                                    .makeYear(vehicleMakeYear)
                                                    .personType(personType)
                                                    .registrationNumber(vehicleRegistrationNumber)
                                                    .registrationDate(resultDate)
                                                    .build();
            registrationCrudService.create(registration);
        } catch (Exception ex) {
            log.warn("Problem with saving record: {}", record.toString());
            log.error("ERROR OCCURRED IN PERSISTING CURRENT RECORD", ex);
        }
    }
}
