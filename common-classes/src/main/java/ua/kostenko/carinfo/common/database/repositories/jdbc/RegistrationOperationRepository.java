package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.mapping.RegistrationOperation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
public class RegistrationOperationRepository extends CachingJdbcRepository<RegistrationOperation> {

    @Autowired
    public RegistrationOperationRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationOperation> getCacheLoader() {
        return new CacheLoader<String, RegistrationOperation>() {
            @Override
            public RegistrationOperation load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationOperation create(@NonNull @Nonnull RegistrationOperation entity) {
        return null;
    }

    @Nullable
    @Override
    public RegistrationOperation update(@NonNull @Nonnull RegistrationOperation entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Nullable
    @Override
    public RegistrationOperation find(@NonNull @Nonnull Long id) {
        return null;
    }

    @Override
    public List<RegistrationOperation> findAll() {
        return null;
    }

    @Override
    public RegistrationOperation findByField(String fieldValue) {
        return null;
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationOperation entity) {
        return false;
    }

    @Override
    public void createAll(Iterable<RegistrationOperation> entities) {

    }
}
