package ua.kostenko.carinfo.common.api.services;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.NonNull;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.FuelType;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

@Service
class FuelTypeService extends CommonDbService<FuelType, String> {

    @Autowired
    protected FuelTypeService(@NonNull @Nonnull DBRepository<FuelType, String> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull FuelType entity) {
        return StringUtils.isNotBlank(entity.getFuelTypeName());
    }

    @Override
    public Optional<FuelType> get(@NonNull @Nonnull FuelType entity) {
        ParamsHolderBuilder builder = new ParamsHolderBuilder();
        builder.param(FuelType.FUEL_NAME, entity.getFuelTypeName());
        FuelType foundEntity = this.repository.findOne(builder.build());
        return Optional.ofNullable(foundEntity);
    }
}
