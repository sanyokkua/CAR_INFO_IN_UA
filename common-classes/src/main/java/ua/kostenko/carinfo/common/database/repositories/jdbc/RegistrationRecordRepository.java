package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.mapping.RegistrationRecord;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
public class RegistrationRecordRepository extends CachingJdbcRepository<RegistrationRecord> {

    @Autowired
    public RegistrationRecordRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationRecord> getCacheLoader() {
        return new CacheLoader<String, RegistrationRecord>() {
            @Override
            public RegistrationRecord load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationRecord create(@NonNull @Nonnull RegistrationRecord entity) {
        return null;
    }

    @Nullable
    @Override
    public RegistrationRecord update(@NonNull @Nonnull RegistrationRecord entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Nullable
    @Override
    public RegistrationRecord find(@NonNull @Nonnull Long id) {
        return null;
    }

    @Override
    public List<RegistrationRecord> findAll() {
        return null;
    }

    @Override
    public RegistrationRecord findByField(String fieldValue) {
        return null;
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationRecord entity) {
        return false;
    }

    @Override
    public void createAll(Iterable<RegistrationRecord> entities) {

    }
}
