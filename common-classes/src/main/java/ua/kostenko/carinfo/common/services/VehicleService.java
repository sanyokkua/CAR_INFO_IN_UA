package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
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
        return null;
    }

    @Nullable
    @Override
    public Vehicle update(@NonNull @Nonnull Vehicle entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Vehicle entity) {
        return false;
    }

    @Override
    public List<Vehicle> findAll() {
        return null;
    }

    @Override
    public Page<Vehicle> find(@NonNull @Nonnull ParamHolderBuilder builder) {
        return null;
    }
}
