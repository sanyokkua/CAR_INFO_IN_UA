package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.ParamHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.Brand;
import ua.kostenko.carinfo.common.records.Model;
import ua.kostenko.carinfo.common.records.Vehicle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class VehicleService implements CrudService<Vehicle> {
    private final PageableRepository<Vehicle> vehicleRepository;
    private final PageableRepository<Brand> brandRepository;
    private final PageableRepository<Model> modelRepository;

    @Autowired
    public VehicleService(@NonNull @Nonnull PageableRepository<Vehicle> vehicleRepository,
                          @NonNull @Nonnull PageableRepository<Brand> brandRepository,
                          @NonNull @Nonnull PageableRepository<Model> modelRepository) {
        this.vehicleRepository = vehicleRepository;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
    }

    @Nullable
    @Override
    public Vehicle create(@NonNull @Nonnull Vehicle entity) {
        if (isValid(entity)) {
            String brand = entity.getBrandName();
            String model = entity.getModelName();
            Brand brandFromDb = brandRepository.findByField(brand);
            Model modelFromDb = modelRepository.findByField(model);
            if (Objects.isNull(brandFromDb) || Objects.isNull(modelFromDb)) {
                log.warn("create: Vehicle can't be created. Model or Brand (from db) is null. Model = {}, Brand = {}", modelFromDb, brandFromDb);
                return null;
            }
            entity.setBrandId(brandFromDb.getBrandId());
            entity.setModelId(modelFromDb.getModelId());
            if (!isExists(entity)) {
                Vehicle vehicle = vehicleRepository.create(entity);
                if (Objects.nonNull(vehicle)) {
                    log.info("create: created entity: {}", vehicle.toString());
                } else {
                    log.warn("create: entity is not created", entity.toString());
                }
                return vehicle;
            } else {
                log.info("create: entity exists. Updating entity");
                return vehicleRepository.find(entity);
            }
        }
        log.info("create: entity is not valid. Returning null");
        return null;
    }

    private boolean isValid(Vehicle entity) {
        String brand = entity.getBrandName();
        String model = entity.getModelName();
        log.info("isValid: validating vehicle. Brand = {}, Model = {}", brand, model);
        if (StringUtils.isNotBlank(brand) && StringUtils.isNotBlank(model)) {
            boolean brandExists = brandRepository.isExists(Brand.builder().brandName(brand).build());
            log.info("isValid: brand {} exists = {}", brand, brandExists);
            boolean modelExists = modelRepository.isExists(Model.builder().modelName(model).build());
            log.info("isValid: model {} exists = {}", model, modelExists);
            return brandExists && modelExists;
        }
        return false;
    }

    @Nullable
    @Override
    public Vehicle update(@NonNull @Nonnull Vehicle entity) {
        if (isValid(entity) && !isExists(entity)) {
            log.info("update: entity doesn't exist. Throwing exception");
            throw new IllegalArgumentException("Entity doesn't exist");
        } else if (isValid(entity) && isExists(entity)) {
            log.info("update: entity exists. Updating entity");
            Brand brandFromDb = brandRepository.findByField(entity.getBrandName());
            Model modelFromDb = modelRepository.findByField(entity.getModelName());
            if (Objects.isNull(brandFromDb) || Objects.isNull(modelFromDb)) {
                log.warn("update: Vehicle can't be updated. Model or Brand (from db) is null. Model = {}, Brand = {}", modelFromDb, brandFromDb);
                return null;
            }
            entity.setModelId(modelFromDb.getModelId());
            entity.setBrandName(modelFromDb.getModelName());
            entity.setBrandId(brandFromDb.getBrandId());
            entity.setBrandName(brandFromDb.getBrandName());
            return vehicleRepository.update(entity);
        }

        log.info("update: entity is not valid. Returning null");
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        log.info("delete: deleting entity with id: {}", id);
        boolean deleted = vehicleRepository.delete(id);
        log.info("delete: entity with id: {} is deleted: ", id, deleted);
        return deleted;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Vehicle entity) {
        log.info("isExists: checking for existence entity = {}", entity.toString());
        boolean exists = vehicleRepository.isExists(entity);
        log.info("isExists: entity = {} exists: {}", entity.toString(), exists);
        return exists;
    }

    @Override
    public List<Vehicle> findAll() {
        log.info("findAll");
        return vehicleRepository.findAll();
    }

    @Override
    public Page<Vehicle> find(@NonNull @Nonnull ParamHolderBuilder builder) {
        log.info("find. Parameters: {}");
        return vehicleRepository.find(builder.build());
    }

    @Nullable
    @Override
    public Vehicle find(@Nonnull Vehicle entity) {
        return vehicleRepository.find(entity);
    }

    @Nullable
    @Override
    public Vehicle find(@Nonnull Long id) {
        return vehicleRepository.find(id);
    }
}
