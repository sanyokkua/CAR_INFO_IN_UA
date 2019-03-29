package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.mapping.RegistrationVehicle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
public class RegistrationVehicleRepository extends CachingJdbcRepository<RegistrationVehicle> {

    @Autowired
    public RegistrationVehicleRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationVehicle> getCacheLoader() {
        return new CacheLoader<String, RegistrationVehicle>() {
            @Override
            public RegistrationVehicle load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationVehicle create(@NonNull @Nonnull RegistrationVehicle entity) {
        return null;
    }

    @Nullable
    @Override
    public RegistrationVehicle update(@NonNull @Nonnull RegistrationVehicle entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Nullable
    @Override
    public RegistrationVehicle find(@NonNull @Nonnull Long id) {
        return null;
    }

    @Override
    public List<RegistrationVehicle> findAll() {
        return null;
    }

    @Override
    public RegistrationVehicle findByField(String fieldValue) {
        return null;
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationVehicle entity) {
        return false;
    }

    @Override
    public void createAll(Iterable<RegistrationVehicle> entities) {

    }
}
