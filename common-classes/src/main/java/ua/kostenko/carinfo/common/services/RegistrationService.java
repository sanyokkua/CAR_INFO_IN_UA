package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.ParamHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

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
    public Registration create(@NonNull @Nonnull Registration entity) {
        return null;
    }

    @Nullable
    @Override
    public Registration update(@NonNull @Nonnull Registration entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Registration entity) {
        return false;
    }

    @Override
    public List<Registration> findAll() {
        return null;
    }

    @Override
    public Page<Registration> find(@NonNull @Nonnull ParamHolderBuilder builder) {
        return null;
    }
}
