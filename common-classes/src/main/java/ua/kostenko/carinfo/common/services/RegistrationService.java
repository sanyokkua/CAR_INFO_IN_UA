package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.Constants;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.*;
import ua.kostenko.carinfo.common.database.repositories.jdbc.crud.PageableRepository;
import ua.kostenko.carinfo.common.database.repositories.jdbc.crud.RegistrationVehicleRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RegistrationService implements CrudService<Registration> { //TODO: rewrite logic
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
    private final RegistrationVehicleRepository vehicleRepository;
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
                               @NonNull @Nonnull RegistrationVehicleRepository vehicleRepository,
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
            Department department = departmentRepository.findByField(entity.getDepartmentCode());
            FuelType fuelType = fuelTypeRepository.findByField(entity.getFuelType());
            Kind kind = kindRepository.findByField(entity.getKind());
            Model model = modelRepository.findByField(entity.getModel());
            Operation operation = operationRepository.findByField(entity.getOperationName());
            Purpose purpose = purposeRepository.findByField(entity.getPurpose());
            if (isNotNull(administrativeObject, bodyType, brand, color, department, fuelType, kind, model, operation, purpose)) {
                Vehicle vehicle = vehicleRepository.findByFields(brand.getBrandId(), model.getModelId());
                if (Objects.nonNull(vehicle)) {
                    Registration result = registrationRepository.create(entity);
                    return result;
                } else {
                    return null;
                }
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
        String operation = entity.getOperationName();
        String departmentName = entity.getDepartmentCode();
//        String departmentAddress = entity.getDepartmentAddress();
//        String departmentEmail = entity.getDepartmentEmail();
//        String kind = entity.getKind();
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

//        boolean operationExists = operationRepository.isExistsId(entity.getOpCode());
//        boolean departmentExists = departmentRepository.isExistsId(entity.getDepCode());
        boolean colorExists = colorRepository.isExists(Color.builder().colorName(color).build());
        boolean bodyTypeExists = bodyTypeRepository.isExists(BodyType.builder().bodyTypeName(bodyType).build());
        boolean fuelTypeExists = fuelTypeRepository.isExists(FuelType.builder().fuelTypeName(fuelType).build());
        boolean isRegistrationNumberValid = StringUtils.isNotBlank(registrationNumber);
        return colorExists && bodyTypeExists && fuelTypeExists && isRegistrationNumberValid;
    }

    @Nullable
    @Override
    public synchronized Registration update(@NonNull @Nonnull Registration entity) {
        if (isValid(entity)) {
            AdministrativeObject administrativeObject = adminRepository.findByField(entity.getAdminObjName());
            BodyType bodyType = bodyTypeRepository.findByField(entity.getBodyType());
            Brand brand = brandRepository.findByField(entity.getBrand());
            Color color = colorRepository.findByField(entity.getColor());
            Department department = departmentRepository.findByField(entity.getDepartmentCode());
            FuelType fuelType = fuelTypeRepository.findByField(entity.getFuelType());
            Kind kind = kindRepository.findByField(entity.getKind());
            Model model = modelRepository.findByField(entity.getModel());
            Operation operation = operationRepository.findByField(entity.getOperationName());
            Purpose purpose = purposeRepository.findByField(entity.getPurpose());
            if (isNotNull(administrativeObject, bodyType, brand, color, department, fuelType, kind, model, operation, purpose)) {
                Page<Vehicle> vehicles = vehicleRepository.find(new ParamsHolderBuilder()
                                                                        .param(Constants.RegistrationBrand.NAME, brand.getBrandName())
                                                                        .param(Constants.RegistrationModel.NAME, model.getModelName())
                                                                        .build());
                Vehicle vehicle = vehicles.get().findFirst().orElseGet(null);
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
    public synchronized Page<Registration> find(@NonNull @Nonnull ParamsHolderBuilder builder) {
        log.info("find. Parameters: {}");
        return registrationRepository.find(builder.build());
    }

    @Nullable
    @Override
    public synchronized Registration find(@Nonnull Registration entity) {
        return registrationRepository.find(entity);
    }

    @Nullable
    @Override
    public synchronized Registration find(@Nonnull Long id) {
        return registrationRepository.find(id);
    }
}
