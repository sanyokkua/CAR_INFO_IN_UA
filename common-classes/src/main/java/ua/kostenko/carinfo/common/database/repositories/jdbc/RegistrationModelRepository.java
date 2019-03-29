package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.mapping.RegistrationModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
public class RegistrationModelRepository extends CachingJdbcRepository<RegistrationModel> {

    @Autowired
    public RegistrationModelRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationModel> getCacheLoader() {
        return new CacheLoader<String, RegistrationModel>() {
            @Override
            public RegistrationModel load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationModel create(@NonNull @Nonnull RegistrationModel entity) {
        return null;
    }

    @Nullable
    @Override
    public RegistrationModel update(@NonNull @Nonnull RegistrationModel entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Nullable
    @Override
    public RegistrationModel find(@NonNull @Nonnull Long id) {
        return null;
    }

    @Override
    public List<RegistrationModel> findAll() {
        return null;
    }

    @Override
    public RegistrationModel findByField(String fieldValue) {
        return null;
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationModel entity) {
        return false;
    }

    @Override
    public void createAll(Iterable<RegistrationModel> entities) {

    }
}
