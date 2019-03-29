package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.mapping.RegistrationFuelType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
public class RegistrationFuelTypeRepository extends CachingJdbcRepository<RegistrationFuelType> {

    @Autowired
    public RegistrationFuelTypeRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationFuelType> getCacheLoader() {
        return new CacheLoader<String, RegistrationFuelType>() {
            @Override
            public RegistrationFuelType load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationFuelType create(@NonNull @Nonnull RegistrationFuelType entity) {
        return null;
    }

    @Nullable
    @Override
    public RegistrationFuelType update(@NonNull @Nonnull RegistrationFuelType entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Nullable
    @Override
    public RegistrationFuelType find(@NonNull @Nonnull Long id) {
        return null;
    }

    @Override
    public List<RegistrationFuelType> findAll() {
        return null;
    }

    @Override
    public RegistrationFuelType findByField(String fieldValue) {
        return null;
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationFuelType entity) {
        return false;
    }

    @Override
    public void createAll(Iterable<RegistrationFuelType> entities) {

    }
}
