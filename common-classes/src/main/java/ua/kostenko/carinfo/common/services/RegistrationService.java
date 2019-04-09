package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.ParamHolderBuilder;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RegistrationService implements CrudService<Registration> {
    private final PageableRepository<AdministrativeObject> adminRepository;
    private final PageableRepository<BodyType> bodyTypeRepository;
    private final PageableRepository<Brand> brandRepository;
    private final PageableRepository<Color> colorRepository;
    private final PageableRepository<Department> departmentRepository;
    private final PageableRepository<FuelType> fuelTypeRepository;
    private final PageableRepository<Kind> kindRepository;
    private final PageableRepository<Model> modelRepository;
    private final PageableRepository<Operation> operationRepository;
    private final PageableRepository<Purpose> purposeRepository;
    private final PageableRepository<Vehicle> vehicleRepository;
    private final PageableRepository<Registration> registrationRepository;

    @Autowired
    public RegistrationService(@NonNull @Nonnull PageableRepository<AdministrativeObject> adminRepository,
                               @NonNull @Nonnull PageableRepository<BodyType> bodyTypeRepository,
                               @NonNull @Nonnull PageableRepository<Brand> brandRepository,
                               @NonNull @Nonnull PageableRepository<Color> colorRepository,
                               @NonNull @Nonnull PageableRepository<Department> departmentRepository,
                               @NonNull @Nonnull PageableRepository<FuelType> fuelTypeRepository,
                               @NonNull @Nonnull PageableRepository<Kind> kindRepository,
                               @NonNull @Nonnull PageableRepository<Model> modelRepository,
                               @NonNull @Nonnull PageableRepository<Operation> operationRepository,
                               @NonNull @Nonnull PageableRepository<Purpose> purposeRepository,
                               @NonNull @Nonnull PageableRepository<Vehicle> vehicleRepository,
                               @NonNull @Nonnull PageableRepository<Registration> registrationRepository) {
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
        this.registrationRepository = registrationRepository;
    }

    @Nullable
    @Override
    public synchronized Registration create(@NonNull @Nonnull Registration entity) {
        if (isValid(entity)) {
            AdministrativeObject administrativeObject = adminRepository.findByField(entity.getAdminObjName());
            BodyType bodyType = bodyTypeRepository.findByField(entity.getBodyType());
            Brand brand = brandRepository.findByField(entity.getBrand());
            Color color = colorRepository.findByField(entity.getColor());
            Department department = departmentRepository.findByField(entity.getDepartmentName());
            FuelType fuelType = fuelTypeRepository.findByField(entity.getFuelType());
            Kind kind = kindRepository.findByField(entity.getKind());
            Model model = modelRepository.findByField(entity.getModel());
            Operation operation = operationRepository.findByField(entity.getOperation());
            Purpose purpose = purposeRepository.findByField(entity.getPurpose());
            if (isNotNull(administrativeObject, bodyType, brand, color, department, fuelType, kind, model, operation, purpose)) {
                Page<Vehicle> vehicles = vehicleRepository.find(new ParamHolderBuilder()
                                                                        .param(Constants.RegistrationBrand.NAME, brand.getBrandName())
                                                                        .param(Constants.RegistrationModel.NAME, model.getModelName())
                                                                        .build());
                Vehicle vehicle = vehicles.get().findFirst().orElseGet(null);
                entity.setColorId(color.getColorId());
                entity.setKindId(kind.getKindId());
                entity.setPurposeId(purpose.getPurposeId());
                entity.setAdminObjectId(administrativeObject.getAdminObjId());
                entity.setBodyTypeId(bodyType.getBodyTypeId());
                entity.setFuelTypeId(fuelType.getFuelTypeId());
                entity.setVehicleId(vehicle.getVehicleId());
                Registration result = registrationRepository.create(entity);
                return result;
            } else {
                return null;
            }
        }
        return null;
    }

    private boolean isNotNull(Object... objs) {
        for (Object obj : objs) {
            if (Objects.isNull(obj)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValid(Registration entity) {

//        String adminObjName = entity.getAdminObjName();
//        String adminObjType = entity.getAdminObjType();
        String operation = entity.getOperation();
        String departmentName = entity.getDepartmentName();
//        String departmentAddress = entity.getDepartmentAddress();
//        String departmentEmail = entity.getDepartmentEmail();
        String kind = entity.getKind();
        String brand = entity.getBrand();
        String model = entity.getModel();
        String color = entity.getColor();
        String bodyType = entity.getBodyType();
//        String purpose = entity.getPurpose();
        String fuelType = entity.getFuelType();
//        Long engineCapacity = entity.getEngineCapacity();
//        Long ownWeight = entity.getOwnWeight();
//        Long totalWeight = entity.getTotalWeight();
//        Long makeYear = entity.getMakeYear();
        String registrationNumber = entity.getRegistrationNumber();
//        Date registrationDate = entity.getRegistrationDate();

        boolean operationExists = operationRepository.isExists(Operation.builder().operationName(operation).build());
        boolean departmentNameExists = departmentRepository.isExists(Department.builder().departmentName(departmentName).build());
        boolean brandExists = brandRepository.isExists(Brand.builder().brandName(brand).build());
        boolean modelExists = modelRepository.isExists(Model.builder().modelName(model).build());
        boolean colorExists = colorRepository.isExists(Color.builder().colorName(color).build());
        boolean bodyTypeExists = bodyTypeRepository.isExists(BodyType.builder().bodyTypeName(bodyType).build());
        boolean fuelTypeExists = fuelTypeRepository.isExists(FuelType.builder().fuelTypeName(fuelType).build());
        boolean isRegistrationNumberValid = StringUtils.isNotBlank(registrationNumber);
        return operationExists && departmentNameExists && brandExists && modelExists && colorExists && bodyTypeExists && fuelTypeExists && isRegistrationNumberValid;
    }

    @Nullable
    @Override
    public synchronized Registration update(@NonNull @Nonnull Registration entity) {
        if (isValid(entity)) {
            AdministrativeObject administrativeObject = adminRepository.findByField(entity.getAdminObjName());
            BodyType bodyType = bodyTypeRepository.findByField(entity.getBodyType());
            Brand brand = brandRepository.findByField(entity.getBrand());
            Color color = colorRepository.findByField(entity.getColor());
            Department department = departmentRepository.findByField(entity.getDepartmentName());
            FuelType fuelType = fuelTypeRepository.findByField(entity.getFuelType());
            Kind kind = kindRepository.findByField(entity.getKind());
            Model model = modelRepository.findByField(entity.getModel());
            Operation operation = operationRepository.findByField(entity.getOperation());
            Purpose purpose = purposeRepository.findByField(entity.getPurpose());
            if (isNotNull(administrativeObject, bodyType, brand, color, department, fuelType, kind, model, operation, purpose)) {
                Page<Vehicle> vehicles = vehicleRepository.find(new ParamHolderBuilder()
                                                                        .param(Constants.RegistrationBrand.NAME, brand.getBrandName())
                                                                        .param(Constants.RegistrationModel.NAME, model.getModelName())
                                                                        .build());
                Vehicle vehicle = vehicles.get().findFirst().orElseGet(null);
                entity.setColorId(color.getColorId());
                entity.setKindId(kind.getKindId());
                entity.setPurposeId(purpose.getPurposeId());
                entity.setAdminObjectId(administrativeObject.getAdminObjId());
                entity.setBodyTypeId(bodyType.getBodyTypeId());
                entity.setFuelTypeId(fuelType.getFuelTypeId());
                entity.setVehicleId(vehicle.getVehicleId());
                Registration result = registrationRepository.update(entity);
                return result;
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public synchronized boolean delete(@NonNull @Nonnull Long id) {
        log.info("delete: deleting entity with id: {}", id);
        boolean deleted = registrationRepository.delete(id);
        log.info("delete: entity with id: {} is deleted: ", id, deleted);
        return deleted;
    }

    @Override
    public synchronized boolean isExists(@NonNull @Nonnull Registration entity) {
        log.info("isExists: checking for existence entity = {}", entity.toString());
        boolean exists = registrationRepository.isExists(entity);
        log.info("isExists: entity = {} exists: {}", entity.toString(), exists);
        return exists;
    }

    @Override
    public synchronized List<Registration> findAll() {
        log.info("findAll");
        return registrationRepository.findAll();
    }

    @Override
    public synchronized Page<Registration> find(@NonNull @Nonnull ParamHolderBuilder builder) {
        log.info("find. Parameters: {}");
        return registrationRepository.find(builder.build());
    }

    @Nullable
    @Override
    public Registration find(@Nonnull Registration entity) {
        return registrationRepository.find(entity);
    }

    @Nullable
    @Override
    public Registration find(@Nonnull Long id) {
        return registrationRepository.find(id);
    }
}
