package ua.kostenko.carinfo.importing.importing.registration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ua.kostenko.carinfo.common.records.*;
import ua.kostenko.carinfo.common.services.CrudService;
import ua.kostenko.carinfo.importing.csv.pojo.RegistrationPojo;
import ua.kostenko.carinfo.importing.importing.Persist;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Slf4j
public class RegistrationPersist implements Persist<RegistrationPojo> {
    private final CrudService<Registration> service;
    private final CrudService<AdministrativeObject> adminRepository;
    private final CrudService<BodyType> bodyTypeRepository;
    private final CrudService<Brand> brandRepository;
    private final CrudService<Color> colorRepository;
    private final CrudService<Department> departmentRepository;
    private final CrudService<FuelType> fuelTypeRepository;
    private final CrudService<Kind> kindRepository;
    private final CrudService<Model> modelRepository;
    private final CrudService<Operation> operationRepository;
    private final CrudService<Purpose> purposeRepository;
    private final CrudService<Vehicle> vehicleRepository;
    private final SimpleDateFormat formatFirst = new SimpleDateFormat("yyyy-MM-dd");//2013-02-02
    private final SimpleDateFormat formatSecond = new SimpleDateFormat("dd-MM-yyyy");//19.02.2019

    public RegistrationPersist(@NonNull @Nonnull CrudService<Registration> service,
                               @NonNull @Nonnull CrudService<AdministrativeObject> adminRepository,
                               @NonNull @Nonnull CrudService<BodyType> bodyTypeRepository,
                               @NonNull @Nonnull CrudService<Brand> brandRepository,
                               @NonNull @Nonnull CrudService<Color> colorRepository,
                               @NonNull @Nonnull CrudService<Department> departmentRepository,
                               @NonNull @Nonnull CrudService<FuelType> fuelTypeRepository,
                               @NonNull @Nonnull CrudService<Kind> kindRepository,
                               @NonNull @Nonnull CrudService<Model> modelRepository,
                               @NonNull @Nonnull CrudService<Operation> operationRepository,
                               @NonNull @Nonnull CrudService<Purpose> purposeRepository,
                               @NonNull @Nonnull CrudService<Vehicle> vehicleRepository) {
        this.service = service;
        this.adminRepository = adminRepository;
        this.bodyTypeRepository = bodyTypeRepository;
        this.brandRepository = brandRepository;
        this.colorRepository = colorRepository;
        this.departmentRepository = departmentRepository;
        this.fuelTypeRepository = fuelTypeRepository;
        this.kindRepository = kindRepository;
        this.modelRepository = modelRepository;
        this.operationRepository = operationRepository;
        this.purposeRepository = purposeRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void persist(@NonNull @Nonnull RegistrationPojo record) {
        log.info("persist: Thread N: {}", Thread.currentThread().getId());
        log.info("persist: Record: {}", record.toString());
        try {
            String personType = record.getPersonType().toUpperCase();
            Long administrativeObjectId = record.getAdministrativeObject();
            Long operationCode = record.getOperationCode();
            String operationName = record.getOperationName().toUpperCase();
            String registrationDate = record.getRegistrationDate().toUpperCase();
            Long departmentCode = record.getDepartmentCode();
            String vehicleBrand = StringUtils.trim(record.getVehicleBrand()).toUpperCase();
            String vehicleModel = StringUtils.trim(record.getVehicleModel()).toUpperCase();
            if (vehicleBrand.contains(vehicleModel)) {
                vehicleBrand = StringUtils.remove(vehicleBrand, vehicleModel);
                vehicleBrand = StringUtils.trim(vehicleBrand);
            }
            Long vehicleMakeYear = record.getVehicleMakeYear();
            String vehicleColor = record.getVehicleColor().toUpperCase();
            String vehicleKind = record.getVehicleKind().toUpperCase();
            String vehicleBodyType = record.getVehicleBodyType().toUpperCase();
            String vehiclePurpose = record.getVehiclePurpose().toUpperCase();
            String vehicleFuelType = record.getVehicleFuelType().toUpperCase();
            Long vehicleEngineCapacity = record.getVehicleEngineCapacity();
            Long vehicleOwnWeight = record.getVehicleOwnWeight();
            Long vehicleTotalWeight = record.getVehicleTotalWeight();
            String vehicleRegistrationNumber = record.getVehicleRegistrationNumber().toUpperCase();

            Operation operation = Operation.builder().operationCode(operationCode).operationName(operationName).build();
            Brand brand = Brand.builder().brandName(vehicleBrand).build();
            Model model = Model.builder().modelName(vehicleModel).build();
            Color color = Color.builder().colorName(vehicleColor).build();
            Kind kind = Kind.builder().kindName(vehicleKind).build();
            BodyType bodyType = BodyType.builder().bodyTypeName(vehicleBodyType).build();
            Purpose purpose = Purpose.builder().purposeName(vehiclePurpose).build();
            FuelType fuelType = FuelType.builder().fuelTypeName(vehicleFuelType).build();

            AdministrativeObject admObj = adminRepository.find(administrativeObjectId);
            Department department = departmentRepository.find(departmentCode);

            Operation foundOperation = operationRepository.find(operation);
            Operation createdOperation = Objects.nonNull(foundOperation) ? foundOperation : operationRepository.create(operation);

            Brand foundBrand = brandRepository.find(brand);
            Brand createdBrand = Objects.nonNull(foundBrand) ? foundBrand : brandRepository.create(brand);

            Model foundModel = modelRepository.find(model);
            Model createdModel = Objects.nonNull(foundModel) ? foundModel : modelRepository.create(model);

            Color foundColor = colorRepository.find(color);
            Color createdColor = Objects.nonNull(foundColor) ? foundColor : colorRepository.create(color);

            Kind foundKind = kindRepository.find(kind);
            Kind createdKind = Objects.nonNull(foundKind) ? foundKind : kindRepository.create(kind);

            BodyType foundBodyType = bodyTypeRepository.find(bodyType);
            BodyType createdBodyType = Objects.nonNull(foundBodyType) ? foundBodyType : bodyTypeRepository.create(bodyType);

            Purpose foundPurpose = purposeRepository.find(purpose);
            Purpose createdPurpose = Objects.nonNull(foundPurpose) ? foundPurpose : purposeRepository.create(purpose);

            FuelType foundFuelType = fuelTypeRepository.find(fuelType);
            FuelType createdFuelType = Objects.nonNull(foundFuelType) ? foundFuelType : fuelTypeRepository.create(fuelType);

            if (Objects.isNull(admObj) || Objects.isNull(department)) {
                log.info("AdminObject or department is null");
                return;
            }

            if (Objects.isNull(createdOperation) || Objects.isNull(createdBrand) || Objects.isNull(createdModel)) {
                log.info("operation or brand or model is null");
                return;
            }

            Vehicle vehicle = Vehicle.builder()
                                     .brandId(createdBrand.getBrandId())
                                     .modelId(createdModel.getModelId())
                                     .registrationBrand(createdBrand.getBrandName())
                                     .registrationModel(createdModel.getModelName())
                                     .build();
            Vehicle foundVehicle = vehicleRepository.find(vehicle);
            Vehicle createdVehicle = Objects.nonNull(foundVehicle) ? foundVehicle : vehicleRepository.create(vehicle);

            if (Objects.isNull(createdVehicle)) {
                log.info("createdVehicle is null");
                return;
            }

            java.sql.Date resultDate = null;

            try {
                java.util.Date date = formatFirst.parse(registrationDate);
                resultDate = new java.sql.Date(date.getTime());
            } catch (Exception ex) {
                log.warn("Problem with parsing date with first formatter: {}", registrationDate);
            }
            if (Objects.isNull(resultDate)) {
                try {
                    java.util.Date date = formatSecond.parse(registrationDate);
                    resultDate = new java.sql.Date(date.getTime());
                } catch (Exception ex) {
                    log.warn("Problem with parsing date with second formatter: {}", registrationDate);
                }
            }

            Registration registration = Registration.builder()
                                                    .adminObjectId(admObj.getAdminObjId())
                                                    .opCode(operation.getOperationCode())
                                                    .depCode(department.getDepartmentCode())
                                                    .kindId(createdKind.getKindId())
                                                    .colorId(createdColor.getColorId())
                                                    .bodyTypeId(createdBodyType.getBodyTypeId())
                                                    .purposeId(Objects.nonNull(createdPurpose) ? createdPurpose.getPurposeId() : null)
                                                    .fuelTypeId(Objects.nonNull(createdFuelType) ? createdFuelType.getFuelTypeId() : null)
                                                    .vehicleId(createdVehicle.getVehicleId())
                                                    .adminObjName(admObj.getAdminObjName())
                                                    .adminObjType(admObj.getAdminObjType())
                                                    .operation(createdOperation.getOperationName())
                                                    .departmentName(department.getDepartmentName())
                                                    .departmentAddress(department.getDepartmentAddress())
                                                    .departmentEmail(department.getDepartmentEmail())
                                                    .kind(createdKind.getKindName())
                                                    .brand(createdBrand.getBrandName())
                                                    .model(createdModel.getModelName())
                                                    .color(createdColor.getColorName())
                                                    .bodyType(createdBodyType.getBodyTypeName())
                                                    .purpose(Objects.nonNull(createdPurpose) ? createdPurpose.getPurposeName() : null)
                                                    .fuelType(Objects.nonNull(createdFuelType) ? createdFuelType.getFuelTypeName() : null)
                                                    .engineCapacity(vehicleEngineCapacity)
                                                    .ownWeight(vehicleOwnWeight)
                                                    .totalWeight(vehicleTotalWeight)
                                                    .makeYear(vehicleMakeYear)
                                                    .personType(personType)
                                                    .registrationNumber(vehicleRegistrationNumber)
                                                    .registrationDate(resultDate)
                                                    .build();
            service.create(registration);
        } catch (Exception ex) {
            log.warn("Problem with saving record: {}", record.toString());
            log.error("ERROR OCCURRED IN PERSISTING CURRENT RECORD", ex);
        }
    }
}
